package com.yiyuanliu.hepan.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.text.style.BackgroundColorSpan;
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
import com.yiyuanliu.hepan.presenter.PostListPresenter;
import com.yiyuanliu.hepan.span.ImageTag;
import com.yiyuanliu.hepan.span.TestSpan;
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
    @BindView(R.id.add_pic) Button addPic;

    @BindView(R.id.toolbar) Toolbar toolbar;

    private PostListPresenter postListPresenter;
    private MoreLoadListener moreLoadListener;
    private PostListAdapter postListAdapter;

    private boolean isLast;

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

        swipeRefreshLayout.setOnRefreshListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        postListPresenter.replyTopic(replyId, text);

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
    public void showAt(final AtUserList atUserList) {
        if (atUserList.stringList == null || atUserList.stringList.size() == 0) {
            Snackbar.make(addPic, "没有可以at的好友", Snackbar.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        List<Map<String, String>> mapList = new ArrayList<>();
        for (String str : atUserList.stringList) {
            Map<String, String > stringStringMap = new HashMap<>();
            stringStringMap.put("name", str);
            mapList.add(stringStringMap);
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, mapList,
                android.R.layout.simple_list_item_1, new String[]{"name"}, new int[]{android.R.id.text1});
        builder.setTitle("选择好友")
                .setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = atUserList.stringList.get(which);
                        text.append(" @" + name + " ");
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(true);
        builder.create().show();

    }

    @Override
    public void loadNewSuccess(PostList.TopicContent topic) {
        error.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);

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

    @OnClick(R.id.add_pic)
    void addPic() {
        PopupMenu popupMenu = new PopupMenu(this, addPic);
        popupMenu.inflate(R.menu.post_list_add);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_pic:
                        if (checkReadPermission()){

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, 0);

                        } else {
                            requestReadPermission();
                        }
                        return true;

                    case R.id.add_user:
                        postListPresenter.loadAt();
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkReadPermission(){
        if (!DeviceUtil.hasM()){
            return true;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestReadPermission(){
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                for (int i = 0;i < permissions.length;i ++){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        addPic();

                    } else {
                        Snackbar.make(addPic, "没有权限无法加载图片", Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    ImageTag imageTagSpan = new ImageTag(this.getResources().getColor(R.color.colorAccent), uri);

                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text.getText());
                    int len = spannableStringBuilder.length();
                    spannableStringBuilder.append("图片");
                    spannableStringBuilder.setSpan(imageTagSpan, len, len + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    text.setText(spannableStringBuilder);
                    text.setCursorVisible(true);
                }
                break;
        }
    }
}
