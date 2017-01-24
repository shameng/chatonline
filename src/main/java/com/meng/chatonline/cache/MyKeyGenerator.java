package com.meng.chatonline.cache;

import com.meng.chatonline.utils.StringUtils;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * @author xindemeng
 *
 * 自定义缓存key生成器
 *
 * 如果不自定义，ehcache的生成key的默认策略是：
 * 1.如果方法没有参数，则使用0作为key。
 * 2.如果只有一个参数的话则使用该参数作为key。
 * 3.如果参数多余一个的话则使用所有参数的hashCode作为key
 */
public class MyKeyGenerator implements KeyGenerator
{

    /**
     * target:所调用的类
     * method:所调用的方法
     * params:方法参数
     */
    //key形如：com.meng.chatonline.service.impl.AuthorityServiceImpl.findOwnAuthorities(1)
    public Object generate(Object target, Method method, Object... params)
    {
        String className = target.getClass().getName();
        String methodName = method.getName();
        String paramsStr = StringUtils.arrToStr(params);
        return className + "." + methodName + "(" + paramsStr + ")";
    }
}
