package com.itheima.hichat.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.itheima.hichat.R;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.utils.ImageViewUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Apple on 2016/12/9.
 */

public class ChatAdapter extends RecyclerView.Adapter {

    private User user;
    private List<EMMessage> emMessageList;
    private Context mContext;

    public void setEmMessageList(List<EMMessage> emMessageList) {
        this.emMessageList = emMessageList;
        notifyDataSetChanged();
    }

    public ChatAdapter(User user,List<EMMessage> emMessageList, Context mContext) {
        this.user = user;
        this.emMessageList = emMessageList;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage.Direct direct = emMessageList.get(position).direct();
        if (direct == EMMessage.Direct.RECEIVE) {
            return 0;//接收
        }
        return 1;//发送
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {//接收
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_message_receive, parent, false);
            return new ReceiveViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_message_send, parent, false);
            return new SendViewHolder(view);//发送
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EMMessage.Direct direct = emMessageList.get(position).direct();
        if(direct == EMMessage.Direct.RECEIVE){
            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.setData(position);
        }else{
            SendViewHolder viewHolder = (SendViewHolder) holder;
            viewHolder.setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return emMessageList != null ? emMessageList.size() : 0;
    }

    class ReceiveViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_header)
        ImageView ivHeader;
        @BindView(R.id.tv_text_msg)
        TextView tvTextMsg;
        @BindView(R.id.iv_image_msg)
        ImageView ivImageMsg;
        @BindView(R.id.iv_voice_msg)
        ImageView ivVoiceMsg;
        @BindView(R.id.tv_video_msg)
        TextureView tvVideoMsg;
        @BindView(R.id.rl_msg_content)
        RelativeLayout rlMsgContent;

        ReceiveViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            //图像
            ImageViewUtils.showImage(mContext,user.getIcon().getUrl(),R.drawable.avatar3,ivHeader);
        }

        public void setData(int position) {
            //消息
            EMMessage emMessage = emMessageList.get(position);
            EMMessageBody body = emMessage.getBody();
            if(body instanceof EMTextMessageBody){
                EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
                tvTextMsg.setText(emTextMessageBody.getMessage());
            }
            setTime(tvTime,position);
        }
    }

    private void setTime(TextView tvTime, int position) {
        if(position == 0){
            tvTime.setVisibility(View.VISIBLE);
            //显示时间
            long msgTime = emMessageList.get(position).getMsgTime();
            String time = DateUtils.getTimestampString(new Date(msgTime));
            tvTime.setText(time);
        }else{
            long preMsgTime = emMessageList.get(position - 1).getMsgTime();
            long msgTime = emMessageList.get(position).getMsgTime();
            if(Math.abs(msgTime - preMsgTime) > 1000*30){
                //显示
                tvTime.setVisibility(View.VISIBLE);
                String time = DateUtils.getTimestampString(new Date(msgTime));
                tvTime.setText(time);
            }else{
                //隐藏
                tvTime.setVisibility(View.GONE);
            }
        }
    }

    class SendViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_header)
        ImageView ivHeader;
        @BindView(R.id.tv_text_msg)
        TextView tvTextMsg;
        @BindView(R.id.iv_image_msg)
        ImageView ivImageMsg;
        @BindView(R.id.iv_voice_msg)
        ImageView ivVoiceMsg;
        @BindView(R.id.tv_video_msg)
        TextureView tvVideoMsg;
        @BindView(R.id.rl_msg_content)
        RelativeLayout rlMsgContent;
        @BindView(R.id.iv_msg_state)
        ImageView ivMsgState;

        SendViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            //图像
            ImageViewUtils.showImage(mContext, DataCacheUtils.currentUser.getIcon().getUrl(),R.drawable.avatar3,ivHeader);
        }

        public void setData(int position) {
            //消息
            EMMessage emMessage = emMessageList.get(position);
            EMMessageBody body = emMessage.getBody();
            if(body instanceof EMTextMessageBody){
                EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
                tvTextMsg.setText(emTextMessageBody.getMessage());
            }

            setTime(tvTime,position);

            EMMessage.Status status = emMessage.status();
            if(status == EMMessage.Status.INPROGRESS){
                ivMsgState.setVisibility(View.VISIBLE);
                //正在发送
                ivMsgState.setBackgroundResource(R.drawable.msg_sending_bg);
                AnimationDrawable animationDrawable = (AnimationDrawable) ivMsgState.getBackground();
                animationDrawable.start();
            }else if(status == EMMessage.Status.SUCCESS){
                //成功
                ivMsgState.setVisibility(View.GONE);
            }else{
                //失败
                ivMsgState.setVisibility(View.VISIBLE);
                ivMsgState.setBackgroundResource(R.drawable.msg_error);
            }
        }
    }
}
