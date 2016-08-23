package com.yiyuanliu.hepan.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.FragmentActivity;
import com.yiyuanliu.hepan.activity.PostListActivity;
import com.yiyuanliu.hepan.base.MoreLoadAdapter;
import com.yiyuanliu.hepan.data.model.Topic;
import com.yiyuanliu.hepan.utils.AvatarTrans;
import com.yiyuanliu.hepan.utils.LayoutUtil;
import com.yiyuanliu.hepan.utils.TimeUtil;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/7/22.
 */
public class MainAdapter extends MoreLoadAdapter {
    private List<Topic> topicList = new ArrayList<>();

    public void addTopics(List<Topic> topicList){
        for (Topic topic:topicList){
            addTopic(topic);
        }
    }

    public void addTopic(Topic topic){
        for (Topic topic1:topicList){
            if (topic1.equals(topic)){
                return;
            }
        }

        topicList.add(topic);
        notifyItemInserted(topicList.size() - 1);
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (isLoading == this.isLoading){
            return;
        } else if (isLoading){
            notifyItemInserted(topicList.size());
        } else {
            notifyItemRemoved(topicList.size());
        }

        this.isLoading = isLoading;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= topicList.size()){
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case -1:
                return LoadingViewHolder.newInstance(parent);
            case 0:
                return new TopicViewHolder(LayoutUtil.inflate(parent, R.layout.item_main_topic));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 0:
                ((TopicViewHolder)holder).bindTopic(topicList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return isLoading ? topicList.size() + 1 : topicList.size();
    }

    public void clear() {
        topicList.clear();
        notifyDataSetChanged();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.username) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.board_name) TextView boardName;

        View itemView;
        private Topic topic;

        public View.OnClickListener onClickListener  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topic == null){
                    return;
                }

                PostListActivity.startActivity(itemView.getContext(), topic.topicId);
            }
        };

        public TopicViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(onClickListener);
        }

        public void bindTopic(final Topic topic){
            Picasso.with(avatar.getContext())
                    .load(topic.userBase.userAvatar)
                    .placeholder(R.drawable.place_avatar)
                    .transform(new AvatarTrans())
                    .into(avatar);

            userName.setText(topic.userBase.userName);
            time.setText(TimeUtil.getRelativeTime(topic.time));
            title.setText(topic.title);

            boardName.setText("·" + topic.board);

            this.topic = topic;
        }
    }
}
