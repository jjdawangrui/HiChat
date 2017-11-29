package com.itheima.hichat.modle.event;

import com.itheima.hichat.modle.bean.User;

/**
 * Created by Apple on 2016/12/5.
 * 用户的行为
 */

public class UserEvent {
    public User user;
    public int type;

    public final static int LOGIN = 0;//登录
    public final static int LOGOUT = 1;//退出

    public UserEvent(User user, int type) {
        this.user = user;
        this.type = type;
    }
}
