package com.meng.chatonline.utils;

/**
 * @author xindemeng
 *
 * 字符串工具类
 */
public class StringUtils
{
    //把数组合并成字符串，用“,”隔开
    public static String arrToStr(String[] ownAuthIds)
    {
        String temp = "";
        if (ValidationUtils.validateArray(ownAuthIds))
        {
            for (String str : ownAuthIds)
            {
                temp += str + ",";
            }
            return temp.substring(0, temp.length() - 1);
        }
        return temp;
    }
}
