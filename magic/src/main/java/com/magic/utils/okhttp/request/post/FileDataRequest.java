package com.magic.utils.okhttp.request.post;

import com.magic.utils.okhttp.request.PostRequest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

public class FileDataRequest extends PostRequest<FileDataRequest> {

    protected byte[]  content;
    protected MediaType contentType = MediaType.parse("application/octet-stream");

    public FileDataRequest(OkHttpClient.Builder builder, Map<String, String> defaultHeaders) {
        super(builder, defaultHeaders);
    }

    public FileDataRequest contentType(String contentType) {
        if (contentType == null) {
            throw new IllegalArgumentException("contentType must not be null.");
        }
        this.contentType = MediaType.parse(contentType);
        return this;
    }

    @Override
    public RequestBody buildRequestBody() {
        RequestBody requestBody = RequestBody.create(this.contentType, this.content);
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "", requestBody)
                .build();
    }

    public FileDataRequest stream(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream must not be null.");
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            if (IOUtils.copy(inputStream, outputStream) == -1) {
                throw new IOException("Copy failed");
            }
            this.content = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Reading stream failed->", e);
        } finally {
            try {
                inputStream.close();
            } catch (final IOException ioe) {
                // ignore
            }
        }
        return this;
    }

    public FileDataRequest file(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null.");
        }
        String filename = file.getName();
        MediaType contentType = guessContentType(filename);
        try {
            this.stream(new FileInputStream(file));
            this.contentType = contentType;
            return this;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected MediaType guessContentType(String filename) {
        if (filename == null) return MediaType.parse("application/octet-stream");
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String pathname = filename.replace("#", "");
        String contentType = fileNameMap.getContentTypeFor(pathname);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return MediaType.parse(contentType);
    }
}