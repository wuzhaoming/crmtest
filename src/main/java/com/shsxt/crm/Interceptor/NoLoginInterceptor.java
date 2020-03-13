package com.shsxt.crm.Interceptor;

import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.exception.NoLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;
    //重写拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 获取cookie，解析用户id
         *  如果用户id存在，并且数据库存在对应的用户记录，放行，否则进行拦截，重定向到登录
         */
        int  userId= LoginUserUtil.releaseUserIdFromCookie(request);
        if (userId==0||null==userService.selectByPrimaryKey(userId)){
            throw  new NoLoginException();
        }
        //没有抛出异常，则放行
        return  true;
    }
}
