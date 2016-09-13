package com.yiyuanliu.hepan.span;

import android.net.Uri;
import android.text.style.ForegroundColorSpan;

/**
 * Created by yiyuan on 2016/8/27.
 */
public class ImageTag extends ForegroundColorSpan {
    private Uri uri;

    public ImageTag(int color, Uri image) {
        super(color);
        uri = image;
    }

    public Uri getUri() {
        return uri;
    }
}
