package com.itheima.hichat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.hichat.R;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.utils.DataCacheUtils;
import com.itheima.hichat.utils.ImageViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hyphenate.chat.a.a.a.i;

/**
 * Created by Apple on 2016/12/8.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return DataCacheUtils.users.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_username)
        TextView tvUsername;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

         public void setData(int position) {
             final User user = DataCacheUtils.users.get(position);
             ImageViewUtils.showImage(ivAvatar.getContext(),user.getIcon().getUrl(),R.drawable.avatar3,ivAvatar);
             tvUsername.setText(user.getUsername());
             //条目点击事件
             itemView.setOnClickListener(new View.OnClickListener(){

                 @Override
                 public void onClick(View v) {
                    if(onFriendItemClickListener != null){
                        onFriendItemClickListener.onItemClick(user);
                    }
                 }
             });
             //条目长按事件
             itemView.setOnLongClickListener(new View.OnLongClickListener() {
                 @Override
                 public boolean onLongClick(View v) {
                     if(onFriendItemClickListener != null){
                         onFriendItemClickListener.onItemLongClick(user);
                     }
                     return true;//处理长按事件
                 }
             });
         }
     }

    public interface OnFriendItemClickListener{
        void onItemClick(User user);
        void onItemLongClick(User user);
    }

    private OnFriendItemClickListener onFriendItemClickListener;

    public void setOnFriendItemClickListener(OnFriendItemClickListener onFriendItemClickListener) {
        this.onFriendItemClickListener = onFriendItemClickListener;
    }
}
