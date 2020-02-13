package com.tk.center.admin.dto;

/**
 * Created by Mingkun on 2020/02/03.
 */
public class ApiPersistReq<T> extends ApiAuthReq{
    protected T model;

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
