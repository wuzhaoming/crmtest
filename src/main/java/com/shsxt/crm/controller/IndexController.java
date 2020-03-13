package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.service.PermissionService;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.exception.NoLoginException;
import com.shsxt.exception.ParamsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.login.LoginException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;


    /**
     * 登录页
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    /**
     * 后端管理主页
     * @return
     */
    @RequestMapping("/main")
    public String main(HttpServletRequest request){

        //从request获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);

        /*
            用户扮演的所有角色分配的的所有菜单，将查询结果放入session中
         */
        List<String> permissions=permissionService.queryUserHasRolePermission(userId);
        request.getSession().setAttribute("permissions",permissions);
        //设置域对象 user
        request.setAttribute("user",userService.selectByPrimaryKey(userId));
        return "main";
    }

}
