package com.itheima.hichat.presenter.activity;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.modle.event.UserEvent;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.view.activity.RegisterActivityView;
import com.itheima.hichat.wrap.SimpleEMCallBack;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Apple on 2016/12/3.
 */

public class RegisterActivityPresenterImpl implements RegisterActivityPresenter {

    private RegisterActivityView registerActivityView;

    public RegisterActivityPresenterImpl(RegisterActivityView registerActivityView) {
        this.registerActivityView = registerActivityView;
    }

    @Override
    public void uploadUserIcon(final User user) {
        //上传头像
        user.getIcon().uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){//异常是空，就说明没有异常
                    //头像上传成功
                    registerActivityView.afterUploadUserIcon(true);
                    saveUserInfoToBmob(user);
                }else{
                    //头像上传失败
                    registerActivityView.afterUploadUserIcon(false);
                }
            }
        });
    }

    //保存用户信息
    private void saveUserInfoToBmob(final User user){
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    //保存成功
                    registerActivityView.afterSaveUserInfoToBmob(true);
                    createEMAccount(user);
                }else{
                    //保存失败
                    registerActivityView.afterSaveUserInfoToBmob(false);
                }
            }
        });
    }

    //在环信的服务器上创建账户
    private void createEMAccount(final User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //该方法是一个同步的方法（有联网的操作：需要在子线程里面执行）
                    EMClient.getInstance().createAccount(user.getUsername(),user.getPassword());
                    //创建成功
                    registerActivityView.afterCreateEMAccount(true);
                    //登录
                    loginEMServer(user);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //创建失败
                    registerActivityView.afterCreateEMAccount(false);
                }
            }
        }).start();
    }

    //登录到环信的服务器
    private void loginEMServer(final User user){
        EMClient.getInstance().login(user.getUsername(), user.getPassword(), new SimpleEMCallBack(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                registerActivityView.afterLoginEMServer(true);
                UserEvent userEvent = new UserEvent(user,UserEvent.LOGIN);
                EventBus.getDefault().post(userEvent);
                //把用户信息缓存
                DataCacheUtils.currentUser = user;
                //加载会话消息
                EMClient.getInstance().chatManager().loadAllConversations();
            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                registerActivityView.afterLoginEMServer(false);
            }
        });
    }
}
