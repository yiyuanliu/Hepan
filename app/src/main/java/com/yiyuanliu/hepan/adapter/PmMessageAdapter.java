package com.yiyuanliu.hepan.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.base.MoreLoadAdapter;
import com.yiyuanliu.hepan.data.model.PmMessage;
import com.yiyuanliu.hepan.span.EmojiTarget;
import com.yiyuanliu.hepan.span.LoadingSpan;
import com.yiyuanliu.hepan.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/8/19.
 */
public class PmMessageAdapter extends MoreLoadAdapter {

    private List<PmMessage> pmMessageList = new ArrayList<>();

    public void addPmMessages(List<PmMessage> pmMessages) {
        for (PmMessage pmMessage:pmMessages) {
            addPmMessage(pmMessage);
        }
    }

    public void addPmMessage(PmMessage pmMessage){
        pmMessageList.add(pmMessage);
        notifyItemInserted(pmMessageList.size() - 1);
    }

    public void clear(){
        pmMessageList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (isLoading == this.isLoading){
            return;
        } else if (isLoading){
            notifyItemInserted(pmMessageList.size());
        } else {
            notifyItemRemoved(pmMessageList.size());
        }
        this.isLoading = isLoading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return LoadingViewHolder.newInstance(parent);
            case 1:
                return MyMessageViewHolder.newInstance(parent);
            case 2:
                return OtherMessageViewHolder.newInstance(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case 0:
                break;
            case 1:
                MyMessageViewHolder myMessageViewHolder = (MyMessageViewHolder) holder;
                ((MyMessageViewHolder) holder).bind(pmMessageList.get(position));
                break;
            case 2:
                OtherMessageViewHolder otherMessageViewHolder = (OtherMessageViewHolder) holder;
                otherMessageViewHolder.bind(pmMessageList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == pmMessageList.size()) {
            return 0;
        } else {
            return pmMessageList.get(position).fromMe ? 1 : 2;
        }
    }

    @Override
    public int getItemCount() {
        return isLoading ? pmMessageList.size() + 1 : pmMessageList.size();
    }

    public void addNewPms(List<PmMessage> pmList)  {
        for (PmMessage pm:pmMessageList) {
            addPmMessage(pm);
        }
    }

    public void addNewPm(PmMessage pmMessage) {
        for (PmMessage pm:pmMessageList) {
            if (pm.time > pmMessage.time) {
                int p = pmMessageList.indexOf(pm);
                pmMessageList.remove(pm);
                notifyItemRemoved(p);
            }
        }

        pmMessageList.add(0, pmMessage);
        notifyItemInserted(0);
    }

    public static class MyMessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content) TextView content;
        @BindView(R.id.time) TextView time;

        public static MyMessageViewHolder newInstance(ViewGroup parent) {
            return new MyMessageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_right, parent, false));
        }

        public MyMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(PmMessage pmMessage){
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(pmMessage.content);
            Pattern pattern = Pattern.compile("\\[mobcent_phiz=([^\\[]*)\\]");
            Matcher matcher = pattern.matcher(pmMessage.content);

            List<PostListAdapter.TextViewHolder.SpanItem> spanList = new ArrayList<>();
            while (matcher.find()){
                final int start = matcher.start();
                final int end = matcher.end();
                final String url = matcher.group(1);
                LoadingSpan loadingSpan = new LoadingSpan(content.getContext());
                spannableStringBuilder.setSpan(loadingSpan,
                        start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanList.add(new PostListAdapter.TextViewHolder.SpanItem(url, loadingSpan));
            }
            content.setText(spannableStringBuilder);

            for (PostListAdapter.TextViewHolder.SpanItem spanItem:spanList) {
                Picasso.with(content.getContext())
                        .load(spanItem.key)
                        .into(new EmojiTarget(spanItem.value, content));
            }

            content.setLinksClickable(true);
            content.setMovementMethod(LinkMovementMethod.getInstance());
            time.setText(TimeUtil.getRelativeTime(pmMessage.time));
        }
    }

    public static class OtherMessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content) TextView content;
        @BindView(R.id.time) TextView time;

        public static OtherMessageViewHolder newInstance(ViewGroup parent) {
            return new OtherMessageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_left, parent, false));
        }

        public OtherMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(PmMessage pmMessage){
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(pmMessage.content);
            Pattern pattern = Pattern.compile("\\[mobcent_phiz=([^\\[]*)\\]");
            Matcher matcher = pattern.matcher(pmMessage.content);
            content.setText(spannableStringBuilder);

            List<PostListAdapter.TextViewHolder.SpanItem> spanList = new ArrayList<>();
            while (matcher.find()){
                final int start = matcher.start();
                final int end = matcher.end();
                final String url = matcher.group(1);
                LoadingSpan loadingSpan = new LoadingSpan(content.getContext());
                spannableStringBuilder.setSpan(loadingSpan,
                        start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanList.add(new PostListAdapter.TextViewHolder.SpanItem(url, loadingSpan));
            }
            content.setText(spannableStringBuilder);

            for (PostListAdapter.TextViewHolder.SpanItem spanItem:spanList) {
                Picasso.with(content.getContext())
                        .load(spanItem.key)
                        .into(new EmojiTarget(spanItem.value, content));
            }

            content.setLinksClickable(true);
            content.setMovementMethod(LinkMovementMethod.getInstance());
            time.setText(TimeUtil.getRelativeTime(pmMessage.time));
        }
    }
}
