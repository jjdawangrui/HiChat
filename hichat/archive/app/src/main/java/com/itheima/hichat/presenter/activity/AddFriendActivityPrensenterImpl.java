package com.itheima.hichat.presenter.activity;

import com.hyphenate.chat.EMClient;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.view.activity.AddFriendActivityView;
import com.itheima.hichat.wrap.SimpleEMCallBack;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Apple on 2016/12/7.
 */

public class AddFriendActivityPrensenterImpl implements AddFriendActivityPresenter {

    private AddFriendActivityView addFriendActivityView;

    public AddFriendActivityPrensenterImpl(AddFriendActivityView addFriendActivityView) {
        this.addFriendActivityView = addFriendActivityView;
    }

    @Override
    public void searchFriend(String keyword) {
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereContains("username",keyword);//注意：如果不是Bmob的收费用户该方法是失效
        //去除已经存在的好友(好友数据集合)
        bmobQuery.addWhereNotContainedIn("username", DataCacheUtils.friends);
        //排序
        bmobQuery.order("username");
        //去除自己
        bmobQuery.addWhereNotEqualTo("username",DataCacheUtils.currentUser.getUsername());
        //查询
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(list.size() != 0 && e == null){
                    //查询成功
                    addFriendActivityView.afterSearchFriend(true,list);
                }else{
                    //查询失败
                    addFriendActivityView.afterSearchFriend(false,null);
                }
            }
        });
    }

    @Override
    public void addFriend(User user) {
        //添加好友
        EMClient.getInstance().contactManager().aysncAddContact(user.getUsername(),"赶快添加我为好友，我想和你一起玩耍!",new SimpleEMCallBack());
    }
}
