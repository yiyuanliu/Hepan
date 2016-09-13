package com.yiyuanliu.hepan.activity.delegate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.LoginActivity;
import com.yiyuanliu.hepan.activity.MainActivity;
import com.yiyuanliu.hepan.activity.NewTopicActivity;
import com.yiyuanliu.hepan.activity.UserInfoActivity;
import com.yiyuanliu.hepan.adapter.MainSpinnerAdapter;
import com.yiyuanliu.hepan.data.AccountManager;
import com.yiyuanliu.hepan.data.Api;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.fragment.MainFragment;
import com.yiyuanliu.hepan.fragment.TopicListFragment;
import com.yiyuanliu.hepan.notify.HeartService;
import com.yiyuanliu.hepan.utils.AvatarTrans;
import com.yiyuanliu.hepan.utils.MainDrawerListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yiyuan on 2016/8/24.
 */
public class SpinnerDelegate implements Delegate, NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener, View.OnClickListener {
    private MainActivity mainActivity;

    public SpinnerDelegate(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private static final String TAG = "MainActivityOld";

    public static final int RC_LOGIN = 0;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    @BindView(R.id.spinner)
    Spinner spinner;
    MainSpinnerAdapter mainSpinnerAdapter;

    private DataManager dataManager;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainActivity.setContentView(R.layout.activity_main);

        HeartService.startService(mainActivity);

        ButterKnife.bind(this, mainActivity.findViewById(android.R.id.content));

        mainActivity.setSupportActionBar(toolbar);
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mainActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        dataManager = DataManager.getInstance(mainActivity);

        mainSpinnerAdapter = new MainSpinnerAdapter(spinner);
        spinner.setAdapter(mainSpinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        initNav();
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public boolean onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mainActivity.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (!DataManager.getInstance(mainActivity).getAccountManager().hasAccount()){
            login(null);
            return true;
        }

        int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);

        switch (id){
            case R.id.nav_new:
                goToPage(0);
                break;
            case R.id.nav_all:
                goToPage(1);
                break;
            case R.id.nav_hot:
                goToPage(2);
                break;
            case R.id.nav_board_list:
                drawer.addDrawerListener(new MainDrawerListener(mainActivity, drawer, MainDrawerListener.TO_BOARD_LIST));
                break;
            case R.id.nav_message:
                drawer.addDrawerListener(new MainDrawerListener(mainActivity, drawer, MainDrawerListener.TO_MESSAGE));
                break;
            case R.id.nav_friend_list:
                drawer.addDrawerListener(new MainDrawerListener(mainActivity, drawer, MainDrawerListener.TO_FRIEND));
                break;
            case R.id.nav_setting:
                drawer.addDrawerListener(new MainDrawerListener(mainActivity, drawer, MainDrawerListener.TO_SETTING));
                break;
            case R.id.action_my_info:
                drawer.addDrawerListener(new MainDrawerListener(mainActivity, drawer, MainDrawerListener.TO_MY_INFO));
                break;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RC_LOGIN:
                if (resultCode == Activity.RESULT_OK){
                    initNav();
                }
                break;
        }
    }

    public void initNav(){
        View head = navigationView.getHeaderView(0);

        final ImageView avatar = (ImageView) head.findViewById(R.id.avatar);
        TextView user = (TextView) head.findViewById(R.id.username);
        Button button = (Button) head.findViewById(R.id.logout);
        button.setOnClickListener(this);

        final AccountManager account = dataManager.getAccountManager();

        if (account.hasAccount()){

            Picasso.with(avatar.getContext())
                    .load(account.getUserAvatar())
                    .transform(new AvatarTrans())
                    .into(avatar);
            user.setText(account.getUserName());
            button.setVisibility(View.VISIBLE);
            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserBase userBase = new UserBase(account.getUserName(), account.getUid(), account.getUserAvatar());
                    UserInfoActivity.startActivity(mainActivity, userBase);
                }
            });
        } else {
            user.setText(mainActivity.getText(R.string.action_login));
            avatar.setImageResource(R.drawable.place_avatar);
            button.setVisibility(View.GONE);

            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat a = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(mainActivity, avatar, mainActivity.getString(R.string.trans_name_login));
                    login(a);
                }
            });
        }

    }

    @OnClick(R.id.fab)
    public void publicNew(){
        if (!dataManager.getAccountManager().hasAccount()) {
            Snackbar.make(navigationView, "登陆后才能发帖", Snackbar.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(mainActivity, NewTopicActivity.class);
        mainActivity.startActivity(intent);
    }

    public void login(ActivityOptionsCompat a){
        Intent intent = new Intent(mainActivity, LoginActivity.class);

        if (a == null){
            mainActivity.startActivityForResult(intent, RC_LOGIN);
        } else {
            mainActivity.startActivityForResult(intent, RC_LOGIN, a.toBundle());
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        goToPage(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void goToPage(int position) {
        this.position = position;

        if (position < 3) {
            spinner.setSelection(position);
        }

        switch (position) {
            case 0:
                if (mainActivity.getSupportFragmentManager().findFragmentByTag("最新发表") != null){
                    break;
                }
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance(Api.TOPIC_SORT_NEW), "最新发表")
                        .commitAllowingStateLoss();
                Log.d(TAG, "goToPosition " + "最新发表");
                break;
            case 1:
                if (mainActivity.getSupportFragmentManager().findFragmentByTag("最新回复") != null){
                    break;
                }
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance(Api.TOPIC_SORT_ALL), "最新回复")
                        .commitAllowingStateLoss();
                Log.d(TAG, "goToPosition " + "最新回复");
                break;
            case 2:
                if (mainActivity.getSupportFragmentManager().findFragmentByTag("今日热门") != null){
                    break;
                }
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TopicListFragment.newInstance(-1, false), "今日热门")
                        .commitAllowingStateLoss();
                Log.d(TAG, "goToPosition " + "今日热门");
                break;
            default:
                Log.d(TAG, "goToPosition " + position + "?");
                break;
        }

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = null;
        switch (position) {
            case 0:
                menuItem = menu.findItem(R.id.nav_new);
                break;
            case 1:
                menuItem = menu.findItem(R.id.nav_all);
                break;
            case 2:
                menuItem = menu.findItem(R.id.nav_hot);
                break;
            default:
                throw new IllegalStateException("Unknown position " + position);
        }
        menuItem.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage("是否退出登录？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataManager.getAccountManager().logout();
                        initNav();
                    }
                }).setNegativeButton("取消", null)
                .show();

    }
}
