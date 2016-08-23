package com.yiyuanliu.hepan.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.UserListAdapter;
import com.yiyuanliu.hepan.base.MoreLoadListener;
import com.yiyuanliu.hepan.contract.UserListView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.presenter.UserListPresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserListActivity extends AppCompatActivity implements UserListView {
    private static final String TAG = "UserListActivity";
    public static String TYPE_ALL = "all";
    public static String TYPE_FRIEND = "friend";

    public static String INTENT_TYPE = "TYPE";

    public static void startActivity(Context context, String type){
        Intent intent = new Intent(context, UserListActivity.class);
        intent.putExtra(INTENT_TYPE, type);
        context.startActivity(intent);
    }

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.text_error) TextView error;
    @BindView(R.id.loading) ProgressBar loading;

    private UserListAdapter userListAdapter;
    private MoreLoadListener moreLoadListener;
    private UserListPresenter userListPresenter;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null){
            type = intent.getStringExtra(INTENT_TYPE);
        }
        userListPresenter = new UserListPresenter(DataManager.getInstance(this), type);
        userListPresenter.bindView(this);

        userListAdapter = new UserListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        moreLoadListener = new MoreLoadListener(linearLayoutManager, userListAdapter, userListPresenter);
        recyclerView.setAdapter(userListAdapter);
        recyclerView.addOnScrollListener(moreLoadListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userListAdapter.getItemCount() == 0){
            loadNew();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userListPresenter.unbindView();
    }

    @OnClick(R.id.text_error)
    void loadNew() {
        loading.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        userListPresenter.loadNew();
    }

    @Override
    public void loadNewSuccess(List<UserBase> dataList) {
        loading.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        userListAdapter.setLoading(false);
        userListAdapter.clear();
        for (UserBase item:dataList){
            userListAdapter.addUserBase(item);
        }
        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadMoreSuccess(List<UserBase> dataList) {
        loading.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        userListAdapter.setLoading(false);
        for (UserBase item:dataList){
            userListAdapter.addUserBase(item);
        }
        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadNewFailed(Throwable throwable) {
        loading.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        error.setText(ExceptionHandle.getMsg(TAG, throwable));
    }

    @Override
    public void loadMoreFailed(Throwable throwable) {
        loading.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        Snackbar.make(recyclerView, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();
    }
}
