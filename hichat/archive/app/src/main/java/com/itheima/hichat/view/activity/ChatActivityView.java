package com.itheima.hichat.view.activity;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by Apple on 2016/12/9.
 */

public interface ChatActivityView {
    void afterSendMsg();
    void afterLoadMessages(List<EMMessage> emMessageList);
    void refreshUI();
    void afterLoadMoreMessages(boolean success,int loadCount);
    void afterReceiveMsg();
}
