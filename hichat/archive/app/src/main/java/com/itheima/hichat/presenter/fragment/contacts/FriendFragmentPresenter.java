package com.itheima.hichat.presenter.fragment.contacts;

import com.itheima.hichat.modle.bean.User;

/**
 * Created by Apple on 2016/12/8.
 */

public interface FriendFragmentPresenter {

    void queryUserInfoByUserName(String username);

    void deleteFriend(User user);
}
