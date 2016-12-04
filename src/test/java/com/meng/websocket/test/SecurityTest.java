package com.meng.websocket.test;

import com.meng.chatonline.utils.SecurityUtils;
import org.junit.Test;

/**
 * @Author xindemeng
 */
public class SecurityTest
{
    @Test
    public void testMd5()
    {
        String salt = SecurityUtils.generateSalt();
        System.out.println(salt);

        String md5 = SecurityUtils.md5Default("111", salt);
        System.out.println(md5);
    }
}
