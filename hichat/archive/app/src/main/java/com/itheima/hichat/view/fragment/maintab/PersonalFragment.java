package com.itheima.hichat.view.fragment.maintab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.hichat.R;
import com.itheima.hichat.base.BaseFragment;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.presenter.fragment.maintab.PersonalFragmentPresenter;
import com.itheima.hichat.presenter.fragment.maintab.PersonalFragmentPresenterImpl;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.utils.ImageViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Apple on 2016/12/1.
 */

public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    ImageView ivIcon;
    TextView tvNickname;
    Button btLogout;
    private PersonalFragmentPresenter personalFragmentPresenter;

    @Override
    public void setDefaultTitle(TextView tv) {
        tv.setText("个人");
    }

    @Override
    public void setEmptyViewInfo(ImageView iv, TextView tv) {
        iv.setImageResource(R.drawable.ic_guest_person_empty);
        tv.setText("可以让附近的人发现你");
    }

    @Override
    public View addLeftHeader(LayoutInflater mInflater, ViewGroup headerLeft) {
        return null;
    }

    @Override
    public View addRightHeader(LayoutInflater mInflater, ViewGroup headerRight) {
        return null;
    }

    public void showLoginHeaderContent() {
        super.showLoginHeaderContent();
        setDefaultHeaderTitle("我的");
    }

    @Override
    public View addLoginBodyContent(LayoutInflater mInflater, ViewGroup content) {
        View view = mInflater.inflate(R.layout.fragment_personal, content, true);
        personalFragmentPresenter = new PersonalFragmentPresenterImpl();
        return view;
    }

    @Override
    public void initLoginBodyContent(View view) {
        ivIcon = ButterKnife.findById(view,R.id.iv_icon);
        tvNickname = ButterKnife.findById(view,R.id.tv_nickname);
        btLogout = ButterKnife.findById(view,R.id.bt_logout);
        btLogout.setOnClickListener(this);
        User user = DataCacheUtils.currentUser;
        if( user != null){
            BmobFile bmobFile = user.getIcon();
            if(bmobFile != null){
                ImageViewUtils.showImage(getContext(),bmobFile.getUrl(),R.drawable.avatar3,ivIcon);
            }

            if(TextUtils.isEmpty(user.getNickname())){
                tvNickname.setText(user.getNickname());
            }else{
                tvNickname.setText(user.getUsername());
            }

        }
    }

    public void onClick(View view){
        super.onClick(view);
        switch (view.getId()){
            case R.id.bt_logout:
                showLogoutAlertDialog();
                break;
        }
    }

    private void showLogoutAlertDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("操作")
                .setMessage("是否退出嗨聊?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        personalFragmentPresenter.logout();
                    }
                })
                .setNegativeButton("取消",null)
                .show();

    }
}
