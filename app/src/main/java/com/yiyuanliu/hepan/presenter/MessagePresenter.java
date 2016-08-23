package com.yiyuanliu.hepan.presenter;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.base.MoreLoadPresenter;
import com.yiyuanliu.hepan.contract.MessageView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.PmJson;
import com.yiyuanliu.hepan.data.model.NotifyPost;
import com.yiyuanliu.hepan.data.model.NotifySys;
import com.yiyuanliu.hepan.data.model.Pm;
import com.yiyuanliu.hepan.fragment.MessageFragment;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/8/18.
 */
public class MessagePresenter extends BasePresenter<MessageView> implements MoreLoadPresenter {

    private DataManager dataManager;
    private int type;
    private Subscription subscription;
    private boolean hasMore;
    private int page = 1;

    public MessagePresenter(DataManager dataManager, int type) {
        this.dataManager = dataManager;
        this.type = type;
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
        if (isLoading()){
            subscription.unsubscribe();
            subscription = null;
        }

        switch (type) {
            case MessageFragment.TYPE_PM:
                final PmJson pmJson = new PmJson(page);
                subscription = dataManager.getApi().loadPmList(pmJson, dataManager.getAccountManager().getUserMap())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Pm.PmList>() {
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
                            public void onNext(Pm.PmList pmList) {
                                if (isViewAttached()){
                                    page ++;
                                    hasMore = pmList.hasMore;
                                    if (pmList.pmList.size() == 0){
                                        throw new RuntimeException("没有消息");
                                    }
                                    getView().loadNewSuccess(pmList.pmList);
                                }
                            }
                        });
                break;
            case MessageFragment.TYPE_NOTIFY_POST:
                subscription = dataManager.getApi().loadNotifyPost(page, dataManager.getAccountManager().getUserMap())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<NotifyPost.NotifyPostList>() {
                            @Override
                            public void onCompleted() {
                                subscription.unsubscribe();;
                                subscription = null;
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().loadNewFailed(e);
                                }
                            }

                            @Override
                            public void onNext(NotifyPost.NotifyPostList notifyPostList) {
                                if (isViewAttached()) {
                                    page ++;
                                    hasMore = notifyPostList.hasMore;
                                    if (notifyPostList.notifyPosts.size() == 0){
                                        throw new RuntimeException("没有消息");
                                    }
                                    getView().loadNewSuccess(notifyPostList.notifyPosts);
                                }
                            }
                        });
                break;
            case MessageFragment.TYPE_NOTIFY_SYS:
                subscription = dataManager.getApi().loadNotifySys(page, dataManager.getAccountManager().getUserMap())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<NotifySys.NotifySysList>() {
                            @Override
                            public void onCompleted() {
                                subscription.unsubscribe();;
                                subscription = null;
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().loadNewFailed(e);
                                }
                            }

                            @Override
                            public void onNext(NotifySys.NotifySysList notifySysList) {
                                if (isViewAttached()) {
                                    page ++;
                                    hasMore = notifySysList.hasMore;
                                    if (notifySysList.notifySysList.size() == 0){
                                        throw new RuntimeException("没有消息");
                                    }
                                    getView().loadNewSuccess(notifySysList.notifySysList);
                                }
                            }
                        });
                break;
            case MessageFragment.TYPE_AT:
                PmJson pmJson1 = new PmJson(page);
                subscription = dataManager.getApi().loadNotifyAt(page, dataManager.getAccountManager().getUserMap())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<NotifyPost.NotifyPostList>() {
                            @Override
                            public void onCompleted() {
                                subscription.unsubscribe();;
                                subscription = null;
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().loadNewFailed(e);
                                }
                            }

                            @Override
                            public void onNext(NotifyPost.NotifyPostList notifyPostList) {
                                if (isViewAttached()) {
                                    page ++;
                                    hasMore = notifyPostList.hasMore;
                                    if (notifyPostList.notifyPosts.size() == 0) {
                                        throw new RuntimeException("没有消息");
                                    }
                                    getView().loadNewSuccess(notifyPostList.notifyPosts);
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void loadMore() {
        if (isLoading()){
            subscription.unsubscribe();
            subscription = null;
        }

        switch (type) {
            case MessageFragment.TYPE_PM:
                final PmJson pmJson = new PmJson(page);
                subscription = dataManager.getApi().loadPmList(pmJson, dataManager.getAccountManager().getUserMap())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Pm.PmList>() {
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
                            public void onNext(Pm.PmList pmList) {
                                if (isViewAttached()){
                                    page ++;
                                    hasMore = pmList.hasMore;
                                    getView().loadMoreSuccess(pmList.pmList);
                                }
                            }
                        });
                break;
            case MessageFragment.TYPE_NOTIFY_POST:
                subscription = dataManager.getApi().loadNotifyPost(page, dataManager.getAccountManager().getUserMap())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<NotifyPost.NotifyPostList>() {
                            @Override
                            public void onCompleted() {
                                subscription.unsubscribe();;
                                subscription = null;
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().loadMoreFailed(e);
                                }
                            }

                            @Override
                            public void onNext(NotifyPost.NotifyPostList notifyPostList) {
                                if (isViewAttached()) {
                                    page ++;
                                    hasMore = notifyPostList.hasMore;
                                    getView().loadMoreSuccess(notifyPostList.notifyPosts);
                                }
                            }
                        });
                break;
            case MessageFragment.TYPE_NOTIFY_SYS:
                subscription = dataManager.getApi().loadNotifySys(page, dataManager.getAccountManager().getUserMap())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<NotifySys.NotifySysList>() {
                            @Override
                            public void onCompleted() {
                                subscription.unsubscribe();;
                                subscription = null;
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().loadMoreFailed(e);
                                }
                            }

                            @Override
                            public void onNext(NotifySys.NotifySysList notifySysList) {
                                if (isViewAttached()) {
                                    page ++;
                                    hasMore = notifySysList.hasMore;
                                    getView().loadMoreSuccess(notifySysList.notifySysList);
                                }
                            }
                        });
                break;

            case MessageFragment.TYPE_AT:
                subscription = dataManager.getApi().loadNotifyAt(page, dataManager.getAccountManager().getUserMap())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<NotifyPost.NotifyPostList>() {
                            @Override
                            public void onCompleted() {
                                subscription.unsubscribe();;
                                subscription = null;
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().loadNewFailed(e);
                                }
                            }

                            @Override
                            public void onNext(NotifyPost.NotifyPostList notifyPostList) {
                                if (isViewAttached()) {
                                    page ++;
                                    hasMore = notifyPostList.hasMore;
                                    getView().loadNewSuccess(notifyPostList.notifyPosts);
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void unbindView() {
        super.unbindView();
        if (subscription != null){
            subscription.unsubscribe();
            subscription = null;
        }
    }
}
