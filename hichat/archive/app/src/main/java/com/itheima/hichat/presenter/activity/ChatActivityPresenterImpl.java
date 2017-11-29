package com.itheima.hichat.presenter.activity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.view.activity.ChatActivityView;
import com.itheima.hichat.wrap.SimpleEMCallBack;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by Apple on 2016/12/9.
 */

public class ChatActivityPresenterImpl implements ChatActivityPresenter {

    private ChatActivityView chatActivityView;
    private List<EMMessage> emMessageList = new ArrayList<>();

    //每次获取消息的数量
    private final int PAGESIZE = 20;

    public ChatActivityPresenterImpl(ChatActivityView chatActivityView) {
        this.chatActivityView = chatActivityView;
    }

    @Override
    public void sendMsg(EMMessage emMessage) {
        //设置为正在发送的状态
        emMessage.setStatus(EMMessage.Status.INPROGRESS);
        //监听消息的发送
        emMessage.setMessageStatusCallback(new SimpleEMCallBack(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                //子线程
                chatActivityView.refreshUI();
        }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                //子线程
                chatActivityView.refreshUI();
            }
        });
        //消息的发送
        EMClient.getInstance().chatManager().sendMessage(emMessage);
        //添加到集合
        emMessageList.add(emMessage);
        //刷新显示
        chatActivityView.afterSendMsg();
    }

    @Override
    public void loadMessages(User user) {
        //获取到会话
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(user.getUsername());
        //标记会话里面的消息已经读取
        conversation.markAllMessagesAsRead();
        if(conversation != null){
            //获取内存中所有的消息（但是不包含最新）
            List<EMMessage> allMessages = conversation.getAllMessages();
            //添加消息到集合
            emMessageList.addAll(allMessages);
        }
        chatActivityView.afterLoadMessages(emMessageList);
    }

    @Override
    public void loadMoreMessages(User user) {
        //获取到会话
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(user.getUsername());
        //获取消息的总数量
        int allMsgCount = conversation.getAllMsgCount();
        if(allMsgCount > emMessageList.size()){
            //还有更多的数据
            String startMsgId =  emMessageList.get(0).getMsgId();//获取开始的Id
            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(startMsgId, PAGESIZE);
            //添加到集合
            emMessageList.addAll(0,emMessages);
            chatActivityView.afterLoadMoreMessages(true,emMessages.size());
        }else{
            chatActivityView.afterLoadMoreMessages(false,0);
        }

    }

    @Override
    public void receiveMsg(EMMessage emMessage) {
        emMessageList.add(emMessage);
        chatActivityView.afterSendMsg();
        //标记消息是已读
        //获取到会话
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(emMessage.getUserName());
        //标记会话里面的消息已经读取
        conversation.markAllMessagesAsRead();
    }

}
