package com.meng.chatonline.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author xindemeng
 */
public class CollectionUtils
{
    public static void sort(List list)
    {
        if (ValidationUtils.validateCollection(list))
            Collections.sort(list);
    }

    //把集合里的元素合并成字符串，用“,”隔开
    public static String collToStr(Collection coll)
    {
        String temp = "";
        if (ValidationUtils.validateCollection(coll))
        {
            for (Object o : coll)
            {
                temp += o + ",";
            }
            return temp.substring(0, temp.length() - 1);
        }
        return temp;
    }
}
