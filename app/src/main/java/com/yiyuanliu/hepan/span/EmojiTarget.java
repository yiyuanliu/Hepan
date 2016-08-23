package com.yiyuanliu.hepan.span;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yiyuanliu.hepan.utils.DeviceUtil;

import java.lang.ref.WeakReference;

/**
 * Created by yiyuan on 2016/8/4.
 */
public class EmojiTarget implements Target {
    private static final String TAG = "EmojiTarget";

    private WeakReference<TextView> textViewWeakReference;
    LoadingSpan loadingSpan;

    public EmojiTarget(LoadingSpan loadingSpan, TextView textView) {
        this.loadingSpan = loadingSpan;
        loadingSpan.setEmojiTarget(this);
        this.textViewWeakReference = new WeakReference<TextView>(textView);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        loadingSpan.setEmojiTarget(null);

        Log.d(TAG, "onBitmapLoaded");

        TextView textView = textViewWeakReference.get();
        if (textView == null) {
            Log.d(TAG, "textview = null");
            return;
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView.getText());
        int start = spannableStringBuilder.getSpanStart(loadingSpan);
        if (start == -1) {
            Log.d(TAG, "start = -1");
            return;
        }
        int end = spannableStringBuilder.getSpanEnd(loadingSpan);
        Log.d(TAG, "start = " + start + " end = " + end);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        bitmapDrawable.setBounds(0, 0,
                (int) (DeviceUtil.dp2px(textView.getContext(), 25)),
                (int) (DeviceUtil.dp2px(textView.getContext(), 25)));

        spannableStringBuilder.removeSpan(loadingSpan);
        ImageSpan imageSpan = new ImageSpan(bitmapDrawable);
        spannableStringBuilder.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableStringBuilder);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        loadingSpan.setEmojiTarget(null);

        Log.e(TAG, "onBitmapFailed");

        TextView textView = textViewWeakReference.get();
        if (textView == null) {
            Log.d(TAG, "textview = null");
            return;
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView.getText());
        int start = spannableStringBuilder.getSpanStart(loadingSpan);
        if (start == -1) {
            Log.d(TAG, "start = -1");
            return;
        }
        int end = spannableStringBuilder.getSpanEnd(loadingSpan);
        Log.d(TAG, "start = " + start + " end = " + end);

        spannableStringBuilder.removeSpan(loadingSpan);
        ImageSpan imageSpan = new ImageSpan(errorDrawable);
        spannableStringBuilder.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableStringBuilder);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
