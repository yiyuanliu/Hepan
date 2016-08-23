package com.yiyuanliu.hepan.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yiyuanliu.hepan.App;
import com.yiyuanliu.hepan.adapter.PostListAdapter;

/**
 * Created by yiyuan on 2016/8/20.
 */
public class PreferenceHelper {
    private static PreferenceHelper preferenceHelper;

    public static PreferenceHelper getInstance(Context context) {

        synchronized (PreferenceHelper.class) {

            if (preferenceHelper == null) {
                preferenceHelper = new PreferenceHelper();
                preferenceHelper.context = context.getApplicationContext();
            }

        }

        return preferenceHelper;
    }

    private Context context;

    public boolean isNotifyEnabled() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (DeviceUtil.isWifi(context)) {
            return !sharedPreferences.getString("notify_wifi", "10").equals("0");
        } else {
            return !sharedPreferences.getString("notify_sim", "30").equals("0");
        }
    }

    public int getNotifyDelay() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (DeviceUtil.isWifi(context)) {
            return Integer.parseInt(sharedPreferences.getString("notify_wifi","10"));
        } else {
            return Integer.parseInt(sharedPreferences.getString("notify_sim","30"));
        }
    }

    public String getImageUrl(PostListAdapter.ImageItem imageItem) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int num = 0;
        if (DeviceUtil.isWifi(context)) {
            num = Integer.parseInt(sharedPreferences.getString("image_wifi", "1"));
        } else {
            num = Integer.parseInt(sharedPreferences.getString("image_sim", "0"));
        }

        switch (num) {
            case 0:
                return imageItem.little;
            case 1:
                return imageItem.middle;
            case 2:
                return imageItem.big;
            default:
                return null;
        }
    }

}
