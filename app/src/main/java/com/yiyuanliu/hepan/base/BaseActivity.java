package com.yiyuanliu.hepan.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yiyuanliu.hepan.data.AccountManager;

/**
 * Created by yiyuan on 2016/7/11.
 * Father of any activity except LoginActivity
 */
public abstract class BaseActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastReceiver = new LogoutBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(AccountManager.ACTION_LOGOUT);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private class LogoutBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //finish();
        }
    }
}
