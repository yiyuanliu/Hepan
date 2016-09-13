package com.yiyuanliu.hepan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.PmMessageAdapter;
import com.yiyuanliu.hepan.base.MoreLoadListener;
import com.yiyuanliu.hepan.contract.PmMessageView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.PmMessage;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.presenter.PmMessagePresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yiyuan on 2016/8/19.
 */
public class PmMessageActivity extends AppCompatActivity implements PmMessageView {

    public static final String TAG = "PmMessageActivity";

    public static final String BUNDLE_USER_ID = "USER_ID";

    public static void startActivity(Context context, UserBase userBase){
        Intent intent = new Intent(context, PmMessageActivity.class);
        intent.putExtra(BUNDLE_USER_ID, userBase);
        context.startActivity(intent);
    }

    @BindView(R.id.progress_loading) ProgressBar loading;
    @BindView(R.id.text_error) TextView error;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @BindView(R.id.text) EditText text;
    @BindView(R.id.send) Button send;

    @BindView(R.id.toolbar) Toolbar toolbar;

    private PmMessagePresenter pmMessagePresenter;
    private PmMessageAdapter pmMessageAdapter;
    private MoreLoadListener moreLoadListener;

    private UserBase userBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null){
            userBase = (UserBase) intent.getSerializableExtra(BUNDLE_USER_ID);
        }
        pmMessagePresenter = new PmMessagePresenter(DataManager.getInstance(this),
                userBase == null ? 0: userBase.userId);
        pmMessagePresenter.bindView(this);
        setTitle(userBase.userName);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(linearLayoutManager);
        pmMessageAdapter = new PmMessageAdapter();
        recyclerView.setAdapter(pmMessageAdapter);

        moreLoadListener = new MoreLoadListener(linearLayoutManager, pmMessageAdapter, pmMessagePresenter);
        recyclerView.addOnScrollListener(moreLoadListener);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        if (pmMessageAdapter.getItemCount() == 0 && !pmMessagePresenter.isLoading()){
            loadNew();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pmMessagePresenter.unbindView();
    }

    @OnClick(R.id.text_error)
    public void loadNew(){
        error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        pmMessagePresenter.loadNew();
    }

    @OnClick(R.id.send)
    public void send(){
        if (text.getText().toString().equals("")) {
            Snackbar.make(text, "请输入内容", Snackbar.LENGTH_SHORT).show();
            return;
        }
        send.setEnabled(false);
        text.setEnabled(false);

        pmMessagePresenter.sendMessage(text.getText().toString());
    }

    @Override
    public void loadNewSuccess(List<PmMessage> pmMessages) {
        error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);

        pmMessageAdapter.clear();
        pmMessageAdapter.addPmMessages(pmMessages);
        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadMoreSuccess(List<PmMessage> dataList) {
        for (PmMessage pmMessage:dataList){
            pmMessageAdapter.setLoading(false);
            pmMessageAdapter.addPmMessage(pmMessage);
        }

        pmMessageAdapter.setLoading(false);
        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadNewFailed(Throwable throwable) {
        error.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);

        error.setText(ExceptionHandle.getMsg(TAG, throwable));
    }

    @Override
    public void loadMoreFailed(Throwable throwable) {
        pmMessageAdapter.setLoading(false);
        Snackbar.make(recyclerView, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void sendFailed(Throwable e) {
        text.setEnabled(true);
        send.setEnabled(true);

        Snackbar.make(text, ExceptionHandle.getMsg(TAG, e), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void sendSuccess() {
        Snackbar.make(text, "发送成功", Snackbar.LENGTH_SHORT).show();
        PmMessage pmMessage = new PmMessage();
        pmMessage.fromMe = true;
        pmMessage.content = text.getText().toString();
        pmMessage.time = System.currentTimeMillis();
        pmMessageAdapter.addNewPm(pmMessage);
        recyclerView.smoothScrollToPosition(0);

        text.setEnabled(true);
        send.setEnabled(true);

        text.setText("");
    }
}
