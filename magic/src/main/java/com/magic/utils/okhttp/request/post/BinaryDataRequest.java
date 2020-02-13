package com.magic.utils.okhttp.request.post;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.util.Map;

public class BinaryDataRequest extends FileDataRequest {

    public BinaryDataRequest(OkHttpClient.Builder builder, Map<String, String> defaultHeaders) {
        super(builder, defaultHeaders);
    }

    public BinaryDataRequest contentType(String contentType) {
        if (contentType == null) {
            throw new IllegalArgumentException("contentType must not be null.");
        }
        this.contentType = MediaType.parse(contentType);
        return this;
    }

    @Override
    public RequestBody buildRequestBody() {
        return RequestBody.create(this.contentType, this.content);
    }

}
