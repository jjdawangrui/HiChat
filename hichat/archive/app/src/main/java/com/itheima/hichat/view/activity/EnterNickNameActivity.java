package com.itheima.hichat.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.itheima.hichat.R;
import com.itheima.hichat.base.BaseActivity;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.wrap.SimpleTextWatcher;

import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

public class EnterNickNameActivity extends BaseActivity implements View.OnClickListener {

    private TextInputEditText etNickName;
    private Button btNext;

    @Override
    public View addContent(LayoutInflater mInflater, FrameLayout content) {
        //false :加载布局到内存   true:加载布局到指定的容器
        View view = mInflater.inflate(R.layout.activity_enter_nick_name,content,true);
        return view;
    }

    @Override
    public void initContent(View view) {
        /**
         * findViewById也可以，但是返回的是View，还需要强制类型转换
         * 而黄油刀自己的方法，返回的是T，不需要转换
         */
        etNickName = ButterKnife.findById(view, R.id.et_nickname);
        btNext = ButterKnife.findById(view, R.id.bt_next);

        //设置监听
        etNickName.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if(TextUtils.isEmpty(s.toString())){
                    btNext.setEnabled(false);
                }else{
                    btNext.setEnabled(true);
                }
            }
        });

        btNext.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_next:
                enterPersonalInfoUI();
                break;
            case R.id.ib_back:
                showAlertExitDialog();
                break;
        }
    }

    private void enterPersonalInfoUI() {
        Intent intent = new Intent(this,PersonalInfoActivity.class);
        String nickname = etNickName.getText().toString().trim();
        User user = new User();
        user.setNickname(nickname);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    //弹出推出提示对话框
    private void showAlertExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要放弃注册吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            showAlertExitDialog();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
