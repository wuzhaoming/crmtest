package com.shsxt.exception;

import com.alibaba.fastjson.JSON;
import com.shsxt.crm.model.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常，实现HandlerExceptionResolver接口
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        /**
         * 首先判断异常类型，如果异常类型为未登录，则进行视图转发
         */
        ModelAndView modelAndView=new ModelAndView();
        //如果异常类是未登录异常
        if (ex instanceof NoLoginException){
            NoLoginException ne= (NoLoginException) ex;
            //进行视图转发
            modelAndView.setViewName("no_login");
            //添加信息
            modelAndView.addObject("msg", ne.getMsg());
            modelAndView.addObject("ctx",request.getContextPath());
            return modelAndView;
        }
        /**
         * 方法返回值类型判断
         *  如果方法级别存在@ResponseBody，方法响应内容为JSON，否则视为视图
         *  handler参加类型为HandlerMethod
         * 返回值
         *   视图：默认错误页面
         *   json：默认的json信息
         */
        //设置逻辑视图名，视图解析器会根据该名字解析到具体的视图页面
        modelAndView.setViewName("errors");
        //添加模型数据
        modelAndView.addObject("code",400);
        modelAndView.addObject("msg","系统异常，请稍后再试");
        //判断handler是否是HandlerMethod的一个实例
        if(handler instanceof HandlerMethod){
            HandlerMethod hm= (HandlerMethod) handler;
            //判断方法上是否ResponseBody注解
            ResponseBody responseBody=hm.getMethod().getDeclaredAnnotation(ResponseBody.class);
           //如果为空，则代表方法级别上没有该注解，返回的则是视图
            if (null==responseBody){
                /**
                 * 方法返回视图
                 */
                //异常是否为登录异常
                if(ex instanceof  ParamsException){
                    ParamsException pe= (ParamsException) ex;
                    modelAndView.addObject("mgs",pe.getMsg());
                    modelAndView.addObject("code",pe.getCode());
                }
                return modelAndView;
            }else{
                /*
                 * 如果不是则返回JSON对象
                 */
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统错误，请稍后再试");
                if (ex instanceof ParamsException){
                    ParamsException pe= (ParamsException) ex;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter printWriter=null;
                try {
                    //写出JSON
                    printWriter=response.getWriter();
                    printWriter.write(JSON.toJSONString(resultInfo));
                    printWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(null!=printWriter){
                        printWriter.close();
                    }
                }
                return null;
            }
        }else{
            return  modelAndView;
        }
    }
}
