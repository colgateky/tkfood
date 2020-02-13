package com.tk.center.admin.framework;

import com.magic.springboot.auth.pojo.LoginUser;
import com.tk.center.admin.dto.ApiAuthReq;
import com.tk.center.admin.dto.ApiReq;
import com.tk.center.common.ApiErrorCode;
import com.tk.center.common.Constant;
import com.tk.center.service.ConfigService;
import com.tk.center.util.IpHelper;
import org.aopalliance.aop.Advice;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mingkun on 2020/02/04.
 */
@Configuration
@Aspect
public class ApiAop implements Advice {

    @Resource
    protected ConfigService configService;

    @Pointcut("execution(public * com.tk.center.admin.controller..*Controller.*(..))")
    //表示匹配com.tk.center.admin.controller包及其子包下的所有方法
    public void apiMethod() {
    }

    @Before("apiMethod()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof ApiReq) {
                ApiReq req = (ApiReq) arg;
                req.setIp(getRemoteIp());
            } else if (arg instanceof com.tk.center.dto.request.ApiReq) {
                com.tk.center.dto.request.ApiReq req = (com.tk.center.dto.request.ApiReq) arg;
                req.setIp(getRemoteIp());
            }
            if (arg instanceof ApiAuthReq || arg instanceof com.tk.center.dto.request.ApiAuthReq) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                LoginUser user = (LoginUser) request.getAttribute("model");
                if (user == null) {
                    ApiErrorCode.LOGIN_FAILED.build("登錄失敗，請檢查").throwSelf();
                }
                String uuid = "";
                //下面判斷使用的apiReq是否為ApiAuthReq，若是且登錄成功，則在req中放入loginUser以供檢驗
                if (arg instanceof ApiAuthReq) {
                    ApiAuthReq req = (ApiAuthReq) arg;
                    req.setLoginUser(user);
                    uuid = req.getUuid();
                } else if (arg instanceof com.tk.center.dto.request.ApiAuthReq) {
                    com.tk.center.dto.request.ApiAuthReq req = (com.tk.center.dto.request.ApiAuthReq) arg;
                    req.setLoginUser(user);
                    uuid = req.getUuid();
                }
                //後台單點登錄
                boolean adminSingleLogin = configService.getSysConfigAsBoolean(Constant.ADMIN_SINGLE_LOGIN);
                if (adminSingleLogin && !"root".equals(user.getUserId())) {
                    if ((!StringUtils.isEmpty(uuid) && uuid.equals(user.getUuid())) == false) {
                        ApiErrorCode.LOGIN_FAILED.build("登錄失敗，請檢查").throwSelf();
                    }
                }
            }
        }
    }

    private String getRemoteIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        return IpHelper.getIp(req);
    }
}
