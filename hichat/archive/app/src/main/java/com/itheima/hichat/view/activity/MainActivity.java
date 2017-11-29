package com.itheima.hichat.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hyphenate.chat.EMClient;
import com.itheima.hichat.R;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.modle.event.MessageEvent;
import com.itheima.hichat.utils.EMUtils;
import com.itheima.hichat.utils.FragmentFactory;
import com.itheima.hichat.view.fragment.maintab.ContactFragment;
import com.itheima.hichat.view.fragment.maintab.ConversationFragment;
import com.itheima.hichat.view.fragment.maintab.LiveFragment;
import com.itheima.hichat.view.fragment.maintab.NearByFragment;
import com.itheima.hichat.view.fragment.maintab.PersonalFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hyphenate.chat.a.a.a.f;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.contaier)
    FrameLayout contaier;
    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;
    private BadgeItem badgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initBottomNavigationBar();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(User user){
        //清空Fragment的缓存
        FragmentFactory.clear();

        //进入登录界面
        startActivity(new Intent(this,LoginActivity.class));
        //关闭MainActivity
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        if(event.type == MessageEvent.RECEIVE){
            dealUnReadMsgAlert();
        }
    }

    Class[] fragments = new Class[]{
            NearByFragment.class,
            LiveFragment.class,
            ConversationFragment.class,
            ContactFragment.class,
            PersonalFragment.class
    };

    /**
     * 应该是初始化第一个Fragment 首页
     */
    //初始化Fragment
    private void initFragment() {
        //把NearByFragment显示在FrameLayout里面
        FragmentManager fm = getSupportFragmentManager();//获取Fragment管理器
        FragmentTransaction ft = fm.beginTransaction();//开启事务
        /**
         * 固定写法，用事务来添加，把fragment添加到容器中
         */
        ft.add(R.id.contaier,FragmentFactory.getInstance(fragments[0]),"0");//把附近的Fragment添加到Container
        ft.commit();//提交
    }

    //底部导航条图片资源
    int[] barIcons = new int[]{
            R.drawable.ic_nav_nearby_active,
            R.drawable.ic_nav_live_active,
            R.drawable.ic_nav_conversation_active,
            R.drawable.ic_nav_contacts_active,
            R.drawable.ic_nav_personal_active
    };

    //初始化底部导航条
    private void initBottomNavigationBar() {
        //底部导航的标签
        String[] barLabels = getResources().getStringArray(R.array.bottombarlabel);

        //未读消息提醒
       /* BadgeItem badgeItem = new BadgeItem();
        badgeItem.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        badgeItem.setText("10");
        badgeItem.show();*/


        badgeItem = new BadgeItem()
                .setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);//设置位置（靠顶部|水平居中）

        bottomNavigationBar
                .setBarBackgroundColor(R.color.bottombarcolor)//设置底部导航条的颜色
                .addItem(new BottomNavigationItem(barIcons[0],barLabels[0]))//添加tab，先图片再文字
                .addItem(new BottomNavigationItem(barIcons[1],barLabels[1]))
                .addItem(new BottomNavigationItem(barIcons[2],barLabels[2]).setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(barIcons[3],barLabels[3]))
                .addItem(new BottomNavigationItem(barIcons[4],barLabels[4]))
                .setActiveColor(R.color.activecolor)//设置选中的颜色
                .setInActiveColor(R.color.inactivecolor)//设置未选中该的颜色
                .setFirstSelectedPosition(0)//让第一个item选中
                .setMode(BottomNavigationBar.MODE_FIXED)//设置为混合模式
                .initialise();//初始化

        //给Tab设置点击监听
        bottomNavigationBar.setTabSelectedListener(new MyTabSelectedListener());
    }

    private class MyTabSelectedListener implements BottomNavigationBar.OnTabSelectedListener{

        //tab选中调用（显示Fragment）
        @Override
        public void onTabSelected(int position) {
//            MyLogger.i("onTabSelected:"+position);
            //1 通过FragmentManager获取Fragment 2 如果不存在，就通过FragmentFactory创建 3 如果存在，显示
            FragmentManager fm = getSupportFragmentManager();
            /**
             * 通过position找到fragment，下面的就一样了
             */
            Fragment fragment = fm.findFragmentByTag(position + "");
            FragmentTransaction ft = fm.beginTransaction();
            if(fragment == null){
                //添加
                ft.add(R.id.contaier,FragmentFactory.getInstance(fragments[position]),position+"");
            }else{
                //显示
                ft.show(fragment);
            }
            ft.commit();
        }

        //之前选中的tab未被选中调用(隐藏Fragment)
        @Override
        public void onTabUnselected(int position) {
//            MyLogger.i("onTabUnselected:"+position);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag(position + "");
            FragmentTransaction ft = fm.beginTransaction();

            ft.hide(fragment);
            ft.commit();
        }

        @Override
        public void onTabReselected(int position) {
//            MyLogger.i("onTabReselected:"+position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //把当前的Activity移至到后台
            //是否为任务栈的根   false:只能够处理点击App图标进入的Activity(Splash)  true:针对所有的Activity
            moveTaskToBack(false);//让退出后，app只是在后台，并没有关掉
            return true;//自己处理后退键的行为，自己消费事件
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dealUnReadMsgAlert();
    }

    private void dealUnReadMsgAlert() {
        int unreadMsgsCount = 0;
        if(EMUtils.isLogin()){
            //查询未读消息的个数
            unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
            unreadMsgsCount = unreadMsgsCount > 99 ? 99:unreadMsgsCount;
            if(unreadMsgsCount == 0){
                badgeItem.hide();
            }else{
                badgeItem.setText(unreadMsgsCount+"");
                badgeItem.show();
            }
        }else{
            badgeItem.hide();
        }
    }
}
