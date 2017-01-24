package com.meng.chatonline.service.impl;

import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.User;
import com.meng.chatonline.model.security.Authority;
import com.meng.chatonline.model.security.Role;
import com.meng.chatonline.security.MyRealm;
import com.meng.chatonline.service.AuthorityService;
import com.meng.chatonline.service.RoleService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.StringUtils;
import com.meng.chatonline.utils.ValidationUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @Author xindemeng
 */
@CacheConfig(cacheNames = "roleCache")
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService
{
    @Resource
    private AuthorityService authorityService;
    @Resource
    private UserService userService;
    @Resource
    private MyRealm realm;

    @Resource(name = "roleDao")
    @Override
    public void setDao(BaseDao<Role> baseDao)
    {
        super.setDao(baseDao);
    }

    @Cacheable
    @Transactional
    public List<Role> findAllRolesWithAuthorities()
    {
        System.out.println("---------查找所有角色-----------------------------");
        String jpql = "select DISTINCT r from Role r left join fetch r.authorities";
        List<Role> roles = this.findEntityByJPQL(jpql);
        return roles;
    }

    @CacheEvict(cacheNames = {"authorityCache","roleCache","userCache"}, allEntries = true)
    @Transactional
    public void saveOrUpdateRole(Role role, String[] ownAuthIds)
    {
        if (ValidationUtils.validateArray(ownAuthIds))
        {
            //获得指定范围内的权限
            List<Authority> authorities = this.authorityService.findAuthoritiesInRange(ownAuthIds);
            role.setAuthorities(new HashSet<Authority>(authorities));
        }
        this.saveOrUpdateEntity(role);

        //清除系统的权限缓存
        realm.clearAllCachedAuthorizationInfo();
    }

    @CacheEvict(cacheNames = {"authorityCache","roleCache","userCache"}, allEntries = true)
    @Transactional
    public void deleteRole(Integer roleId)
    {
        //先删除Role和Authority关联表的有关记录
        String sql = "delete from role_authority where role_id = ?";
        this.executeSql(sql, roleId);

        //再删除Role和User关联表的有关记录
        String sql2 = "delete from user_role where role_id = ?";
        this.executeSql(sql2, roleId);

        //最后删除Role表
        String jqpl = "delete from Role r where r.id = ?";
        this.BatchEntityByJPQL(jqpl, roleId);

        //清除系统的权限缓存
        realm.clearAllCachedAuthorizationInfo();
    }

    @Transactional
    //属于该用户的角色
    public List<Role> findOwnRoles(Integer userId)
    {
        //两种都可以
        String jpql = "select r from User u join u.roles r where u.id = ?";
//        String jpql = "from Role r where r.id in (" +
//                "select r2.id from User u join u.roles r2 where u.id = ?)";
        List<Role> roles = this.findEntityByJPQL(jpql, userId);
        return roles;
    }

    @Transactional
    //不属于该用户的角色
    public List<Role> findNotOwnRoles(Integer userId)
    {
        String jpql = "from Role r where r.id not in (" +
                "select r2.id from User u join u.roles r2 where u.id = ?)";
        List<Role> roles = this.findEntityByJPQL(jpql, userId);
        return roles;
    }

    /**
     * 得到指定id范围的角色
     *
     * @param ownRoleIds
     */
    @Transactional
    public List<Role> findRolesInRange(String[] ownRoleIds)
    {
        if (ValidationUtils.validateArray(ownRoleIds))
        {
            String roleIds = StringUtils.arrToStr(ownRoleIds);
            String jpql = "from Role r where r.id in (" + roleIds + ")";
            List<Role> roles = this.findEntityByJPQL(jpql);
            return roles;
        }
        return null;
    }

    @Cacheable
    //获得公有角色
    @Transactional
    public List<Role> findCommonRoles()
    {
        String jpql = "from Role r where r.common = ?";
        List<Role> roles = this.findEntityByJPQL(jpql, true);
        return roles;
    }
}
