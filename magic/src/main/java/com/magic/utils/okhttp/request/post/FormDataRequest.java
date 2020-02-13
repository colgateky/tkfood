package com.magic.utils.okhttp.request.post;

import com.magic.utils.okhttp.request.PostRequest;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FormDataRequest extends PostRequest<FormDataRequest> {

    private Map<String, String> formParams = new ConcurrentHashMap<>();

    public FormDataRequest(OkHttpClient.Builder builder, Map<String, String> defaultHeaders) {
        super(builder, defaultHeaders);
    }

    @Override
    public FormDataRequest header(String key, String value) {
        return (FormDataRequest) _header(key, value);
    }

    @Override
    public FormDataRequest removeHeader(String key) {
        return (FormDataRequest) _removeHeader(key);
    }

    public FormDataRequest param(String name, String value) {
        if (name == null || value == null) {
            throw new IllegalArgumentException("form param name and value must not be null.");
        }
        this.formParams.put(name, value);
        return this;
    }

    public FormDataRequest param(Map<String, String> parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("form parameters must not be null.");
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            this.param(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public RequestBody buildRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        if (this.formParams != null && this.formParams.size() > 0) {
            for (Map.Entry<String, String> entry : this.formParams.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

}
