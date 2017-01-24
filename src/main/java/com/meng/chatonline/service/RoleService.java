package com.meng.chatonline.service;

import com.meng.chatonline.model.security.Role;

import java.util.List;

/**
 * @Author xindemeng
 */
public interface RoleService extends BaseService<Role>
{
    List<Role> findAllRolesWithAuthorities();

    void saveOrUpdateRole(Role role, String[] ownAuthIds);

    void deleteRole(Integer roleId);

    //属于该用户的角色
    List<Role> findOwnRoles(Integer userId);

    //不属于该用户的角色
    List<Role> findNotOwnRoles(Integer userId);

    /**
    得到指定id范围的角色
     */
    List<Role> findRolesInRange(String[] ownRoleIds);

    //获得公有角色
    public List<Role> findCommonRoles();

}
