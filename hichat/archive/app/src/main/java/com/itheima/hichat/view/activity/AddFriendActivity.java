package com.itheima.hichat.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.hichat.R;
import com.itheima.hichat.adapter.AddFriendRVAdapter;
import com.itheima.hichat.base.BaseActivity;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.presenter.activity.AddFriendActivityPrensenterImpl;
import com.itheima.hichat.presenter.activity.AddFriendActivityPresenter;
import com.itheima.hichat.utils.TextViewUtils;
import com.itheima.hichat.widget.RecycleViewDivider;
import com.itheima.hichat.wrap.SimpleTextWatcher;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFriendActivity extends BaseActivity implements View.OnClickListener,AddFriendActivityView,AddFriendRVAdapter.OnSearchFriendItemAddFriendClick {

    EditText etSearchUsername;
    ImageButton ibSearch;
    RecyclerView addfriendRv;
    ImageView ivNodata;
    private AddFriendActivityPresenter addFriendActivityPresenter;
    private AddFriendRVAdapter addFriendRVAdapter;


    //在团队开发中约束界面跳转
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,AddFriendActivity.class));
    }


    @Override
    public View addContent(LayoutInflater mInflater, FrameLayout content) {
        View view = mInflater.inflate(R.layout.activity_add_friend, content, true);
        return view;
    }

    @Override
    public void initContent(View view) {
        etSearchUsername = ButterKnife.findById(view,R.id.et_search_username);
        ibSearch = ButterKnife.findById(view,R.id.ib_search);
        addfriendRv = ButterKnife.findById(view,R.id.addfriend_rv);
        ivNodata = ButterKnife.findById(view,R.id.iv_nodata);
        ibSearch.setEnabled(false);

        /**
         * et只要写了东西，搜索按钮就可以点击
         */
        etSearchUsername.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if(!TextUtils.isEmpty(s.toString().trim())){
                    ibSearch.setEnabled(true);
                }else{
                    ibSearch.setEnabled(false);
                }
            }
        });

        /**
         * et写东西，同时搜索
         */
        etSearchUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == KeyEvent.KEYCODE_SEARCH && !TextUtils.isEmpty(etSearchUsername.getText().toString().trim())){
                    doSearch();
                }
                return false;
            }
        });

        ibSearch.setOnClickListener(this);
        //设置标题
        setTitle("搜索好友");
        addFriendActivityPresenter = new AddFriendActivityPrensenterImpl(this);

        addfriendRv.setLayoutManager(new LinearLayoutManager(this));
        addfriendRv.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1, Color.BLACK));
        addFriendRVAdapter = new AddFriendRVAdapter(this,null);
        addfriendRv.setAdapter(addFriendRVAdapter);
        addFriendRVAdapter.setOnSearchFriendItemAddFriendClick(this);
    }

    public void onClick(View view){
        super.onClick(view);
        switch (view.getId()){
            case R.id.ib_search:
                doSearch();
                break;
        }
    }

    //执行搜索
    private void doSearch() {
        //搜索
        addFriendActivityPresenter.searchFriend(TextViewUtils.getContent(etSearchUsername));
        //隐藏输入法
        hideSofeInputMethod();
    }

    //隐藏输入法(输入法是一个系统的服务)
    private void hideSofeInputMethod() {
        //获取输入法的服务
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //从窗体上隐藏输入法
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    @Override
    public void afterSearchFriend(boolean success, List<User> users) {
        if(success){
            //显示数据
            addFriendRVAdapter.setUsers(users);
            //隐藏没有数据的图标
            ivNodata.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAddFriendClick(User user) {
        addFriendActivityPresenter.addFriend(user);
    }
}
