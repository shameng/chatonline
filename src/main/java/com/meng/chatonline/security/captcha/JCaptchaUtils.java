package com.meng.chatonline.security.captcha;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xindemeng
 *
 * 提供相应的API来验证当前请求输入的验证码是否正确。
 */
public class JCaptchaUtils
{
    private static final MyManageableImageCaptchaService captchaService =
            new MyManageableImageCaptchaService(new FastHashMapCaptchaStore(),
                                                new MyLoginCaptchaEngine(), 180, 100000, 75000);

    //验证当前请求输入的验证码否正确；并从CaptchaService中删除已经生成的验证码；
    public static boolean validateResponse(HttpServletRequest request, String userCaptchaResponse)
    {
        if (request.getSession(false) == null)
            return false;

        boolean validate = false;
        try
        {
            String id = request.getSession().getId();
            validate = captchaService.validateResponseForID(id, userCaptchaResponse);
        } catch (CaptchaServiceException e)
        {
            e.printStackTrace();
        }
        return validate;
    }

    //验证当前请求输入的验证码是否正确；但不从CaptchaService中删除已经生成的验证码
    //（比如Ajax验证时可以使用，防止多次生成验证码）；
    public static boolean hasCaptcha(HttpServletRequest request, String userCaptchaResponse)
    {
        if (request.getSession(false) == null)
            return false;

        boolean validate = false;
        try
        {
            String id = request.getSession().getId();
            validate = captchaService.hasCaptcha(id, userCaptchaResponse);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return validate;
    }

    public static MyManageableImageCaptchaService getCaptchaService()
    {
        return captchaService;
    }
}
