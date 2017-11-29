package com.itheima.hichat.presenter.activity;

import com.itheima.hichat.modle.bean.User;

/**
 * Created by Apple on 2016/12/7.
 */

public interface AddFriendActivityPresenter {
    void searchFriend(String keyword);

    void addFriend(User user);
}
