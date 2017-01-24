package com.meng.chatonline.security;

import com.meng.chatonline.exception.LoginException;
import com.meng.chatonline.model.ActiveUser;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xindemeng
 *
 * 登陆凭证匹配器，增加了限制同一用户名重复登陆次数的功能，
 * 如果多次输入错误将持续不能登陆，不能登陆时间由缓冲池的timeToIdleSeconds属性决定
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher
{
    private String cacheName;
    private CacheManager cacheManager;
    //最多重复错误输入次数
    private int maxRetryCount;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
    {
        String account = (String) token.getPrincipal();

        Cache<String, AtomicInteger> cache = this.getCache();
        if (cache != null)
        {
            AtomicInteger retryCount = cache.get(account);
            if (retryCount == null)
            {
                retryCount = new AtomicInteger(0);
                cache.put(account, retryCount);
            }
            //如果超过五次就抛出异常
            if (retryCount.incrementAndGet() > getMaxRetryCount())
            {
                throw new ExcessiveAttemptsException();
            }
            boolean match = super.doCredentialsMatch(token, info);
            //如果账号密码正确
            if (match)
                cache.remove(account);
            return match;
        }

        return super.doCredentialsMatch(token, info);
    }

    //获取缓存池，使用AtomicInteger类型来控制并发
    public Cache<String, AtomicInteger> getCache()
    {
        return cacheManager.getCache(getCacheName());
    }

    public String getCacheName()
    {
        return cacheName;
    }

    public void setCacheName(String cacheName)
    {
        this.cacheName = cacheName;
    }

    public CacheManager getCacheManager()
    {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
    }

    public int getMaxRetryCount()
    {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount)
    {
        this.maxRetryCount = maxRetryCount;
    }
}
