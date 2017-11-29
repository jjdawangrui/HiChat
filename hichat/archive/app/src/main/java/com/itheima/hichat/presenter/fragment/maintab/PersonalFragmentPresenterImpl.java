package com.itheima.hichat.presenter.fragment.maintab;

import com.hyphenate.chat.EMClient;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.wrap.SimpleEMCallBack;

import org.greenrobot.eventbus.EventBus;

import static com.lljjcoder.citypickerview.utils.JLogUtils.D;

/**
 * Created by Apple on 2016/12/7.
 */

public class PersonalFragmentPresenterImpl implements PersonalFragmentPresenter {

    @Override
    public void logout() {
        EMClient.getInstance().logout(true, new SimpleEMCallBack() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                doLogoutOperator();
            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                doLogoutOperator();
            }
        });

    }


    private void doLogoutOperator(){
        //清空数据
        DataCacheUtils.clear();

        //发出通知给MainActivity
        EventBus.getDefault().post(new User());

    }

}
