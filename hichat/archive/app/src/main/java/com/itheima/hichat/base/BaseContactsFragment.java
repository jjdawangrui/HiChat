package com.itheima.hichat.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.hichat.R;
import com.itheima.hichat.widget.RecycleViewDivider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Apple on 2016/12/7.
 */

public class BaseContactsFragment extends Fragment {

    @BindView(R.id.rv)
    RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_contacts_fragment, container, false);
        ButterKnife.bind(this, view);
        //初始化处理，设置布局管理器
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.HORIZONTAL,1, Color.BLACK));
        return view;
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        rv.setAdapter(adapter);
    }
}
