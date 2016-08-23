package com.yiyuanliu.hepan.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/7/29.
 */
public class NewTopicAdapter extends RecyclerView.Adapter {

    public List<Uri> list = new ArrayList<>();
    public TextViewHolder textViewHolder;
    public HeaderViewHolder headerViewHolder;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                return HeaderViewHolder.newInstance(parent);
            case 2:
                return TextViewHolder.newInstance(parent);
            case 3:
                return ImageViewHolder.newInstance(parent);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0){
            this.headerViewHolder = (HeaderViewHolder) holder;
        } else if (position == 1){
            textViewHolder = (TextViewHolder) holder;
        } else {
            ((ImageViewHolder)holder).bind(list.get(position - 2));
        }
    }

    @Override
    public int getItemCount() {
        return 2 + list.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return 1;
            case 1:
                return 2;
            default:
                return 3;
        }
    }

    public void addImage(Uri uri) {
        list.add(uri);
        notifyItemInserted(list.size() + 1);
    }

    public void remove(int position) {
        if (position >= 2) {
            int p = position - 2;
            list.remove(p);
            notifyItemRemoved(position);
            return;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title) EditText title;
        @BindView(R.id.classification) Spinner classification;

        public static HeaderViewHolder newInstance(ViewGroup viewGroup){
            View item = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_new_topic_header, viewGroup, false);
            return new HeaderViewHolder(item);
        }

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public String getTitle() {
            return title.getText().toString();
        }

        public Spinner getClassification(){
            return classification;
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text) EditText post;

        public static TextViewHolder newInstance(ViewGroup parent){
            View item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_new_topic_text, parent, false);
            return new TextViewHolder(item);
        }

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public String getContent(){
            return post.getText().toString();
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements Target {

        @BindView(R.id.image) ImageView imageView;

        public static ImageViewHolder newInstance(ViewGroup parent){
            View item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post_image, parent, false);
            return new ImageViewHolder(item);
        }

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Uri uri){
            Picasso.with(imageView.getContext())
                    .load(uri)
                    .resize(DeviceUtil.getMaxWidth(imageView.getContext(), 32), DeviceUtil.getScreenHeight(imageView.getContext()))
                    .placeholder(R.drawable.palce_image)
                    .centerInside()
                    .onlyScaleDown()
                    .error(R.drawable.palce_image)
                    .into(this);
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

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(placeHolderDrawable);
        }
    }

}
