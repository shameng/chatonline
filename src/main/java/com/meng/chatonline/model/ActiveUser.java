package com.meng.chatonline.model;

import java.io.Serializable;

/**
 * @Author xindemeng
 *
 * 放在session里的user
 */
public class ActiveUser extends User implements Serializable
{
    public ActiveUser() {}

    public ActiveUser(Integer id)
    {
        super(id);
    }

    public ActiveUser(Integer id, String account, String name)
    {
        super(id, account, name);
    }
}
