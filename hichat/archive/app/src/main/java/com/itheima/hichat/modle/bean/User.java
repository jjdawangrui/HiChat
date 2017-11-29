package com.itheima.hichat.modle.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Apple on 2016/12/3.
 * 用户基本信息
 */

public class User extends BmobObject {
    private String nickname;
    private String username;
    private String password;
    private BmobFile icon;//BmobFile数据类型在通过Intent传递的过程中会丢失
    private String brithday;
    private String home;
    private int sex;// 0 男   1 女

    public User() {
    }

    public User(String nickname, String username, String password, BmobFile icon, String brithday, String home, int sex) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.icon = icon;
        this.brithday = brithday;
        this.home = home;
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public String getBrithday() {
        return brithday;
    }

    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
