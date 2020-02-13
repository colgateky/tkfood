package com.tk.center.admin.controller.config;

import com.magic.springboot.auth.annotation.AuthResource;
import com.magic.springboot.service.MagicService;
import com.magic.utils.JsonUtils;
import com.magic.utils.UUIDGenerator;
import com.tk.center.admin.dto.*;
import com.tk.center.common.ApiErrorCode;
import com.tk.center.dto.config.SysConfigCategoryQuery;
import com.tk.center.dto.config.SysConfigQuery;
import com.tk.center.entity.config.SysConfig;
import com.tk.center.entity.config.SysConfigCategory;
import com.tk.center.entity.record.OperationLog;
import com.tk.center.service.BaseService;
import com.tk.center.service.ConfigService;
import com.tk.center.service.OperationService;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mingkun on 2020/02/05.
 */
@RestController
@RequestMapping("/admin/system")
public class SystemController {

    @Resource
    protected ConfigService configService;
    @Resource
    protected BaseService baseService;
    @Resource
    protected MagicService magicService;
    @Resource
    protected OperationService operationService;

    @RequestMapping("sys_configs.{ext}")
    public ApiPageResp getSysConfigPage(@RequestBody ApiQueryReq<SysConfigQuery> req) {
        Page<SysConfig> page = magicService.getPage(req.getQuery(), SysConfig.class);
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("sys_config_persist.{ext}")
    @AuthResource({"system_config_edit"})
    public ApiEntityPersistResp persistSysConfig(@RequestBody ApiPersistReq<SysConfig> req) {
        Map<String, Object> originalChangeMap = new HashMap<>();
        Map<String, Object> newChangeMap = new HashMap<>();
        SysConfig model = req.getModel();
        String originalModelJson = JsonUtils.toJsonWithNull(configService.getSysConfig(model.getId()));
        boolean isAdd = "null".equals(originalModelJson);
        if (model == null || StringUtils.isEmpty(model.getName())) {
            ApiErrorCode.PERSIST_FAILED.build("信息缺失").throwSelf();
        }
        if (model.getCreated() == null) {
            model.setCreated(new Date());
        }
        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        ret.setModel(magicService.persist(model));

        String newModelJson = JsonUtils.toJsonWithNull(model);
        Map<String, Object> newModelMap = JsonUtils.jsonToMap(new JSONObject(newModelJson));
        boolean hasChange = !newModelJson.equals(originalModelJson);

        if (!isAdd) {
            List<Map<String, Object>> maps = baseService.getLogStatusMaps(originalModelJson, newModelJson);
            originalChangeMap = maps.get(0);
            newChangeMap = maps.get(1);
        }
        if (isAdd || hasChange) {
            operationService.insertOperationLog(OperationLog.create(req.getLoginUser().getUserId(),
                    "ID:" + model.getId() + " 配置名称:" + model.getName(),
                    isAdd ? "" : JsonUtils.toJsonWithNull(originalChangeMap),
                    isAdd ? JsonUtils.toJson2(newModelMap) : JsonUtils.toJsonWithNull(newChangeMap),
                    isAdd ? "新增" : "修改", "", req.getIp(), model.getClass()));
        }
        return ret;
    }

    @RequestMapping("sys_config_delete.{ext}")
    @AuthResource("system_config_delete")
    public ApiEntityDeleteResp deleteNoticeType(@RequestBody ApiEntityDeleteReq req) {
        SysConfig originModel = configService.getSysConfig(req.getId());
        String originModelJson = JsonUtils.toJson2(originModel);
        operationService.insertOperationLog(OperationLog.create(req.getLoginUser().getUserId(),
                "ID:" + originModel.getId() + " 配置名称:" + originModel.getName(),
                originModelJson, "", "删除", "", req.getIp(), originModel.getClass()));
        String id = req.getId();
        magicService.delete(id, SysConfig.class);
        ApiEntityDeleteResp ret = new ApiEntityDeleteResp();
        return ret;
    }

    @RequestMapping("sys_config_list.{ext}")
    public ApiListResp getSysConfigList(@RequestBody ApiQueryReq<SysConfigQuery> req) {
        ApiListResp ret = new ApiListResp();
        ret.setList(magicService.getList(req.getQuery(), SysConfig.class));
        return ret;
    }

    @RequestMapping("sys_config.{ext}")
    public ApiModeResp getSysConfig(@RequestBody ApiIdReq req) {
        ApiModeResp ret = new ApiModeResp();
        String value = configService.getSysConfigAsString(req.getId());
        ret.setModel(value);
        return ret;
    }

    @RequestMapping("sys_config_categories.{ext}")
    public ApiPageResp getPage(@RequestBody ApiQueryReq<SysConfigCategoryQuery> req) {
        Page<SysConfigCategory> page = magicService.getPage(req.getQuery(), SysConfigCategory.class);
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("sys_config_category_persist.{ext}")
    @AuthResource({"sys_config_category_edit"})
    public ApiEntityPersistResp persistSysConfigCategory(@RequestBody ApiPersistReq<SysConfigCategory> req) {
        SysConfigCategory model = req.getModel();
        if (model == null || StringUtils.isEmpty(model.getName())) {
            ApiErrorCode.PERSIST_FAILED.build("信息缺失").throwSelf();
        }
        if (StringUtils.isEmpty(model.getId())) {
            model.setId(UUIDGenerator.generate());
        }
        if (model.getCreated() == null) {
            model.setCreated(new Date());
        }
        ApiEntityPersistResp ret = new ApiEntityPersistResp();
        ret.setModel(magicService.persist(model));
        return ret;
    }

    @RequestMapping("sys_config_category_delete.{ext}")
    @AuthResource("sys_config_category_delete")
    public ApiEntityDeleteResp deleteSysConfigCategory(@RequestBody ApiEntityDeleteReq req) {
        magicService.delete(req.getId(), SysConfigCategory.class);
        ApiEntityDeleteResp ret = new ApiEntityDeleteResp();
        return ret;
    }

    @RequestMapping("sys_config_category_list.{ext}")
    public ApiListResp getList(@RequestBody ApiQueryReq<SysConfigCategoryQuery> req) {
        ApiListResp ret = new ApiListResp();
        ret.setList(magicService.getList(req.getQuery(), SysConfigCategory.class));
        return ret;
    }

}
