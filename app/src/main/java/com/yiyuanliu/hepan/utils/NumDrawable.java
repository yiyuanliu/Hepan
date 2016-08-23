package com.yiyuanliu.hepan.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

import com.yiyuanliu.hepan.R;

/**
 * Created by yiyuan on 2016/8/20.
 */
public class NumDrawable extends Drawable {

    private int num;
    private Paint paint;
    private int colorWhite;
    private int colorAccent;
    private TextPaint textPaint;
    private float size;
    private float textHalfHeight;

    public NumDrawable (Context context, int num) {
        this.num = num;
        paint = new Paint();
        paint.setAntiAlias(true);
        colorAccent = context.getResources().getColor(R.color.colorAccent);
        colorWhite = context.getResources().getColor(R.color.colorWhite);
        paint.setColor(colorWhite);

        textPaint = new TextPaint();
        textPaint.setColor(colorAccent);
        size = DeviceUtil.dp2px(context, 15);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(size);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textHalfHeight = - (fm.descent + fm.ascent) / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();

        int r = Math.min(rect.width(), rect.height()) / 2;
        int x = rect.width() / 2;
        int y = rect.height() / 2;

        canvas.drawCircle(x, y, r, paint);
        canvas.drawText(String.valueOf(num), x, y + textHalfHeight , textPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
