package com.tk.center.admin.controller;

import com.magic.springboot.dto.PageQuery;
import com.magic.utils.JsonUtils;
import com.magic.utils.UUIDGenerator;
import com.tk.center.admin.dto.ApiEntityDeleteReq;
import com.tk.center.admin.dto.ApiEntityDeleteResp;
import com.tk.center.admin.dto.ApiEntityPersistResp;
import com.tk.center.admin.dto.ApiPersistReq;
import com.tk.center.entity.BaseModel;
import com.tk.center.entity.record.OperationLog;
import com.tk.center.service.BaseService;
import com.tk.center.service.OperationService;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mingkun on 2020/02/04.
 */
public abstract class BaseController<M extends BaseModel, Q extends PageQuery> extends QueryController<M, Q> {

    @Resource
    protected BaseService baseService;

    @Resource
    protected OperationService operationService;

    protected abstract Class<M> getModelCls();

    protected String createId() {
        return UUIDGenerator.generate();
    }

    protected void onAfterPersist(M m) {

    }
    protected void onAfterDelete(String id) {

    }
    protected void onBeforePersist(M m) {

    }

    @RequestMapping("persist.{ext}")
    public ApiEntityPersistResp persist(@RequestBody ApiPersistReq<M> req) {
        boolean isAdd = baseService.get(req.getModel().getId(), req.getModel().getClass()) == null;
        this.onBeforePersist(req.getModel());
        if (StringUtils.isEmpty(req.getModel().getId())) {
            req.getModel().setId(createId());
        }
        if (isAdd) {
            req.getModel().setCreated(new Date());
        }
        M origin = baseService.get(req.getModel().getId(), getModelCls());
        boolean hasChange = !JsonUtils.toJsonWithNull(req.getModel()).equals(JsonUtils.toJsonWithNull(origin));
        if (hasChange) {
            req.getModel().setUpdated(new Date());
        }
        M m = baseService.persist(req.getModel());
        String originalModelJson = JsonUtils.toJsonWithNull(origin);
        String newModelJson = JsonUtils.toJsonWithNull(req.getModel());
        Map<String, Object> newModelMap = JsonUtils.jsonToMap(new JSONObject(newModelJson));
        Map<String, Object> originalChangeMap = new HashMap<>();
        Map<String, Object> newChangeMap = new HashMap<>();
        this.onAfterPersist(m);
        ApiEntityPersistResp ret = new ApiEntityPersistResp();

        ret.setModel(m);
        if (isAdd || hasChange) {
            if (!isAdd) {
                if ("null".equals(originalModelJson)){
                    originalModelJson = "{}";
                }
                List<Map<String, Object>> maps = baseService.getLogStatusMaps(originalModelJson, newModelJson);
                originalChangeMap = maps.get(0);
                newChangeMap = maps.get(1);
            }
            if (newModelMap.get("name") == null || "null".equals(newModelMap.get("name").toString())){
                newModelMap.put("name", "");
            }
            String name = StringUtils.isEmpty(newModelMap.get("name")) ? "" : " (" + newModelMap.get("name") + ")";
            String relatedId = newModelMap.get("id") + name;
            operationService.insertOperationLog(OperationLog.create(req.getLoginUser().getUserId(), relatedId,
                    isAdd ? "" : JsonUtils.toJsonWithNull(originalChangeMap),
                    isAdd ? JsonUtils.toJson2(newModelMap) : JsonUtils.toJsonWithNull(newChangeMap),
                    isAdd ? "新增" : "修改", "", req.getIp(), getModelCls()));
        }
        return ret;
    }

    @RequestMapping("delete.{ext}")
    public ApiEntityDeleteResp delete(@RequestBody ApiEntityDeleteReq req) {
        String modelJson = JsonUtils.toJson2(this.baseService.get(req.getId(), getModelCls()));
        Map<String, Object> modelMap = JsonUtils.jsonToMap(new JSONObject(modelJson));

        baseService.delete(req.getId(), getModelCls());
        this.onAfterDelete(req.getId());
        ApiEntityDeleteResp ret = new ApiEntityDeleteResp();

        operationService.insertOperationLog(OperationLog.create(req.getLoginUser().getUserId(),
                (String) modelMap.get("name"), modelJson, "", "刪除", "", req.getIp(),
                getModelCls()));
        return ret;
    }
}
