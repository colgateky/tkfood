package com.magic.springboot.dao;

import com.magic.springboot.client.Client;
import com.magic.springboot.config.MultiConfig;
import com.magic.springboot.dto.PageQuery;
import com.magic.springboot.dto.Pagination;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xm on 2017/5/19.
 */
@Service
public class DaoHelper {
    @Resource
    protected MongoTemplate mongoTemplate;
    @Resource
    protected Client client;
    protected boolean useGlobal;

    protected MongoTemplate get() {
        if (MultiConfig.enableMulti && !useGlobal && MultiConfig.MultiListener != null) {
            return MultiConfig.MultiListener.getMongoTemplate(client.getClientId());
        }
        return mongoTemplate;
    }

    public <T> boolean removeById(Object id, Class<T> cls) {
        return this.removeById(id, cls, "id");
    }

    public <T> boolean removeById(Object id, Class<T> cls, String keyFieldName) {
        BasicQuery q = this.createQuery();
        Criteria c = new Criteria();
        c.and(keyFieldName).is(id);
        q.addCriteria(c);
        Object ret = this.findAndRemove(q, cls);
        return ret != null;
    }
    public <T> List<T> findIds(PageQuery query, Class<T> cls) {
        Query q = buildQuery(query, false);
        q.fields().include("id");
        List<T> ts = get().find(q, cls);
        return ts;
    }
    public long getCount(Query query, Class cls){
        long count = this.get().count(query, cls);
        return count;
    }
    public long getCount(PageQuery query, Class cls) {
        BasicQuery q = buildQuery(query, false);
        return this.get().count(q, cls);
    }
    public <T> void insert(List<T> objs, int batchSize) {
        if(objs.size() > 0) {
            if(batchSize <= 0) {
                this.get().insertAll(objs);
            } else {
                List<T> batch = new ArrayList();
                Iterator<T> var4 = objs.iterator();

                while(var4.hasNext()) {
                    T item = var4.next();
                    batch.add(item);
                    if(batch.size() >= batchSize) {
                        this.get().insertAll(batch);
                        batch.clear();
                    }
                }

                if(batch.size() > 0) {
                    this.get().insertAll(batch);
                }

            }
        }
    }
    public BasicQuery buildQuery(PageQuery query, boolean sortFlag) {
        BasicQuery q = createQuery();
        if (query == null) {
            return q;
        }
        Criteria c = new Criteria();
        query.buildCriteria(c);
        q.addCriteria(c);
        if (sortFlag) {
            DBObject sort = createObject();
            query.buildSort(sort);
            q.setSortObject(sort);
        }
        return q;
    }
    public <T> Page<T> getPage(PageQuery pageQuery, Class cls)  {
        Query query = buildQuery(pageQuery, true);
        return this.getPage(pageQuery.getPageNo(), pageQuery.getPageSize(), query, cls);
    }
    public <T> Page<T> getPage(PageQuery pageQuery, Query query, Class cls)  {
        return this.getPage(pageQuery.getPageNo(), pageQuery.getPageSize(), query, cls);
    }
    public <T> Page<T> getPage(int pageNo, int pageSize, Query query, Class cls) {
        long totalCount = this.get().count(query, cls);
        Pagination<T> page = new Pagination(pageNo, pageSize, totalCount);
        query.skip((pageNo - 1) * pageSize);
        query.limit(pageSize);
        List<T> datas = this.find(query, cls);
        page.setContent(datas);
        return page;
    }

    public BasicQuery createQuery() {
        return new BasicQuery(this.createObject());
    }

    public BasicQuery createIdQuery(String id) {
        BasicQuery q = createQuery();
        Criteria c = new Criteria();
        c.and("id").is(id);
        q.addCriteria(c);
        return q;
    }
    public DBObject createObject() {
        return BasicDBObjectBuilder.start().get();
    }

    public <T> List<T> find(PageQuery query, Class<T> cls) {
        BasicQuery q = buildQuery(query, true);
        return this.get().find(q, cls);
    }
    public <T> List<T> find(Query query, Class<T> cls) {
        return this.get().find(query, cls);
    }

    public <T> T findOne(Query query, Class<T> cls) {
        return this.get().findOne(query, cls);
    }

    public <T> T findOne(PageQuery query, Class<T> cls) {
        return this.get().findOne(buildQuery(query, true), cls);
    }

    public <T> List<T> findAll(Class<T> cls) {
        return this.get().findAll(cls);
    }

    public <T> T findAndModify(Query query, Update update, Class<T> cls) {
        return this.get().findAndModify(query, update, cls);
    }
    public <T> WriteResult remove(PageQuery query, Class<T> cls) {
        return this.get().remove(buildQuery(query, false), cls);
    }
    public <T> T findAndRemove(Query query, Class<T> cls) {
        return this.get().findAndRemove(query, cls);
    }

    public <T> int remove(Query query, Class<T> cls) {
        WriteResult wr = this.get().remove(query, cls);
        return wr != null?wr.getN():0;
    }

    public void updateFirst(Query query, Update update, Class cls) {
        this.get().updateFirst(query, update, cls);
    }
    public void updateMulti(Query query, Update update, Class cls) {
        this.get().updateMulti(query, update, cls);
    }

    public <T> T save(T bean) {
        this.get().save(bean);
        return bean;
    }

    public <T> T insert(T bean) {
        this.get().insert(bean);
        return bean;
    }

    public <T> T findById(String id, Class<T> cls) {
        return this.get().findById(id, cls);
    }

    public <T> T findById(String id, String collectionName, Class<T> cls) {
        return this.get().findById(id, cls, collectionName);
    }

    public <T> void fetchCollections(PageQuery query, CollectionIterator<T> iter, Class<T> cls) {
        this.fetchCollections(buildQuery(query, true), iter, cls);
    }
    public <T> void fetchCollections(Query query, CollectionIterator<T> iter, Class<T> cls) {
        CloseableIterator<T> citer = this.get().stream(query, cls);

        while(citer.hasNext()) {
            iter.onFetchOne(citer.next());
        }

    }

    public <T> void fetchCollections(PageQuery query, CollectionBreakIterator<T> iter, Class<T> cls) {
        this.fetchCollections(buildQuery(query, true), iter, cls);
    }
    public <T> void fetchCollections(Query query, CollectionBreakIterator<T> iter, Class<T> cls) {
        CloseableIterator<T> citer = this.get().stream(query, cls);

        while(citer.hasNext()) {
            if (iter.onFetchOne(citer.next())) {
                break;
            }
        }

    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isUseGlobal() {
        return useGlobal;
    }

    public void setUseGlobal(boolean useGlobal) {
        this.useGlobal = useGlobal;
    }
}
