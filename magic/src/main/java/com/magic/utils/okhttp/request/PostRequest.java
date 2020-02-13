package com.magic.utils.okhttp.request;

import com.magic.utils.okhttp.request.post.BinaryDataRequest;
import com.magic.utils.okhttp.request.post.FileDataRequest;
import com.magic.utils.okhttp.request.post.FormDataRequest;
import com.magic.utils.okhttp.request.post.RawDataRequest;
import com.magic.utils.okhttp.serializer.DefaultJsonSerializer;
import com.magic.utils.okhttp.serializer.JsonSerializer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

public class PostRequest<Req extends PostRequest> extends AbstractRequest<PostRequest> {

    protected JsonSerializer jsonSerializer = new DefaultJsonSerializer();

    public PostRequest(OkHttpClient.Builder builder, Map<String, String> defaultHeaders) {
        super(builder, defaultHeaders);
    }

    @Override
    public RequestBody buildRequestBody() {
        throw new UnsupportedOperationException("unsupported operation.");
    }

    @Override
    public Request buildRequest(Request.Builder builder) {
        return builder.post(buildRequestBody()).build();
    }

    @Override
    public Req header(String key, String value) {
        throw new UnsupportedOperationException("unsupported operation.");
    }

    @Override
    public Req removeHeader(String key) {
        throw new UnsupportedOperationException("unsupported operation.");
    }

    @Override
    public Req url(String url) {
        return (Req) super.url(url);
    }

    public Req serializer(JsonSerializer jsonSerializer) {
        if (jsonSerializer != null) {
            this.jsonSerializer = jsonSerializer;
        }
        return (Req) this;
    }

    // form
    public FormDataRequest form() {
        return new FormDataRequest(this.builder, this.defaultHeaders).url(this.getUrl());
    }

    // raw
    public RawDataRequest raw() {
        return new RawDataRequest(this.builder, this.defaultHeaders).url(this.getUrl());
    }

    // multipartFile
    public FileDataRequest multipartFile() {
        return new FileDataRequest(this.builder, this.defaultHeaders).url(this.getUrl());
    }

    //binary
    public BinaryDataRequest binary() {
        return (BinaryDataRequest) new BinaryDataRequest(this.builder, this.defaultHeaders).url(this.getUrl());
    }
}
