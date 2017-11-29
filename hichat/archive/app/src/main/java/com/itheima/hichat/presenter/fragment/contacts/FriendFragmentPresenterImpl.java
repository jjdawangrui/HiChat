package com.itheima.hichat.presenter.fragment.contacts;

import com.hyphenate.chat.EMClient;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.view.fragment.contacts.FriendFragmentView;
import com.itheima.hichat.wrap.SimpleEMCallBack;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Apple on 2016/12/8.
 */

public class FriendFragmentPresenterImpl implements FriendFragmentPresenter {

    private FriendFragmentView friendFragmentView;

    public FriendFragmentPresenterImpl(FriendFragmentView friendFragmentView) {
        this.friendFragmentView = friendFragmentView;
    }

    @Override
    public void queryUserInfoByUserName(final String username) {
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("username",username);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                User user = null;
                if(list.size() !=  0 && e == null){
                    //查询成功
                    user = list.get(0);
                }else{
                    //查询失败
                    user = new User();
                    user.setUsername(username);
                }
                //把数据存储到缓存
                DataCacheUtils.add(user);
                //刷新显示
                friendFragmentView.afterQueryUserInfoByUserName();
            }
        });
    }

    @Override
    public void deleteFriend(User user) {
        EMClient.getInstance().contactManager().aysncDeleteContact(user.getUsername(),new SimpleEMCallBack());
    }
}
