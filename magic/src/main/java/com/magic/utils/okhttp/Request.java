package com.magic.utils.okhttp;


import com.magic.utils.okhttp.request.GetRequest;
import com.magic.utils.okhttp.request.PostRequest;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class Request {

    private static final Logger LOGGER = LoggerFactory.getLogger(Request.class);

    /**
     * 连接超时时间, 单位：毫秒
     */
    public static final long DEFAULT_CONNECT_TIMEOUT = 5000;

    /**
     * 读取超时时间, 单位：毫秒
     */
    public static final long DEFAULT_READ_TIMEOUT = 30000;

    /**
     * 写入超时时间, 单位：毫秒
     */
    public static final long DEFAULT_WRITE_TIMEOUT = 30000;


    // OkHttp
    private OkHttpClient.Builder builder;

    private Map<String, String> defaultHeaders;

    public Request() {
        this.builder = new OkHttpClient.Builder();
        //连接超时时间
        this.builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        //读取超时时间
        this.builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        //写入超时时间
        this.builder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
//        this.builder
//                .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
//                .followSslRedirects(false);  //https的重定向也自己处理
        this.defaultHeaders = new ConcurrentHashMap<>(10);

        TrustManagerFactory trustManagerFactory = null;
        try {
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] x509Certificates = new X509Certificate[0];
                    return x509Certificates;
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            this.builder.sslSocketFactory(sslSocketFactory, trustManager).build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.error("set sslSocketFactory error", e);
            throw new RuntimeException("set sslSocketFactory error");
        }

    }

    public Request connectTimeout(int connectTimeout) {
        if (connectTimeout <= 0) {
            LOGGER.error("Connect timeout must not be less than 0.");
            throw new IllegalArgumentException("Connect timeout must not be less than 0.");
        }
        this.builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    public Request readTimeout(int readTimeout) {
        if (readTimeout <= 0) {
            LOGGER.error("Read timeout must not be less than 0.");
            throw new IllegalArgumentException("Read timeout must not be less than 0.");
        }
        this.builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    public Request writeTimeout(int writeTimeout) {
        if (writeTimeout <= 0) {
            LOGGER.error("Write timeout must not be less than 0.");
            throw new IllegalArgumentException("Write timeout must not be less than 0.");
        }
        this.builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    public Request connectionPool(ConnectionPool connectionPool) {
        this.builder.connectionPool(connectionPool);
        return this;
    }

    public Request defaultHeader(String name, String value) {
        this.defaultHeaders.put(name, value);
        return this;
    }

    public Map<String, String> getDefaultHeaders() {
        return new ConcurrentHashMap<>(this.defaultHeaders);
    }

    public OkHttpClient.Builder getBuilder() {
        return builder;
    }

    public Request addInterceptor(Interceptor customInterceptor) {
        this.builder.addInterceptor(customInterceptor);
        return this;
    }

    public GetRequest get(String url) {
        return new GetRequest(builder, getDefaultHeaders()).url(url);
    }

    public PostRequest post(String url) {
        return new PostRequest(builder, getDefaultHeaders()).url(url);
    }

}


