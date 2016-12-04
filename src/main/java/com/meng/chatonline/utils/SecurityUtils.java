package com.meng.chatonline.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.UUID;

/**
 * @Author xindemeng
 *
 * 安全工具类
 */
public class SecurityUtils
{
    //MD5加密
    public static String md5(String password, String salt, int hashIterations)
    {
        Md5Hash md5Hash = new Md5Hash(password, salt, hashIterations);
        return md5Hash.toString();
    }

    //默认散列两次
    public static String md5Default(String password, String salt)
    {
        return md5(password, salt, 2);
    }

    //生成盐
    public static String generateSalt()
    {
        String salt = UUID.randomUUID().toString();
        return salt;
    }
}
