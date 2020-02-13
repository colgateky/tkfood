package com.magic.springboot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xm on 2018/1/29.
 */

@Configuration
public class SpringBeanUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;


    public  void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static <T> T getBean(Class<T> cls) {
        try {
            Object _restTemplate = applicationContext.getBean(cls);
            return (T) _restTemplate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}