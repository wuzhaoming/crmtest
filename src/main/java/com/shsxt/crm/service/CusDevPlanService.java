package com.shsxt.crm.service;

import com.shsxt.base.BaseQuery;
import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.vo.CusDevPlan;
import com.shsxt.exception.AsserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    //mapper注入
    @Autowired
    private SaleChanceMapper saleChanceMapper;

    /**
     * 保存开发计划项目
     * 1.参数校验
     *  营销记录id， 非空且必须存在
     *  计划项内容   非空
     *  计划项时间   非空
     * 2.参数默认值设置
     *  is_valid createDate updateDate
     * 3.执行添加，判断结果
     *
     */
    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        //非空且必要性判断
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        //设置有效
        cusDevPlan.setIsValid(1);
        //设置创建还是件
        cusDevPlan.setCreateDate(new Date());
        //设置更新时间
        cusDevPlan.setUpdateDate(new Date());
        //判断是否成功
        AsserUtil.isTrue(insertSelective(cusDevPlan)<1,"更新失败");
    }

    private void checkParams(Integer saleChanceId, String planItem, Date planDate) {
        AsserUtil.isTrue(null==saleChanceId||null==saleChanceMapper.selectByPrimaryKey(saleChanceId),"请设置营销id");
        AsserUtil.isTrue(StringUtils.isBlank(planItem),"请输入计划项内容");
        AsserUtil.isTrue(null==planDate,"请指定计划项时间");
    }

    /**
     * 更新用户计划
     * 1.参数判断
     *  id           非空且记录存在
     *  营销机会id    非空且记录必须存在
     *  计划项内容    非空
     *  计划项时间    非空
     * 2.参数默认值设置
     *  updateDate  当前时间
     * 3.执行更新，判断结果
     */
    public void updateCuseDevPlan(CusDevPlan cusDevPlan){
        //1.参数判断
        AsserUtil.isTrue(null==cusDevPlan.getId()||null==selectByPrimaryKey(cusDevPlan.getId()),"待更新记录不存在");
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        //2.参数默认值设置
        cusDevPlan.setUpdateDate(new Date());
        //3.执行更新
        AsserUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项更新失败");
    }

    /**
     * 删除计划项任务
     */
    public void delCusDevPlan(Integer id){
        CusDevPlan cusDevPlan=selectByPrimaryKey(id);
        AsserUtil.isTrue(null==id||null==cusDevPlan,"待删除记录不存在");
        cusDevPlan.setIsValid(0);
        AsserUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录删除失败");
    }


}
