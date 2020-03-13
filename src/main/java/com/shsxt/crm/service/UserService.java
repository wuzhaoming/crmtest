package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.dao.UserRoleMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import com.shsxt.crm.vo.UserRole;
import com.shsxt.exception.AsserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService<User,Integer> {
    /**
     * 1.参数校验
     *  用户名：非空
     *  密码：非空
     * 2.根据用户名 查询用户记录
     * 3.校验用户名存在
     *  不存在-记录不存在，方法结束
     *  4.用户存在
     *      校验密码->密码错误，方法结束
     *  5.用户存在且密码正确
     *     密码正确，返回用户相关信息
     */
    @Autowired
    UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    public UserModel login(String userName,String userPwd){
        //判断是否为空
        checkLoginParam(userName,userPwd);
        //查询用户
        User user=userMapper.selectUserByUserName(userName);
        //判断用户
        AsserUtil.isTrue(null==user,"用户已注销或不在");
        String k1=Md5Util.encode(userPwd);
        String k2=user.getUserPwd();
        AsserUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userPwd))),"密码错误");
        //返回一个用户数据（少量）
        return buildUserModelInfo(user);

    }

    private UserModel buildUserModelInfo(User user) {
        return new UserModel(UserIDBase64.encoderUserID(user.getId()),user.getUserName(),user.getTrueName());
    }

    private void checkLoginParam(String userName, String userPwd) {
        AsserUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AsserUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }

    /*修改密码
    *1.参数校验
    *   userId 非空，记录必须存在
    *   oldPassword 非空，数据必须与数据库的一直
    *   newPassword 非空，新密码不能与旧密码一致
    *   confirmPassword 非空，必须与新密码一致
    * 2.设置用户信面
    *      新密码加密
    * 3.执行更新
    */

    //添加事务，防止不成功
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        //对所有密码都进行判空操作
        checkParams(userId,oldPassword,newPassword,confirmPassword);
        //从数据库中查询用户
        User user=selectByPrimaryKey(userId);
        //设置新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        //更新到数据库中
        AsserUtil.isTrue(updateByPrimaryKeySelective(user)<1,"更新失败");
    }

    private void checkParams(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        User user=selectByPrimaryKey(userId);
        //判空操作
        AsserUtil.isTrue(null==userId||null==user,"用户未登录或不存在");
        AsserUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        AsserUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        AsserUtil.isTrue(StringUtils.isBlank(confirmPassword),"请输入确认密码");
       //密码确认
        AsserUtil.isTrue(!(newPassword.equals(confirmPassword)),"两次密码不一致");
        //查询密码
        String k1=Md5Util.encode(oldPassword);
        String k2=user.getUserPwd();
        AsserUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"旧密码不正确");
        //密码比对
        AsserUtil.isTrue(oldPassword.equals(newPassword),"旧密码不能与老密码一致");

    }

    /**
     *保存用户
     * @param user
     * 1.参数校验：
     *      用户名 非空且唯一
     *      email 非空 格式合法
     *      手机号 非空且格式合法
     * 2.设置默认参数
     *      isValid 1
     *      createDate updateDate
     *      userPwd 123456->md5加密
     * 3.执行添加
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user){
        //空判
        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        //查询用户
        User temp=userMapper.selectUserByUserName(user.getUserName());
        //判断用户
        AssertUtil.isTrue(null!=temp&&(temp.getIsValid()==1),"该用户已存在");
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(insertHasKey(user)==null,"用户添加失败");
        int userId=user.getId();
        //添加角色
       relaionUserRole(userId,user.getRoleIds());

    }

    /**
     * 用户角色分配
     * 角色分配
     *  原始角色不存在，添加新的角色记录
     *  原始角色存在，添加新的角色记录
     *  原始角色存在，清空所有角色
     *  原始角色存在，移出部分角色
     * 如何进行角色分配？
     *  如果用户原始角色存在，首先清空原始所有的角色
     *  添加新的角色记录到用户角色表
     * @param userId
     * @param roleIds
     */
    private void relaionUserRole(int userId, List<Integer> roleIds) {
        int count=userRoleMapper.countUserRoleByUserId(userId);
        if(count>0){
            AsserUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色分配失败");
        }
        if(null!=roleIds&&roleIds.size()>0){
            List<UserRole> userRoles=new ArrayList<>();
            roleIds.forEach(
                    roleId->{
                        UserRole userRole=new UserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        userRole.setCreateDate(new Date());
                        userRole.setUpdateDate(new Date());
                        userRoles.add(userRole);
                    });
            AsserUtil.isTrue(userRoleMapper.insertBatch(userRoles)<userRoles.size(),"用户角色分配失败");
        }





    }

    private void checkParams(String userName, String email, String phone) {
        AsserUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AsserUtil.isTrue(StringUtils.isBlank(email),"请输入邮箱地址");
        AsserUtil.isTrue(!(PhoneUtil.isMobile(phone)),"手机号格式不合法");
    }


    /**
     * 更新 用户
     * 1.参数校验
     *      id 非空且记录必须存在
     *      用户名 非空且唯一
     *      email 非空格式合法
     *      手机号 非空格式合法
     * 2.设置默认参数
     *      update
     * 3.执行更新 判断结果
     *
     *
     * @param user
     */
    public void updateUser(User user){
        AsserUtil.isTrue(null==user.getId()||null==selectByPrimaryKey(user.getId()),"该记录不存在");
        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        User temp=userMapper.selectUserByUserName(user.getUserName());
        if(null!=temp&&temp.getIsValid()==1){
            AsserUtil.isTrue(!(user.getId().equals(temp.getId())),"该用户已存在");
        }
        user.setUpdateDate(new Date());
        AsserUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户更新失败");
        relaionUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 删除单条用户
     * @param userId
     */
    public void deleteUser(Integer userId){
        User user=selectByPrimaryKey(userId);
        AsserUtil.isTrue(null==userId||null==user,"要删除的记录不存在");
        int count=userRoleMapper.countUserRoleByUserId(userId);
        if(count>0){
            AsserUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色分配失败");
        }
        user.setIsValid(0);
        AsserUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户记录删除失败");
    }
}
