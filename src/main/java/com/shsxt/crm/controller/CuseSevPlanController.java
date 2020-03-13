package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.CusDevPlanQuery;
import com.shsxt.crm.service.CusDevPlanService;
import com.shsxt.crm.vo.CusDevPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CuseSevPlanController extends BaseController {

    @Autowired
    private CusDevPlanService cusDevPlanService;

    @RequestMapping("/index")
    public String index(){
        return "cus_dev_plan";
    }
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlansByParams(CusDevPlanQuery devPlanQuery){
        Map<String,Object> kk=cusDevPlanService.queryByParamsForData(devPlanQuery);
        return kk;
    }

    /**
     * 保存项目计划项
     * @param cusDevPlan
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.saveCusDevPlan(cusDevPlan);
        return success("计划项添加成功");
    }

    /**
     * 更新计划项
     * @param cusDevPlan
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCuseDevPlan(cusDevPlan);
        return  success("计划更新成功");
    }

    /**
     * 删除计划项：设置为无效
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.delCusDevPlan(id);
        return success("计划项目删除成功（设置为无效）");
    }


}
