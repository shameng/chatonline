package com.meng.chatonline.service.oauth2.impl;

import com.meng.chatonline.service.oauth2.ClientService;
import com.meng.chatonline.service.oauth2.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xindemeng
 */
@Service("oAuthService")
public class OAuth2ServiceImpl implements OAuth2Service
{
    private Cache cache;

    @Resource
    private ClientService clientService;

    @Autowired
    public OAuth2ServiceImpl(CacheManager cacheManager)
    {
        this.cache = cacheManager.getCache("client-code-cache");
    }

    //添加授权码
    public void addAuthCode(String authCode, String account)
    {
        cache.put(authCode, account);
    }

    //添加访问令牌
    public void addAccessToken(String accessToken, String account)
    {
        cache.put(accessToken, account);
    }

    //验证auth code是否有效
    public boolean checkAuthCode(String authCode)
    {
        return cache.get(authCode) != null;
    }

    //验证access token是否有效
    public boolean checkAccessToken(String accessToken)
    {
        return cache.get(accessToken) != null;
    }

    public String getAccountByAuthCode(String authCode)
    {
        return (String) cache.get(authCode).get();
    }

    public String getAccountByAccessToken(String accessToken)
    {
        return (String) cache.get(accessToken).get();
    }

    //auth code和access token的过期时间，其实就是缓存池里元素的有效时间
    public long getExpireIn()
    {
        return 3600L;
    }

    //验证clientId是否存在
    public boolean checkClientId(String ClientId)
    {
        return clientService.findByClientId(ClientId) != null;
    }

    //验证clientSecret是否存在
    public boolean checkClientSecret(String clientSecret)
    {
        return clientService.findByClientSecret(clientSecret) != null;
    }
}
