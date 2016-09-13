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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.TopicListAdapter;
import com.yiyuanliu.hepan.base.MoreLoadListener;
import com.yiyuanliu.hepan.contract.TopicListView;
import com.yiyuanliu.hepan.data.Api;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.Topic;
import com.yiyuanliu.hepan.presenter.TopicListPresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;
import com.yiyuanliu.hepan.utils.RecyclerDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TopicListFragment extends Fragment implements TopicListView, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_BOARD_ID = "BOARD_ID";
    private static final String ARG_IS_USER = "IS_USER";
    private static final String TAG = "TopicListFragment";

    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.text_error) TextView error;
    @BindView(R.id.container) FrameLayout container;

    private TopicListAdapter topicListAdapter;
    private MoreLoadListener moreLoadListener;
    private TopicListPresenter topicListPresenter;

    private Unbinder unbinder;
    private int boardId;
    private boolean isUser;

    private boolean isLoadingNew;


    public TopicListFragment() {
        // Required empty public constructor
    }

    public static TopicListFragment newInstance(int id, boolean isUser) {
        TopicListFragment fragment = new TopicListFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_BOARD_ID, id);
        args.putBoolean(ARG_IS_USER, isUser);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            boardId = getArguments().getInt(ARG_BOARD_ID);
            isUser = getArguments().getBoolean(ARG_IS_USER);

            if (boardId == 0 && !isUser){
                throw new RuntimeException("boardId = 0");
            }
        }

        topicListPresenter = new
                TopicListPresenter(DataManager.getInstance(getContext()), boardId, isUser, Api.TOPIC_SORT_NEW);
        topicListAdapter = new TopicListAdapter();
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
        topicListPresenter.bindView(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        moreLoadListener = new MoreLoadListener(linearLayoutManager, topicListAdapter, topicListPresenter);
        recyclerView.setAdapter(topicListAdapter);
        recyclerView.addOnScrollListener(moreLoadListener);
        recyclerView.addItemDecoration(new RecyclerDivider(getContext()));

        if (topicListAdapter.getItemCount() == 0){
            loadNew();
            isLoadingNew = true;
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
        topicListAdapter.setLoading(false);
        topicListPresenter.unbindView();

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

        topicListAdapter.clear();
        topicListAdapter.addTopics(dataList);

        swipeRefreshLayout.setRefreshing(false);
        topicListAdapter.setLoading(false);

        moreLoadListener.checkForLoad();

        recyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMoreSuccess(List<Topic> dataList) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);

        topicListAdapter.addTopics(dataList);
        swipeRefreshLayout.setRefreshing(false);
        topicListAdapter.setLoading(false);

        moreLoadListener.checkForLoad();
    }

    @Override
    public void loadNewFailed(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setRefreshing(false);
        topicListAdapter.setLoading(false);

        error.setText(ExceptionHandle.getMsg(TAG, throwable));

    }

    @Override
    public void loadMoreFailed(Throwable throwable) {
        topicListAdapter.setLoading(false);

        Snackbar.make(swipeRefreshLayout, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onRefresh() {
        topicListPresenter.loadNew();
    }

    @OnClick(R.id.text_error)
    public void loadNew(){
        swipeRefreshLayout.setRefreshing(false);
        error.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

        topicListPresenter.loadNew();
    }

}
