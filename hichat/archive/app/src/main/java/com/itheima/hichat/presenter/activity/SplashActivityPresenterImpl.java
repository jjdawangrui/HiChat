package com.itheima.hichat.presenter.activity;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.view.activity.SplashActivityView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Apple on 2016/12/7.
 */

public class SplashActivityPresenterImpl implements SplashActivityPresenter {

    private SplashActivityView splashActivityView;

    public SplashActivityPresenterImpl(SplashActivityView splashActivityView) {
        this.splashActivityView = splashActivityView;
    }

    @Override
    public void queryUserInfo() {
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("username", EMClient.getInstance().getCurrentUser());
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(list.size() != 0 && e == null){
                    DataCacheUtils.currentUser = list.get(0);
                }else{
                    User user = new User();
                    user.setUsername(EMClient.getInstance().getCurrentUser());
                    DataCacheUtils.currentUser = user;
                }
                //获取好友列表
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

                //从Bmob服务器获取好友的详细信息，在splash页面，预加载
                getAllContactsInfoFromBmob();
            }

            @Override
            public void onError(int i, String s) {
                //进入主界面
                splashActivityView.afterQueryUserInfo();
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
                //进入主界面
                splashActivityView.afterQueryUserInfo();
            }
        });
    }

}
