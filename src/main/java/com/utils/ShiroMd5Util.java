package com.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.entity.User;

/**
 * MD5密码加密工具
 * @author ljh
 */
public class ShiroMd5Util {
	
	/**
	 * User的密码加密方法
	 * @param user
	 * @return 加密后的密码
	 */
    public static String SysMd5(User user) {
        String hashAlgorithmName = "MD5";//加密方式  
        Object crdentials =user.getPassword();//密码原值  
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());//以账号作为盐值  
        int hashIterations = 1024;//加密1024次  
        SimpleHash hash = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);
        
        return hash.toString();
    }  
    
}
