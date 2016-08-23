package com.yiyuanliu.hepan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.data.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/8/8.
 */
public class UserInfoAdapter extends RecyclerView.Adapter {
    private UserInfo userInfo;

    public UserInfoAdapter(UserInfo userInfo){
        this.userInfo = userInfo;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return HeaderViewHolder.newInstance(parent);
            default:
                return ItemViewHolder.newInstance(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            ((HeaderViewHolder) holder).bind(userInfo);
        } else {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            ((ItemViewHolder) holder).bind(userInfo.itemList.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return 1 + userInfo.itemList.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.username) TextView username;
        @BindView(R.id.sign) TextView sign;

        public static HeaderViewHolder newInstance(ViewGroup parent){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_header, parent, false));
        }

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(UserInfo userInfo){
            Picasso.with(avatar.getContext())
                    .load(userInfo.userBase.userAvatar)
                    .into(avatar);

            username.setText(userInfo.userBase.userName + "(" + userInfo.userBase.userGender + ")");
            sign.setText(userInfo.sign == null || userInfo.sign.equals("")
                    ? "这个人很懒，不用理他" : userInfo.sign);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1) TextView textView;

        public static ItemViewHolder newInstance(ViewGroup parent){
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(UserInfo.Item item) {
            textView.setText(item.key + ": " + item.value);
        }
    }
}
