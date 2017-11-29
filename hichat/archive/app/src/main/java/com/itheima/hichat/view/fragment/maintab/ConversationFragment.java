package com.itheima.hichat.view.fragment.maintab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.itheima.hichat.R;
import com.itheima.hichat.adapter.ConversationRVAdapter;
import com.itheima.hichat.base.BaseFragment;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.modle.event.MessageEvent;
import com.itheima.hichat.presenter.fragment.maintab.ConversationFragmentPresenter;
import com.itheima.hichat.presenter.fragment.maintab.ConversationFragmentPresenterImpl;
import com.itheima.hichat.utils.MyLogger;
import com.itheima.hichat.view.activity.ChatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Apple on 2016/12/1.
 */

public class ConversationFragment extends BaseFragment implements ConversationFragmentView,ConversationRVAdapter.OnConversationItemClickListener{

    private ConversationRVAdapter adapter;
    private ConversationFragmentPresenter conversationFragmentPresenter;

    @Override
    public void setDefaultTitle(TextView tv) {
        tv.setText("会话");
    }
    @Override
    public void setEmptyViewInfo(ImageView iv, TextView tv) {
        iv.setImageResource(R.drawable.ic_guest_messag_empty);
        tv.setText("可以让附近的人发收消息");
    }

    @Override
    public View addLeftHeader(LayoutInflater mInflater, ViewGroup headerLeft) {
        return null;
    }

    @Override
    public View addRightHeader(LayoutInflater mInflater, ViewGroup headerRight) {
        return null;
    }

    public void showLoginHeaderContent(){
        super.showLoginHeaderContent();
        setHeaderTitleFormat("消息",0);
    }

    @Override
    public View addLoginBodyContent(LayoutInflater mInflater, ViewGroup content) {
        View view = mInflater.inflate(R.layout.fragment_conversation,content,true);
        return view;
    }

    @Override
    public void initLoginBodyContent(View view) {
        RecyclerView conversationRV = ButterKnife.findById(view,R.id.conversation_rv);
        conversationRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ConversationRVAdapter(null,getContext());
        conversationRV.setAdapter(adapter);
        adapter.setOnConversationItemClickListener(this);

        conversationFragmentPresenter = new ConversationFragmentPresenterImpl(this);
        //进行会话列表的查询
        conversationFragmentPresenter.loadAllConversations();
    }

    @Override
    public void afterLoadAllConversations(List<EMConversation> emConversationList) {
        adapter.setEmConversationList(emConversationList);
        setHeaderTitleFormat("消息",emConversationList.size());
    }

    @Override
    public void onConversationItemClick(User user) {
        //进入聊天界面
        ChatActivity.startActivity(getContext(),user);
    }

    @Override
    public void onResume() {
        super.onResume();
        //重新加载会话
        conversationFragmentPresenter.loadAllConversations();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        if(event.type == MessageEvent.RECEIVE){
            //重新加载会话
            conversationFragmentPresenter.loadAllConversations();
        }
    }

}
