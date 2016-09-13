package com.yiyuanliu.hepan.utils;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.yiyuanliu.hepan.activity.BoardListActivity;
import com.yiyuanliu.hepan.activity.MainActivity;
import com.yiyuanliu.hepan.activity.MessageActivity;
import com.yiyuanliu.hepan.activity.SettingsActivity;
import com.yiyuanliu.hepan.activity.UserInfoActivity;
import com.yiyuanliu.hepan.activity.UserListActivity;
import com.yiyuanliu.hepan.data.AccountManager;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.UserBase;

/**
 * Created by yiyuan on 2016/8/24.
 */
public class MainDrawerListener implements DrawerLayout.DrawerListener {
    public static final int TO_BOARD_LIST = 0;
    public static final int TO_MESSAGE = 1;
    public static final int TO_FRIEND = 2;
    public static final int TO_SETTING = 3;
    public static final int TO_MY_INFO = 5;

    private MainActivity mainActivity;
    private DrawerLayout drawer;
    private int to;

    public MainDrawerListener(MainActivity mainActivity, DrawerLayout drawerLayout, int to) {
        this.mainActivity = mainActivity;
        this.drawer = drawerLayout;
        this.to = to;
    }

    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        switch (to) {
            case TO_BOARD_LIST:
                drawer.removeDrawerListener(this);
                Intent intent = new Intent(mainActivity, BoardListActivity.class);
                mainActivity.startActivity(intent);
                break;
            case TO_FRIEND:
                drawer.removeDrawerListener(this);
                UserListActivity.startActivity(mainActivity, UserListActivity.TYPE_FRIEND);
                break;
            case TO_MESSAGE:
                drawer.removeDrawerListener(this);
                MessageActivity.startActivity(mainActivity);
                break;
            case TO_MY_INFO:
                AccountManager account = DataManager.getInstance(mainActivity).getAccountManager();
                UserBase userBase = new UserBase(account.getUserName(), account.getUid(), account.getUserAvatar());
                UserInfoActivity.startActivity(mainActivity, userBase);
                break;
            case TO_SETTING:
                drawer.removeDrawerListener(this);
                SettingsActivity.startActivity(mainActivity);
                break;
        }
        drawer.removeDrawerListener(this);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }
}
