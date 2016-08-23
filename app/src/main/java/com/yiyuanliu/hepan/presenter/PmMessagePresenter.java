package com.yiyuanliu.hepan.presenter;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.base.MoreLoadPresenter;
import com.yiyuanliu.hepan.contract.PmMessageView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.PmMessage;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/8/19.
 */
public class PmMessagePresenter extends BasePresenter<PmMessageView> implements MoreLoadPresenter {

    private DataManager dataManager;
    private Subscription subscription;
    private boolean hasMore;
    private long endTime = 0;
    private int uid;
    private int cache = 0;

    public PmMessagePresenter(DataManager dataManager, int uid) {
        this.dataManager = dataManager;
        this.uid = uid;
    }

    @Override
    public boolean isLoading() {
        return subscription!= null && !subscription.isUnsubscribed();
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    public void loadNew(){
        endTime = 0;
        cache = 0;
        subscription = dataManager.getApi().loadPmList(uid, endTime, 0, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PmMessage.PmMessageList>() {
                    @Override
                    public void onCompleted() {
                        subscription.unsubscribe();
                        subscription = null;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().loadNewFailed(e);
                        }
                    }

                    @Override
                    public void onNext(PmMessage.PmMessageList pmMessageList) {
                        if (isViewAttached()) {
                            hasMore = pmMessageList.hasMore;
                            endTime = pmMessageList.start;
                            cache += pmMessageList.pmMessages.size();
                            getView().loadNewSuccess(pmMessageList.pmMessages);
                        }
                    }
                });

    }

    @Override
    public void loadMore() {
        subscription = dataManager.getApi().loadPmList(uid, endTime, cache, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PmMessage.PmMessageList>() {
                    @Override
                    public void onCompleted() {
                        subscription.unsubscribe();
                        subscription = null;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().loadMoreFailed(e);
                        }
                    }

                    @Override
                    public void onNext(PmMessage.PmMessageList pmMessageList) {
                        if (isViewAttached()) {
                            hasMore = pmMessageList.hasMore;
                            endTime = pmMessageList.start;
                            cache += pmMessageList.pmMessages.size();
                            getView().loadMoreSuccess(pmMessageList.pmMessages);
                        }
                    }
                });
    }

    public void sendMessage(String content) {
        dataManager.getApi().sendMessage(uid, content, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().sendFailed(e);
                        }
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        if (isViewAttached()) {
                            getView().sendSuccess();
                        }
                    }
                });
    }

    @Override
    public void unbindView() {
        super.unbindView();
        if (isLoading()) {
            subscription.unsubscribe();
            subscription = null;
        }

    }
}
