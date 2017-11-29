package com.itheima.hichat.view.fragment.maintab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.itheima.hichat.R;
import com.itheima.hichat.base.BaseFragment;

import butterknife.ButterKnife;

import static com.lljjcoder.citypickerview.utils.JLogUtils.D;

/**
 * Created by Apple on 2016/12/1.
 */

public class NearByFragment extends BaseFragment {
    @Override
    public void setDefaultTitle(TextView tv) {
        tv.setText("附近");
    }
    @Override
    public void setEmptyViewInfo(ImageView iv, TextView tv) {

    }

    @Override
    public View addLeftHeader(LayoutInflater mInflater, ViewGroup headerLeft) {
        View view = mInflater.inflate(R.layout.nearby_header,headerLeft,true);
        TabLayout nearby_tablayout = ButterKnife.findById(view,R.id.nearby_tablayout);
        //添加Tab
        addTabs(nearby_tablayout);
        return view;
    }

    private void addTabs(TabLayout nearby_tablayout) {
        String[] nearbytabs = getContext().getResources().getStringArray(R.array.nearbytabs);
        for (int i = 0; i < nearbytabs.length; i++) {
            //nearby_tablayout.addTab(nearby_tablayout.newTab().setText(nearbytabs[i]));
            TextView tv = new TextView(getContext());
            tv.setText(nearbytabs[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(20);
            if(i == 0){
                tv.setTextColor(Color.BLACK);
            }else{
                tv.setTextColor(Color.DKGRAY);
            }
            nearby_tablayout.addTab(nearby_tablayout.newTab().setCustomView(tv));
        }
        //设置选择监听
        nearby_tablayout.setOnTabSelectedListener(new MyOnTabSelectedListener());
    }

    private class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener{

        //Tab被选择
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            TextView tv = (TextView) tab.getCustomView();
            tv.setTextColor(Color.BLACK);
        }

        //选中的tab被切换
        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            TextView tv = (TextView) tab.getCustomView();
            tv.setTextColor(Color.DKGRAY);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }


    @Override
    public View addRightHeader(LayoutInflater mInflater, ViewGroup headerRight) {
        return null;
    }

    public void showLoginHeaderContent(){
        super.showLoginHeaderContent();
        setDefaultHeaderTitle("");
    }


    @Override
    public View addLoginBodyContent(LayoutInflater mInflater, ViewGroup content) {
        return null;
    }

    @Override
    public void initLoginBodyContent(View view) {

    }
}
