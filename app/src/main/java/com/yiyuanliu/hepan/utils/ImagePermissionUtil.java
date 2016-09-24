package com.yiyuanliu.hepan.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;


/**
 * Created by yiyuan on 2016/9/24.
 */

public class ImagePermissionUtil {
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkReadPermission(Context context){
        if (!DeviceUtil.hasM()){
            return true;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermission(Activity context, int requestCode) {
        context.requestPermissions(new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode
        );
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermission(Fragment fragment, int requestCode) {
        fragment.requestPermissions(new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode
        );
    }
}
