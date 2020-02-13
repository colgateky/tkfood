package com.magic.springboot.service;

import com.magic.springboot.dao.DaoHelper;
import com.magic.springboot.dto.PageQuery;
import com.mongodb.WriteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xm on 2017/11/4.
 */
@Service
public class MagicServiceImpl implements MagicService {
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
    public <M> M getOne(PageQuery query, Class<M> cls) {
        return daoHelper.findOne(query, cls);
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
    public boolean delete(PageQuery query, Class cls) {
        WriteResult wr = daoHelper.remove(query, cls);
        return wr.getN() > 0;
    }

    @Override
    public <M> M get(String id, Class<M> cls) {
        return daoHelper.findById(id, cls);
    }

    @Override
    public <M> List<M> findIds(PageQuery query, Class<M> cls) {
        return daoHelper.findIds(query, cls);
    }

    @Override
    public <M> void updateField(String id, String field, Object value, Class<M> cls) {
        Map<String, Object> fields = new HashMap<>();
        fields.put(field, value);
        updateFields(id, fields, cls);
    }

    @Override
    public <M> void updateFields(String id, Map<String, Object> fields, Class<M> cls) {
        BasicQuery q = daoHelper.createIdQuery(id);
        Update update = new Update();
        for (Map.Entry<String, Object> e : fields.entrySet()) {
            update.set(e.getKey(), e.getValue());
        }
        daoHelper.updateFirst(q, update, cls);
    }

    public DaoHelper getDaoHelper() {
        return daoHelper;
    }

    public void setDaoHelper(DaoHelper daoHelper) {
        this.daoHelper = daoHelper;
    }
}
