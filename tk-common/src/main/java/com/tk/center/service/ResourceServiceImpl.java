package com.tk.center.service;

import com.magic.utils.CryptUtils;
import com.magic.utils.JsonUtils;
import com.magic.utils.http.HttpUtils;
import com.tk.center.common.ApiErrorCode;
import com.tk.center.common.ServerConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Mingkun on 2020/02/07.
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Resource
    protected ServerConfig serverConfig;
    protected HttpUtils httpUtils = new HttpUtils();

    @Override
    public void upload(String path, InputStream inputStream) {
        String url = serverConfig.getUploadUrl();
        url += "/upload?name=" + path;
        String content = httpUtils.postWithFile(url, inputStream, null);
        if (StringUtils.isEmpty(content)){
            ApiErrorCode.FILE_UPLOAD_ERROR.throwSelf();
        }
        Map<String, Object> m = JsonUtils.parseJson(content, Map.class);
        Integer result = (Integer)m.get("result");
        if (result != 0){
            ApiErrorCode.FILE_UPLOAD_ERROR.throwSelf();
        }
    }

    @Override
    public byte[] getFileData(String path) {
        String url = serverConfig.getUploadUrl();
        url += "/" + path;
        return httpUtils.getAsBytes(url);
    }

    @Override
    public String getResourcePath(String id) {
        File remoteFile = new File(new File(new  File("resource", id.substring(0, 2)), id.substring(2, 4)), id);
        return remoteFile.getPath().replace("\\","/");
    }

    @Override
    public String getAvatarPath(String memberId) {
        String fileName = CryptUtils.getMd5(memberId) + "_" + serverConfig.getClientId() + ".jpg";
        File remoteFile = new File(new File("resource", "avatar"), fileName);
        String avatar = remoteFile.getPath().replace("\\","/");
        return avatar;
    }

    @Override
    public String getAvatarPathWithoutResource(String memberId) {
        String fileName = CryptUtils.getMd5(memberId) + "_" + serverConfig.getClientId() + ".jpg";
        File remoteFile = new File("avatar", fileName);
        String avatar = remoteFile.getPath().replace("\\","/");
        return avatar;
    }
}
