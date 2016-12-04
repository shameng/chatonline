package com.meng.chatonline.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author xindemeng
 */
@Table(name="users")
@Entity
public class User implements Serializable
{
    private Integer id;
    private String account;
    private String name;
    private String password;
    private String salt;

    public User()
    {
    }

    public User(Integer id, String name, String password)
    {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User(String name, String password)
    {
        this.name = name;
        this.password = password;
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

    @Override
    public String toString()
    {
        return "User{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
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
        return this.id * 37 + name.hashCode();
    }
}
