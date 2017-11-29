package com.itheima.hichat.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.hichat.R;
import com.itheima.hichat.base.BaseActivity;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.presenter.activity.RegisterActivityPresenter;
import com.itheima.hichat.presenter.activity.RegisterActivityPresenterImpl;
import com.itheima.hichat.utils.MyToast;
import com.itheima.hichat.utils.ValidateUtils;
import com.itheima.hichat.wrap.SimpleTextWatcher;

import java.io.File;

import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;

public class RegisterActivity extends BaseActivity implements View.OnClickListener,RegisterActivityView {

    private static final int SUCCESS_UPLOAD_ICON = 100;
    private static final int ERROR_UPLOAD_ICON = 101;
    private static final  int SUCCESS_SAVE_USER_INFO = 102;
    private static final int SUCCESS_CREATE_EMACCOUNT = 103;
    private static final int  SUCCESS_LOGIN_EMSERVER = 104;
    private static final int ERROR_LOGIN_EMSERVER = 105;
    private View rlLoading;
    private TextView tvInfo;
    private User user;
    private RegisterActivityPresenter registerActivityPresenter;

    /**
     * what是标识符，相当于id，key，而obj是传的值
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS_UPLOAD_ICON:
                case SUCCESS_SAVE_USER_INFO:
                case SUCCESS_CREATE_EMACCOUNT:
                case SUCCESS_LOGIN_EMSERVER:
                    String content = (String) msg.obj;
                    tvInfo.setText(content);
                break;
                case ERROR_UPLOAD_ICON:
                case ERROR_LOGIN_EMSERVER:
                    //关闭加载界面
                    rlLoading.setVisibility(View.GONE);
                    MyToast.show(RegisterActivity.this,(String) msg.obj);
                break;
            }
        }
    };

    @Override
    public View addContent(LayoutInflater mInflater, FrameLayout content) {
        View view = mInflater.inflate(R.layout.activity_register,content,true);
        return view;
    }

    TextInputEditText etPwd;
    TextInputEditText etConfirmPwd;
    Button btRegister;
    TextInputEditText etUsername;

    @Override
    public void initContent(View view) {
        etPwd = ButterKnife.findById(view,R.id.et_pwd);
        etConfirmPwd = ButterKnife.findById(view,R.id.et_confirm_pwd);
        btRegister = ButterKnife.findById(view,R.id.bt_register);
        etUsername = ButterKnife.findById(view,R.id.et_username);

        btRegister.setOnClickListener(this);

        //设置TextInputEditText输入内容改变监听
        SimpleTextWatcher textWatcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                //用户名、密码、确认密码是否为空
                String username = etUsername.getText().toString();
                String pwd = etPwd.getText().toString();
                String confirmPwd = etConfirmPwd.getText().toString();
                if(TextUtils.isEmpty(username) ||
                        TextUtils.isEmpty(pwd) ||
                        TextUtils.isEmpty(confirmPwd)){
                    if(btRegister.isEnabled()){
                        btRegister.setEnabled(false);
                    }
                }else{
                    if(!btRegister.isEnabled()){
                        btRegister.setEnabled(true);
                    }
                }
            }
        };
        etUsername.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);
        etConfirmPwd.addTextChangedListener(textWatcher);

        //监听软键盘的操作
        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    if(TextUtils.isEmpty(etUsername.getText().toString().trim())){
                        MyToast.show(RegisterActivity.this,"用户名输入为空");
                    }
                }
                return false;
            }
        });
        user = (User) getIntent().getSerializableExtra("user");
        registerActivityPresenter = new RegisterActivityPresenterImpl(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_register:
                register();
                break;
            case R.id.ib_back:
                if(rlLoading != null && rlLoading.getVisibility() == View.VISIBLE){
                    //点击后退图标和按钮都应该不执行任何操作
                }else{
                    //否则就执行父类的，返回
                    super.onClick(view);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(rlLoading != null && rlLoading.getVisibility() == View.VISIBLE){
                return true;//中断事件
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void register() {
        //校验用户名、密码是否合法，密码和确认密码是否一致
        boolean isOK = validateData();
        if(isOK){
            //显示加载界面
            addLoadingUI();

            //封装数据到User对象
            putDataToUser();

            //执行后续注册的行为
            registerActivityPresenter.uploadUserIcon(user);
        }
    }

    //封装数据到User对象
    private void putDataToUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPwd.getText().toString().trim();
        user.setUsername(username);
        user.setPassword(password);
        //因为会丢失，再次把User的Icon设置
        File file = new File(getFilesDir(),user.getNickname()+".jpg");
        BmobFile bmobFile = new BmobFile(file);
        user.setIcon(bmobFile);
    }

    //显示加载界面
    private void addLoadingUI(){
        /**
         * getLayoutInflater得到布局加载器
            把loading布局加载到前面Activity的跟布局上，getRlRoot，很强势
         */
        View view = getLayoutInflater().inflate(R.layout.loading, getRlRoot(), true);
        //初始化控件
        rlLoading = ButterKnife.findById(view, R.id.rl_loading);
        tvInfo = ButterKnife.findById(view, R.id.tv_info);
        tvInfo.setText("正在注册");
    }

    //校验用户名、密码是否合法，密码和确认密码是否一致
    private boolean validateData() {
        //用户名
        String username = etUsername.getText().toString().trim();
        boolean validateUserName = ValidateUtils.validateUserName(username);
        if(!validateUserName){
            MyToast.show(RegisterActivity.this,"用户名不合法");
            etUsername.setFocusable(true);
            return false;
        }
        //密码
        String pwd = etPwd.getText().toString().trim();
        boolean validatePwd = ValidateUtils.validatePassword(pwd);
        if(!validatePwd){
            MyToast.show(RegisterActivity.this,"密码不合法");
//            etPwd.getText().clear();
//            etConfirmPwd.getText().clear();
            etPwd.setFocusable(true);
            return false;
        }
        //确认密码
        String confirmPwd = etConfirmPwd.getText().toString().trim();
        if(!confirmPwd.equals(pwd)){
            MyToast.show(RegisterActivity.this,"密码和确认密码不一致");
//            etConfirmPwd.getText().clear();
            etConfirmPwd.setFocusable(true);
            return false;
        }
        return true;
    }


    @Override
    public void afterUploadUserIcon(boolean success) {
        if(success){
            //更新加载界面的信息：头像上传成功
            sendMessage(SUCCESS_UPLOAD_ICON,"上传头像成功");
        }else{
            //吐司提示：注册失败
            sendMessage(ERROR_UPLOAD_ICON,"注册失败");
        }
    }

    @Override
    public void afterSaveUserInfoToBmob(boolean success) {
        if(success){
            sendMessage(SUCCESS_SAVE_USER_INFO,"保存用户信息成功");
        }else{
            sendMessage(ERROR_UPLOAD_ICON,"注册失败");
        }
    }

    @Override
    public void afterCreateEMAccount(boolean success) {
        if(success){
            sendMessage(SUCCESS_CREATE_EMACCOUNT,"创建用户成功");
        }else{
            sendMessage(ERROR_UPLOAD_ICON,"注册失败");
        }
    }

    @Override
    public void afterLoginEMServer(boolean success) {
        if(success){
            sendMessage(SUCCESS_LOGIN_EMSERVER,"登录成功");
        }else{
            sendMessage(ERROR_LOGIN_EMSERVER,"登录失败");
        }
        //进入MainActivity
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * what是标识符，相当于id，key，而obj是传的值
     */
    private void sendMessage(int what,String content){
        Message message = Message.obtain();
        message.what = what;
        message.obj = content;
        mHandler.sendMessage(message);
    }
}
