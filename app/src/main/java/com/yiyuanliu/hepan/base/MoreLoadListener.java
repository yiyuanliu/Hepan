package com.yiyuanliu.hepan.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by yiyuan on 2016/7/22.
 */
public class MoreLoadListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager linearLayoutManager;
    private MoreLoadAdapter moreLoadAdapter;
    private MoreLoadPresenter moreLoadPresenter;
    private int minSize;

    public MoreLoadListener(LinearLayoutManager linearLayoutManager,
                            MoreLoadAdapter moreLoadAdapter,
                            MoreLoadPresenter moreLoadPresenter,
                            int minSize){
        this.linearLayoutManager = linearLayoutManager;
        this.moreLoadAdapter = moreLoadAdapter;
        this.moreLoadPresenter = moreLoadPresenter;
        this.minSize = minSize;

        if (minSize <= 0){
            throw new RuntimeException("minSize shouldn't little than 0");
        }
    }

    public MoreLoadListener(LinearLayoutManager linearLayoutManager,
                            MoreLoadAdapter moreLoadAdapter,
                            MoreLoadPresenter moreLoadPresenter){
        this(linearLayoutManager, moreLoadAdapter, moreLoadPresenter, 10);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        checkForLoad();
    }

    public void checkForLoad(){
        if (linearLayoutManager.findLastVisibleItemPosition() >= linearLayoutManager.getItemCount() - minSize - 1
                && moreLoadPresenter.hasMore() && !moreLoadPresenter.isLoading()){
            moreLoadPresenter.loadMore();
            moreLoadAdapter.setLoading(true);
        }
    }
}
