package com.yiyuanliu.hepan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.LoginActivity;
import com.yiyuanliu.hepan.adapter.MainAdapter;
import com.yiyuanliu.hepan.adapter.MainSpinnerAdapter;
import com.yiyuanliu.hepan.base.MoreLoadListener;
import com.yiyuanliu.hepan.contract.MainView;
import com.yiyuanliu.hepan.data.AccountManager;
import com.yiyuanliu.hepan.data.Api;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.Forum;
import com.yiyuanliu.hepan.data.model.Topic;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.fragment.MainFragment;
import com.yiyuanliu.hepan.fragment.TopicListFragment;
import com.yiyuanliu.hepan.notify.HeartService;
import com.yiyuanliu.hepan.presenter.MainPresenter;
import com.yiyuanliu.hepan.utils.AvatarTrans;
import com.yiyuanliu.hepan.utils.ExceptionHandle;
import com.yiyuanliu.hepan.utils.HepanException;
import com.yiyuanliu.hepan.utils.NoTopicException;
import com.yiyuanliu.hepan.utils.RecyclerDivider;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    public static final int RC_LOGIN = 0;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    @BindView(R.id.spinner) Spinner spinner;
    MainSpinnerAdapter mainSpinnerAdapter;

    private DataManager dataManager;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HeartService.startService(this);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        dataManager = DataManager.getInstance(this);

        mainSpinnerAdapter = new MainSpinnerAdapter(spinner);
        spinner.setAdapter(mainSpinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        initNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (!DataManager.getInstance(this).getAccountManager().hasAccount()){
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
                final DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        drawer.removeDrawerListener(this);
                        Intent intent = new Intent(MainActivity.this, BoardListActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                };
                drawer.addDrawerListener(drawerListener);
                break;
            case R.id.nav_message:
                final DrawerLayout.DrawerListener drawerListener1 = new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        drawer.removeDrawerListener(this);
                        MessageActivity.startActivity(MainActivity.this);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                };
                drawer.addDrawerListener(drawerListener1);
                break;
            case R.id.nav_friend_list:
                final DrawerLayout.DrawerListener drawerListener2 = new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        drawer.removeDrawerListener(this);
                        UserListActivity.startActivity(MainActivity.this, UserListActivity.TYPE_FRIEND);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                };
                drawer.addDrawerListener(drawerListener2);
                break;
            case R.id.nav_setting:
                final DrawerLayout.DrawerListener drawerListener3 = new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        drawer.removeDrawerListener(this);
                        SettingsActivity.startActivity(MainActivity.this);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                };
                drawer.addDrawerListener(drawerListener3);
                break;
            case R.id.action_my_info:
                final DrawerLayout.DrawerListener drawerListener4 = new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        AccountManager account = DataManager.getInstance(MainActivity.this).getAccountManager();
                        UserBase userBase = new UserBase(account.getUserName(), account.getUid(), account.getUserAvatar());
                        UserInfoActivity.startActivity(MainActivity.this, userBase);
                        drawer.removeDrawerListener(this);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                };
                drawer.addDrawerListener(drawerListener4);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RC_LOGIN:
                if (resultCode == RESULT_OK){
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
                    UserInfoActivity.startActivity(MainActivity.this, userBase);
                }
            });
        } else {
            user.setText(getText(R.string.action_login));
            avatar.setImageResource(R.drawable.place_avatar);
            button.setVisibility(View.GONE);

            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat a = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(MainActivity.this, avatar, getString(R.string.trans_name_login));
                    login(a);
                }
            });
        }

    }

    @OnClick(R.id.fab)
    public void publicNew(){
        Intent intent = new Intent(this, NewTopicActivity.class);
        startActivity(intent);
    }

    public void login(ActivityOptionsCompat a){
        Intent intent = new Intent(this, LoginActivity.class);

        if (a == null){
            startActivityForResult(intent, RC_LOGIN);
        } else {
            startActivityForResult(intent, RC_LOGIN, a.toBundle());
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
                if (getSupportFragmentManager().findFragmentByTag("最新发表") != null){
                    break;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance(Api.TOPIC_SORT_NEW), "最新发表")
                        .commitAllowingStateLoss();
                Log.d(TAG, "goToPosition " + "最新发表");
                break;
            case 1:
                if (getSupportFragmentManager().findFragmentByTag("最新回复") != null){
                    break;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance(Api.TOPIC_SORT_ALL), "最新回复")
                        .commitAllowingStateLoss();
                Log.d(TAG, "goToPosition " + "最新回复");
                break;
            case 2:
                if (getSupportFragmentManager().findFragmentByTag("今日热门") != null){
                    break;
                }
                getSupportFragmentManager().beginTransaction()
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
