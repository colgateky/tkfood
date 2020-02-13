package com.tk.center.admin.dto;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class ApiQueryReq<T> extends ApiAuthReq {
    protected T query;

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }
}
