package com.itheima.hichat.view.fragment.maintab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.hichat.R;
import com.itheima.hichat.adapter.ContactsFragmentVPAdapter;
import com.itheima.hichat.base.BaseFragment;
import com.itheima.hichat.modle.event.FriendEvent;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.utils.FragmentFactory;
import com.itheima.hichat.view.activity.AddFriendActivity;
import com.itheima.hichat.view.fragment.contacts.AttentionFragment;
import com.itheima.hichat.view.fragment.contacts.AuthenticateFragment;
import com.itheima.hichat.view.fragment.contacts.FansFragment;
import com.itheima.hichat.view.fragment.contacts.FriendFragment;
import com.itheima.hichat.view.fragment.contacts.GroupFragment;
import com.itheima.hichat.wrap.SimpleOnTabSelectedListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;
import static com.lljjcoder.citypickerview.utils.JLogUtils.A;

/**
 * Created by Apple on 2016/12/1.
 */

public class ContactFragment extends BaseFragment implements View.OnClickListener {

    private TabLayout contacts_tableLayout;
    private ViewPager contacts_vp;
    private String[] contacts_tab;

    @Override
    public void setDefaultTitle(TextView tv) {
        tv.setText("通讯录");
    }

    @Override
    public void setEmptyViewInfo(ImageView iv, TextView tv) {
        iv.setImageResource(R.drawable.ic_guest_contact_empty);
        tv.setText("可以让附近的人互动");
    }

    @Override
    public View addLeftHeader(LayoutInflater mInflater, ViewGroup headerLeft) {
        return null;
    }

    @Override
    public View addRightHeader(LayoutInflater mInflater, ViewGroup headerRight) {
        View view = mInflater.inflate(R.layout.header_contacts_fragment,headerRight,true);
        ImageButton ib_add_friend = ButterKnife.findById(view,R.id.ib_add_friend);
        ib_add_friend.setOnClickListener(this);
        return view;
    }

    public void showLoginHeaderContent(){
        super.showLoginHeaderContent();
        setHeaderTitleFormat("好友",0);
    }


    @Override
    public View addLoginBodyContent(LayoutInflater mInflater, ViewGroup content) {
        View view = mInflater.inflate(R.layout.fragment_contacts,content,true);
        return view;
    }

    //字节码数组
    Class[] clazzs = new Class[]{
            FriendFragment.class,
            AttentionFragment.class,
            FansFragment.class,
            GroupFragment.class,
            AuthenticateFragment.class
    };

    @Override
    public void initLoginBodyContent(View view) {
        contacts_tableLayout = findById(view, R.id.contacts_tableLayout);
        contacts_vp = findById(view, R.id.contacts_vp);

        //加载tab资源
        contacts_tab = getResources().getStringArray(R.array.contacts_tab);
        //创建Fragment的实例
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < contacts_tab.length; i++) {
            Fragment fragment = FragmentFactory.getInstance(clazzs[i]);
            Bundle bundle = new Bundle();
            bundle.putString("title", contacts_tab[i]);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
                                                                    //这里有嵌套，所以要子Fragment管理器
        ContactsFragmentVPAdapter adapter = new ContactsFragmentVPAdapter(getChildFragmentManager(),fragments);
        contacts_vp.setAdapter(adapter);
        //让TabLayout和ViewPager进行关联
        contacts_tableLayout.setupWithViewPager(contacts_vp);

        //监听Tab的切换
        contacts_tableLayout.setOnTabSelectedListener(new SimpleOnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int position = tab.getPosition();
                dealTitle(position);
            }
        });
        dealTitle(0);
    }

    //处理标题的切换
    private void dealTitle(int position){
        int size = 0;
        switch (position){
            case 0:
                size = DataCacheUtils.users.size();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
        setHeaderTitleFormat(contacts_tab[position],size);
    }


    public void onClick(View view){
        super.onClick(view);
        switch (view.getId()){
            case R.id.ib_add_friend:
                AddFriendActivity.startActivity(getContext());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FriendEvent event){
        dealTitle(0);
    }

}
