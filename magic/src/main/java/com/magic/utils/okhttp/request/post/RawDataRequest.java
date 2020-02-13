package com.magic.utils.okhttp.request.post;

import com.magic.utils.okhttp.request.PostRequest;
import com.magic.utils.okhttp.serializer.DefaultJsonSerializer;
import com.magic.utils.okhttp.serializer.JsonSerializer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.nio.charset.Charset;
import java.util.Map;

public class RawDataRequest extends PostRequest<RawDataRequest> {

    private String content;
    private String contentType;
    private Charset charset = Charset.forName("utf-8");
    private JsonSerializer jsonSerializer = new DefaultJsonSerializer();

    public RawDataRequest(OkHttpClient.Builder builder, Map<String, String> defaultHeaders) {
        super(builder, defaultHeaders);
    }

    @Override
    public RequestBody buildRequestBody() {
        MediaType contentType = MediaType.parse(String.format("%s; charset=%s", this.contentType, this.charset));
        return RequestBody.create(contentType, this.content);
    }

    public RawDataRequest text(String text) {
        this.content = text == null ? "" : text;
        this.contentType = "text/plain";
        return this;
    }

    public RawDataRequest json(String json) {
        if (json == null || json.length() == 0) {
            throw new IllegalArgumentException("json must not be null.");
        }
        this.content = json;
        this.contentType = "application/json";
        return this;
    }

    public RawDataRequest json(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object must not be null.");
        }
        this.content = jsonSerializer.toJson(object);
        this.contentType = "application/json";
        return this;
    }

    public RawDataRequest xml(String xml) {
        if (xml == null || xml.length() == 0) {
            throw new IllegalArgumentException("xml must not be null.");
        }
        this.content = xml;
        this.contentType = "application/xml";
        return this;
    }

}
