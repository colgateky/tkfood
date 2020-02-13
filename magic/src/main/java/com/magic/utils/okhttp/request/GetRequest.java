package com.magic.utils.okhttp.request;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

public class GetRequest extends AbstractRequest<GetRequest> {

    public GetRequest(OkHttpClient.Builder builder, Map<String, String> defaultHeaders) {
        super(builder, defaultHeaders);
    }

    @Override
    public RequestBody buildRequestBody() {
        return null;
    }

    @Override
    public Request buildRequest(Request.Builder builder) {
        return builder.build();
    }
}
