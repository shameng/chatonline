package com.meng.chatonline.model.security;

import com.meng.chatonline.constant.Constants;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author xindemeng
 */
@Table(name = "authorities")
@Entity
public class Authority implements Comparable<Authority>, Serializable
{
    private static final long serialVersionUID = 88300476301025417L;

    private Integer id;
    @NotEmpty
    private String name;
    //使用@NotEmpty会报异常No validator could be found for type: java.lang.Integer
    @NotNull
    //权限类型，0:菜单类型，1:权限类型
    private Integer type;
    @NotEmpty
    private String url;
    @NotEmpty
    private String code;
    //是否可用，默认可用
    private Boolean available = true;
    //是否公有的
    private Boolean common = false;
    //如果是权限的类型的话，指明属于哪个菜单类型权限
    private Authority menu;
    //角色
    private Set<Role> roles = new HashSet<Role>();

    public Authority(){}

    public Authority(Integer id)
    {
        this.id = id;
    }

    //mappedBy映射的是Authority类的Roles集合名.
    //有mappedBy的一方为关系被维护端。
    //关系维护端负责外键纪录的更新 ，关系被维护端是没有权力更新外键纪录的。只能通过关系维护端设置与关系被维护端的关系。
    @ManyToMany(mappedBy = "authorities", cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE})
    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    @JoinColumn(name="menu_id")
    @ManyToOne
    public Authority getMenu()
    {
        return menu;
    }

    public void setMenu(Authority menu)
    {
        this.menu = menu;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(nullable = false)
    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    @Column(nullable = false)
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Column(nullable = false)
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @Column(nullable = false)
    public Boolean getAvailable()
    {
        return available;
    }

    public void setAvailable(Boolean available)
    {
        this.available = available;
    }

    @Column(nullable = false)
    public Boolean getCommon()
    {
        return common;
    }

    public void setCommon(Boolean common)
    {
        this.common = common;
    }

    @Override
    public String toString()
    {
        return "Authority{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Transient
    public String getDesc()
    {
        return "名称：" + name + "\r\n" +
                "类型：" + (type == Constants.MENU_TYPE? "菜单" : "权限") + "\r\n" +
                "URL：" + url + "\r\n" +
                "权限代码：" + code + "\r\n" +
                "是否可用：" + (available? "是" : "否");
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        else {
            if (obj instanceof Authority) {
                Authority authority = (Authority) obj;
                if (authority.id == this.id)
                    return true;
            }
        }
        return false;
    }

    public int compareTo(Authority o)
    {
        //排序规则：1.菜单-->权限，2.可用-->不可用
        //。。。用不上
//        int flag = this.getAvailable().compareTo(o.getAvailable());
//
//        //如果flag=0即属性相等
//        if (flag == 0)
//            return this.getType().compareTo(o.getType());
//        return flag == 1? -1 : 1;

        //根据菜单来排序
        if (this.menu != null && o.getMenu() != null)
        {
            int flag = this.menu.getId().compareTo(o.getMenu().getId());
            return flag;
        }
        return 0;
    }
}
