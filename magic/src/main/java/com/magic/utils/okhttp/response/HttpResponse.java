package com.magic.utils.okhttp.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.magic.utils.okhttp.serializer.DefaultJsonSerializer;
import com.magic.utils.okhttp.serializer.JsonSerializer;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {

    private okhttp3.Response rawResponse;

    private JsonSerializer jsonSerializer = new DefaultJsonSerializer();

    public HttpResponse(Response rawResponse) {
        this.rawResponse = rawResponse;
    }

    public HttpResponse serializer(JsonSerializer jsonSerializer) {
        if (jsonSerializer != null) {
            this.jsonSerializer = jsonSerializer;
        }
        return this;
    }

    public String asString() {
        try {
            return rawResponse.body() == null ? "" : rawResponse.body().string(); //string() auto close
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T asBean(Class<T> classOfT) {
        ResponseBody responseBody = rawResponse.body();
        if (responseBody == null) return null;
        try {
            return jsonSerializer.fromJson(responseBody.string(), classOfT); //string() auto close
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T asBean(TypeReference<T> valueTypeRefT) {
        ResponseBody responseBody = rawResponse.body();
        if (responseBody == null) return null;
        try {
            return jsonSerializer.fromJson(responseBody.string(), valueTypeRefT); //string() auto close
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


//    public void asFile(File saveFile) {
//    }

//    public void asStream(OutputStream out) {
//        try {
//            IOUtils.copy(this.rawResponse.body().byteStream(), out);
//        } finally {
//            IOUtils.closeQuietly(this.rawResponse);
//        }
//    }
}
