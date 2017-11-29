package com.itheima.hichat.utils;

import com.itheima.hichat.modle.bean.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.hyphenate.chat.a.a.a.f;

/**
 * Created by Apple on 2016/12/7.
 */

public final class DataCacheUtils {

    //当前用户
    public static User currentUser;
    //好友列表
    public static List<String> friends = new ArrayList<>();

    //好友列表详细
    public static List<User> users = new ArrayList<>();

    //添加
    public static void add(User user){
        friends.add(user.getUsername());
        users.add(user);

        //排序
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getUsername().compareTo(o2.getUsername());
            }
        });
    }

    //查找
    public static User find(String username){
        for (User user : users) {
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    //移除
    public static void remove(User user){
        users.remove(user);
    }


    //清空
    public static void clear(){
        currentUser = null;
        friends.clear();
        users.clear();
    }
}
