package com.magic.utils.http;


import com.magic.utils.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xyz on 2015/8/15.
 */
public class HttpUtils {
    public static DefaultHttpClient createHttp11Client(SSLSocketFactory sslSocketFactory) {
        return createHttpClient(HttpVersion.HTTP_1_1, sslSocketFactory);
    }
    public static DefaultHttpClient createHttp10Client() {
        return createHttpClient(HttpVersion.HTTP_1_0);
    }
    private static int defaultConnecttionTimeout = 5000;
    private static int defaultSoTimeout = 180000;
    private static int connectionTimeout = defaultConnecttionTimeout;
    private static int soTimeout = defaultSoTimeout;

    public static void restoreTimeout() {
        connectionTimeout = defaultConnecttionTimeout;
        soTimeout = defaultSoTimeout;
    }
    public static void setTimeout(int connectionTimeout, int soTimeout) {
        HttpUtils.connectionTimeout = connectionTimeout;
        HttpUtils.soTimeout = soTimeout;
    }
    private static DefaultHttpClient createHttpClient(HttpVersion version) {
        return createHttpClient(version, null);
    }
    private static DefaultHttpClient createHttpClient(HttpVersion version, SSLSocketFactory sf) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            if (sf == null) {
                sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            }
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, version);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ConnManagerParams.setTimeout(params, connectionTimeout);
            HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
            HttpConnectionParams.setSoTimeout(params, soTimeout);
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            DefaultHttpClient httpclient =  new DefaultHttpClient(ccm, params);
            httpclient.addRequestInterceptor(new HttpRequestInterceptor() {

                public void process(
                        final HttpRequest request,
                        final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }

            });

            httpclient.addResponseInterceptor(new HttpResponseInterceptor() {

                public void process(
                        final HttpResponse response,
                        final HttpContext context) throws HttpException, IOException {
                    HttpEntity entity = response.getEntity();
                    Header ceheader = entity.getContentEncoding();
                    if (ceheader != null) {
                        HeaderElement[] codecs = ceheader.getElements();
                        for (int i = 0; i < codecs.length; i++) {
                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(
                                        new GzipDecompressingEntity(response.getEntity()));
                                return;
                            }
                        }
                    }
                }
            });
            return httpclient;
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static class SSLSocketFactoryEx extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType)
                        throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType)
                        throws java.security.cert.CertificateException {

                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }



    public static HttpUtils getInstance() {
        return new HttpUtils();
    }
    public static HttpUtils getInstance(String proxyHost, int proxyPort) {
        HttpUtils ret = new HttpUtils();
        ret.proxyHost = proxyHost;
        ret.proxyPort = proxyPort;
        return ret;
    }

    protected SSLSocketFactory sslSocketFactory;
    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }
    public void setSslContext(SSLContext sslContext) {
        this.sslSocketFactory = new SSLSocketFactory(sslContext);
    }

    protected String proxyHost;
    protected int proxyPort;

    protected HttpClient initClient() {
        HttpClient httpClient = createHttp11Client(sslSocketFactory);
//        if (StringUtils.isEmpty(proxyHost) == false) {
//            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
//            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
//        }
        return httpClient;
    }
    public ProxyInject proxyInject;
    public interface ProxyInject {
        String inject(String host);
    }
    public class ProxyResult {
        public String url;
        public String host;
    }
    protected ProxyResult checkProxy(String urlString) {
        String url = urlString;
        int i = urlString.indexOf(':');
        int j = urlString.indexOf('/', 9);
        String host = urlString.substring(i+3, j);
        if (StringUtils.isEmpty(proxyHost) == false) {
            url = urlString.substring(0, i) + "://" + proxyHost + ":" + proxyPort + urlString.substring(j);
        } else if (proxyInject != null) {
            String proxy = proxyInject.inject(host);
            if (proxy != null) {
                url = urlString.substring(0, i) + "://" + proxy + urlString.substring(j);
            }
        }
        ProxyResult ret = new ProxyResult();
        ret.url = url;
        ret.host = host;
        return ret;
    }
    protected HttpGet initHttpGet(String urlString, Map<String, String> headers) throws MalformedURLException {
        ProxyResult pr = checkProxy(urlString);
        HttpGet ret = new HttpGet(pr.url);
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Host", pr.host);
        setHeaders(ret, headers);
        return ret;
    }

    protected HttpPost initHttpPost(String urlString, Map<String, String> headers) throws MalformedURLException {
        ProxyResult pr = checkProxy(urlString);
        HttpPost ret = new HttpPost(pr.url);
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Host", pr.host);
        setHeaders(ret, headers);
        return ret;
    }

    protected HttpPut initHttpPut(String urlString, Map<String, String> headers) throws MalformedURLException {
        ProxyResult pr = checkProxy(urlString);
        HttpPut ret = new HttpPut(pr.url);
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Host", pr.host);
        setHeaders(ret, headers);
        return ret;
    }

    public boolean getAsFile(String urlString, String filepath) {
        try {
            HttpClient httpclient = initClient();
            HttpGet httpPost = initHttpGet(urlString, null);
            HttpResponse resp = httpclient.execute(httpPost);
            InputStream ins = null;
            if (resp == null || resp.getStatusLine().getStatusCode() != 200) {
                httpPost.abort();
                return false;
            }
            ins = resp.getEntity().getContent();
            FileOutputStream bout = new FileOutputStream(filepath);
            int BUFFER = 4096;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = ins.read(data, 0, BUFFER)) != -1) {
                bout.write(data, 0, count);
            }
            if (ins != null) {
                ins.close();
            }
            bout.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String postJsonAsString(String urlString, Map<String, String> headers, String content) {
        StringBuffer result = new StringBuffer();
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", "application/json");
        int ret = postAsString(urlString, content, headers, result);
        if (ret == 200) return result.toString();
        if (result.length() == 0) {
            return null;
        }
        return result.toString();
    }

    public String postJsonAsPlainText(String urlString, Map<String, String> headers, String content) {
        StringBuffer result = new StringBuffer();
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", "text/plain");
        int ret = postAsString(urlString, content, headers, result);
        if (ret == 200) return result.toString();
        if (result.length() == 0) {
            return null;
        }
        return result.toString();
    }

    public String postXMLAsString(String urlString, Map<String, String> headers, String content) {
        StringBuffer result = new StringBuffer();
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", "application/xml");
        int ret = postAsString(urlString, content, headers, result);
        if (ret == 200) return result.toString();
        if (result.length() == 0) {
            return null;
        }
        return result.toString();
    }

    private void setHeaders(Object obj, Map<String, String> headers) {
        if (headers != null) {
            if (obj instanceof HttpPost) {
                HttpPost method = (HttpPost)obj;
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    method.setHeader(header.getKey(), header.getValue());
                }
            } else if (obj instanceof HttpGet){
                HttpGet method = (HttpGet)obj;
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    method.setHeader(header.getKey(), header.getValue());
                }
            } else if (obj instanceof HttpPut){
                HttpPut method = (HttpPut) obj;
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    method.setHeader(header.getKey(), header.getValue());
                }
            }
        }
    }
    public String postFormAsString(String urlString, Map<String, String> headers, Map<String, String> ps) {
        return postFormAsString(urlString, headers, ps, 0);
    }
    public String postFormAsString(String urlString, Map<String, String> headers, Map<String, String> ps, int retry) {
        try {
            HttpClient httpclient = initClient();
            HttpPost httpPost = initHttpPost(urlString, headers);
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            for (Map.Entry<String, String> e : ps.entrySet()) {
                BasicNameValuePair p = new BasicNameValuePair(e.getKey(), e.getValue());
                params.add(p);
            }
            UrlEncodedFormEntity reqentity = new UrlEncodedFormEntity(params, "utf-8");
            httpPost.setEntity(reqentity);
            HttpResponse resp = httpclient.execute(httpPost);
            int status = resp.getStatusLine().getStatusCode();
            InputStream ins = null;
            HttpEntity entity = resp.getEntity();
            ins = entity.getContent();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int BUFFER = 4096;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = ins.read(data, 0, BUFFER)) != -1) {
                bout.write(data, 0, count);
            }
            if (ins != null) {
                ins.close();
            }
            return new String(bout.toByteArray(), "utf-8");
        } catch (org.apache.http.conn.ConnectTimeoutException e1) {
            if (retry <= 0) {
                return null;
            }
            return postFormAsString(urlString, headers, ps, retry - 1);
        }
        catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }
    }

    public String postFormAsString(String urlString, Map<String, String> headers, String content) {
        StringBuffer result = new StringBuffer();
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        int ret = postAsString(urlString, content, headers, result);
        if (ret == 200) return result.toString();
        if (result.length() == 0) {
            return null;
        }
        return result.toString();
    }

    public int postAsString(String urlString, String content, Map<String, String> headers, StringBuffer sb) {
        int status = -1;
        try {
            sb.setLength(0);
            HttpClient httpclient = initClient();
            HttpPost httpPost = initHttpPost(urlString, headers);

            setHeaders(httpPost, headers);
            httpPost.setEntity(new StringEntity(content, "utf-8"));
            HttpResponse resp = httpclient.execute(httpPost);
            status = resp.getStatusLine().getStatusCode();
            InputStream ins = null;
            HttpEntity entity = resp.getEntity();
            ins = entity.getContent();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int BUFFER = 4096;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = ins.read(data, 0, BUFFER)) != -1) {
                bout.write(data, 0, count);
            }
            if (ins != null) {
                ins.close();
            }
            sb.append(new String(bout.toByteArray(), "utf-8"));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return status;
    }


    public int postAsStream(String urlString, InputStream inputStream, Map<String, String> headers, StringBuffer sb) {
        int status = -1;
        try {
            sb.setLength(0);
            HttpClient httpclient = initClient();
            HttpPost httpPost = initHttpPost(urlString, headers);

            setHeaders(httpPost, headers);
            httpPost.setEntity(new InputStreamEntity(inputStream, inputStream.available()));
            HttpResponse resp = httpclient.execute(httpPost);
            status = resp.getStatusLine().getStatusCode();
            InputStream ins = null;
            HttpEntity entity = resp.getEntity();
            ins = entity.getContent();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int BUFFER = 4096;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = ins.read(data, 0, BUFFER)) != -1) {
                bout.write(data, 0, count);
            }
            if (ins != null) {
                ins.close();
            }
            sb.append(new String(bout.toByteArray(), "utf-8"));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return status;
    }


    public byte[] getAsBytes(String url) {
        try {
            HttpClient httpclient = initClient();

            HttpGet httpPost = initHttpGet(url, null);
            HttpResponse resp = httpclient.execute(httpPost);
            InputStream ins = null;
            if (resp == null || resp.getStatusLine().getStatusCode() != 200) {
                httpPost.abort();
                return null;
            }
            ins = resp.getEntity().getContent();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int BUFFER = 4096;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = ins.read(data, 0, BUFFER)) != -1) {
                bout.write(data, 0, count);
            }
            if (ins != null) {
                ins.close();
            }
            return bout.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String buildParams(Map<String, Object> ps) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> e : ps.entrySet()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(e.getKey()).append('=').append(URLEncoder.encode(e.getValue().toString()));
        }
        return sb.toString();
    }

    public String getAsString(String urlString, Map<String, String> headers, Map<String, Object> ps) {
        try {
            if (ps != null) {
                String params = buildParams(ps);
                urlString += "?" + params;
            }

            HttpClient httpclient = initClient();
            HttpGet httpPost = initHttpGet(urlString, headers);
            HttpResponse resp = httpclient.execute(httpPost);
            InputStream ins = null;
//            if (resp == null || resp.getStatusLine().getStatusCode() != 200) {
//                httpPost.abort();
//                return null;
//            }
            Header contentType = resp.getFirstHeader("Content-Type");
            String charset = null;
            if (contentType != null) {
                String v = contentType.getValue();
                int i = v.indexOf("charset=");
                if (i > 0) {
                    charset = v.substring(i + 8, v.length());
                }
            }
            ins = resp.getEntity().getContent();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int BUFFER = 4096;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = ins.read(data, 0, BUFFER)) != -1) {
                bout.write(data, 0, count);
            }
            if (ins != null) {
                ins.close();
            }
            String content = null;
            if (charset != null) {
                try {
                    content = new String(bout.toByteArray(), charset);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (content == null) {
                content = new String(bout.toByteArray(), "utf-8");
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUrlStringBefore302(String urlString, Map<String, String> headers, Map<String, Object> ps) {
        try {
            if (ps != null) {
                String params = buildParams(ps);
                urlString += "?" + params;
            }
            HttpClient httpclient = initClient();
            HttpParams params = httpclient.getParams();
            params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
            HttpGet httpPost = initHttpGet(urlString, headers);
            HttpResponse resp = httpclient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY
                    || resp.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY) {
                Header[] hs = resp.getHeaders("Location");
                if(hs!=null && hs.length>0){
                    String redirectUrl = hs[0].getValue();
                    System.out.println("重定向的URL:"+redirectUrl);
                    redirectUrl = redirectUrl.replace(" ", "%20");
                    return redirectUrl;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String putAsString(String urlString, Map<String, String> headers, Map<String, Object> ps) {
        try {
            if (ps != null) {
                String params = buildParams(ps);
                urlString += "?" + params;
            }

            HttpClient httpclient = initClient();
            HttpPut httpPost = initHttpPut(urlString, headers);
            HttpResponse resp = httpclient.execute(httpPost);
            InputStream ins = null;
            Header contentType = resp.getFirstHeader("Content-Type");
            String charset = null;
            if (contentType != null) {
                String v = contentType.getValue();
                int i = v.indexOf("charset=");
                if (i > 0) {
                    charset = v.substring(i + 8, v.length());
                }
            }
            ins = resp.getEntity().getContent();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int BUFFER = 4096;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = ins.read(data, 0, BUFFER)) != -1) {
                bout.write(data, 0, count);
            }
            if (ins != null) {
                ins.close();
            }
            String content = null;
            if (charset != null) {
                try {
                    content = new String(bout.toByteArray(), charset);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (content == null) {
                content = new String(bout.toByteArray(), "utf-8");
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String putJsonAsString(String urlString, Map<String, String> headers, String json) {
        try {
            headers.put("Content-Type", "application/json");
            HttpClient httpclient = initClient();
            HttpPut httpPost = initHttpPut(urlString, headers);
            httpPost.setEntity(new StringEntity(json, "utf-8"));
            HttpResponse resp = httpclient.execute(httpPost);
            InputStream ins = null;
            Header contentType = resp.getFirstHeader("Content-Type");
            String charset = null;
            if (contentType != null) {
                String v = contentType.getValue();
                int i = v.indexOf("charset=");
                if (i > 0) {
                    charset = v.substring(i + 8, v.length());
                }
            }
            ins = resp.getEntity().getContent();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int BUFFER = 4096;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = ins.read(data, 0, BUFFER)) != -1) {
                bout.write(data, 0, count);
            }
            if (ins != null) {
                ins.close();
            }
            String content = null;
            if (charset != null) {
                try {
                    content = new String(bout.toByteArray(), charset);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (content == null) {
                content = new String(bout.toByteArray(), "utf-8");
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIpFromRequest(HttpServletRequest req) {
        String ret = "";
        String forwardIp = req.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(forwardIp)) {
            String[] items = forwardIp.trim().split(",");
            if (items.length > 0) {
                String ip = items[0].trim();
                if (StringUtils.isEmpty(ip) == false) {
                    return ip;
                }
            }
        }
        String realIp = req.getHeader("X-Real-IP");
        if (realIp != null && realIp.length() > 4) {
            ret = realIp.trim();
        } else {
            ret = req.getRemoteAddr();
        }
        return ret;
    }

    public String createFormHtml(Map<String, String> formItems, String action, String method) {
        String html = String.format("<form id=\"payform\" name=\"payform\" action=\"%s\" method=\"%s\">",
                action, method);
        if (formItems != null) {
            StringBuilder sb = new StringBuilder();
            formItems.forEach((k, v) -> sb.append(
                    String.format("<input id=\"%s\" name=\"%s\" type=\"hidden\" value=\"%s\"/>", k, k, v)));
            html += sb.toString();
        }
        html += "</form>";
        html += "<script>document.getElementById('payform').submit();</script>";
        return html;
    }

    public String postFormAsStringWithCookieSet(String urlString, Map<String, String> headers, Map<String, String> ps, int retry) {
        try {
            HttpClient httpclient = initClient();
            HttpClientParams.setCookiePolicy(httpclient.getParams(), CookieSpecs.BROWSER_COMPATIBILITY);
            HttpPost httpPost = initHttpPost(urlString, headers);
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            for (Map.Entry<String, String> e : ps.entrySet()) {
                BasicNameValuePair p = new BasicNameValuePair(e.getKey(), e.getValue());
                params.add(p);
            }
            UrlEncodedFormEntity reqentity = new UrlEncodedFormEntity(params, "utf-8");
            httpPost.setEntity(reqentity);
            HttpResponse resp = httpclient.execute(httpPost);
            int status = resp.getStatusLine().getStatusCode();
            InputStream ins = null;
            HttpEntity entity = resp.getEntity();
            ins = entity.getContent();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int BUFFER = 4096;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = ins.read(data, 0, BUFFER)) != -1) {
                bout.write(data, 0, count);
            }
            if (ins != null) {
                ins.close();
            }
            return new String(bout.toByteArray(), "utf-8");
        } catch (org.apache.http.conn.ConnectTimeoutException e1) {
            if (retry <= 0) {
                return null;
            }
            return postFormAsStringWithCookieSet(urlString, headers, ps, retry - 1);
        }
        catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }
    }

    public String postWithFile(String url, InputStream inputStream, Map<String, String> headers){
        StringBuffer result = new StringBuffer();
        if (headers == null) {
            headers = new HashMap<>();
        }
//        String content = IOUtils.readTxtFile(inputStream);
        headers.put("Content-Type", "multipart/form-data");
        int ret = postAsStream(url, inputStream, headers, result);
        if (ret == 200) return result.toString();
        if (result.length() == 0) {
            return null;
        }
        return result.toString();
    }
}
