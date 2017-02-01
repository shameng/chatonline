package com.meng.chatonline.security.captcha;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author xindemeng
 *
 * 用于生成验证码图片的过滤器
 */
public class JCaptchaFilter extends OncePerRequestFilter
{

    //CaptchaService使用当前会话ID当作key获取相应的验证码图片；
    //另外需要设置响应内容不进行浏览器端缓存。
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        response.setDateHeader("Expires", 0L);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String id = request.getRequestedSessionId();
        BufferedImage bufferedImage = JCaptchaUtils.getCaptchaService().getImageChallengeForID(id);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        try
        {
            outputStream.flush();
        }finally
        {
            outputStream.close();
        }
    }
}
