package com.itheima.hichat.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.itheima.hichat.R;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.modle.event.UserEvent;
import com.itheima.hichat.utils.EMUtils;
import com.itheima.hichat.view.activity.EnterNickNameActivity;
import com.itheima.hichat.view.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Apple on 2016/12/1.
 */

public abstract class BaseFragment extends Fragment {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.header_left)
    LinearLayout headerLeft;
    @BindView(R.id.header_right)
    LinearLayout headerRight;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.empty_ll)
    LinearLayout emptyLl;
    @BindView(R.id.content)
    FrameLayout content;

    private LayoutInflater inflater;
    private View addLeftHeader;
    private View addRightHeader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        //隐藏后退图标
        ibBack.setVisibility(View.GONE);
        setEmptyViewInfo(ivEmpty,tvInfo);
        setDefaultTitle(tvTitle);
        //设置界面的显示
        changeUI();
    }

    //设置缺省的标题
    public abstract void setDefaultTitle(TextView tv);

    //设置空布局的图片和文字
    public abstract  void setEmptyViewInfo(ImageView iv,TextView tv);

    @OnClick({R.id.ib_back, R.id.bt_register, R.id.bt_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                break;
            case R.id.bt_register:
                //进入输入昵称的界面
                startActivity(new Intent(getContext(), EnterNickNameActivity.class));
                break;
            case R.id.bt_login:
                //进入登录界面
                LoginActivity.startActivity(getContext());
                break;
        }
    }

    //设置标题的内容
    public void setDefaultHeaderTitle(String content){
        tvTitle.setText(content);
    }

    public void setHeaderTitleFormat(String content,int size){
        //格式化字符串的内容
        String title = String.format(getString(R.string.formattitle), content, size);
        tvTitle.setText(title);
    }

    //头
    public abstract View addLeftHeader(LayoutInflater mInflater,ViewGroup headerLeft);
    public abstract View addRightHeader(LayoutInflater mInflater,ViewGroup headerRight);

    //Body
    public void hideEmptyView(){
        emptyLl.setVisibility(View.GONE);
    }

    public void showEnptyView(){
        emptyLl.setVisibility(View.VISIBLE);
    }

    //添加登录后的Body
    public abstract View addLoginBodyContent(LayoutInflater mInflater,ViewGroup content);
    //初始化
    public abstract  void initLoginBodyContent(View view);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserEvent userEvent){
        if(userEvent.type == UserEvent.LOGIN){
            //改变UI
            changeUI();
        }
    }

    //改变UI
    private void changeUI() {
        //标题
        showHeaderContent();
        //内容
        showBodyContent();
    }

    //显示标题的内容
    private void showHeaderContent() {
        boolean isLogin = EMUtils.isLogin();
        if(isLogin){
            //显示登陆的标题
            showLoginHeaderContent();
        }else{
            //显示未登录的标题
            showLogoutHeaderContent();
        }
    }

    //显示登陆的标题
    public void showLoginHeaderContent(){
        addLeftHeader = addLeftHeader(inflater, headerLeft);
        addRightHeader = addRightHeader(inflater, headerRight);
    };

    //显示未登录的标题
    private void showLogoutHeaderContent() {
        setDefaultTitle(tvTitle);
    }

    //显示Body的内容
    private void showBodyContent() {
        boolean isLogin = EMUtils.isLogin();
        if(isLogin){
            //显示登陆的Body
            View view = addLoginBodyContent(inflater,content);
            initLoginBodyContent(view);
            hideEmptyView();
        }else{
            //显示未登录的Body
            showEnptyView();
        }
    }


}
