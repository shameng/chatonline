package com.meng.chatonline.model;

import java.io.Serializable;

/**
 * @Author xindemeng
 *
 * 放在session里的user
 */
public class ActiveUser extends User implements Serializable
{
    private static final long serialVersionUID = -5131009085285855912L;

    public ActiveUser() {}

    public ActiveUser(Integer id)
    {
        super(id);
    }

    public ActiveUser(Integer id, String account, String name)
    {
        super(id, account, name);
    }

    public ActiveUser(Integer id, String account, String name, Boolean superAdmin)
    {
        super(id, account, name, superAdmin);
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
