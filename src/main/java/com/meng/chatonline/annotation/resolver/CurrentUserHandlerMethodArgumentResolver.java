package com.meng.chatonline.annotation.resolver;

import com.meng.chatonline.annotation.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author xindemeng
 *
 * 自定义的注释解析器
 */
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver
{
    public CurrentUserHandlerMethodArgumentResolver() {}

    public boolean supportsParameter(MethodParameter parameter)
    {
        if (parameter.hasParameterAnnotation(CurrentUser.class))
            return true;
        return false;
    }

    //返回从session里获取的当前用户
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception
    {
        CurrentUser currentUserAnnotation = parameter.getParameterAnnotation(CurrentUser.class);
        return webRequest.getAttribute(currentUserAnnotation.value(), NativeWebRequest.SCOPE_SESSION);
    }
}
