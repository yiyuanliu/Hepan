package com.yiyuanliu.hepan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.UserInfoActivity;
import com.yiyuanliu.hepan.base.MoreLoadAdapter;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.utils.AvatarTrans;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/7/29.
 */
public class UserListAdapter extends MoreLoadAdapter {
    private List<UserBase> userBaseList = new ArrayList<>();

    public void addUserBase(UserBase userBase){
        userBaseList.add(userBase);
        notifyItemInserted(userBaseList.size() - 1);
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (this.isLoading == isLoading){
            return;
        }

        if (isLoading){
            notifyItemInserted(userBaseList.size());
        } else {
            notifyItemRemoved(userBaseList.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case -1:
                return LoadingViewHolder.newInstance(parent);
            case 0:
                return UserViewHolder.newInstance(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case -1:
                break;
            case 0:
                ((UserViewHolder)holder).bind(userBaseList.get(position));
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position >= userBaseList.size()){
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return userBaseList.size() + (isLoading ? 1 : 0);
    }

    public void clear() {
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.username) TextView username;
        @BindView(R.id.avatar) ImageView avatar;

        private UserBase userBase;

        public static UserViewHolder newInstance(ViewGroup parent){
            return new UserViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
        }

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(UserBase userBase) {
            username.setText(userBase.userName);
            Picasso.with(avatar.getContext())
                    .load(userBase.userAvatar)
                    .placeholder(R.drawable.place_avatar)
                    .transform(new AvatarTrans())
                    .into(avatar);
            this.userBase = userBase;
        }

        @Override
        public void onClick(View v) {
            UserInfoActivity.startActivity(v.getContext(), userBase);
        }
    }
}
