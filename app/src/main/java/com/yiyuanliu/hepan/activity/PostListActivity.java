package com.yiyuanliu.hepan.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.PostListAdapter;
import com.yiyuanliu.hepan.base.MoreLoadListener;
import com.yiyuanliu.hepan.contract.PostListView;
import com.yiyuanliu.hepan.data.Api;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.PostList;
import com.yiyuanliu.hepan.data.model.AtUserList;
import com.yiyuanliu.hepan.data.model.Rate;
import com.yiyuanliu.hepan.data.model.RateInfo;
import com.yiyuanliu.hepan.dialog.RateDialog;
import com.yiyuanliu.hepan.dialog.TopicAdminDialog;
import com.yiyuanliu.hepan.presenter.PostListPresenter;
import com.yiyuanliu.hepan.span.ImageTag;
import com.yiyuanliu.hepan.utils.DeviceUtil;
import com.yiyuanliu.hepan.utils.ExceptionHandle;
import com.yiyuanliu.hepan.utils.MyLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostListActivity extends AppCompatActivity implements PostListView, PostListAdapter.Listener, SwipeRefreshLayout.OnRefreshListener, RateDialog.RateListener {
    public static final String TAG = "PostListActivity";

    public static final String BUNDLE_TOPIC_ID = "TOPIC_ID";

    public static void startActivity(Context context, int topicId){
        Intent intent = new Intent(context, PostListActivity.class);
        intent.putExtra(BUNDLE_TOPIC_ID, topicId);
        context.startActivity(intent);
    }

    @BindView(R.id.text_error) TextView error;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private PostListPresenter postListPresenter;
    private MoreLoadListener moreLoadListener;
    private PostListAdapter postListAdapter;
    private int topicId;

    private boolean isLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null){
            topicId = intent.getIntExtra(BUNDLE_TOPIC_ID, 0);
        }
        postListPresenter = new PostListPresenter(DataManager.getInstance(this), topicId);
        postListPresenter.bindView(this);

        LinearLayoutManager linearLayoutManager = new MyLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        postListAdapter = new PostListAdapter(this);
        recyclerView.setAdapter(postListAdapter);
        moreLoadListener = new MoreLoadListener(linearLayoutManager, postListAdapter, postListPresenter);
        recyclerView.addOnScrollListener(moreLoadListener);
        getSupportActionBar().setTitle("浏览帖子");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (postListAdapter.getItemCount() == 0 && !postListPresenter.isLoading()) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadNew();
                }
            }, 100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postListPresenter.unbindView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_list, menu);
        for (int i = 0;i < menu.size(); i ++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.getIcon().setColorFilter(0xff000000, PorterDuff.Mode.SRC_IN);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_order_def:
                if (isLast) {
                    isLast = false;
                    postListPresenter.setOrder(Api.ORDER_DEF);
                    loadNew();
                }

                break;
            case R.id.action_order_last:
                if (!isLast) {
                    isLast = true;
                    postListPresenter.setOrder(Api.ORDER_LAST);
                    loadNew();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.text_error)
    public void loadNew(){
        error.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        postListPresenter.loadNew();
    }

    @Override
    public void voteSuccessful(List<PostList.TopicContent.PollInfo.PollItem> pollItemList) {
        postListAdapter.setVote(true, pollItemList);
    }

    @Override
    public void voteFailed(Throwable throwable) {
        Snackbar.make(recyclerView, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();
        postListAdapter.setVote(true, null);
    }

    @Override
    public void loadNewSuccess(PostList.TopicContent topic) {
        error.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);

        postListAdapter.clear();
        postListAdapter.addTopic(topic);
    }

    @Override
    public void loadNewSuccess(List<PostList.TopicReply> dataList) {
        for (PostList.TopicReply reply:dataList){
            postListAdapter.addTopicReply(reply);
        }

        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadMoreSuccess(List<PostList.TopicReply> dataList) {
        postListAdapter.setLoading(false);
        for (PostList.TopicReply reply:dataList){
            postListAdapter.addTopicReply(reply);
        }

        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadNewFailed(Throwable throwable) {
        error.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

        error.setText(ExceptionHandle.getMsg(TAG, throwable));
    }

    @Override
    public void loadMoreFailed(Throwable throwable) {
        Snackbar.make(recyclerView, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.reply)
    @Override
    public void onReply() {
        TopicAdminDialog.show(this, topicId, 0);
    }

    @Override
    public void onReply(int replyId, String name) {
        TopicAdminDialog.show(this, topicId, replyId);
    }

    @Override
    public void onVote(PostList.TopicContent.PollInfo pollInfo, List<Integer> integerList) {
        postListPresenter.vote(integerList);
    }

    @Override
    public void onRefresh() {
        loadNew();
    }

    private RateDialog mRateDialog;
    private RateInfo mRateInfo;

    @Override
    public void rateInfoLoaded(RateInfo rateInfo) {
        mRateDialog = RateDialog.showDialog(this, this, rateInfo);
        mRateInfo = rateInfo;
    }

    @Override
    public void rateInfoLoadedFailed(Throwable throwable) {
        Snackbar.make(recyclerView, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void rateFinished(Rate rate) {
        if (rate.cancelDialog) {
            mRateDialog.dismiss();
        }

        if (rate.successful) {
            Snackbar.make(recyclerView, "评分成功", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(recyclerView, rate.info, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void rateFailed(Throwable throwable) {
        mRateDialog.showError(throwable);
    }

    @Override
    public void onRate(int score, String reason, boolean notify) {
        postListPresenter.rate(mRateInfo, score, reason, notify);
    }

    @Override
    public void onRate(String rateUrl) {
        postListPresenter.loadRateInfo(rateUrl);
    }

    @Override
    public void onClickLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
