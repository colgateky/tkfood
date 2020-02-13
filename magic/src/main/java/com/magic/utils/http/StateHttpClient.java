package com.magic.utils.http;

import com.magic.utils.DateUtils;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by xyz on 2015/7/11.
 */
public class StateHttpClient {
    public static interface INameResolver {
        public WebServerIpResolver getResolver(String name);
    }
    public class Response {
        public HttpUriRequest currentRequest;
        public HttpHost currentHost;
        private CloseableHttpResponse resp;
        private byte[] responseData;

        public Response(CloseableHttpResponse resp, HttpUriRequest currentRequest, HttpHost currentHost) {
            this.resp = resp;
            this.currentRequest = currentRequest;
            this.currentHost = currentHost;
            this.parseResponseData();
        }
        public int getStatusCode() {
            return resp.getStatusLine().getStatusCode();
        }
        public String getLocation() {
            Header header = resp.getFirstHeader("Location");
            if (header != null)
                return header.getValue();
            else
                return null;
        }
        private void parseResponseData() {
            try {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                Header ceheader = resp.getEntity().getContentEncoding();
                boolean gzipTag = false;
                if (ceheader != null) {
                    for (HeaderElement element : ceheader.getElements()) {
                        if (element.getName().equalsIgnoreCase("gzip")) {
                            gzipTag = true;
                            break;
                        }
                    }
                }
                InputStream ins = resp.getEntity().getContent();
                if (gzipTag) {
                    ins = new GZIPInputStream(ins);
                }
                byte[] buff = new byte[10240];
                long c = 0;
                while (true) {
                    int len = ins.read(buff);
                    if (len <= 0) break;
                    bout.write(buff, 0, len);
                    c += len;
                    if (limit > 0 && len > limit) {
                        break;
                    }
                }
                limit = 0;
                responseData = bout.toByteArray();
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        public HttpResponse getResponse() { return resp; }
        public byte[] getResponseData() {
            return responseData;
        }

        public String getResponseAsString() {
            byte[] data = getResponseData();
            if (data == null) return null;

            ContentType contentType = ContentType.getOrDefault(resp.getEntity());
            Charset charset = contentType.getCharset();
            if (charset == null)
                return new String(data, Charset.forName("utf-8"));
            return new String(data, charset);
        }
    }

    private INameResolver nameResolver;

    public void setNameResolver(INameResolver nameResolver) {
        this.nameResolver = nameResolver;
    }
    private String userAgent;
    public void setUserAgent(String ua) {
        this.userAgent = ua;
    }
    private String proxyHost;
    private int proxyPort;
    public void setProxy(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;

    }
    private BasicCookieStore cookieStore;

    private void setResolveIpInvalid(DefaultHttpClient client, String urlString) throws Exception {
        URL url = new URL(urlString);
        WebServerIpResolver webServerIpResolver = null;
        if (nameResolver != null) {
            webServerIpResolver = nameResolver.getResolver(url.getHost());
        }
        if (webServerIpResolver != null) {
            HttpHost host = (HttpHost)client.getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY);
            if (host != null) {
                webServerIpResolver.setInvalidIp(host.getHostName());
            }
        }
    }

    private void setResolveIp(DefaultHttpClient client, String urlString) throws Exception {
        URL url = new URL(urlString);
        WebServerIpResolver webServerIpResolver = null;
        if (nameResolver != null) {
            webServerIpResolver = nameResolver.getResolver(url.getHost());
        }
        int port = url.getPort();
        if (port <= 0) {
            port = url.getDefaultPort();
        }
        if (webServerIpResolver != null) {
            String ip = webServerIpResolver.resolveIp();
            if (ip != null) {
                HttpHost httpHost = new HttpHost(ip, port);
                client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
            }
        } else if (StringUtils.isEmpty(proxyHost) == false && proxyPort > 0) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
    }

    private DefaultHttpClient getHttpClient(String urlString) throws Exception {
        DefaultHttpClient httpClient = HttpUtils.createHttp11Client(null);
        httpClient.setCookieStore(cookieStore);

        setResolveIp(httpClient, urlString);
        return httpClient;
    }
    public void resetCookies() {
        cookieStore = new BasicCookieStore();
    }
    public StateHttpClient() {
        cookieStore = new BasicCookieStore();
        setUserAgent(userAgent);
    }
    public StateHttpClient(String userAgent) {
        cookieStore = new BasicCookieStore();
        setUserAgent(userAgent);
    }
    public StateHttpClient(BasicCookieStore store, String userAgent) {
        cookieStore = store;
        setUserAgent(userAgent);
    }

    public String getCookie(String domain, String name) {
        for (Cookie cookie : cookieStore.getCookies()) {
            if (cookie.getDomain().equals(domain) && cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void setCookie(String domain, String path, String name, String value) {
        BasicClientCookie cookie = new BasicClientCookie(name,
                value);
        cookie.setExpiryDate(DateUtils.addDay(new Date(), 1));
        cookie.setVersion(0);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookieStore.addCookie(cookie);
    }
//    public void setCookie(String domain, String path, String name, String value) {
//        BasicClientCookie cookie = new BasicClientCookie(name,
//                value);
//        cookie.setVersion(1);
//        cookie.setDomain(domain);
//        cookie.setPath(path);
////        cookie.setSecure(true);
//        cookie.setAttribute(ClientCookie.VERSION_ATTR, "1");
//        cookie.setAttribute(ClientCookie.DOMAIN_ATTR, domain);
//        cookie.setExpiryDate(DateUtils.addDay(new Date(), 1));
//        cookieStore.addCookie(cookie);
//    }

    private long limit;
    public void setLimit(long limit) {
        this.limit = limit;
    }

    private HttpContext createContext() {
        HttpClientContext context = HttpClientContext.create();
        Registry<CookieSpecProvider> registry = RegistryBuilder
                .<CookieSpecProvider> create()
                .register("default", new BestMatchSpecFactory())
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY,
                        new BrowserCompatSpecFactory()).build();
        context.setCookieSpecRegistry(registry);
        context.setCookieStore(cookieStore);
        return context;
    }
    public Response get(String urlString) {
        return get(urlString, 0);
    }
    public Response get(String urlString, Map<String, String> headers) {
        return get(urlString, headers, 0);
    }
    public Response get(String urlString, int retry) {
        return get(urlString, null, retry);
    }
    public Response get(String urlString, Map<String, String> headers, int retry) {
        DefaultHttpClient client = null;
        try {
            client = getHttpClient(urlString);
            HttpGet get = new HttpGet(urlString);
            if (userAgent != null) {
                get.setHeader("User-Agent", userAgent);
            }
            if (headers != null) {
                for (Map.Entry<String, String> kv : headers.entrySet()) {
                    get.setHeader(kv.getKey(), kv.getValue());
                }
            }
            HttpContext context = createContext();


            CloseableHttpResponse resp = client.execute(get, context);
            HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(
                    ExecutionContext.HTTP_REQUEST);
            HttpHost currentHost = (HttpHost)  context.getAttribute(
                    ExecutionContext.HTTP_TARGET_HOST);
            return new Response(resp, currentReq, currentHost);
        } catch (ConnectTimeoutException exp1) {
            try {
                setResolveIpInvalid(client, urlString);
            } catch (Exception eee) {
                eee.printStackTrace();
            }
            if (retry < 0) {
                exp1.printStackTrace();
                return null;
            }
            return get(urlString, retry-1);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public Response post(String urlString, Map<String, String> headers, Map<String, Object> params,
                         int retry) {
        DefaultHttpClient client = null;
        try {
            client = getHttpClient(urlString);
            HttpPost post = new HttpPost(urlString);
            if (userAgent != null) {
                post.setHeader("User-Agent", userAgent);
            }
            if (headers != null) {
                for (Map.Entry<String, String> kv : headers.entrySet()) {
                    post.setHeader(kv.getKey(), kv.getValue());
                }
            }
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for(Map.Entry<String, Object> kv : params.entrySet()) {
                nvps.add(new BasicNameValuePair(kv.getKey(), kv.getValue() == null? "":kv.getValue().toString()));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            HttpContext context = createContext();

            CloseableHttpResponse resp = client.execute(post, context);
            HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(
                    ExecutionContext.HTTP_REQUEST);
            HttpHost currentHost = (HttpHost)  context.getAttribute(
                    ExecutionContext.HTTP_TARGET_HOST);
            return new Response(resp, currentReq, currentHost);
        } catch (ConnectTimeoutException exp1) {
            try {
                setResolveIpInvalid(client, urlString);
            } catch (Exception eee) {
                eee.printStackTrace();
            }
            if (retry < 0) {
                exp1.printStackTrace();
                return null;
            }
            return post(urlString, headers, params,retry-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response post(String urlString, Map<String, String> headers, Map<String, Object> params) {
        return post(urlString, headers, params, 0);
    }

    public Response post(String urlString, Map<String, Object> params) {
        return post(urlString, null, params, 0);
    }
}
