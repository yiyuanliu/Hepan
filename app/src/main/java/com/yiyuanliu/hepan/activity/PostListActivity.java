package com.yiyuanliu.hepan.activity;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.PostListAdapter;
import com.yiyuanliu.hepan.base.MoreLoadListener;
import com.yiyuanliu.hepan.contract.PostListView;
import com.yiyuanliu.hepan.data.Api;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.PostList;
import com.yiyuanliu.hepan.presenter.PostListPresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;
import com.yiyuanliu.hepan.utils.MyLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostListActivity extends AppCompatActivity implements PostListView, PostListAdapter.Listener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "PostListActivity";

    public static final String BUNDLE_TOPIC_ID = "TOPIC_ID";

    public static void startActivity(Context context, int topicId){
        Intent intent = new Intent(context, PostListActivity.class);
        intent.putExtra(BUNDLE_TOPIC_ID, topicId);
        context.startActivity(intent);
    }

    @BindView(R.id.text_error) TextView error;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.quote) TextView quote;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.text) EditText text;
    @BindView(R.id.send) Button send;

    @BindView(R.id.toolbar) Toolbar toolbar;

    private PostListPresenter postListPresenter;
    private MoreLoadListener moreLoadListener;
    private PostListAdapter postListAdapter;

    private boolean isLast;

    private boolean isLoadingNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int topicId = 0;
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

        isLoadingNew = true;
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (isLoadingNew && swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        text.post(new Runnable() {
            @Override
            public void run() {
                text.clearFocus();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (postListAdapter.getItemCount() == 0 && !postListPresenter.isLoading()){
            loadNew();
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

    @OnClick(R.id.send)
    public void send(){
        send.setEnabled(false);
        text.setEnabled(false);

        int replyId = 0;

        if (quote.getVisibility() == View.VISIBLE){
            replyId = (Integer) quote.getTag();
        }

        Snackbar.make(text, "发送中...", Snackbar.LENGTH_INDEFINITE).show();
        postListPresenter.replyTopic(replyId, text.getText().toString());

    }

    @OnClick(R.id.quote)
    public void cancelQuote(){
        quote.setVisibility(View.GONE);
    }

    @Override
    public void replySuccess() {
        Snackbar.make(text, "发送成功", Snackbar.LENGTH_SHORT).show();
        text.setEnabled(true);
        text.setText(null);
        send.setEnabled(true);

        moreLoadListener.checkForLoad();
    }

    @Override
    public void replyFailed(Throwable throwable) {
        Log.e(TAG, throwable.getMessage(), throwable);
        Snackbar.make(text, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();
        send.setEnabled(true);
        text.setEnabled(true);
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

        isLoadingNew = false;

        quote.setVisibility(View.GONE);

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
        for (PostList.TopicReply reply:dataList){
            postListAdapter.setLoading(false);
            postListAdapter.addTopicReply(reply);
        }

        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadNewFailed(Throwable throwable) {
        error.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

        isLoadingNew = false;

        error.setText(ExceptionHandle.getMsg(TAG, throwable));
    }

    @Override
    public void loadMoreFailed(Throwable throwable) {
        Snackbar.make(recyclerView, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onReply() {
        text.requestFocus();
        quote.setVisibility(View.GONE);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(text, 0);
    }

    @Override
    public void onReply(int replyId, String name) {
        quote.setVisibility(View.VISIBLE);
        quote.setText("评论 " + name + ":");
        quote.setTag(new Integer(replyId));

        text.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(text, 0);
    }

    @Override
    public void onVote(PostList.TopicContent.PollInfo pollInfo, List<Integer> integerList) {
        postListPresenter.vote(integerList);
    }

    @Override
    public void onRefresh() {
        loadNew();
    }
}
