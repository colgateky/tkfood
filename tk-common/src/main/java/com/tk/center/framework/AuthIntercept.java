package com.tk.center.framework;

import com.tk.center.common.ApiErrorCode;
import com.magic.springboot.auth.annotation.AuthResource;
import com.magic.springboot.auth.annotation.NoAuth;
import com.magic.springboot.auth.pojo.LoginUser;
import com.magic.springboot.service.AuthService;
import com.magic.springboot.service.RedisCacheService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xm on 2017/5/20.
 */
@Component
public class AuthIntercept implements HandlerInterceptor {
    @Resource
    protected RedisCacheService redisCacheService;

    @Resource
    protected AuthService authService;

    @Resource
    protected LoginUser loginUser;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = "";
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }
//        LoginUser loginUser = (LoginUser)request.getSession().getAttribute("model");

        HandlerMethod hm = (HandlerMethod)handler;
        Class cls = hm.getBean().getClass();
        if(hm.getMethodAnnotation(NoAuth.class) == null && cls.getAnnotation(NoAuth.class) == null) {
            LoginUser lu = null;
            if (StringUtils.isEmpty(token) == false) {
                String userId = TokenHelper.decryptToken(token);
                if (StringUtils.isEmpty(userId)){
                    ApiErrorCode.LOGIN_FAILED.build("登录失败，未授权用户").throwSelf();
                }
                lu = redisCacheService.hashGet("model", userId, LoginUser.class);
                /*if (lu == null) {
                    lu = authService.getLoginUser(userId);
                    redisCacheService.hashSet("model", userId, lu);
                }*/
            }
            if (lu == null) {
                ApiErrorCode.LOGIN_FAILED.build("登录失败，未授权用户").throwSelf();
            } else {
                loginUser.setUserId(lu.getUserId());
                loginUser.setUsername(lu.getUsername());
                loginUser.setRoot(lu.isRoot());
                loginUser.setResources(lu.getResources());

                int hour = 2;
                if("root".equals(loginUser.getUsername())){
                    hour = 24;
                }
                Cookie c = new Cookie("token", TokenHelper.generateToken(lu.getUserId()));
                c.setMaxAge(hour * 60 * 60);
                c.setPath("/");
                response.addCookie(c);
                request.setAttribute("model", lu);
                AuthResource a1 = (AuthResource) cls.getAnnotation(AuthResource.class);
                AuthResource a2 = hm.getMethodAnnotation(AuthResource.class);
                if (a1 != null || a2 != null) {
                    boolean failed = false;
                    if (a1 != null) {
                        for (String s : a1.value()) {
                            if (!lu.check(s)) {
                                ApiErrorCode.AUTH_FAILED.throwSelf();
                            }
                        }
                    }
                    if (!failed && a2 != null) {
                        for (String s : a2.value()) {
                            if (!lu.check(s)) {
                                ApiErrorCode.AUTH_FAILED.throwSelf();
                            }
                        }
                    }
                }

                return true;
            }
        } else {
            return true;
        }
        return true;
    }


    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        LoginUser model = (LoginUser)request.getAttribute("model");
//        if (model != null) {
//            response.addCookie(new Cookie("token", TokenHelper.generateToken(model.getPlayerId())));
//        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
