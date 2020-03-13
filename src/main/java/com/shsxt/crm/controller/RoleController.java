package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.RoleQuery;
import com.shsxt.crm.service.RoleService;
import com.shsxt.crm.vo.Role;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(){
        return roleService.queryAllRoles();
    }
    @RequestMapping("/index")
    public String role(){
        return "role";
    }
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryROlesByParams(RoleQuery roleQuery){
        Map<String, Object> map = roleService.queryByParamsForData(roleQuery);
        return map;
    }
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.saveRole(role);
        return success("角色记录保存成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Role role){
        roleService.updateRole(role);
        return success("角色更新成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer id){
        roleService.deleteRole(id);
        return success("角色删除成功");
    }
    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mids,Integer roleId){
        roleService.addGrant(mids,roleId);
        return  success("权限添加成功");
    }





}
