package com.itheima.hichat;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.modle.event.FriendEvent;
import com.itheima.hichat.modle.event.MessageEvent;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.utils.MyLogger;
import com.itheima.hichat.view.activity.ChatActivity;
import com.itheima.hichat.view.activity.MainActivity;
import com.itheima.hichat.wrap.SimpleEMCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.Bmob;

import static com.hyphenate.chat.a.a.a.e;
import static com.lljjcoder.citypickerview.utils.JLogUtils.A;

/**
 * Created by Apple on 2016/12/2.
 */

public class MyApplication extends Application {

    private int duan;
    private int yulu;
    private SoundPool soundPool;
    private NotificationManager nm;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    //初始化
    private void init() {
        initSoundPool();
        initBmob();//初始化Bmob
        initEM();//初始化环信的SDK
        setListener();
    }

    private void initSoundPool() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        //加载资源
        duan = soundPool.load(getApplicationContext(), R.raw.duan, 0);
        yulu = soundPool.load(getApplicationContext(), R.raw.yulu, 0);
    }

    //初始化Bmob
    private void initBmob() {
        Bmob.initialize(this, "a6b540254d3ca9aebf9d2a32b48660a7");
    }

    //初始化环信的SDK
    private void initEM() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        //false :接收到好友请求需要手动的同意或者拒绝
        //true:接收到好友请求,直接添加
        options.setAcceptInvitationAlways(true);
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    //设置监听
    private void setListener(){
        setConnectionListener();
        setMessageListener();
        setContactListener();
    }

    //设置连接监听
    private void setConnectionListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int error) {
                switch (error){
                    case EMError.USER_LOGIN_ANOTHER_DEVICE://在其他的设备上登录
                        //处理方式和点击退出按钮是一个逻辑
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
                        break;
                }
            }
        });
    }

    private void doLogoutOperator(){
        //清空数据
        DataCacheUtils.clear();

        //发出通知给MainActivity
        EventBus.getDefault().post(new User());

    }

    //设置消息的监听
    private void setMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            //接收到消息
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                EMMessage emMessage = list.get(0);
                String from = emMessage.getFrom();
                EMMessageBody body = emMessage.getBody();
                if(body instanceof EMTextMessageBody){
                    EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
                    String message = emTextMessageBody.getMessage();
                    MyLogger.i(from+":"+message);
                }

                if(isRunningForcegound()){
                    //播放短声音
                    soundPool.play(duan,1.0f,1.0f,0,0,1.0f);
                }else{
                    //播放长声音
                    soundPool.play(yulu,1.0f,1.0f,0,0,1.0f);
                    //发送通知
                    sendNotification(emMessage);
                }

                //通知ConversationFragment  ChatActivity
                EventBus.getDefault().post(new MessageEvent(emMessage,MessageEvent.RECEIVE));

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
    }

    //发送通知
    private void sendNotification(EMMessage emMessage) {
        if(nm == null){
            nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        //点击通知后的事件(先打开MainActivity 再打开ChatActivity)
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
        chatIntent.putExtra("user",DataCacheUtils.find(emMessage.getFrom()));
        Intent[] intents = {mainIntent,chatIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 100, intents, PendingIntent.FLAG_UPDATE_CURRENT);

        //构建通知
        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.app_icon)//设置小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.app_icon))//设置通知右边的大图标
                .setContentTitle(emMessage.getUserName())//设置标题
                .setContentText(((EMTextMessageBody)emMessage.getBody()).getMessage())//设置内容
                .setAutoCancel(true)//设置点击通知通知自动消失
                .setContentIntent(pendingIntent)//设置点击通知后的事件
                .build();
        //发送通知
        nm.notify(1,notification);
    }

    //判断应用程序是否在前台运行
    private boolean isRunningForcegound(){
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(10);//获取正在运行的任务栈
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);//获取最顶端的任务栈
        ComponentName topActivity = runningTaskInfo.topActivity;//获取任务站顶端的Activity
        String packageName = topActivity.getPackageName();//获取包名
        return packageName.equals(getPackageName());
    }


    //设置联系人监听
    private void setContactListener(){
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            //增加了联系人时回调此方法
            @Override
            public void onContactAdded(String s) {
                //通知FriendFragment
                EventBus.getDefault().post(new FriendEvent(s,FriendEvent.ADD));
            }

            //删除好友回调
            @Override
            public void onContactDeleted(String s) {
                MyLogger.i("已经删除了好友:"+s);
                //查找
                User user = DataCacheUtils.find(s);
                //清除缓存数据
                DataCacheUtils.remove(user);
                //通知FriendFragment
                EventBus.getDefault().post(new FriendEvent(s,FriendEvent.DELETE));
            }

            //接受到好友请求
            @Override
            public void onContactInvited(String s, String s1) {
                MyLogger.i("用户:"+s+",请求信息："+s1);
                //同意
//                EMClient.getInstance().contactManager().asyncAcceptInvitation(s,new SimpleEMCallBack());
                //拒绝
//                EMClient.getInstance().contactManager().asyncDeclineInvitation(s,new SimpleEMCallBack());
            }

            @Override
            public void onContactAgreed(String s) {

            }

            @Override
            public void onContactRefused(String s) {

            }
        });
    }
}
