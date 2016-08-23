package com.yiyuanliu.hepan.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.NotifyPostAdapter;
import com.yiyuanliu.hepan.adapter.NotifySysAdapter;
import com.yiyuanliu.hepan.adapter.PmAdapter;
import com.yiyuanliu.hepan.base.MoreLoadAdapter;
import com.yiyuanliu.hepan.base.MoreLoadListener;
import com.yiyuanliu.hepan.contract.MessageView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.Topic;
import com.yiyuanliu.hepan.presenter.MessagePresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;
import com.yiyuanliu.hepan.utils.HepanException;
import com.yiyuanliu.hepan.utils.NoTopicException;
import com.yiyuanliu.hepan.utils.RecyclerDivider;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.http.PUT;

public class MessageFragment extends Fragment implements MessageView, SwipeRefreshLayout.OnRefreshListener {
    public static final int TYPE_NOTIFY_POST = 0;
    public static final int TYPE_NOTIFY_SYS = 1;
    public static final int TYPE_PM = 2;
    public static final int TYPE_AT = 4;
    private static final String TAG = "MessageFragment";

    public static MessageFragment newInstance(int type) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.text_error) TextView error;

    private boolean isLoadingNew;

    private MoreLoadListener moreLoadListener;

    private MessagePresenter messagePresenter;

    private NotifyPostAdapter notifyPostAdapter;
    private NotifySysAdapter notifySysAdapter;
    private PmAdapter pmAdapter;

    private int type;
    private Unbinder unbinder;

    public MessageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }

        messagePresenter = new MessagePresenter(DataManager.getInstance(getContext()), type);
        switch (type) {
            case TYPE_NOTIFY_POST:
                notifyPostAdapter = new NotifyPostAdapter();
                break;
            case TYPE_NOTIFY_SYS:
                notifySysAdapter = new NotifySysAdapter();
                break;
            case TYPE_PM:
                pmAdapter = new PmAdapter();
                break;
            case TYPE_AT:
                notifyPostAdapter = new NotifyPostAdapter();
                break;
            default:
                throw new IllegalStateException("wrong type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        messagePresenter.bindView(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MoreLoadAdapter moreLoadAdapter = null;
        switch (type) {
            case TYPE_AT: case TYPE_NOTIFY_POST:
                moreLoadAdapter = notifyPostAdapter;
                break;
            case TYPE_NOTIFY_SYS:
                moreLoadAdapter = notifySysAdapter;
                break;
            case TYPE_PM:
                moreLoadAdapter = pmAdapter;
                break;
        }
        moreLoadListener = new MoreLoadListener(linearLayoutManager, moreLoadAdapter, messagePresenter);
        recyclerView.setAdapter(moreLoadAdapter);
        recyclerView.addOnScrollListener(moreLoadListener);
        recyclerView.addItemDecoration(new RecyclerDivider(getContext()));

        if (moreLoadAdapter.getItemCount() == 0){
            isLoadingNew = true;
            loadNew();
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (isLoadingNew && swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(true);
                    }
                }
            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
            error.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        switch (type) {
            case TYPE_AT: case TYPE_NOTIFY_POST:
                notifyPostAdapter.setLoading(false);
                break;
            case TYPE_NOTIFY_SYS:
                notifySysAdapter.setLoading(false);
                break;
            case TYPE_PM:
                pmAdapter.setLoading(false);
                break;
        }
        messagePresenter.unbindView();

        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }

    @Override
    public void loadNewSuccess(List dataList) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);

        isLoadingNew = false;

        switch (type) {
            case TYPE_AT: case TYPE_NOTIFY_POST:
                notifyPostAdapter.clear();
                notifyPostAdapter.addNotifyPosts(dataList);
                notifyPostAdapter.setLoading(false);
                break;
            case TYPE_NOTIFY_SYS:
                notifySysAdapter.clear();
                notifySysAdapter.addNotifySys(dataList);
                notifySysAdapter.setLoading(false);
                break;
            case TYPE_PM:
                pmAdapter.clear();
                pmAdapter.addPms(dataList);
                pmAdapter.setLoading(false);
                break;
        }

        swipeRefreshLayout.setRefreshing(false);

        moreLoadListener.checkForLoad();

        recyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMoreSuccess(List dataList) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);

        switch (type) {
            case TYPE_AT: case TYPE_NOTIFY_POST:
                notifyPostAdapter.addNotifyPosts(dataList);
                notifyPostAdapter.setLoading(false);
                break;
            case TYPE_NOTIFY_SYS:
                notifySysAdapter.addNotifySys(dataList);
                notifySysAdapter.setLoading(false);
                break;
            case TYPE_PM:
                pmAdapter.addPms(dataList);
                pmAdapter.setLoading(false);
                break;
        }
        swipeRefreshLayout.setRefreshing(false);

        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadNewFailed(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);

        isLoadingNew = false;

        swipeRefreshLayout.setRefreshing(false);
        switch (type) {
            case TYPE_AT: case TYPE_NOTIFY_POST:
                notifyPostAdapter.setLoading(false);
                break;
            case TYPE_NOTIFY_SYS:
                notifySysAdapter.setLoading(false);
                break;
            case TYPE_PM:
                pmAdapter.setLoading(false);
                break;
        }

        error.setText(ExceptionHandle.getMsg(TAG, throwable));

    }

    @Override
    public void loadMoreFailed(Throwable throwable) {
        switch (type) {
            case TYPE_AT: case TYPE_NOTIFY_POST:
                notifyPostAdapter.setLoading(false);
                break;
            case TYPE_NOTIFY_SYS:
                notifySysAdapter.setLoading(false);
                break;
            case TYPE_PM:
                pmAdapter.setLoading(false);
                break;
        }

        Snackbar.make(swipeRefreshLayout, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onRefresh() {
        messagePresenter.loadNew();
    }

    @OnClick(R.id.text_error)
    public void loadNew(){
        swipeRefreshLayout.setRefreshing(true);
        error.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

        messagePresenter.loadNew();
    }
}
