package com.yiyuanliu.hepan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.BigImageActivity;
import com.yiyuanliu.hepan.activity.UserInfoActivity;
import com.yiyuanliu.hepan.base.MoreLoadAdapter;
import com.yiyuanliu.hepan.data.bean.Content;
import com.yiyuanliu.hepan.data.bean.PostList;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.span.EmojiTarget;
import com.yiyuanliu.hepan.span.LinkSpan;
import com.yiyuanliu.hepan.span.LoadingSpan;
import com.yiyuanliu.hepan.utils.AvatarTrans;
import com.yiyuanliu.hepan.utils.DeviceUtil;
import com.yiyuanliu.hepan.utils.IntentUtil;
import com.yiyuanliu.hepan.utils.PreferenceHelper;
import com.yiyuanliu.hepan.utils.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Url;

/**
 * Created by yiyuan on 2016/7/23.
 */
public class PostListAdapter extends MoreLoadAdapter {
    private List<BaseItem> dataList = new ArrayList<>();
    private List<PostList.TopicReply> topicReplyList = new ArrayList<>();
    private VoteViewHolder voteViewHolder;

    public PostListAdapter(Listener listener) {
        this.listener = listener;
    }

    private Listener listener;

    public void clear(){
        dataList.clear();
        topicReplyList.clear();
        notifyDataSetChanged();
    }

    public void setVote(boolean isSuccessful, List<PostList.TopicContent.PollInfo.PollItem> pollItemList) {
        voteViewHolder.isLoading = false;
        if (isSuccessful) {
            voteViewHolder.voteItem.pollInfo.poll_status = 1;
        }
        voteViewHolder.voteItem.pollInfo.poll_item_list = pollItemList;
        voteViewHolder.bind(voteViewHolder.voteItem);
    }

    public void addTopic(PostList.TopicContent topicContent){
        int from = dataList.size();
        dataList.addAll(BaseItem.gen(topicContent));
        int to = dataList.size();

        notifyItemRangeInserted(from, from - to);
    }

    public void addTopicReply(PostList.TopicReply topicReply){

        for (PostList.TopicReply topicReply1:topicReplyList) {
            if (topicReply1.reply_posts_id == topicReply.reply_posts_id){
                return;
            }
        }

        topicReplyList.add(topicReply);
        int from = dataList.size();
        dataList.addAll(BaseItem.gen(topicReply));
        int to = dataList.size();

        notifyItemRangeInserted(from, from - to);
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (this.isLoading == isLoading){
            return;
        }

        this.isLoading = isLoading;
        if (isLoading){
            notifyItemInserted(dataList.size());
        } else {
            notifyItemRemoved(dataList.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < dataList.size()){
            return dataList.get(position).getType();
        } else {
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case -1:
                return LoadingViewHolder.newInstance(parent);
            case BaseItem.TYPE_TEXT:
                return TextViewHolder.newInstance(parent);
            case BaseItem.TYPE_IMAGE:
                return ImageViewHolder.newInstance(parent);
            case BaseItem.TYPE_AUDIO:
                return AudioViewHolder.newInstance(parent);
            case BaseItem.TYPE_VIDEO:
                return VideoViewHolder.newInstance(parent);
            case BaseItem.TYPE_VOTE:
                voteViewHolder = VoteViewHolder.newInstance(parent, listener);
                return voteViewHolder;
            case BaseItem.TYPE_ATTACHMENT:
                return AttachmentViewHolder.newInstance(parent);
            case BaseItem.TYPE_USER:
                return UserViewHolder.newInstance(parent);
            case BaseItem.TYPE_END:
                return EndViewHolder.newInstance(parent, listener);
            case BaseItem.TYPE_TOPIC_END:
                return TopicEndHolder.newInstance(parent, listener);
            case BaseItem.TYPE_QUOTE:
                return QuoteViewHolder.newInstance(parent);
            case BaseItem.TYPE_TITLE:
                return TitleViewHolder.newInstance(parent);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);

        if (type == -1){
            return;
        }

        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        baseViewHolder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return isLoading ? dataList.size() + 1 : dataList.size();
    }

    public static abstract class BaseItem{
        public static final int TYPE_TEXT = 0;
        public static final int TYPE_IMAGE = 1;
        public static final int TYPE_AUDIO = 2;
        public static final int TYPE_VIDEO = 3;
        public static final int TYPE_VOTE = 4;
        public static final int TYPE_ATTACHMENT = 5;

        public static final int TYPE_USER = 6;
        public static final int TYPE_END = 7;
        public static final int TYPE_TOPIC_END = 8;
        public static final int TYPE_QUOTE = 9;
        public static final int TYPE_TITLE = 10;

        public abstract int getType();

        public static List<BaseItem> gen(PostList postList){
            List<BaseItem> baseItemList = new ArrayList<>();

            baseItemList.addAll(gen(postList.topic));

            for (PostList.TopicReply topicReply:postList.list){
                baseItemList.addAll(gen(topicReply));
            }

            return baseItemList;
        }

        public static List<BaseItem> gen(PostList.TopicContent topicContent){
            List<BaseItem> baseItemList = new ArrayList<>();

            TitleItem titleItem = new TitleItem();
            titleItem.title = topicContent.title;
            baseItemList.add(titleItem);

            UserItem userItem = new UserItem();
            userItem.userBase =
                    new UserBase(topicContent.user_nick_name, topicContent.user_id, topicContent.icon);
            userItem.replyTime = topicContent.create_date;
            baseItemList.add(userItem);

            baseItemList.addAll(gen(topicContent.content));

            if (topicContent.poll_info != null){
                VoteItem voteItem = new VoteItem();
                voteItem.pollInfo = topicContent.poll_info;
                baseItemList.add(voteItem);
            }

            TopicEndItem endItem = new TopicEndItem();
            endItem.topicId = topicContent.topic_id;
            endItem.readNum = topicContent.hits;
            endItem.replyNum = topicContent.replies;

            for (PostList.TopicContent.ExtraPanelBean extraPanel:topicContent.extraPanel) {
                if (extraPanel != null && extraPanel.type.equals("rate")) {
                    endItem.rateUrl = extraPanel.action;
                }
            }

            for (PostList.TopicContent.ManagePanelBean managePanel: topicContent.managePanel) {
                if (managePanel != null && managePanel.type.equals("edit")) {
                    endItem.editUrl = managePanel.action;
                }

                if (managePanel != null && managePanel.type.equals("delthread")) {
                    endItem.deleteUrl = managePanel.action;
                }
            }

            baseItemList.add(endItem);

            return baseItemList;
        }

        public static List<BaseItem> gen(PostList.TopicReply topicReply){
            List<BaseItem> baseItemList = new ArrayList<>();

            UserItem userItem = new UserItem();
            userItem.userBase =
                    new UserBase(topicReply.reply_name, topicReply.reply_id, topicReply.icon);
            userItem
                    .replyTime = topicReply.posts_date;
            baseItemList.add(userItem);

            if (topicReply.is_quote == 1){
                QuoteItem quoteItem = new QuoteItem();
                quoteItem.content = topicReply.quote_content;
                baseItemList.add(quoteItem);
            }

            baseItemList.addAll(gen(topicReply.reply_content));

            EndItem endItem = new EndItem();
            endItem.postId = topicReply.reply_posts_id;
            endItem.userName = topicReply.reply_name;

            baseItemList.add(endItem);

            return baseItemList;
        }

        public static List<BaseItem> gen(List<Content> contentList){
            List<BaseItem> baseItemList = new ArrayList<>();

            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            for (Content content:contentList){
                switch (content.type){
                    case Content.TYPE_NORMAL:
                        if (stringBuilder == null){
                            stringBuilder = new SpannableStringBuilder();
                        }

                        stringBuilder.append(content.infor);
                        break;
                    case Content.TYPE_ATTACHMENT:
                        if (stringBuilder != null){
                            TextItem textItem = new TextItem();
                            textItem.string = stringBuilder;
                            stringBuilder = null;
                            baseItemList.add(textItem);
                        }

                        AttachmentItem attachmentItem = new AttachmentItem();
                        attachmentItem.url = content.url;
                        attachmentItem.dec = content.desc;
                        attachmentItem.name = content.infor;
                        baseItemList.add(attachmentItem);

                        break;
                    case Content.TYPE_LINK:
                        if (stringBuilder == null){
                            stringBuilder = new SpannableStringBuilder();
                        } else {
                            stringBuilder.append(" ");
                        }

                        LinkSpan linkSpan = new LinkSpan(content.infor, content.url);
                        stringBuilder.append(content.infor + " ");
                        stringBuilder.setSpan(linkSpan, stringBuilder.length() - content.infor.length() - 1,
                                stringBuilder.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    case Content.TYPE_MUSIC:
                        if (stringBuilder != null){
                            TextItem textItem = new TextItem();
                            textItem.string = stringBuilder;
                            stringBuilder = null;
                            baseItemList.add(textItem);
                        }

                        AudioItem audioItem = new AudioItem();
                        audioItem.audio = content.infor;
                        baseItemList.add(audioItem);
                        break;
                    case Content.TYPE_PICTURE:
                        if (stringBuilder != null){
                            TextItem textItem = new TextItem();
                            textItem.string = stringBuilder;
                            stringBuilder = null;
                            baseItemList.add(textItem);
                        }

                        ImageItem imageItem = new ImageItem();
                        imageItem.little = content.infor.replace("xgsize", "mobcentSmallPreview");
                        imageItem.middle = content.infor.replace("xgsize", "mobcentBigPreview");
                        imageItem.big = content.originalInfo;
                        baseItemList.add(imageItem);
                        break;
                    case Content.TYPE_VIDEO:
                        if (stringBuilder != null){
                            TextItem textItem = new TextItem();
                            textItem.string = stringBuilder;
                            stringBuilder = null;
                            baseItemList.add(textItem);
                        }

                        VideoItem videoItem = new VideoItem();
                        videoItem.video = content.infor;
                        baseItemList.add(videoItem);
                        break;
                }
            }

            if (stringBuilder != null){
                TextItem textItem = new TextItem();
                textItem.string = stringBuilder;
                baseItemList.add(textItem);
            }

            return baseItemList;
        }

    }

    public static class TextItem extends BaseItem{
        public SpannableStringBuilder string;

        @Override
        public int getType() {
            return 0;
        }

    }

    public static class ImageItem extends BaseItem{
        public String little;
        public String big;
        public String middle;

        @Override
        public int getType() {
            return 1;
        }

        @Override
        public String toString() {
            return "little imageView " + little + "\n" + "big imageView " + big;
        }
    }

    public static class AudioItem extends BaseItem{
        public String audio;

        @Override
        public int getType() {
            return 2;
        }

        @Override
        public String toString() {
            return audio;
        }
    }

    public static class VideoItem extends BaseItem{
        public String video;

        @Override
        public int getType() {
            return 3;
        }

        @Override
        public String toString() {
            return video;
        }
    }

    public static class VoteItem extends BaseItem{
        public PostList.TopicContent.PollInfo pollInfo;

        @Override
        public int getType() {
            return 4;
        }

    }

    public static class AttachmentItem extends BaseItem{
        public String url;
        public String dec;
        public String name;

        @Override
        public int getType() {
            return 5;
        }

        @Override
        public String toString() {
            return "url " + url + "\n" + "dec " + dec;
        }
    }

    public static class UserItem extends BaseItem{
        public UserBase userBase;
        public long replyTime;

        @Override
        public int getType() {
            return 6;
        }

        @Override
        public String toString() {
            return userBase.userName;
        }
    }

    public static class EndItem extends BaseItem{
        public int postId;
        public String userName;
        public Map<String, Url> map = new HashMap<>();

        @Override
        public int getType() {
            return 7;
        }

        @Override
        public String toString() {
            return "postId" + postId;
        }
    }

    public static class TopicEndItem extends BaseItem {

        public int topicId;
        public int readNum;
        public int replyNum;

        public String rateUrl;
        public String editUrl;
        public String deleteUrl;

        @Override
        public int getType() {
            return 8;
        }
    }

    public static class QuoteItem extends BaseItem{
        public String content;
        public int id;

        @Override
        public int getType() {
            return 9;
        }
    }

    public static class TitleItem extends BaseItem {

        public String title;

        @Override
        public int getType() {
            return 10;
        }
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder{

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(BaseItem baseItem);
    }

    public static class TextViewHolder extends BaseViewHolder{
        private static final String TAG = "TextViewHolder";
        @BindView(R.id.text) TextView textView;

        public static TextViewHolder newInstance(ViewGroup viewGroup){
            return new TextViewHolder(LayoutInflater
                    .from(viewGroup.getContext()).inflate(R.layout.item_post_text, viewGroup, false));
        }

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(BaseItem baseItem){
            TextItem textItem = (TextItem) baseItem;

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textItem.string);
            Pattern pattern = Pattern.compile("\\[mobcent_phiz=([^\\[]*)\\]");
            Matcher matcher = pattern.matcher(textItem.string);

            List<SpanItem> spanList = new ArrayList<>();
            while (matcher.find()){
                final int start = matcher.start();
                final int end = matcher.end();
                final String url = matcher.group(1);
                LoadingSpan loadingSpan = new LoadingSpan(textView.getContext());
                spannableStringBuilder.setSpan(loadingSpan,
                        start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanList.add(new SpanItem(url, loadingSpan));
            }
            textView.setText(spannableStringBuilder);

            for (SpanItem spanItem:spanList) {
                Log.d(TAG, spanItem.key);
                Picasso.with(textView.getContext())
                        .load(spanItem.key)
                        .into(new EmojiTarget(spanItem.value, textView));
            }

            textView.setLinksClickable(true);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        public static class SpanItem {
            public String key;
            public LoadingSpan value;

            public SpanItem(String url, LoadingSpan loadingSpan) {
                key = url;
                value = loadingSpan;
            }
        }
    }

    public static class ImageViewHolder extends BaseViewHolder implements Target, View.OnClickListener {
        public static final String TAG = "ImageViewHolder";

        @BindView(R.id.image) ImageView imageView;

        private ImageItem imageItem;

        public static ImageViewHolder newInstance(ViewGroup viewGroup){
            return new ImageViewHolder(LayoutInflater
                    .from(viewGroup.getContext()).inflate(R.layout.item_post_image, viewGroup, false));
        }

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(BaseItem baseItem) {
            imageItem = (ImageItem) baseItem;

            Picasso.with(imageView.getContext())
                    .load(PreferenceHelper.getInstance(imageView.getContext()).getImageUrl(imageItem))
                    .placeholder(R.drawable.palce_image)
                    .resize(DeviceUtil.getScreenWidth(imageView.getContext()),
                            DeviceUtil.getScreenHeight(imageView.getContext()))
                    .centerInside()
                    .onlyScaleDown()
                    .error(R.drawable.palce_image)
                    .into(this);

            imageView.setOnClickListener(this);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            int finalWidth = DeviceUtil.dp2px(imageView.getContext(), width);
            finalWidth = Math.min(finalWidth, DeviceUtil.getMaxWidth(imageView.getContext(), 32));

            float scale = finalWidth / (float) width;
            int finalHeight = (int) (height * scale);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(finalWidth, finalHeight);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(bitmap);

            Log.d(TAG, width + " " + height + " " + finalWidth + " " + finalHeight + " " + scale);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d(TAG, "onBitmapFailed");
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d(TAG, "onPreparedLoad");
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(placeHolderDrawable);
        }

        @Override
        public void onClick(View v) {
            BigImageActivity.startActivity(v.getContext(), imageItem.big);
        }
    }

    public static class AudioViewHolder extends BaseViewHolder {
        @BindView(R.id.describe) TextView textView;
        @BindView(R.id.state) ImageView imageView;

        public static AudioViewHolder newInstance(ViewGroup viewGroup){
            return new AudioViewHolder(LayoutInflater
                    .from(viewGroup.getContext()).inflate(R.layout.item_post_attachment, viewGroup, false));
        }

        public AudioViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(BaseItem baseItem) {
            final AudioItem audioItem = (AudioItem) baseItem;

            textView.setText("音频");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLink(itemView.getContext(), audioItem.audio);
                }
            });
        }
    }

    public static class VideoViewHolder extends BaseViewHolder{
        @BindView(R.id.describe) TextView textView;
        @BindView(R.id.state) ImageView imageView;

        public static VideoViewHolder newInstance(ViewGroup viewGroup){
            return new VideoViewHolder(LayoutInflater
                    .from(viewGroup.getContext()).inflate(R.layout.item_post_attachment, viewGroup, false));
        }

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(BaseItem baseItem) {
            final VideoItem videoItem = (VideoItem) baseItem;

            textView.setText("视频");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLink(itemView.getContext(), videoItem.video);
                }
            });
        }
    }

    public static class VoteViewHolder extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.container) LinearLayout linearLayout;
        @BindView(R.id.loading) ProgressBar loading;

        boolean isLoading;

        private int selNum;
        private List<CheckBox> checkBoxList = new ArrayList<>();
        private VoteItem voteItem;
        private Listener listener;

        public static VoteViewHolder newInstance(ViewGroup viewGroup, Listener listener){
            VoteViewHolder voteViewHolder = new VoteViewHolder(LayoutInflater
                    .from(viewGroup.getContext()).inflate(R.layout.item_post_vote, viewGroup, false));
            voteViewHolder.listener = listener;
            return voteViewHolder;
        }

        public VoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(BaseItem baseItem) {
            voteItem = (VoteItem) baseItem;

            linearLayout.removeAllViews();
            checkBoxList.clear();

            if (isLoading){
                bindLoading(voteItem);
                return;
            } else {
                loading.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }

            TextView textView = new TextView(itemView.getContext());
            textView.setText("最多可投" + voteItem.pollInfo.type + "项");
            linearLayout.addView(textView);

            for (PostList.TopicContent.PollInfo.PollItem pollItem:voteItem.pollInfo.poll_item_list) {
                CheckBox checkBox = new CheckBox(linearLayout.getContext());
                checkBoxList.add(checkBox);
                linearLayout.addView(checkBox);
            }

            if (voteItem.pollInfo.poll_status == 1 || (voteItem.pollInfo.deadline != 0
                    && voteItem.pollInfo.deadline * 1000 < System.currentTimeMillis())) {
                String str = voteItem.pollInfo.poll_status == 1 ? "已投票" : "投票已经结束";
                textView.setText(str);
                for (int i = 0;i < checkBoxList.size(); i ++) {
                    CheckBox checkBox = checkBoxList.get(i);
                    PostList.TopicContent.PollInfo.PollItem pollItem = voteItem.pollInfo.poll_item_list.get(i);
                    checkBox.setClickable(false);
                    checkBox.setText(pollItem.name + "(" + Math.max(pollItem.total_num, pollItem.totalNum) + "人选择)");
                }
            } else {
                for (int i = 0;i < checkBoxList.size(); i ++) {
                    CheckBox checkBox = checkBoxList.get(i);
                    PostList.TopicContent.PollInfo.PollItem pollItem = voteItem.pollInfo.poll_item_list.get(i);
                    checkBox.setEnabled(true);
                    checkBox.setText(pollItem.name);
                }

                Button button = new Button(linearLayout.getContext());
                button.setText("投票");
                linearLayout.addView(button);
                button.setOnClickListener(this);
            }

        }

        private void bindLoading(VoteItem voteItem){
            loading.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            List<Integer> integerList = new ArrayList<>();

            for (int i = 0;i < checkBoxList.size();i ++) {
                CheckBox checkBox = checkBoxList.get(i);
                PostList.TopicContent.PollInfo.PollItem pollItem = voteItem.pollInfo.poll_item_list.get(i);

                if (checkBox.isChecked()) {
                    integerList.add(pollItem.poll_item_id);
                }
            }

            if (integerList.size() == 0) {
                Toast.makeText(v.getContext(), "没有选择", Toast.LENGTH_SHORT).show();
                return;
            }

            if (integerList.size() <= voteItem.pollInfo.type) {
                listener.onVote(voteItem.pollInfo, integerList);
                isLoading = true;
                this.bindLoading(voteItem);
            } else {
                Toast.makeText(v.getContext(), "超出选项数目", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class AttachmentViewHolder extends BaseViewHolder{
        @BindView(R.id.describe) TextView textView;
        @BindView(R.id.state) ImageView imageView;

        public static AttachmentViewHolder newInstance(ViewGroup viewGroup){
            return new AttachmentViewHolder(LayoutInflater
                    .from(viewGroup.getContext()).inflate(R.layout.item_post_attachment, viewGroup, false));
        }

        public AttachmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(BaseItem baseItem) {
            final AttachmentItem attachmentItem = (AttachmentItem) baseItem;

            textView.setText(attachmentItem.name + attachmentItem.dec);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLink(itemView.getContext(), attachmentItem.url);
                }
            });
        }
    }

    public static class UserViewHolder extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.username) TextView username;
        @BindView(R.id.time) TextView time;

        private UserBase userBase;

        public static UserViewHolder newInstance(ViewGroup viewGroup){
            return new UserViewHolder(LayoutInflater
                    .from(viewGroup.getContext()).inflate(R.layout.item_post_user, viewGroup, false));
        }

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            avatar.setOnClickListener(this);
            username.setOnClickListener(this);
        }

        @Override
        public void bind(BaseItem baseItem) {
            UserItem userItem = (UserItem) baseItem;
            userBase = ((UserItem) baseItem).userBase;

            Picasso.with(avatar.getContext())
                    .load(userItem.userBase.userAvatar)
                    .placeholder(R.drawable.place_avatar)
                    .transform(new AvatarTrans())
                    .into(avatar);

            username.setText(userItem.userBase.userName);
            time.setText(TimeUtil.getRelativeTime(userItem.replyTime));
        }

        @Override
        public void onClick(View v) {
            if (userBase == null) return;
            UserInfoActivity.startActivity(v.getContext(), userBase);
        }
    }

    public static class EndViewHolder extends BaseViewHolder{
        @BindView(R.id.report) TextView report;
        @BindView(R.id.reply) TextView reply;
        private Listener listener;

        public static EndViewHolder newInstance(ViewGroup viewGroup, Listener listener){
            listener = listener;
            return new EndViewHolder(LayoutInflater
                    .from(viewGroup.getContext()).inflate(R.layout.item_post_end, viewGroup, false), listener);
        }

        public EndViewHolder(View itemView, Listener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(BaseItem baseItem) {
            final EndItem endItem = (EndItem) baseItem;

            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReply(endItem.postId, endItem.userName);
                }
            });
        }
    }

    public static class TopicEndHolder extends BaseViewHolder{
        @BindView(R.id.reply) TextView reply;
        @BindView(R.id.report) TextView report;
        @BindView(R.id.reply_num) TextView replyNum;
        @BindView(R.id.rate) TextView rate;
        @BindView(R.id.edit) TextView edit;

        Listener listener;

        public static TopicEndHolder newInstance(ViewGroup viewGroup, Listener listener){
            TopicEndHolder topicEndHolder = new TopicEndHolder(LayoutInflater
                    .from(viewGroup.getContext()).inflate(R.layout.item_post_topic_end, viewGroup, false));
            topicEndHolder.listener = listener;
            return topicEndHolder;
        }

        public TopicEndHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(BaseItem baseItem) {
            final TopicEndItem topicEndItem = (TopicEndItem) baseItem;
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReply();
                }
            });
            if (topicEndItem.replyNum > 0) {
                replyNum.setText(topicEndItem.replyNum + "条评论");
            } else {
                replyNum.setText("没有评论");
            }

            if (topicEndItem.rateUrl != null) {
                rate.setVisibility(View.VISIBLE);
                rate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onRate(topicEndItem.rateUrl);
                    }
                });
            } else {
                rate.setVisibility(View.GONE);
            }

            if (topicEndItem.editUrl != null) {
                edit.setVisibility(View.VISIBLE);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickLink(topicEndItem.editUrl);
                    }
                });
            } else {
                edit.setVisibility(View.GONE);
            }

        }
    }

    public static class QuoteViewHolder extends BaseViewHolder {
        @BindView(R.id.quote) TextView textView;

        public static QuoteViewHolder newInstance(ViewGroup parent){
            return new QuoteViewHolder(LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.item_post_quote, parent, false));
        }

        public QuoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(BaseItem baseItem) {
            QuoteItem quoteItem = (QuoteItem) baseItem;
            textView.setText(quoteItem.content);
        }
    }

    public static class TitleViewHolder extends BaseViewHolder {

        @BindView(R.id.title) TextView title;

        public static TitleViewHolder newInstance(ViewGroup parent) {
            return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post_title, parent, false));
        }

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(BaseItem baseItem) {
            TitleItem titleItem = (TitleItem) baseItem;
            title.setText(titleItem.title);
        }
    }

    public interface Listener {
        void onReply();
        void onReply(int replyId, String name);
        void onVote(PostList.TopicContent.PollInfo pollInfo, List<Integer> integerList);

        void onRate(String rateUrl);

        void onClickLink(String url);
    }

    private static void openLink(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (!IntentUtil.hasActivity(context, intent)){
            Toast.makeText(context, "没有可以启动链接的程序", Toast.LENGTH_SHORT).show();
        } else {
            context.startActivity(intent);
        }
    }
}
