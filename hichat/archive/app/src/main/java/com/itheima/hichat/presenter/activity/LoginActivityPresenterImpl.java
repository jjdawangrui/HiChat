package com.itheima.hichat.presenter.activity;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.modle.event.UserEvent;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.view.activity.LoginActivityView;
import com.itheima.hichat.wrap.SimpleEMCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.lljjcoder.citypickerview.utils.JLogUtils.D;

/**
 * Created by Apple on 2016/12/7.
 */

public class LoginActivityPresenterImpl implements LoginActivityPresenter {

    private LoginActivityView loginActivityView;

    public LoginActivityPresenterImpl(LoginActivityView loginActivityView) {
        this.loginActivityView = loginActivityView;
    }

    @Override
    public void login(final String username, String password) {
        EMClient.getInstance().login(username,password,new SimpleEMCallBack(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                loginActivityView.afterLogin(true);
                queryUesrInfo(username);
                //加载会话消息
                EMClient.getInstance().chatManager().loadAllConversations();
            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                loginActivityView.afterLogin(false);
            }
        });
    }

    //通过用户名到Bmob云数据通过username进行查询
    private void queryUesrInfo(final String username){
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("username",username);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                User user;
                if(list.size() != 0 && e == null){
                    //获取到了数据
                   user = list.get(0);
                }else{
                    user = new User();
                    user.setUsername(username);
               }
                //把当前的用户信息缓存到内存中
                DataCacheUtils.currentUser = user;

                //发送消息通知主界面里面的5个Fragment
                EventBus.getDefault().post(new UserEvent(user,UserEvent.LOGIN));

                //获取联系人列表
                getAllContactFromServer();
            }
        });
    }

    //从环信的服务器上获取好友列表
    public void getAllContactFromServer(){
        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                //缓存好友列表
                DataCacheUtils.friends = strings;

                //从Bmob服务器获取好友的详细信息
                getAllContactsInfoFromBmob();
            }

            @Override
            public void onError(int i, String s) {
                //通知显示进入主界面
                loginActivityView.afterQueryUserInfo();
            }
        });
    }

    //从Bmob服务器获取好友的详细信息
    private void getAllContactsInfoFromBmob(){
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereContainedIn("username",DataCacheUtils.friends);
        bmobQuery.order("username");
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(list.size() != 0 && e == null){
                    DataCacheUtils.users = list;
                }
                //通知显示进入主界面
                loginActivityView.afterQueryUserInfo();
            }
        });
    }
}
