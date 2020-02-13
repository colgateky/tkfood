package com.tk.center.admin.controller;

import com.magic.springboot.auth.annotation.AuthResource;
import com.magic.springboot.service.MagicService;
import com.magic.utils.UUIDGenerator;
import com.tk.center.admin.dto.*;
import com.tk.center.common.ApiErrorCode;
import com.tk.center.common.ServerConfig;
import com.tk.center.dto.resource.FileResourceLibraryQuery;
import com.tk.center.dto.resource.FileResourceQuery;
import com.tk.center.entity.member.Member;
import com.tk.center.entity.resource.FileResource;
import com.tk.center.entity.resource.FileResourceLibrary;
import com.tk.center.entity.types.MimeType;
import com.tk.center.service.ResourceService;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mingkun on 2020/02/07.
 */
@RestController
@RequestMapping("/admin/resource")
public class ResourceController {

    @Resource
    protected ServerConfig serverConfig;
    @Resource
    protected MagicService magicService;
    @Resource
    protected ResourceService resourceService;

    private final String imgSuffix = "*jpg*png*gif*";
    private final String videoSuffix = "*mp4*";

    private String getSuffix(String filename) {
        filename = filename.toLowerCase();
        int i = filename.lastIndexOf('.');
        if (i < 0) {
            return "";
        }
        return filename.substring(i+1);
    }
    private MimeType getMimeType(String suffix) {
        if (imgSuffix.contains(suffix)) {
            return MimeType.IMAGE;
        }
        if (videoSuffix.contains(suffix)) {
            return MimeType.VIDEO;
        }
        return MimeType.UNKNOWN;
    }

    @RequestMapping("image_resource.{ext}")
    public void getImageResource(String id, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(id)) {
            response.sendError(404);
            return;
        }
        String path = resourceService.getResourcePath(id);
        byte[] data = resourceService.getFileData(path);

        response.setHeader("Content-Type", "image/png");
        response.getOutputStream().write(data);
    }

    @RequestMapping("avatar_resource.{ext}")
    public void getAvatar(String memberId, HttpServletResponse response) throws IOException {
        String path = resourceService.getAvatarPath(memberId);
        Member member = magicService.get(memberId, Member.class);
        if (member != null){
            String avatar = member.getAvatar();
            if (!StringUtils.isEmpty(avatar)){
                int index = avatar.indexOf("?");
                if (index > 0){
                    avatar = avatar.substring(0, index);
                }
            }
            path = avatar;
        }

        byte[] data = resourceService.getFileData(path);
        response.setHeader("Content-Type", "image/png");
        response.getOutputStream().write(data);
    }

    @RequestMapping("upload")
    @AuthResource("resource_upload")
    public ApiEntityPersistResp upload(@RequestParam("file") MultipartFile file, String libraryId) throws IOException {
        String resId = UUIDGenerator.generate();
        String filename = file.getOriginalFilename().toLowerCase();
        String suffix = getSuffix(filename);
        MimeType mimeType = getMimeType(suffix);

        String path = resourceService.getResourcePath(resId);
        resourceService.upload(path, file.getInputStream());

        FileResource res = new FileResource();
        res.setId(resId);
        res.setMimeType(mimeType);
        res.setLibraryId(libraryId);
        res.setName(filename);
        res.setPath(path);
        res.setCreated(new Date());
        magicService.persist(res);

        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        ret.setModel(res);
        return ret;
    }

    @RequestMapping("upload_list")
    @AuthResource("resource_upload")
    public ApiListResp upload(HttpServletRequest request, String libraryId, HttpSession session) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file[]");
        List<Object> list = new ArrayList<>();
        for (MultipartFile file : files) {
            ApiEntityPersistResp r = upload(file, libraryId);
            list.add(r.getModel());
        }
        ApiListResp ret = new ApiListResp();
        ret.setList(list);
        return ret;
    }

    @RequestMapping("file_resources.{ext}")
    public ApiPageResp fileResources(@RequestBody ApiQueryReq<FileResourceQuery> req){
        FileResourceQuery query = req.getQuery();
        Page<FileResource> page = magicService.getPage(query, FileResource.class);

        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("file_resource_delete.{ext}")
    @AuthResource("resource_delete")
    public ApiEntityDeleteResp deleteFileResource(@RequestBody ApiEntityDeleteReq req){
        String id = req.getId();
        if (StringUtils.isEmpty(id)){
            ApiErrorCode.PERSIST_FILERESOURCE_FAILED.build("刪除資源所需的資料缺失").throwSelf();
        }
        boolean result = magicService.delete(id, FileResource.class);
        if(!result)
            ApiErrorCode.PERSIST_FILERESOURCE_FAILED.build("刪除失敗").throwSelf();
        return(new ApiEntityDeleteResp());

    }

    @RequestMapping("file_resource_update.{ext}")
    @AuthResource("resource_edit")
    public ApiEntityPersistResp updateFileResource(@RequestBody ApiPersistReq<FileResource> req){
        FileResource source = req.getModel();
        if (source == null){
            ApiErrorCode.PERSIST_FILERESOURCE_FAILED.build("更新資源所需的資料缺失").throwSelf();
        }
        String id = source.getId();
        if (StringUtils.isEmpty(id)){
            ApiErrorCode.PERSIST_FILERESOURCE_FAILED.build("更新資源所需的資料缺失").throwSelf();
        }
        FileResource s = magicService.get(id, FileResource.class);
        if (s == null){
            ApiErrorCode.PERSIST_FILERESOURCE_FAILED.build("找不到要更新的資源").throwSelf();
        }
        s.setName(source.getName());
        s.setLibraryId(source.getLibraryId());
        source = magicService.persist(s);

        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        ret.setModel(source);
        return ret;
    }

    @RequestMapping("file_resource_libraries.{ext}")
    @AuthResource("resource_library")
    public ApiPageResp getFileResourceLibraries(@RequestBody ApiQueryReq<FileResourceLibraryQuery> req){
        FileResourceLibraryQuery query = req.getQuery();
        Page<FileResourceLibrary> page = magicService.getPage(query, FileResourceLibrary.class);
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("file_resource_library_persist.{ext}")
    @AuthResource("resource_library_edit")
    public ApiEntityPersistResp persistFileResource(@RequestBody ApiPersistReq<FileResourceLibrary> req){
        FileResourceLibrary library = req.getModel();
        if (library == null || StringUtils.isEmpty(library.getName())){
            ApiErrorCode.PERSIST_FILERESOURCELIBRARY_FAILED.build("保存資源庫的資料缺失").throwSelf();
        }
        FileResourceLibraryQuery query = new FileResourceLibraryQuery();
        query.setName(library.getName());
        FileResourceLibrary result = magicService.getOne(query, FileResourceLibrary.class);
        if(StringUtils.isEmpty(library.getId())){
            if (result != null){
                ApiErrorCode.PERSIST_FILERESOURCELIBRARY_FAILED.build("資源庫名稱已存在").throwSelf();
            }
            library.setId(UUIDGenerator.generate());
        }else{
            if(result != null && library.getId().equals(result.getId())){
                ApiErrorCode.PERSIST_FILERESOURCELIBRARY_FAILED.build("資源庫名稱已存在").throwSelf();
            }
        }
        if (library.getCreated() == null){
            library.setCreated(new Date());
        }
        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        ret.setModel(magicService.persist(library));
        return ret;
    }

    @RequestMapping("file_resource_library_delete.{ext}")
    @AuthResource("resource_library_delete")
    public ApiEntityDeleteResp deleteFileResourceLibrary(@RequestBody ApiEntityDeleteReq req){
        magicService.delete(req.getId(), FileResourceLibrary.class);
        return(new ApiEntityDeleteResp());

    }

    @RequestMapping("all_file_resource_libraries.{ext}")
    @AuthResource("resource")
    public ApiListResp getAllFileResourceLibraries(@RequestBody ApiAuthReq req){
        FileResourceLibraryQuery query = new FileResourceLibraryQuery();
        List<FileResourceLibrary> list = magicService.getList(query, FileResourceLibrary.class);
        ApiListResp ret = new ApiListResp();
        ret.setList(list);
        return ret;
    }
}
