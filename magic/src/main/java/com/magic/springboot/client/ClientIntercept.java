package com.magic.springboot.client;

import com.magic.springboot.config.MultiConfig;
import com.magic.springboot.dao.DaoHelper;
import com.magic.springboot.service.RedisCacheService;
import com.magic.springboot.service.RedisCacheServiceImpl;
import com.magic.springboot.service.RedisLockServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ifuck on 2018/8/25.
 */
@Component
public class ClientIntercept implements HandlerInterceptor {
    @Resource
    protected Client client;
    @Resource
    protected DaoHelper daoHelper;
    @Resource
    protected RedisCacheServiceImpl redisCacheService;
    @Resource
    protected RedisLockServiceImpl redisLockService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        String clientId = request.getHeader("Client");
        client.setClientId(clientId);

        if (MultiConfig.MultiListener != null){
            MultiConfig.MultiListener.init(daoHelper, redisCacheService, redisLockService);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
