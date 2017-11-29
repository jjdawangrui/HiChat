package com.itheima.hichat.view.fragment.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.itheima.hichat.adapter.FriendAdapter;
import com.itheima.hichat.base.BaseContactsFragment;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.modle.event.FriendEvent;
import com.itheima.hichat.presenter.fragment.contacts.FriendFragmentPresenter;
import com.itheima.hichat.presenter.fragment.contacts.FriendFragmentPresenterImpl;
import com.itheima.hichat.view.activity.ChatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Apple on 2016/12/7.
 */

public class FriendFragment extends BaseContactsFragment implements FriendFragmentView,FriendAdapter.OnFriendItemClickListener{

    private FriendFragmentPresenter friendFragmentPresenter;
    private FriendAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        friendFragmentPresenter = new FriendFragmentPresenterImpl(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FriendEvent event){
        if(event.type == FriendEvent.ADD){
            //添加好友（从Bmob数据库查询好友详情）
            friendFragmentPresenter.queryUserInfoByUserName(event.username);
        }else if(event.type == FriendEvent.DELETE){
            //刷新界面
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new FriendAdapter();
        setAdapter(adapter);
        adapter.setOnFriendItemClickListener(this);
    }

    @Override
    public void afterQueryUserInfoByUserName() {
        //刷新显示
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(User user) {
        //进入聊天界面
        ChatActivity.startActivity(getContext(),user);
    }

    @Override
    public void onItemLongClick(final User user) {
        //弹出删除提醒的对话框
        new AlertDialog.Builder(getContext())
                .setTitle("删除")
                .setMessage("您真的要删除"+user.getUsername()+"吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            friendFragmentPresenter.deleteFriend(user);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }
}
