package com.meng.chatonline.service.impl;

import com.meng.chatonline.Param;
import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.security.Authority;
import com.meng.chatonline.security.MyRealm;
import com.meng.chatonline.service.AuthorityService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.CollectionUtils;
import com.meng.chatonline.utils.StringUtils;
import com.meng.chatonline.utils.ValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xindemeng
 */
@Service("authorityService")
public class AuthorityServiceImpl extends BaseServiceImpl<Authority> implements AuthorityService
{
    @Resource
    private MyRealm realm;

    @Resource(name = "authorityDao")
    @Override
    public void setDao(BaseDao<Authority> baseDao)
    {
        super.setDao(baseDao);
    }

    @Transactional
    //获得用户的所有权限，返回set集合防止里面有重复的元素
    public List<Authority> getAuthoritiesByUserId(Integer userId)
    {
        String sql = "select * from authorities where available = ? and id in (" +
                "select authority_id from role_authority where role_id in (" +
                "SELECT role_id FROM user_role, roles " +
                "WHERE available = ? AND user_id = ? AND id = role_id))";
        return this.executeSQLQuery(Authority.class, sql, true, true, userId);
    }

    @Transactional
    //获得所有菜单类型的权限
    public List<Authority> findMenuAuthorities()
    {
        String jpql = "from Authority a where a.type = ?";
        List<Authority> authorities = this.findEntityByJPQL(jpql, Param.MENU_TYPE);
        return authorities;
    }

    @Transactional
    public void deleteAuthority(Integer authId)
    {
        //该菜单包含的权限id集合
        List<Integer> ids = new ArrayList<Integer>();

        Authority authority = this.getEntity(authId);
        //如果是菜单类型权限，则还要把其包含的普通类型权限删除
        if (authority.getType() == Param.MENU_TYPE)
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

    @Transactional
    //获得属于该角色的Authority，只包含权限类型的
    public List<Authority> findOwnAuthorities(Integer roleId)
    {
        String jpql = "select a from Authority a join a.roles r " +
                "where a.type = ? and r.id = ?";
        List<Authority> authorities = this.findEntityByJPQL(jpql, Param.AUTH_TYPE, roleId);
        return authorities;
    }

    @Transactional
    //获得不属于该角色的Authority，只包含权限类型的
    public List<Authority> findNotOwnAuthorities(Integer roleId)
    {
        String jpql = "from Authority a where a.type = ? and a.id not in (" +
                "select a2.id from Authority a2 join a2.roles r where r.id = ?)";
        List<Authority> authorities = this.findEntityByJPQL(jpql, Param.AUTH_TYPE, roleId);
        return authorities;
    }

    @Transactional
    //获得所有权限类型的权限
    public List<Authority> findAuthAuthorities()
    {
        String jpql = "from Authority a where a.type = ?";
        List<Authority> authorities = this.findEntityByJPQL(jpql, Param.AUTH_TYPE);
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

    @Transactional
    public void saveOrUpdateAuthority(Authority authority)
    {
        this.saveOrUpdateEntity(authority);

        //清除系统的权限缓存
        realm.clearAllCachedAuthorizationInfo();
    }

}