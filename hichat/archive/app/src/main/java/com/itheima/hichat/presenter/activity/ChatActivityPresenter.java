package com.itheima.hichat.presenter.activity;

import com.hyphenate.chat.EMMessage;
import com.itheima.hichat.modle.bean.User;

/**
 * Created by Apple on 2016/12/9.
 */

public interface ChatActivityPresenter {

    void sendMsg(EMMessage emMessage);

    void loadMessages(User user);

    void loadMoreMessages(User user);

    void receiveMsg(EMMessage emMessage);
}
