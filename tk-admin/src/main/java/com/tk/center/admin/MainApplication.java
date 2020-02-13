package com.tk.center.admin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.magic.springboot.auth.config.AuthConfig;
import com.magic.springboot.config.MultiConfig;
import com.tk.center.MultiApplication;
import com.tk.center.framework.AuthIntercept;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.annotation.Resource;
import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.util.List;


@Component
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableScheduling
@Configuration // 宣告及注册bean到spring容器中
@EnableConfigurationProperties
//@ComponentScan是用来扫描哪些package中有挂上@Component, @Service, @Controller, @RestController, @Repository, @Configuration的类别来自动注册为bean
@ComponentScan(basePackages = {"com.tk.center", "com.magic.springboot"})

public class MainApplication extends MultiApplication {

    public static void main(String[] args) {
        MultiConfig.enableMulti = false;
        MultiConfig.MultiListener = new ClientListener();
        SpringApplication.run(MainApplication.class, args);
    }

    @Resource
    protected AuthIntercept authIntercept;

    @Bean
    public AuthConfig authConfig() throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind
        AuthConfig ret = mapper.readValue(new ClassPathResource("auth.yml").getInputStream(), AuthConfig.class);
        return ret;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        //拦截器 加入权限控制的部份
        registry.addInterceptor(authIntercept);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(16 * 1024L * 1024L);
        return factory.createMultipartConfig();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //argumentResolvers.add(new RequestJsonParamMethodArgumentResolver());
    }
}
