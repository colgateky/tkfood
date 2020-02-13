package com.tk.center.service;

import com.magic.springboot.dao.DaoHelper;
import com.magic.springboot.dto.PageQuery;
import com.magic.utils.JsonUtils;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mingkun on 2020/02/03.
 */
@Service
public class BaseServiceImpl implements BaseService {

    @Resource
    protected DaoHelper daoHelper;


    @Override
    public <M> Page<M> getPage(PageQuery query, Class<M> cls) {
        return daoHelper.getPage(query, cls);
    }

    @Override
    public <M> List<M> getList(PageQuery query, Class<M> cls) {
        return daoHelper.find(query, cls);
    }

    @Override
    public long count(Class cls) {
        return daoHelper.getCount(daoHelper.createQuery(), cls);
    }

    @Override
    public long count(PageQuery query, Class cls) {
        return daoHelper.getCount(query, cls);
    }

    @Override
    public <M> M persist(M model) {
        return daoHelper.save(model);
    }

    @Override
    public boolean delete(String id, Class cls) {
        return daoHelper.removeById(id, cls);
    }

    @Override
    public <M> M get(String id, Class<M> cls) {
        return daoHelper.findById(id, cls);
    }

    @Override
    public List<Map<String, Object>> getLogStatusMaps(String originalModelJson, String newModelJson) {
        Map<String, Object> originModelMap = JsonUtils.jsonToMap(new JSONObject(originalModelJson));
        Map<String, Object> newModelMap = JsonUtils.jsonToMap(new JSONObject(newModelJson));
        Map<String, Object> originalChangeMap = new HashMap<>();
        Map<String, Object> newChangeMap = new HashMap<>();

        originModelMap.forEach((k, v) -> {
            if (!originModelMap.get(k).equals(newModelMap.get(k))) {
                originalChangeMap.put(k, v);
                newChangeMap.put(k, newModelMap.get(k));
            }
        });

        List<Map<String, Object>> result = new ArrayList<>();
        result.add(originalChangeMap);
        result.add(newChangeMap);
        return result;
    }
}
