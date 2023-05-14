package com.ajin.util;

import com.ajin.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @author ajin
 * @create 2023-05-14 2:28
 */
public class ShiroUtil {
    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
