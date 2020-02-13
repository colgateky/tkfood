package com.tk.center.service;

import java.io.InputStream;

public interface ResourceService {
    void upload(String path, InputStream inputStream);

    byte[] getFileData(String path);

    String getResourcePath(String id);

    String getAvatarPath(String playerId);

    String getAvatarPathWithoutResource(String playerId);
}
