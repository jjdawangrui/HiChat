package com.itheima.hichat.view.activity;

import com.itheima.hichat.modle.bean.User;

import java.util.List;

/**
 * Created by Apple on 2016/12/7.
 */

public interface AddFriendActivityView {
    void afterSearchFriend(boolean success, List<User> users);
}
