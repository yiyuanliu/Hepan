package com.yiyuanliu.hepan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.yiyuanliu.hepan.activity.delegate.Delegate;
import com.yiyuanliu.hepan.activity.delegate.SpinnerDelegate;
import com.yiyuanliu.hepan.activity.delegate.TabDelegate;
import com.yiyuanliu.hepan.utils.PreferenceHelper;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import rx.Subscription;

/**
 * Created by yiyuan on 2016/8/24.
 */
public class MainActivity extends AppCompatActivity {
    private Delegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (PreferenceHelper.getInstance(this).getMainActivityType()) {
            case 0:
                delegate = new SpinnerDelegate(this);
                break;
            case 1:
                delegate = new TabDelegate(this);
                break;
            case 2:
                break;
        }
        delegate.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        delegate.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delegate.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!delegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return delegate.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        delegate.onActivityResult(requestCode, resultCode, data);
    }


}
