package com.meng.chatonline.service;

import com.meng.chatonline.model.security.Authority;

import java.util.List;

/**
 * @Author xindemeng
 */
public interface AuthorityService extends BaseService<Authority>
{
    //获得用户的所有权限
    List<Authority> getAuthoritiesByUserId(Integer userId);

    //获得所有的权限
    List<Authority> findAllAuthorities();

    //获得所有菜单类型的权限
    List<Authority> findMenuAuthorities();

    void deleteAuthority(Integer authId);

    //获得属于该角色的Authority，只包含权限类型的
    List<Authority> findOwnAuthorities(Integer roleId);

    //获得不属于该角色的Authority，只包含权限类型的
    List<Authority> findNotOwnAuthorities(Integer roleId);

    //获得所有权限类型的权限
    List<Authority> findAuthAuthorities();

    //获得指定范围内的权限
    List<Authority> findAuthoritiesInRange(String[] ownAuthIds);

    void saveOrUpdateAuthority(Authority authority);
}
