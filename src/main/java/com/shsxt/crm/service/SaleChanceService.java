package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseService;
import com.shsxt.crm.enums.DevResult;
import com.shsxt.crm.enums.StateStatus;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.vo.SaleChance;
import com.shsxt.exception.AsserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 营销机会-Service
 */
@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    /**
     * 营销机会查询-Map集合返回
     * @param saleChanceQuery
     * @return
     */
   public Map<String,Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery){
       Map<String,Object> result=new HashMap<>();
       //开始页数
       PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getRows());
       //把查询的结果放入到pagaInfo对象中
       List<SaleChance> saleChances = selectByParams(saleChanceQuery);
       PageInfo<SaleChance> pageInfo=new PageInfo<>(saleChances);
       //获取当前总条数
       result.put("toatal",pageInfo.getTotal());
       //获取当前条目信息
       result.put("rows",pageInfo.getList());
       //返回当前条数目 Map集合对象
       return  result;
   }

    /**
     * 营销机会添加
     * @param saleChance
     */
   public void saveSaleChance(SaleChance saleChance){
       /**
        * 1.参数校验
        *   customerName：非空
        *   linkMan：非空
        *   linkPhone：非空 11位手机号
        * 2.设置相关参数默认值
        *   state：默认未分配 如果选中分配人，则state为已分配
        *   assingTime：时间为当前系统时间
        *   devResult：默认未开发 如果选择分配人 DevResult为开发中
        *   isValid：默认有效
        *   createDate updateData:默认当前系统时间
        *  3.执行添加，判断结果
        */
       chcekParams(saleChance.getCustomerName(),saleChance.getLinkPhone(),saleChance.getLinkMan());
       //默认未分配
       saleChance.setState(StateStatus.UNSTATE.getType());
        //默认未开发
       saleChance.setState(DevResult.UNDEV.getStatus());
       //判断是否有注册人
       if (StringUtils.isNotBlank(saleChance.getAssignMan())){
           saleChance.setState(StateStatus.STATED.getType());
           saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());
       }
       //设置有效
       saleChance.setIsValid(1);
       //设置注册时间
       saleChance.setAssignTime(new Date());
       //设置更新时间
       saleChance.setUpdateDate(new Date());
       //判断是否添加成功
       AsserUtil.isTrue(insertSelective(saleChance)<1,"营销机会添加失败");
   }
    //空判
    private void chcekParams(String customerName, String linkPhone, String linkMan) {
        AsserUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名");
        AsserUtil.isTrue(StringUtils.isBlank(linkMan),"请输入手机号");
        AsserUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        //判断手机号是否合法
        AsserUtil.isTrue(!(PhoneUtil.isMobile(linkPhone)),"手机号不合法");
   }

    /**
     * 营销机会更新
     * @param saleChance
     */
   public void updateSaleChance(SaleChance saleChance){
       //判空
       AsserUtil.isTrue(null==saleChance.getId(),"待更新记录不存在");
       //获取临时对象(从数据库中查询出来的)
       SaleChance temp=selectByPrimaryKey(saleChance.getId());
       AsserUtil.isTrue(null==temp,"待更新记录不存在");
       //参数判空
       chcekParams(saleChance.getCustomerName(),saleChance.getLinkPhone(),saleChance.getLinkMan());

       //更新数据
       //数据库中 通过查看分配人 查看分配状态
       /*
       数据库对象k 和 前台对象z
       1.k中没有分配人员，z有分配人员，则更新分配状态：已分配，注册时间：系统时间；开发状态：正在卡覅
       2.k中有分配人员，z中没有分配人员，则更新分配状态：未分配，注册时间：null，开发状态：未开发
        */
       if(StringUtils.isBlank(temp.getAssignMan())&&StringUtils.isNotBlank(saleChance.getAssignMan())){
           //设置开发状态，若是有则认为已分配
            saleChance.setState(StateStatus.STATED.getType());
            //设置注册时间
           saleChance.setAssignTime(new Date());
           //设置开发状态 开发中
           saleChance.setDevResult(DevResult.DEVING.getStatus());
       }else if(StringUtils.isNotBlank(temp.getAssignMan())&&StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setAssignMan("");
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
       }
       //更新时间统一为当前时间
       saleChance.setUpdateDate(new Date());
        //更新到数据库中
       AsserUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"营销机会更新失败");
   }

    /**
     * 营销机会删除
     * @param ids
     */
   public void deleteSaleChancesByIds(Integer[] ids){
       AsserUtil.isTrue(null==ids||ids.length==0,"请选择待删除的数据库");
       Integer flag1=deleteBatch(ids);
       Integer flag2=ids.length;
       boolean flag=flag1<flag2;
       AsserUtil.isTrue(flag,"机会数据删除失败|");
   }



}
