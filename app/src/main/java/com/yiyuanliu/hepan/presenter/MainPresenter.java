package com.yiyuanliu.hepan.presenter;

import android.util.Log;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.base.MoreLoadPresenter;
import com.yiyuanliu.hepan.contract.MainView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.TopicList;
import com.yiyuanliu.hepan.utils.NoTopicException;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/7/22.
 */
public class MainPresenter extends BasePresenter<MainView> implements MoreLoadPresenter {
    private static final String TAG = "MainPresenter";

    private DataManager dataManager;
    private Subscription subscription;
    private boolean hasMore;
    private int page;
    private String sort;

    public MainPresenter(DataManager dataManager, String sort) {
        this.dataManager = dataManager;
        this.sort = sort;
    }

    public void setSort(String sort){
        this.sort = sort;
    }

    @Override
    public boolean isLoading() {
        return subscription != null && !subscription.isUnsubscribed();
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    public void loadNew(){
        page = 1;
        cancelTask();

        subscription = dataManager.getApi().topicList(0, page, sort,
                dataManager.getAccountManager().getUserMap(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TopicList>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        page = 1;

                        if (isViewAttached()){
                            getView().loadNewFailed(e);
                        }
                    }

                    @Override
                    public void onNext(TopicList topicList) {
                        page += 1;

                        if (isViewAttached()){
                            hasMore = topicList.hasMore;
                            if (topicList.topicList == null || topicList.topicList.size() == 0){
                                throw new NoTopicException();
                            }
                            getView().loadNewSuccess(topicList.topicList);
                        }
                    }
                });
    }

    @Override
    public void loadMore() {
        if (!hasMore){
            throw new RuntimeException("can't loadForum more beacuse no more item");
        }

        subscription = dataManager.getApi().topicList(0, page, sort,
                dataManager.getAccountManager().getUserMap(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TopicList>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().loadMoreFailed(e);
                        }

                        Log.e(TAG, e.getMessage(), e);
                    }

                    @Override
                    public void onNext(TopicList topicList) {
                        page += 1;

                        if (isViewAttached()){
                            hasMore = topicList.hasMore;

                            getView().loadMoreSuccess(topicList.topicList);
                        }
                    }
                });
    }

    public void cancelTask(){
        if (isLoading()){
            subscription.unsubscribe();
            subscription = null;
        }
    }
}
