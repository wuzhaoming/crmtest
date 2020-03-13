package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.sun.prism.impl.ps.BaseShaderContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController extends BaseController{
    @RequestMapping("/tree03")
    public String tree03(){
        return "tree03";
    }

}
