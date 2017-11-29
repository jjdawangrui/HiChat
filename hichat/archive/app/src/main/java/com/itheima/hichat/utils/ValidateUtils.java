package com.itheima.hichat.utils;

/**
 * Created by X599 on 2016/11/12.
 * 校验工具类
 */

public final class ValidateUtils {
    public ValidateUtils() {
    }

    //校验用户名
    public static boolean validateUserName(String username){

        String regex = "(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,8}";
        return username.matches(regex);
    }

    //校验密码
    public static boolean validatePassword(String password){

        String regex = "(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,8}";
        return password.matches(regex);
    }
}
