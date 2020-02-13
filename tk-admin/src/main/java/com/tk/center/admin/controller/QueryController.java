package com.tk.center.admin.controller;

import com.magic.springboot.dto.PageQuery;
import com.magic.springboot.service.MagicService;
import com.tk.center.admin.dto.ApiListResp;
import com.tk.center.admin.dto.ApiPageResp;
import com.tk.center.admin.dto.ApiQueryReq;
import com.tk.center.entity.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Mingkun on 2020/02/04.
 */
public abstract class QueryController<M extends BaseModel, Q extends PageQuery> {

    @Resource
    protected MagicService magicService;

    protected abstract Class<M> getModelCls();

    protected void onAfterPage(Page<M> page) {

    }

    protected void onBeforePage(ApiQueryReq<Q> req) {

    }

    protected void onAfterList(List<M> list) {

    }

    protected void onBeforeList(ApiQueryReq<Q> req) {

    }

    @RequestMapping("page.{ext}")
    public ApiPageResp page(@RequestBody ApiQueryReq<Q> req) {
        this.onBeforePage(req);
        Page<M> page = magicService.getPage(req.getQuery(), getModelCls());
        this.onAfterPage(page);
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }

    @RequestMapping("list.{ext}")
    public ApiListResp list(@RequestBody ApiQueryReq<Q> req) {
        this.onBeforeList(req);
        List<M> list = magicService.getList(req.getQuery(), getModelCls());
        this.onAfterList(list);
        ApiListResp ret = new ApiListResp();
        ret.setList(list);
        return ret;
    }
}
