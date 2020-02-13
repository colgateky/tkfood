package com.magic.utils.okhttp.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultJsonSerializer implements JsonSerializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJsonSerializer.class);

    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public <T> T fromJson(String content, Class<T> classOfT) {
        try {
            return objectMapper.readValue(content, classOfT);
        } catch (IOException e) {
            LOGGER.error("from json error", e);
            throw new IllegalArgumentException("from json error.");
        }
    }

    @Override
    public <T> T fromJson(String content, TypeReference<T> valueTypeRefT) {
        try {
            return objectMapper.readValue(content, valueTypeRefT);
        } catch (IOException e) {
            LOGGER.error("from json error", e);
            throw new IllegalArgumentException("from json error.");
        }
    }

    @Override
    public String toJson(Object src) {
        try {
            return objectMapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            LOGGER.error("to json string error", e);
            throw new IllegalArgumentException("src to json error.");
        }
    }
}
