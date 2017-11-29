package com.itheima.hichat.utils;

import com.hyphenate.chat.EMClient;

/**
 * Created by Apple on 2016/12/5.
 */

public final class EMUtils {
    private EMUtils() {
    }


    //判断用户是否已经登陆到了环信的服务器
    public static boolean isLogin(){
        //EMClient.getInstance().isConnected() 是否连接到了环信的服务器
        //EMClient.getInstance().isLoggedInBefore() 之前是否登录到了环信的服务器
        return EMClient.getInstance().isConnected()&&EMClient.getInstance().isLoggedInBefore();
    }
}
