package com.meng.chatonline.model.security;

import com.meng.chatonline.utils.ValidationUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author xindemeng
 */
@Table(name="roles")
@Entity
public class Role implements Serializable, Comparable<Role>
{
    private static final long serialVersionUID = 8517841198789066378L;

    private Integer id;
    private String name;
    //是否公共的，规定只有一个公共的角色，里面包含了所有公共的权限
    private Boolean common = false;
    //是否可用，默认可用
    private Boolean available = true;
    private Set<Authority> authorities = new HashSet<Authority>();

    public Role(){}

    public Role(String name, Boolean available)
    {
        this.name = name;
        this.available = available;
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
    public Boolean getCommon()
    {
        return common;
    }

    public void setCommon(Boolean common)
    {
        this.common = common;
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

    @JoinTable(name="role_authority",
            joinColumns = {@JoinColumn(name="role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="authority_id", referencedColumnName = "id")})
    //如果设置了fetch = FetchType.EAGER，那么表示取出这条数据时，它关联的数据也同时取出放入内存中
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE})
    public Set<Authority> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities)
    {
        this.authorities = authorities;
    }

    @Override
    public String toString()
    {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Transient
    public String getDesc()
    {
        StringBuffer buffer = new StringBuffer("");
        buffer.append("名称：" + this.name + "\r\n");
        buffer.append("是否可用：" + (this.available?"是":"否") + "\r\n");
        if (ValidationUtils.validateCollection(this.authorities))
        {
            buffer.append("包含权限：" + "\r\n");
            for (Authority auth : this.authorities)
            {
                buffer.append(" •" + auth.getName());
                buffer.append((auth.getAvailable() ? "" : "（不可用）") + "\r\n");
            }
        }
        return buffer.toString();
    }

    public int compareTo(Role o)
    {
        return this.name.compareTo(o.getName());
    }
}
