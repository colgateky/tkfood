package com.magic.springboot.service;

import com.magic.springboot.dto.PageQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by xm on 2017/11/4.
 */
public interface MagicService {
    <M> Page<M> getPage(PageQuery query, Class<M> cls);
    <M> List<M> getList(PageQuery query, Class<M> cls);
    <M> M getOne(PageQuery query, Class<M> cls);
    long count(Class cls);
    long count(PageQuery query, Class cls);
    <M> M persist(M model);
    boolean delete(String id, Class cls);
    boolean delete(PageQuery query, Class cls);
    <M> M get(String id, Class<M> cls);
    <M> List<M> findIds(PageQuery query, Class<M> cls);
    <M> void updateField(String id, String field, Object value, Class<M> cls);
    <M> void updateFields(String id, Map<String, Object> fields, Class<M> cls);
}
