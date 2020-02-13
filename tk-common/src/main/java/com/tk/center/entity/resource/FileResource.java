package com.tk.center.entity.resource;

import com.tk.center.entity.BaseModel;
import com.tk.center.entity.types.MimeType;

/**
 * Created by Mingkun on 2020/02/07.
 */
public class FileResource extends BaseModel {
    protected String name;
    protected String path;
    protected MimeType mimeType;
    protected String libraryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
}
