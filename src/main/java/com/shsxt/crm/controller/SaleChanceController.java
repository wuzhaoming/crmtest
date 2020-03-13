package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.annotions.CrmLog;
import com.shsxt.crm.annotions.RequirePermission;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController extends BaseController {
    @Autowired
    private SaleChanceService saleChanceService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @RequestMapping("/index")
    @CrmLog(module = "营销机会管理注解版",oper = "主页展示注解版")
    public String index(){
        return "sale_chance";
    }
    @RequestMapping("/list")
    @ResponseBody
    @CrmLog(module = "营销机会管理注解版",oper = "多条件查询注解版")
    @RequirePermission(code="101001")
    public Map<String,Object> querySaleChancesByParms(SaleChanceQuery saleChanceQuery){
        System.out.println(saleChanceQuery);
        return saleChanceService.querySaleChancesByParams(saleChanceQuery);
    }
    @RequestMapping("/save")
    @ResponseBody
    @CrmLog(module = "营销机会管理注解版",oper = "添加注解版")
    @RequirePermission(code="101002")
    public ResultInfo saveSaleChance(SaleChance saleChance){
        saleChance.setCreateMan(userService.selectByPrimaryKey(LoginUserUtil.releaseUserIdFromCookie(request)).getTrueName());
        saleChanceService.saveSaleChance(saleChance);
        return success("营销机会添加成功");
    }
    @RequestMapping("/update")
    @ResponseBody
    @CrmLog(module = "营销机会管理注解版",oper = "更新注解版")
    @RequirePermission(code="101001")
    public ResultInfo updateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会更新成功");
    }
    @RequestMapping("/delete")
    @ResponseBody
    @CrmLog(module = "营销机会管理注解版",oper = "删除注解版")
    @RequirePermission(code="101001")
    public ResultInfo deleteSaleChances(Integer[] ids){
        saleChanceService.deleteSaleChancesByIds(ids);
        return  success("营销机会删除成功");
    }



}
