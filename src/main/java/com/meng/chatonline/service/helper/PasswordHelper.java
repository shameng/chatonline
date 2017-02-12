package com.meng.chatonline.service.helper;

import com.meng.chatonline.model.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author xindemeng
 */
@Service
public class PasswordHelper
{
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    @Value("${password.algorithmName}")
    private String algorithmName = "md5";
    @Value("${password.hashIterations}")
    private int hashIterations = 2;

    //分配salt和加密密码
    public void encryptPassword(User user)
    {
        user.setSalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(getAlgorithmName(), user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()), getHashIterations())
                .toHex();
        user.setPassword(newPassword);
    }

    public RandomNumberGenerator getRandomNumberGenerator()
    {
        return randomNumberGenerator;
    }

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator)
    {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public String getAlgorithmName()
    {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName)
    {
        this.algorithmName = algorithmName;
    }

    public int getHashIterations()
    {
        return hashIterations;
    }

    public void setHashIterations(int hashIterations)
    {
        this.hashIterations = hashIterations;
    }
}
