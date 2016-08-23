package com.yiyuanliu.hepan.span;

import android.content.Context;
import android.text.style.ImageSpan;

import com.yiyuanliu.hepan.R;

/**
 * Created by yiyuan on 2016/8/9.
 */
public class LoadingSpan extends ImageSpan {
    private EmojiTarget emojiTarget;

    public LoadingSpan(Context context) {
        super(context, R.drawable.place_emojy);
    }

    public void setEmojiTarget(EmojiTarget emojiTarget) {
        this.emojiTarget = emojiTarget;
    }
}
