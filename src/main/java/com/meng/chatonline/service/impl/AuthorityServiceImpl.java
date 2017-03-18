package com.meng.chatonline.service.impl;

import com.meng.chatonline.constant.Constants;
import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.security.Authority;
import com.meng.chatonline.model.security.Role;
import com.meng.chatonline.security.MyRealm;
import com.meng.chatonline.service.AuthorityService;
import com.meng.chatonline.service.RoleService;
import com.meng.chatonline.utils.CollectionUtils;
import com.meng.chatonline.utils.StringUtils;
import com.meng.chatonline.utils.ValidationUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xindemeng
 */
@CacheConfig(cacheNames = "authorityCache")
@Service("authorityService")
public class AuthorityServiceImpl extends BaseServiceImpl<Authority> implements AuthorityService
{
    @Resource
    private MyRealm realm;
    @Resource
    private RoleService roleService;

    @Resource(name = "authorityDao")
    @Override
    public void setDao(BaseDao<Authority> baseDao)
    {
        super.setDao(baseDao);
    }

    @Cacheable
    @Transactional
    //获得用户的所有权限
    public List<Authority> getAuthoritiesByUserId(Integer userId)
    {
        String sql = "select * from authorities where available = ? and id in (" +
                "select authority_id from role_authority where role_id in (" +
                "SELECT role_id FROM user_role, roles " +
                "WHERE available = ? AND user_id = ? AND id = role_id))";
        return this.executeSQLQuery(Authority.class, sql, true, true, userId);
    }

    @Cacheable
    @Transactional
    //获得所有的权限
    public List<Authority> findAllAuthorities()
    {
        System.out.println("----------------查找所有的权限-------------------");
        return this.findAllEntities();
    }

    @Cacheable
    @Transactional
    //获得所有菜单类型的权限
    public List<Authority> findMenuAuthorities()
    {
        String jpql = "from Authority a where a.type = ?";
        List<Authority> authorities = this.findEntityByJPQL(jpql, Constants.MENU_TYPE);
        return authorities;
    }

    @CacheEvict(cacheNames = {"authorityCache","roleCache","userCache"}, allEntries = true)
    @Transactional
    public void deleteAuthority(Integer authId)
    {
        //该菜单包含的权限id集合
        List<Integer> ids = new ArrayList<Integer>();

        Authority authority = this.getEntity(authId);
        //如果是菜单类型权限，则还要把其包含的普通类型权限删除
        if (authority.getType() == Constants.MENU_TYPE)
        {
            String jpql1 = "from Authority a where a.menu.id = ?";
            List<Authority> auths = this.findEntityByJPQL(jpql1, authId);
            for (Authority auth : auths)
            {
                ids.add(auth.getId());
            }
        }

        //先删除有关Role和Authority关联表上的记录
        String idsStr = CollectionUtils.collToStr(ids);
        idsStr += idsStr.length() == 0? authId : ","+authId;
        String sql = "delete from role_authority where authority_id in ("+ idsStr +")";
        this.executeSql(sql);

        //再删除该菜单包含的权限
        if (ValidationUtils.validateCollection(ids))
        {
            String jpql = "delete from Authority a where a.id in ("+CollectionUtils.collToStr(ids)+")";
            this.BatchEntityByJPQL(jpql);
        }

        //最后删除该权限
        this.deleteEntity(authority);

        //清除系统的权限缓存
        realm.clearAllCachedAuthorizationInfo();
    }

    @Cacheable
    @Transactional
    //获得属于该角色的Authority，只包含权限类型的
    public List<Authority> findOwnAuthorities(Integer roleId)
    {
        String jpql = "select a from Authority a join a.roles r " +
                "where a.type = ? and r.id = ?";
        List<Authority> authorities = this.findEntityByJPQL(jpql, Constants.AUTH_TYPE, roleId);
        return authorities;
    }

    @Cacheable
    @Transactional
    //获得不属于该角色的Authority，只包含权限类型的
    public List<Authority> findNotOwnAuthorities(Integer roleId)
    {
        String jpql = "from Authority a where a.type = ? and a.id not in (" +
                "select a2.id from Authority a2 join a2.roles r where r.id = ?)";
        List<Authority> authorities = this.findEntityByJPQL(jpql, Constants.AUTH_TYPE, roleId);
        return authorities;
    }

    @Cacheable
    @Transactional
    //获得所有权限类型的权限
    public List<Authority> findAuthAuthorities()
    {
        String jpql = "from Authority a where a.type = ?";
        List<Authority> authorities = this.findEntityByJPQL(jpql, Constants.AUTH_TYPE);
        return authorities;
    }

    //获得指定范围内的权限
    public List<Authority> findAuthoritiesInRange(String[] ownAuthIds)
    {
        String authIds = StringUtils.arrToStr(ownAuthIds);
        String jpql = "from Authority a where a.id in ("+authIds+")";
        List<Authority> authorities = this.findEntityByJPQL(jpql);
        return authorities;
    }

    @CacheEvict(cacheNames = {"authorityCache","roleCache","userCache"}, allEntries = true)
    @Transactional
    public void saveOrUpdateAuthority(Authority authority)
    {
        //原先的权限
        Boolean originalCommon = null;
        if (authority.getId() != null)
            originalCommon = this.getEntity(authority.getId()).getCommon();

        authority = this.saveOrUpdateEntity(authority);

        //如果是权限（按钮）类型的话要检查公共属性
        if (authority.getType() == Constants.AUTH_TYPE)
        {
            //如果是新建的权限或者权限的公共属性改变了，则要对公共角色做出相应的改变
            if (originalCommon == null
                    || originalCommon != authority.getCommon())
            {
                //获得公共角色
                Role role = this.roleService.findCommonRoles().get(0);
                //如果变成了公共的，则向角色权限关系表插入
                if (authority.getCommon())
                {
                    String sql = "insert into role_authority(role_id, authority_id) values(?,?)";
                    this.executeSql(sql, role.getId(), authority.getId());
                }
                //如果非新建的而且变成了非公共的，则向角色权限关系表删除
                else if (originalCommon != null && !authority.getCommon())
                {
                    String sql = "delete from role_authority where role_id = ? and authority_id = ?";
                    this.executeSql(sql, role.getId(), authority.getId());
                }
            }
        }

        //清除系统的权限缓存
        realm.clearAllCachedAuthorizationInfo();
    }

}
