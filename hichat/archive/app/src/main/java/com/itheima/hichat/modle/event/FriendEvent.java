package com.itheima.hichat.modle.event;

/**
 * Created by Apple on 2016/12/8.
 * 联系人事件
 */

public class FriendEvent {
    public String username;
    public int type;

    public FriendEvent(String username, int type) {
        this.username = username;
        this.type = type;
    }

    public final static int ADD = 0;//添加好友
    public final static int DELETE = 1;//删除好友
}
