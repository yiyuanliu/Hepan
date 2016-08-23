package com.yiyuanliu.hepan.adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.base.MoreLoadAdapter;
import com.yiyuanliu.hepan.data.bean.NotifyListSys;
import com.yiyuanliu.hepan.data.model.NotifySys;
import com.yiyuanliu.hepan.data.model.Pm;
import com.yiyuanliu.hepan.utils.AvatarTrans;
import com.yiyuanliu.hepan.utils.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/8/18.
 */
public class NotifySysAdapter extends MoreLoadAdapter {
    private boolean isLoading;
    private List<NotifySys> notifySysList = new ArrayList<>();

    public void addNotifySys(List<NotifySys> notifySyses) {
        for (NotifySys pm:notifySyses) {
            addNotifySys(pm);
        }
    }

    public void addNotifySys(NotifySys pm) {
        notifySysList.add(pm);
        notifyItemInserted(notifySysList.size() - 1);
    }

    public void clear(){
        notifySysList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (isLoading == this.isLoading){
            return;
        } else if (isLoading){
            notifyItemInserted(notifySysList.size());
        } else {
            notifyItemRemoved(notifySysList.size());
        }

        this.isLoading = isLoading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return LoadingViewHolder.newInstance(parent);
            case 0:
                return NotifySysViewHolder.newInstance(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= notifySysList.size()) {
            return;
        }

        NotifySysViewHolder notifySysViewHolder = (NotifySysViewHolder) holder;
        notifySysViewHolder.bind(notifySysList.get(position));
    }

    @Override
    public int getItemCount() {
        return isLoading ? notifySysList.size() + 1 : notifySysList.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        return position == notifySysList.size() ? 1 : 0;
    }

    public static class NotifySysViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.username)
        TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.action1) TextView textView;

        private NotifySys notifySys;

        public static NotifySysViewHolder newInstance(ViewGroup parent) {
            NotifySysViewHolder notifySysViewHolder = new NotifySysViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notify_sys, parent, false));
            return notifySysViewHolder;
        }

        public NotifySysViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bind(NotifySys notifySys) {
            userName.setText(notifySys.userBase.userName);
            Picasso.with(itemView.getContext())
                    .load(notifySys.userBase.userAvatar)
                    .placeholder(R.drawable.place_avatar)
                    .transform(new AvatarTrans())
                    .into(avatar);
            time.setText(TimeUtil.getRelativeTime(notifySys.date));
            title.setText(notifySys.note);

            this.notifySys = notifySys;
            if (notifySys.actionList == null || notifySys.actionList.size() == 0) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(notifySys.actionList.get(0).title);
            }
        }

        @Override
        public void onClick(View v) {
            if (notifySys == null || notifySys.actionList == null || notifySys.actionList.size() == 0) {
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            ListAdapter adapter = new ArrayAdapter<NotifyListSys.BodyBean.DataBean.Action>
                    (itemView.getContext(), android.R.layout.simple_list_item_1, notifySys.actionList);
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(notifySys.actionList.get(which).redirect));
                    itemView.getContext().startActivity(intent);
                }
            }).create().show();
        }
    }
}
