package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.ModuleMapper;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.dto.TreeDto;
import com.shsxt.crm.vo.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public List<TreeDto> queryAllModules(){
        return moduleMapper.queryAllModules();
    }
    public List<TreeDto> queryAllModules(Integer roleId){
        List<TreeDto> treeDtos = moduleMapper.queryAllModules();
        //根据角色id，查询用户拥有的菜单id List<Integer>
        List<Integer> roleHasMids=permissionMapper.queryRoleHasAlModuleIdsByRoleId(roleId);
        if(null!=roleHasMids&&roleHasMids.size()>0){
            treeDtos.forEach(treeDto->{
                if(roleHasMids.contains(treeDto.getId())){
                    //说明当前用户分配了该菜单
                    treeDto.setChecked(true);
                }
            });
        }
        return treeDtos;
    }
}
