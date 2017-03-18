package com.meng.chatonline.annotation;

import com.meng.chatonline.constant.Constants;

import java.lang.annotation.*;

/**
 * @author xindemeng
 *
 * 绑定当前登录的用户
 * 不同于@ModelAttribute
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser
{
    /**
     * 当前用户在session中的名字
     */
    String value() default Constants.CURRENT_USER;
}
