package com.yiyuanliu.hepan.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import com.yiyuanliu.hepan.R;

/**
 * Created by yiyuan on 2016/8/27.
 */
public class TestSpan extends DynamicDrawableSpan {
    Drawable drawable;

    public TestSpan(Context context) {
        drawable = context.getResources().getDrawable(R.drawable.bg_image_span);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, 0, text.length(),
                drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2, paint);
    }
}
