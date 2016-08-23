package com.yiyuanliu.hepan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.PmMessageActivity;
import com.yiyuanliu.hepan.base.MoreLoadAdapter;
import com.yiyuanliu.hepan.data.model.Pm;
import com.yiyuanliu.hepan.utils.AvatarTrans;
import com.yiyuanliu.hepan.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PmAdapter extends MoreLoadAdapter {
    private boolean isLoading;
    private List<Pm> pmList = new ArrayList<>();

    public void addPms(List<Pm> pmList) {
        for (Pm pm:pmList) {
            addPm(pm);
        }
    }

    public void addPm(Pm pm) {
        pmList.add(pm);
        notifyItemInserted(pmList.size() - 1);
    }

    public void clear(){
        pmList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (isLoading == this.isLoading){
            return;
        } else if (isLoading){
            notifyItemInserted(pmList.size());
        } else {
            notifyItemRemoved(pmList.size());
        }

        this.isLoading = isLoading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return LoadingViewHolder.newInstance(parent);
            case 0:
                return PmViewHolder.newInstance(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= pmList.size()) {
            return;
        }

        PmViewHolder pmViewHolder = (PmViewHolder) holder;
        pmViewHolder.bind(pmList.get(position));
    }

    @Override
    public int getItemCount() {
        return isLoading ? pmList.size() + 1 : pmList.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        return position == pmList.size() ? 1 : 0;
    }

    public static class PmViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.username) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.title) TextView title;

        private Pm pm;

        public static PmViewHolder newInstance(ViewGroup parent) {
            PmViewHolder pmViewHolder = new PmViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_pm, parent, false));
            return pmViewHolder;
        }

        public PmViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pm == null) {
                        return;
                    }

                    PmMessageActivity.startActivity(itemView.getContext(), pm.toUserBase);
                }
            });
        }

        public void bind(Pm pm) {
            this.pm = pm;
            userName.setText(pm.toUserBase.userName);
            Picasso.with(itemView.getContext())
                    .load(pm.toUserBase.userAvatar)
                    .placeholder(R.drawable.place_avatar)
                    .transform(new AvatarTrans())
                    .into(avatar);
            time.setText(TimeUtil.getRelativeTime(pm.lastDate));
            title.setText(pm.lastSummary);
        }
    }
}
