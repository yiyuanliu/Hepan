package com.yiyuanliu.hepan.activity.delegate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.BoardListActivity;
import com.yiyuanliu.hepan.activity.LoginActivity;
import com.yiyuanliu.hepan.activity.MessageActivity;
import com.yiyuanliu.hepan.activity.NewTopicActivity;
import com.yiyuanliu.hepan.activity.SettingsActivity;
import com.yiyuanliu.hepan.activity.UserInfoActivity;
import com.yiyuanliu.hepan.activity.UserListActivity;
import com.yiyuanliu.hepan.adapter.MainSpinnerAdapter;
import com.yiyuanliu.hepan.data.AccountManager;
import com.yiyuanliu.hepan.data.Api;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.fragment.MainFragment;
import com.yiyuanliu.hepan.fragment.TopicListFragment;
import com.yiyuanliu.hepan.notify.HeartService;
import com.yiyuanliu.hepan.utils.AvatarTrans;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yiyuan on 2016/8/24.
 */
public interface Delegate {
    void onCreate(Bundle savedInstanceState) ;
    void onResume();
    void onDestroy();
    boolean onBackPressed();
    boolean onCreateOptionsMenu(Menu menu);
    void onActivityResult(int requestCode, int resultCode, Intent data) ;
}
