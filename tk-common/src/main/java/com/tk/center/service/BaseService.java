package com.tk.center.service;

import com.magic.springboot.dto.PageQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface BaseService {
    <M> Page<M> getPage(PageQuery query, Class<M> cls);
    <M> List<M> getList(PageQuery query, Class<M> cls);
    long count(Class cls);
    long count(PageQuery query, Class cls);
    <M> M persist(M model);
    boolean delete(String id, Class cls);
    <M> M get(String id, Class<M> cls);
    List<Map<String, Object>> getLogStatusMaps(String originChangeJson, String afterChangeJson);

}
