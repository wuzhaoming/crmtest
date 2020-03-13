package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.ModuleMapper;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.dao.RoleMapper;
import com.shsxt.crm.vo.Module;
import com.shsxt.crm.vo.Permission;
import com.shsxt.crm.vo.Role;
import com.shsxt.exception.AsserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
   private PermissionMapper permissionMapper;
    @Autowired
    private  ModuleMapper moduleMapper;



    /**
     * 返回查询的用户List列表 Map集合
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(){
        return roleMapper.queryAllRoles();
    }

    /**
     * 添加记录
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRole(Role role){
        AsserUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名字");
        Role temp=roleMapper.queryRoleByRoleName(role.getRoleName());
        AsserUtil.isTrue(null!=temp,"用户已存在");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AsserUtil.isTrue(insertSelective(role)<1,"角色记录添加失败");
    }

    /**
     * 更新用户角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        AsserUtil.isTrue(null==role.getId()||null==selectByPrimaryKey(role.getId()),"待修改的记录不存在");
        AsserUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
        Role temp=roleMapper.selectByPrimaryKey(role.getId());
        AsserUtil.isTrue(null==temp&&(temp.getId().equals(role.getId())),"该角色已存在");
        role.setUpdateDate(new Date());
        AsserUtil.isTrue(updateByPrimaryKeySelective(role)<1,"角色更新失败");
    }

    /**
     * 删除记录
     * @param userId
     */
    public void deleteRole(Integer userId){
        Role temp=selectByPrimaryKey(userId);
        AsserUtil.isTrue(null==temp.getId()||null==temp,"待删除的记录不存在");
        temp.setIsValid(0);
        boolean flag=updateByPrimaryKeySelective(temp)<1;
        AsserUtil.isTrue(flag,"角色记录删除失败");
    }
    /**
     * 给角色授权资源展示界面
     * 核心表-  t_permissiion t_role校验用户存在
     *  如果角色存在原始权限则删除角色原始权限
     *      然后添加角色新的权限，批量添加权限到t_permission
     */
    public void addGrant(Integer[] mids,Integer roleId){
        Role temp=selectByPrimaryKey(roleId);
        AsserUtil.isTrue(null==roleId||null==temp,"待授权角色不存在");
        int count=permissionMapper.countPermissionByRoleId(roleId);
        if(count>0){
            AsserUtil.isTrue(permissionMapper.deletePermissionsByRoleId(roleId)<count,"权限分配失败");
        }
        if(null!=mids&&mids.length>0){
            List<Permission> permissions=new ArrayList<>();
            for (Integer mid:mids){
                Permission permission=new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mid);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permissions.add(permission);
            }
            permissionMapper.insertBatch(permissions);
        }


    }



}
