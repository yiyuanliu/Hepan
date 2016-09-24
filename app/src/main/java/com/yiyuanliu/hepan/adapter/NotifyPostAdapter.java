package com.yiyuanliu.hepan.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.PostListActivity;
import com.yiyuanliu.hepan.activity.UserInfoActivity;
import com.yiyuanliu.hepan.activity.UserListActivity;
import com.yiyuanliu.hepan.base.MoreLoadAdapter;
import com.yiyuanliu.hepan.data.model.NotifyPost;
import com.yiyuanliu.hepan.dialog.TopicAdminDialog;
import com.yiyuanliu.hepan.utils.AvatarTrans;
import com.yiyuanliu.hepan.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/8/18.
 */
public class NotifyPostAdapter extends MoreLoadAdapter{
    private boolean isLoading;
    private List<NotifyPost> notifyPostList = new ArrayList<>();

    public void addNotifyPosts(List<NotifyPost> notifyPosts) {
        for (NotifyPost pm:notifyPosts) {
            addNotifyPost(pm);
        }
    }

    public void addNotifyPost(NotifyPost notifyPost) {
        notifyPostList.add(notifyPost);
        notifyItemInserted(notifyPostList.size() - 1);
    }

    public void clear(){
        notifyPostList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (isLoading == this.isLoading){
            return;
        } else if (isLoading){
            notifyItemInserted(notifyPostList.size());
        } else {
            notifyItemRemoved(notifyPostList.size());
        }

        this.isLoading = isLoading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return LoadingViewHolder.newInstance(parent);
            case 0:
                return NotifyPostViewHolder.newInstance(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= notifyPostList.size()) {
            return;
        }

        NotifyPostViewHolder notifyPostViewHolder = (NotifyPostViewHolder) holder;
        notifyPostViewHolder.bind(notifyPostList.get(position));
    }

    @Override
    public int getItemCount() {
        return isLoading ? notifyPostList.size() + 1 : notifyPostList.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        return position == notifyPostList.size() ? 1 : 0;
    }

    public static class NotifyPostViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.username) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.quote) TextView quote;
        public static NotifyPostViewHolder newInstance(ViewGroup parent) {
            NotifyPostViewHolder notifyPostViewHolder = new NotifyPostViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notify_post, parent, false));
            return notifyPostViewHolder;
        }

        public NotifyPostViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final NotifyPost notifyPost) {
            userName.setText(notifyPost.user.userName);
            Picasso.with(itemView.getContext())
                    .load(notifyPost.user.userAvatar)
                    .placeholder(R.drawable.place_avatar)
                    .transform(new AvatarTrans())
                    .into(avatar);
            time.setText(TimeUtil.getRelativeTime(notifyPost.replyDate));

            title.setText(notifyPost.replyContent);
            quote.setText(notifyPost.topicContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setItems(new String[]{"回复评论", "查看 ta 的资料", "查看原帖"},
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            TopicAdminDialog.show((AppCompatActivity) itemView.getContext(),
                                                    notifyPost.topicId, notifyPost.replyRemindId);
                                            break;
                                        case 1:
                                            UserInfoActivity.startActivity(itemView.getContext(), notifyPost.user);
                                            break;
                                        case 2:
                                            PostListActivity.startActivity(itemView.getContext(), notifyPost.topicId);
                                            break;
                                    }
                                }
                            })
                            .show();
                }
            });
        }
    }
}
