package com.meng.chatonline.controller.oauth2;

import com.meng.chatonline.constant.Constants;
import com.meng.chatonline.exception.OAuth2LoginException;
import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.service.oauth2.ClientService;
import com.meng.chatonline.service.oauth2.OAuth2Service;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author xindemeng
 *
 * OAuth2授权控制器
 */
@Controller
@RequestMapping("/oauth2")
public class AuthorizeController
{
    @Resource
    private OAuth2Service oAuth2Service;
    @Resource
    private ClientService clientService;

    /*
    1、首先通过如http://localhost:8080/chatonline/authorize?client_id=c1ebe466-1cdc-4bd3-ab69-77c3561b9dee&response_type=code&redirect_uri=http://localhost:9080/chatonline-client/oauth2-login
       访问授权页面；
    2、该控制器首先检查clientId是否正确；如果错误将返回相应的错误信息；
    3、然后判断用户是否登录了，如果没有登录首先到登录页面登录；
    4、登录成功后生成相应的auth code即授权码，然后重定向到客户端地址，如http://localhost:9080/chatonline-client/oauth2-login?code=52b1832f5dff68122f4f00ae995da0ed；
       在重定向到的地址中会带上code参数（授权码），接着客户端可以根据授权码去换取access token。
     */
    @RequestMapping("/authorize")
    public Object authorize(Model model, HttpServletRequest request)
            throws OAuthSystemException, URISyntaxException, OAuth2LoginException
    {
        try
        {
            //构建OAuth授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

            //检查传入的客户端id是否正确
            if (!oAuth2Service.checkClientId(oauthRequest.getClientId()))
            {
                OAuthResponse oAuthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                return new ResponseEntity(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
            }

            Subject subject = SecurityUtils.getSubject();
            //如果用户没有登陆
            if (!subject.isAuthenticated())
            {
                //登陆失败时跳转到登陆页面
                if (!login(subject, request))
                {
                    model.addAttribute("client", clientService.findByClientId(oauthRequest.getClientId()));
                    return "oauth2/oauth2Login";
                }
            }

            ActiveUser user = (ActiveUser) subject.getPrincipal();
            //授权码
            String authorizationCode = null;
            //responseType目前仅支持CODE，另外还有TOKEN
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if (responseType.equals(ResponseType.CODE.toString()))
            {
                //生成授权码
                OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = oAuthIssuer.authorizationCode();
                oAuth2Service.addAuthCode(authorizationCode, user.getAccount());
            }

            //进行OAuth响应构建
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
            //设置授权码
            builder.setCode(authorizationCode);
            //得到客户端重定向地址
            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

            //构建响应
            final OAuthResponse oAuthResponse = builder.location(redirectURI).buildQueryMessage();

            //根据oAuthResponse返回ResponseEntity响应
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(oAuthResponse.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        } catch (OAuthProblemException e)
        {
            e.printStackTrace();

            //出错处理
            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri))
                return new ResponseEntity("OAuth callback url needs to be provided by client!!!",
                        HttpStatus.NOT_FOUND);

            //返回错误消息（如?error=）
            final OAuthResponse oAuthResponse =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .error(e).location(redirectUri)
                    .buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(oAuthResponse.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }
    }

    //登陆
    private boolean login(Subject subject, HttpServletRequest request) throws OAuth2LoginException
    {
        if("get".equalsIgnoreCase(request.getMethod()))
            return false;

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password))
            return false;

        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        try
        {
            subject.login(token);
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
//            request.setAttribute(Constants.ERROR_MSG, "登陆失败：" + e.getClass().getName());
//            return false;
            throw new OAuth2LoginException(e.getClass().getName());
        }
    }

}
