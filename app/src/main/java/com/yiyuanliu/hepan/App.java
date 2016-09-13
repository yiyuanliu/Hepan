package com.yiyuanliu.hepan;

import android.app.Application;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.utils.leaks.IMMLeaks;

import im.fir.sdk.FIR;

/**
 * Created by yiyuan on 2016/7/30.
 */
public class App extends Application {
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Picasso.Builder builder = new Picasso.Builder(app);
        Picasso picasso = builder.defaultBitmapConfig(Bitmap.Config.RGB_565).build();
        Picasso.setSingletonInstance(picasso);

        IMMLeaks.fixFocusedViewLeak(this);
        FIR.init(this);
    }

    public static App getApp() {
        return app;
    }
}
