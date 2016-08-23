package com.yiyuanliu.hepan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.UserInfoAdapter;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.fragment.TopicListFragment;
import com.yiyuanliu.hepan.fragment.UserInfoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/8/18.
 */
public class UserInfoActivity extends AppCompatActivity {

    public static void startActivity(Context context, UserBase userBase){
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("user", userBase);
        context.startActivity(intent);
    }

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.view_pager) ViewPager viewPager;

    private UserBase userBase;
    private UserInfoAdapter userInfoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            userBase = (UserBase) intent.getSerializableExtra("user");
        }
        setTitle(userBase.userName);

        userInfoAdapter = new UserInfoAdapter(getSupportFragmentManager(), userBase);
        viewPager.setAdapter(userInfoAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class UserInfoAdapter extends FragmentPagerAdapter {

        private static final String PAGE_INFO = "个人信息";
        private static final String PAGE_TOPIC = "ta的帖子";

        private UserBase userBase;

        public UserInfoAdapter(FragmentManager fm, UserBase userBase) {
            super(fm);
            this.userBase = userBase;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return UserInfoFragment.newInstance(userBase);
            } else if (position == 1) {
                return TopicListFragment.newInstance(userBase.userId, true);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return PAGE_INFO;
            } else {
                return PAGE_TOPIC;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_chat) {
            PmMessageActivity.startActivity(this, userBase);
        }
        return super.onOptionsItemSelected(item);
    }
}
