package com.yiyuanliu.hepan;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Interpolator;
import android.util.Log;
import android.view.animation.Animation;

import com.instabug.library.IBGInvocationEvent;
import com.instabug.library.Instabug;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.utils.leaks.IMMLeaks;

import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

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

        new Instabug.Builder(this, "d647c6f97d30d0fec7c3670ec8794c2f")
                .setInvocationEvent(IBGInvocationEvent.IBGInvocationEventShake)
                .setEmailFieldRequired(false)
                .build();

        IMMLeaks.fixFocusedViewLeak(this);
    }

    public static App getApp() {
        return app;
    }
}
