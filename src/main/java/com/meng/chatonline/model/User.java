package com.meng.chatonline.model;

import com.meng.chatonline.model.security.Role;
import com.meng.chatonline.utils.ValidationUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author xindemeng
 */
@Table(name="users")
@Entity
public class User implements Serializable
{
    private static final long serialVersionUID = 2418805577466640476L;

    private Integer id;
    private String account;
    private String name;
    private String password;
    private String salt;
    private Boolean superAdmin = false;
    //角色
    private Set<Role> roles = new HashSet<Role>();

    public User() {}

    public User(Integer id)
    {
        this.id = id;
    }

    public User(Integer id, String account, String name)
    {
        this.id = id;
        this.account = account;
        this.name = name;
    }

    public User(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    public User(Integer id, String account, String name, Set<Role> roles)
    {
        this.id = id;
        this.account = account;
        this.name = name;
        this.roles = roles;
    }

    public User(Integer id, String account, String name, Boolean superAdmin)
    {
        this.id = id;
        this.account = account;
        this.name = name;
        this.superAdmin = superAdmin;
    }

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Column(nullable = false)
    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    @Column(nullable = false)
    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    @Column(nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(nullable = false)
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Boolean getSuperAdmin()
    {
        return superAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin)
    {
        this.superAdmin = superAdmin;
    }

    @JoinTable(name="user_role",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName = "id")})
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE})
    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    @Override
    public String toString()
    {
        return "{" +
                "'id':" + id +
                ", 'account':'" + account + '\'' +
                ", 'name':'" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        else {
            if (obj instanceof User) {
                User user = (User) obj;
                if (user.id == this.id)
                    return true;
            }
        }
        return false;
    }

//    重写hashcode方法为了将数据存入HashSet/HashMap/Hashtable类时进行比较
    @Override
    public int hashCode()
    {
        return this.id * 37;
    }

}
