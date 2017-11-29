package com.itheima.hichat.view.fragment.maintab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.hichat.base.BaseFragment;

/**
 * Created by Apple on 2016/12/1.
 */

public class LiveFragment extends BaseFragment {
    @Override
    public void setDefaultTitle(TextView tv) {
        tv.setText("直播");
    }
    @Override
    public void setEmptyViewInfo(ImageView iv, TextView tv) {

    }

    @Override
    public View addLeftHeader(LayoutInflater mInflater, ViewGroup headerLeft) {
        return null;
    }

    @Override
    public View addRightHeader(LayoutInflater mInflater, ViewGroup headerRight) {
        return null;
    }

    public void showLoginHeaderContent(){
        super.showLoginHeaderContent();
        setDefaultHeaderTitle("嗨聊直播");
    }

    @Override
    public View addLoginBodyContent(LayoutInflater mInflater, ViewGroup content) {
        return null;
    }

    @Override
    public void initLoginBodyContent(View view) {

    }
}
