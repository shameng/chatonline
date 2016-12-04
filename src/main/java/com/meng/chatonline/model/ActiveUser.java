package com.meng.chatonline.model;

import java.io.Serializable;

/**
 * @Author xindemeng
 *
 * 放在session里的user
 */
public class ActiveUser implements Serializable
{
    private Integer id;
    private String account;
    private String name;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
