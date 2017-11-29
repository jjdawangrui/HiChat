package com.itheima.hichat.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.hichat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Apple on 2016/12/1.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_activity);
        ButterKnife.bind(this);
        View view = addContent(getLayoutInflater(),content);
        initContent(view);
    }

    //设置标题内容
    public void setTitle(String title){
        tvTitle.setText(title);
    }

    //返回根布局
    /**
     * 给外界调用，很神奇
     */
    public RelativeLayout getRlRoot(){
        return rlRoot;
    }

    //添加内容
    public abstract View addContent(LayoutInflater mInflater,FrameLayout content);

    public abstract void initContent(View view);

    @OnClick(R.id.ib_back)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_back:
                finish();
                break;
        }
    }
}
