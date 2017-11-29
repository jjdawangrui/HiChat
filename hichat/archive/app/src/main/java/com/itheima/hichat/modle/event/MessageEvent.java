package com.itheima.hichat.modle.event;

import com.hyphenate.chat.EMMessage;

/**
 * Created by Apple on 2016/12/10.
 */

public class MessageEvent {
    public EMMessage emMessage;
    public int type;

    public MessageEvent(EMMessage emMessage, int type) {
        this.emMessage = emMessage;
        this.type = type;
    }

    public final static int RECEIVE = 0;
}
