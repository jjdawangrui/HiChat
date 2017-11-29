package com.itheima.hichat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.itheima.hichat.R;
import com.itheima.hichat.modle.bean.User;
import com.itheima.hichat.utils.ImageViewUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Apple on 2016/12/7.
 */

public class AddFriendRVAdapter extends RecyclerView.Adapter<AddFriendRVAdapter.ViewHolder> {

    private List<User> users;
    private Context context;
    private final LayoutInflater mInflater;

    public AddFriendRVAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        mInflater = LayoutInflater.from(context);
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_add_friend, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 这是什么？？？
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.btn_add)
        Button btnAdd;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setPosition(int position) {
            final User user = users.get(position);
            ImageViewUtils.showImage(context,user.getIcon().getUrl(),R.drawable.avatar3,ivAvatar);
            tvUsername.setText(user.getUsername());
            tvTime.setText(user.getCreatedAt());
            btnAdd.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(onSearchFriendItemAddFriendClick != null){
                        onSearchFriendItemAddFriendClick.onAddFriendClick(user);
                    }
                }
            });
        }
    }

    /**
     * 相当于自己写一个点击事件接口，接口里的方法，在AddFriendActivity里面实现
     * 然后又交给Impl具体实现
     */
    public interface OnSearchFriendItemAddFriendClick{
        public void onAddFriendClick(User user);
    }

    private OnSearchFriendItemAddFriendClick onSearchFriendItemAddFriendClick;

    public void setOnSearchFriendItemAddFriendClick(OnSearchFriendItemAddFriendClick onSearchFriendItemAddFriendClick) {
        this.onSearchFriendItemAddFriendClick = onSearchFriendItemAddFriendClick;
    }
}
