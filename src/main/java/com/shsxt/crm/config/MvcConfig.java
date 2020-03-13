package com.shsxt.crm.config;

import com.shsxt.crm.Interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器生效配置
 */

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    //注入Bean对象
    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return  new NoLoginInterceptor();
    }
    //为某些资源添加过滤器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login","/index","/static/**");
    }
}
