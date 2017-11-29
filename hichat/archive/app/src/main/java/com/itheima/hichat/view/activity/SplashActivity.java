package com.itheima.hichat.view.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.itheima.hichat.R;
import com.itheima.hichat.presenter.activity.SplashActivityPresenter;
import com.itheima.hichat.presenter.activity.SplashActivityPresenterImpl;
import com.itheima.hichat.utils.CheckPermissionUtils;
import com.itheima.hichat.utils.EMUtils;

public class SplashActivity extends AppCompatActivity implements SplashActivityView{

    private static final int ENTER_MAIN_UI = 100;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ENTER_MAIN_UI:
                    //进入主界面
                    enterMainUI();
                    break;
            }
        }
    };

    private void enterMainUI(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if(permissions.length == 0){
            //权限都申请了
            //是否登录
            if(EMUtils.isLogin()){
                //加载用户的信息
                SplashActivityPresenter splashActivityPresenter = new SplashActivityPresenterImpl(this);
                splashActivityPresenter.queryUserInfo();
            }else{
                //过2s后进入主界面(消息机制)
                mHandler.sendEmptyMessageDelayed(ENTER_MAIN_UI,2000);
            }
        }else{
            //申请权限
            ActivityCompat.requestPermissions(this,permissions,100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            for (int grantResult : grantResults) {
                if(grantResult == PackageManager.PERMISSION_DENIED){
                    finish();
                    return;
                }
            }
            mHandler.sendEmptyMessageDelayed(ENTER_MAIN_UI,1000);
        }
    }

    @Override
    public void afterQueryUserInfo() {
       enterMainUI();
    }
}
