package com.magic.utils.okhttp.request;

import com.magic.utils.okhttp.response.HttpResponse;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public abstract class AbstractRequest<Req extends HttpRequest> implements HttpRequest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractRequest.class);

    protected String url;

    protected Map<String, String> headers = new ConcurrentHashMap<>();

    protected Map<String, String> queryParams = new ConcurrentHashMap<>();

    protected OkHttpClient.Builder builder;
    protected Map<String, String> defaultHeaders;

    public AbstractRequest(OkHttpClient.Builder builder, Map<String, String> defaultHeaders) {
        this.builder = builder;
        this.defaultHeaders = defaultHeaders;
    }

    @Override
    public Req url(String url) {
        this.url = url;
        return (Req) this;
    }

    @Override
    public Req queryString(String name, String value) {
        if (name == null || value == null) {
            throw new IllegalArgumentException("name and value must not be null.");
        }
        this.queryParams.put(name, value);
        return (Req) this;
    }

    @Override
    public Req header(String key, String value) {
        return _header(key, value);
    }

    protected Req _header(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key and value must not be null.");
        }
        this.headers.put(key, value);
        return (Req) this;
    }

    @Override
    public Req removeHeader(String key) {
        return _removeHeader(key);
    }

    protected Req _removeHeader(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        this.headers.remove(key);
        this.defaultHeaders.remove(key);
        return (Req) this;
    }

    public abstract RequestBody buildRequestBody();

    public abstract Request buildRequest(Request.Builder builder);

    protected void mergeHeader(Request.Builder builder) {
        if (this.defaultHeaders != null && this.defaultHeaders.size() > 0) {
            for (Map.Entry<String, String> entry : this.defaultHeaders.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        if (this.headers != null && this.headers.size() > 0) {
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
    }

    private Call buildCall(Request request) {
        return builder.build().newCall(request);
    }

    @Override
    public HttpResponse execute() {
        Request.Builder builder = new Request.Builder();
        mergeHeader(builder);
        builder.url(this.buildUrl());
        Request request = buildRequest(builder);
        Call call = buildCall(request);
        try {
            Response response = call.execute();
            HttpResponse httpResponse = new HttpResponse(response);
            if (response.isSuccessful()) {
                return httpResponse;
            }
            throw new RuntimeException(response.body() == null ? response.message() : response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String buildUrl() {
        StringBuilder sb = new StringBuilder(this.url);
        if (this.url.contains("?")) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        if (this.queryParams != null && this.queryParams.size() > 0) {
            for (Map.Entry<String, String> entry : this.queryParams.entrySet()) {
                try {
                    sb.append(entry.getKey()).append("=")
                            .append(URLEncoder.encode(entry.getValue(), "utf-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("encode query params failed.", e.getMessage());
                    throw new IllegalArgumentException("build url, unsupported encoding.");
                }
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }
}
