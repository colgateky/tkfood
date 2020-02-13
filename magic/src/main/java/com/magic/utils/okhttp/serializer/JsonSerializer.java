package com.magic.utils.okhttp.serializer;

import com.fasterxml.jackson.core.type.TypeReference;

public interface JsonSerializer {

    <T> T fromJson(String content, Class<T> classOfT);

    <T> T fromJson(String content, TypeReference<T> valueTypeRefT);

    String toJson(Object src);
}
