package com.itheima.hichat.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.itheima.hichat.R;
import com.itheima.hichat.base.BaseActivity;
import com.itheima.hichat.presenter.activity.LoginActivityPresenter;
import com.itheima.hichat.presenter.activity.LoginActivityPresenterImpl;
import com.itheima.hichat.utils.MyToast;
import com.itheima.hichat.utils.TextViewUtils;
import com.itheima.hichat.utils.ValidateUtils;
import com.itheima.hichat.wrap.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hyphenate.chat.a.a.a.i;

public class LoginActivity extends BaseActivity implements View.OnClickListener,LoginActivityView {


    private static final  int SUCCESS_LOGIN = 100;
    private static final int ERROR_LOGIN = 101;
    TextInputEditText etUsername;
    TextInputEditText etPwd;
    Button btLogin;
    TextView tvSelectArea;
    TextView tvLoginQuestion;
    private View loadingUI;
    private TextView tvInfo;
    private LoginActivityPresenter loginActivityPresenter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS_LOGIN:
                    tvInfo.setText((String)msg.obj);
                    break;
                case ERROR_LOGIN:
                    tvInfo.setText((String)msg.obj);
                    loadingUI.setVisibility(View.GONE);
                    break;
            }
        }
    };


    //让其他的模块来进行调用的
    public static void startActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addContent(LayoutInflater mInflater, FrameLayout content) {
        View view = mInflater.inflate(R.layout.activity_login, content, true);
        return view;
    }

    @Override
    public void initContent(View view) {
        etUsername = ButterKnife.findById(view,R.id.et_username);
        etPwd = ButterKnife.findById(view,R.id.et_pwd);
        btLogin = ButterKnife.findById(view,R.id.bt_login);
        tvSelectArea = ButterKnife.findById(view,R.id.tv_select_area);
        tvLoginQuestion = ButterKnife.findById(view,R.id.tv_login_question);

        btLogin.setOnClickListener(this);

        createTextViewUnderline(tvSelectArea);
        createTextViewUnderline(tvLoginQuestion);

        //输入改变监听器
        final SimpleTextWatcher textWatcher = new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String username = TextViewUtils.getContent(etUsername);
                String pwd = TextViewUtils.getContent(etPwd);
                if(TextUtils.isEmpty(username)|| TextUtils.isEmpty(pwd)){
                    btLogin.setEnabled(false);
                }else{
                    btLogin.setEnabled(true);
                }
            }
        };
        etUsername.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);

        loginActivityPresenter = new LoginActivityPresenterImpl(this);
    }

    //创建TextView的下划线
    public void createTextViewUnderline(TextView textView){
        //设置画笔有一个下划线的标记
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //设置抗锯齿
        textView.getPaint().setAntiAlias(true);
    }

    public void onClick(View view){
        super.onClick(view);
        switch (view.getId()){
            case R.id.bt_login:
                login();
                break;
        }
    }

    //登录
    private void login() {
        boolean validateInfo = validateInfo();
        if(validateInfo){
            //加载加载的布局
            if(loadingUI == null){
                loadingUI = getLayoutInflater().inflate(R.layout.loading, getRlRoot(), true);
                tvInfo = ButterKnife.findById(loadingUI, R.id.tv_info);
            }
            tvInfo.setText("正在登录");
            String username = TextViewUtils.getContent(etUsername);
            String pwd = TextViewUtils.getContent(etPwd);
            //登录
            loginActivityPresenter.login(username,pwd);
        }
    }

    //校验用户的用户名、密码
    private boolean validateInfo(){
        String username = TextViewUtils.getContent(etUsername);
        String pwd = TextViewUtils.getContent(etPwd);
        if(!ValidateUtils.validateUserName(username)){
            MyToast.show(this,"用户名不合法");
            return false;
        }
        if(!ValidateUtils.validateUserName(pwd)){
            MyToast.show(this,"密码不合法");
            return false;
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(loadingUI != null && loadingUI.getVisibility() == View.VISIBLE){
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void afterLogin(boolean success) {
        if(success){
            sendMessage(SUCCESS_LOGIN,"登录成功");
        }else{
            sendMessage(ERROR_LOGIN,"登录失败");
        }
    }

    @Override
    public void afterQueryUserInfo() {
        //进入主界面
        startActivity(new Intent(this,MainActivity.class));
        //关闭
        finish();
    }

    //发送消息
    private void sendMessage(int what,String content){
        Message message = Message.obtain();
        message.what = what;
        message.obj = content;
        mHandler.sendMessage(message);
    }
}
