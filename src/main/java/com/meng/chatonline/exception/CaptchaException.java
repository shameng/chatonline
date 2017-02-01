package com.meng.chatonline.exception;

/**
 * @author xindemeng
 *
 * 验证码异常
 */
public class CaptchaException extends Exception
{
    public CaptchaException()
    {
        super();
    }

    public CaptchaException(String message)
    {
        super(message);
    }

    public String toString()
    {
        return CaptchaException.class.getName();
    }
}
