package com.meng.chatonline.service.oauth2;

/**
 * @author xindemeng
 */
public interface OAuth2Service
{
    //添加授权码
    void addAuthCode(String authCode, String account);
    //添加访问令牌
    void addAccessToken(String accessToken, String account);
    //验证auth code是否有效
    boolean checkAuthCode(String authCode);
    //验证access token是否有效
    boolean checkAccessToken(String accessToken);

    String getAccountByAuthCode(String authCode);
    String getAccountByAccessToken(String accessToken);

    //auth code和access token的过期时间
    long getExpireIn();

    //验证clientId是否存在
    boolean checkClientId(String ClientId);
    //验证clientSecret是否存在
    boolean checkClientSecret(String clientSecret);
}
