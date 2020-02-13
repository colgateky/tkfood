package com.tk.center.service;

import com.magic.springboot.dao.DaoHelper;
import com.magic.springboot.service.LockFlow;
import com.magic.springboot.service.RedisLockService;
import com.tk.center.entity.LongVariable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Mingkun on 2020/02/06.
 */
@Service
public class VariableServiceImpl implements VariableService {
    @Resource
    protected DaoHelper daoHelper;
    @Resource
    protected RedisLockService redisLockService;

    private long getLongAndIncInside(String key) {
        LongVariable lv = daoHelper.findById(key, LongVariable.class);
        if (lv == null) {
            lv = new LongVariable();
            lv.setId(key);
            lv.setValue(1);
            lv.setCreated(new Date());
        }
        if (lv.getValue() == 0) {
            lv.setValue(1);
        }
        long r = lv.getValue();
        lv.setValue(lv.getValue()+1);
        daoHelper.save(lv);
        return r;
    }
    @Override
    public long getLongAndInc(String key) {
        long[] ret = new long[1];
        while(!redisLockService.lockNoError(key, new LockFlow() {
            @Override
            public void run() {
                ret[0] = getLongAndIncInside(key);
            }
        }));
        return ret[0];
    }
    @Override
    public String generateId(Class entityCls, int size) {
        return generateId(entityCls, size, "");
    }

    @Override
    public String generateId(Class entityCls, int size, String prefix) {
        StringBuffer ret = new StringBuffer();
        while(!redisLockService.lockNoError(entityCls.getSimpleName(), new LockFlow() {
            @Override
            public void run() {
                while (true) {
                    long id = getLongAndIncInside(entityCls.getSimpleName());
                    String s = prefix + String.format("%0" + size + "d", id);
                    Object obj = daoHelper.findById(s, entityCls);
                    if (obj == null) {
                        ret.append(s);
                        break;
                    }
                }
            }
        }));
        return ret.toString();
    }
}
