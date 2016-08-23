package com.yiyuanliu.hepan.fragment;

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
import com.yiyuanliu.hepan.adapter.MainAdapter;
import com.yiyuanliu.hepan.base.MoreLoadListener;
import com.yiyuanliu.hepan.contract.MainView;
import com.yiyuanliu.hepan.data.Api;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.Topic;
import com.yiyuanliu.hepan.presenter.MainPresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;
import com.yiyuanliu.hepan.utils.HepanException;
import com.yiyuanliu.hepan.utils.NoTopicException;
import com.yiyuanliu.hepan.utils.RecyclerDivider;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yiyuan on 2016/8/6.
 */
public class MainFragment extends Fragment implements MainView, SwipeRefreshLayout.OnRefreshListener {
    public static final String ARG_SORT = "SORT";
    private static final String TAG = "MainFragment";

    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.text_error) TextView error;

    private MainAdapter mainAdapter;
    private MoreLoadListener moreLoadListener;
    private MainPresenter mainPresenter;

    private Unbinder unbinder;
    private String sort;

    private boolean isLoadingNew;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String sort) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        args.putString(ARG_SORT, sort);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sort = getArguments().getString(ARG_SORT);

            if (sort == null){
                throw new RuntimeException("sort can't be null");
            } else if (!sort.equals(Api.TOPIC_SORT_ALL) && !sort.equals(Api.TOPIC_SORT_NEW)){
                throw new RuntimeException("sort must be Api.TOPIC_SORT_ALL or Api.TOPIC_SORT_NEW");
            }
        }

        mainPresenter = new MainPresenter(DataManager.getInstance(getContext()), sort);
        mainAdapter = new MainAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mainPresenter.bindView(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        moreLoadListener = new MoreLoadListener(linearLayoutManager, mainAdapter, mainPresenter);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.addOnScrollListener(moreLoadListener);
        recyclerView.addItemDecoration(new RecyclerDivider(getContext()));

        if (mainAdapter.getItemCount() == 0){
            isLoadingNew = true;
            loadNew();
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (isLoadingNew && swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(true);
                    }
                }
            });
        } else {
            error.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        mainAdapter.setLoading(false);
        mainPresenter.unbindView();

        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }

    @Override
    public void loadNewSuccess(List<Topic> dataList) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);

        isLoadingNew = false;

        mainAdapter.clear();
        mainAdapter.addTopics(dataList);

        swipeRefreshLayout.setRefreshing(false);
        mainAdapter.setLoading(false);

        moreLoadListener.checkForLoad();

        recyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMoreSuccess(List<Topic> dataList) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);

        mainAdapter.addTopics(dataList);
        swipeRefreshLayout.setRefreshing(false);
        mainAdapter.setLoading(false);

        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadNewFailed(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);

        isLoadingNew = false;

        swipeRefreshLayout.setRefreshing(false);
        mainAdapter.setLoading(false);

        error.setText(ExceptionHandle.getMsg(TAG, throwable));

    }

    @Override
    public void loadMoreFailed(Throwable throwable) {
        mainAdapter.setLoading(false);

        Snackbar.make(swipeRefreshLayout, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onRefresh() {
        mainPresenter.loadNew();
    }

    @OnClick(R.id.text_error)
    public void loadNew(){
        swipeRefreshLayout.setRefreshing(true);
        error.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

        mainPresenter.loadNew();
    }
}
