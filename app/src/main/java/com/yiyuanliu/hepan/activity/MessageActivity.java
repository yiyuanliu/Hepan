package com.yiyuanliu.hepan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.fragment.MessageFragment;
import com.yiyuanliu.hepan.utils.NumDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/8/19.
 */
public class MessageActivity extends AppCompatActivity {
    public static void startActivity(Context context){
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    public static Intent getIntent(Context context, int at, int pm, int post, int sys) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra("at", at);
        intent.putExtra("pm", pm);
        intent.putExtra("post", post);
        intent.putExtra("sys", sys);

        return intent;
    }

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MessageAdapter messageAdapter = new MessageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(messageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if (intent != null) {
            int at = intent.getIntExtra("at", 0);
            if (at > 0) {
                tabLayout.getTabAt(1).setIcon(new NumDrawable(this, at));
                viewPager.setCurrentItem(1);
            }
            int pm = intent.getIntExtra("pm", 0);
            if (pm > 0) {
                tabLayout.getTabAt(2).setIcon(new NumDrawable(this, pm));
                viewPager.setCurrentItem(2);
            }
            int post = intent.getIntExtra("post", 0);
            if (post > 0) {
                tabLayout.getTabAt(0).setIcon(new NumDrawable(this, post));
                viewPager.setCurrentItem(0);
            }
            int sys = intent.getIntExtra("sys", 0);
            if (sys > 0) {
                tabLayout.getTabAt(3).setIcon(new NumDrawable(this, sys));
                viewPager.setCurrentItem(3);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class MessageAdapter extends FragmentPagerAdapter {

        public MessageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MessageFragment.newInstance(MessageFragment.TYPE_NOTIFY_POST);
                case 1:
                    return MessageFragment.newInstance(MessageFragment.TYPE_AT);
                case 2:
                    return MessageFragment.newInstance(MessageFragment.TYPE_PM);
                case 3:
                    return MessageFragment.newInstance(MessageFragment.TYPE_NOTIFY_SYS);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "帖子回复";
                case 1:
                    return "@我";
                case 2:
                    return "私信";
                case 3:
                    return "系统通知";
            }
            return null;
        }
    }

}
