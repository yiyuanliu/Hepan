package com.yiyuanliu.hepan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.utils.DeviceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/9/5.
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.subtitle) TextView subtitle;

    private boolean atyStarted;
    private boolean isFirstResumed = true;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity();
            atyStarted = true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_splash);

        ButterKnife.bind(this);

        title.setAlpha(0);
        subtitle.setAlpha(0);

        title.animate().alpha(1).setDuration(500).start();
        title.animate().translationY(- DeviceUtil.dp2px(this, 50)).setDuration(600);
    }

    @Override
    protected void onResume() {
        if (isFirstResumed)
            title.postDelayed(runnable, 1000);
        else {
            startActivity();
        }
        isFirstResumed = false;

        super.onResume();
    }

    @Override
    protected void onStop() {
        if (!atyStarted && title != null) {
            title.removeCallbacks(runnable);
        }
        super.onStop();
    }

    private void startActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0 ,android.R.anim.fade_out);
    }
}
