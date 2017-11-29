package com.itheima.hichat.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.itheima.hichat.R;
import com.itheima.hichat.adapter.ChatAdapter;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.modle.event.MessageEvent;
import com.itheima.hichat.presenter.activity.ChatActivityPresenter;
import com.itheima.hichat.presenter.activity.ChatActivityPresenterImpl;
import com.itheima.hichat.utils.TextViewUtils;
import com.itheima.hichat.wrap.SimpleTextWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity implements ChatActivityView {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.header_left)
    LinearLayout headerLeft;
    @BindView(R.id.header_right)
    LinearLayout headerRight;
    @BindView(R.id.conversation_rv)
    RecyclerView conversationRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.ib_voice)
    ImageButton ibVoice;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.ib_keyboard)
    ImageButton ibKeyboard;
    @BindView(R.id.bt_voice)
    Button btVoice;
    @BindView(R.id.ib_face)
    ImageButton ibFace;
    @BindView(R.id.ib_add)
    ImageButton ibAdd;
    @BindView(R.id.bt_send)
    Button btSend;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.ib_add_face)
    ImageButton ibAddFace;
    @BindView(R.id.ib_all_face)
    ImageButton ibAllFace;
    @BindView(R.id.ib_custom)
    ImageButton ibCustom;
    @BindView(R.id.ib_tuzi)
    ImageButton ibTuzi;
    @BindView(R.id.ib_tusiji)
    ImageButton ibTusiji;
    @BindView(R.id.ib_setting)
    ImageButton ibSetting;
    @BindView(R.id.ll_face)
    LinearLayout llFace;
    @BindView(R.id.activity_conversation)
    LinearLayout activityConversation;
    private User user;
    private ChatActivityPresenter chatActivityPresenter;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra("user");
        tvTitle.setText(user.getUsername());

        etMsg.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if(TextUtils.isEmpty(s.toString().trim())){
                    ibAdd.setVisibility(View.VISIBLE);
                    btSend.setVisibility(View.GONE);
                }else{
                    ibAdd.setVisibility(View.GONE);
                    btSend.setVisibility(View.VISIBLE);
                }
            }
        });
        conversationRv.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(user,null,this);
        conversationRv.setAdapter(chatAdapter);

        chatActivityPresenter = new ChatActivityPresenterImpl(this);
        //加载消息列表
        chatActivityPresenter.loadMessages(user);

        //给SwipeRefreshLayout设置监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chatActivityPresenter.loadMoreMessages(user);
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        if(event.type == MessageEvent.RECEIVE){
            //比较接收到的消息是不是正在聊天的好友发送的？
            EMMessage emMessage = event.emMessage;
            if(emMessage.getUserName().equals(user.getUsername())){
                //添加消息到集合
                chatActivityPresenter.receiveMsg(emMessage);
            }
        }
    }

    public static void startActivity(Context context, User user) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

    @OnClick({R.id.ib_back, R.id.bt_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.bt_send:
                sendMsg();
                break;
        }
    }

    private void sendMsg() {
        //创建消息对象
        EMMessage emMessage = EMMessage.createTxtSendMessage(TextViewUtils.getContent(etMsg), user.getUsername());
        //发送消息
        chatActivityPresenter.sendMsg(emMessage);

        //清空输入框的内容
        etMsg.getText().clear();
    }

    @Override
    public void afterSendMsg() {
        chatAdapter.notifyDataSetChanged();
        //滑动到最后
        conversationRv.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    public void afterLoadMessages(List<EMMessage> emMessageList) {
        chatAdapter.setEmMessageList(emMessageList);
        //滑动到最后
        conversationRv.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    public void refreshUI() {
        //主线程操作显示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //刷新一个条目
                chatAdapter.notifyItemChanged(chatAdapter.getItemCount() - 1);
            }
        });
    }

    @Override
    public void afterLoadMoreMessages(boolean success,int loadCount) {
        if(success){
            //显示加载的消息
            chatAdapter.notifyDataSetChanged();
            conversationRv.scrollToPosition(loadCount - 1);
            //关闭加载界面
            swipeRefreshLayout.setRefreshing(false);
        }else{
            //关闭加载界面
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void afterReceiveMsg() {
        //显示加载的消息
        chatAdapter.notifyDataSetChanged();
        conversationRv.scrollToPosition(chatAdapter.getItemCount() - 1);
    }


}
