package com.meng.chatonline.utils;

import java.util.Collection;

/**
 * @Author xindemeng
 */
public class ValidationUtils
{
    public static boolean validateCollection(Collection collection)
    {
        if(collection != null && collection.size() > 0)
            return true;
        return false;
    }

    public static boolean validateStr(String str)
    {
        if (str != null && str != "")
            return true;
        return false;
    }

    public static boolean validateArray(Object[] arr)
    {
        if (arr != null && arr.length > 0)
            return true;
        return false;
    }

}
