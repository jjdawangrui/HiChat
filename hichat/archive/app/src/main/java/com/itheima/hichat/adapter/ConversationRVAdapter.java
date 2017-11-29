package com.itheima.hichat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
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
 * Created by Apple on 2016/12/10.
 */

public class ConversationRVAdapter extends RecyclerView.Adapter<ConversationRVAdapter.ViewHolder> {

    private List<EMConversation> emConversationList;
    private Context context;

    public void setEmConversationList(List<EMConversation> emConversationList) {
        this.emConversationList = emConversationList;
        notifyDataSetChanged();
    }

    public ConversationRVAdapter(List<EMConversation> emConversationList, Context context) {
        this.emConversationList = emConversationList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return emConversationList != null ? emConversationList.size() : 0;
    }

   class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_msg_count)
        TextView tvMsgCount;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

       public void setData(int position) {
           EMConversation emConversation = emConversationList.get(position);
           String userName = emConversation.getUserName();
           final User user = DataCacheUtils.find(userName);
           ImageViewUtils.showImage(context,user.getIcon().getUrl(),R.drawable.avatar3,ivAvatar);

           tvUsername.setText(userName);

           //时间和内容都应该显示的是该会话最新的一条消息
           EMMessage lastMessage = emConversation.getLastMessage();
           tvTime.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
           EMMessageBody body = lastMessage.getBody();
           if(body instanceof EMTextMessageBody){
               EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
               String message = emTextMessageBody.getMessage();
               tvContent.setText(message);
           }
           //未读消息
           int unreadMsgCount = emConversation.getUnreadMsgCount();
           unreadMsgCount = unreadMsgCount > 99 ? 99:unreadMsgCount;
           if(unreadMsgCount == 0){
               tvMsgCount.setVisibility(View.GONE);
           }else{
               tvMsgCount.setVisibility(View.VISIBLE);
               tvMsgCount.setText(unreadMsgCount+"");
           }

           itemView.setOnClickListener(new View.OnClickListener(){

               @Override
               public void onClick(View v) {
                    if(onConversationItemClickListener != null){
                        onConversationItemClickListener.onConversationItemClick(user);
                    }
               }
           });
       }
   }

    public interface  OnConversationItemClickListener {
        void onConversationItemClick(User user);
    }
    private OnConversationItemClickListener onConversationItemClickListener;

    public void setOnConversationItemClickListener(OnConversationItemClickListener onConversationItemClickListener) {
        this.onConversationItemClickListener = onConversationItemClickListener;
    }
}
