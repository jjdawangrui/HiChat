package com.itheima.hichat.presenter.fragment.maintab;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.itheima.hichat.view.fragment.maintab.ConversationFragmentView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Apple on 2016/12/10.
 */

public class ConversationFragmentPresenterImpl implements ConversationFragmentPresenter {

    private List<EMConversation> emConversationList = new ArrayList<EMConversation>();

    private ConversationFragmentView conversationFragmentView;

    public ConversationFragmentPresenterImpl(ConversationFragmentView conversationFragmentView) {
        this.conversationFragmentView = conversationFragmentView;
    }

    @Override
    public void loadAllConversations() {
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        emConversationList.clear();
        emConversationList.addAll(allConversations.values());
        //进行会话列表的排序
        Collections.sort(emConversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {
                //倒序（时间）
                return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
            }
        });
        //刷新界面
        conversationFragmentView.afterLoadAllConversations(emConversationList);
    }
}
