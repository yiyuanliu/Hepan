package com.yiyuanliu.hepan.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by yiyuan on 2016/8/4.
 */
public class IntentUtil {
    public static boolean hasActivity(Context context, Intent intent){
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}
