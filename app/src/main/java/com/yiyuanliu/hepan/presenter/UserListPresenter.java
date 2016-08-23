package com.yiyuanliu.hepan.presenter;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.base.MoreLoadPresenter;
import com.yiyuanliu.hepan.base.MoreLoadView;
import com.yiyuanliu.hepan.contract.UserListView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.UserList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/7/29.
 */
public class UserListPresenter extends BasePresenter<UserListView> implements MoreLoadPresenter {
    private final String type;
    private DataManager dataManager;
    private Subscription subscription;
    private boolean hasMore;
    private int page = 1;

    public UserListPresenter(DataManager dataManager, String type){
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
        cancel();
        page = 1;
        subscription = dataManager.getApi().loadUserList(page, type, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserList>() {
                    @Override
                    public void onCompleted() {
                        cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().loadNewFailed(e);
                        }
                    }

                    @Override
                    public void onNext(UserList userList) {
                        if (isViewAttached()){
                            hasMore = userList.hasMore;
                        }
                        getView().loadNewSuccess(userList.userBaseList);
                        page ++;
                    }
                });

    }

    @Override
    public void loadMore() {
        subscription = dataManager.getApi().loadUserList(page, type, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserList>() {
                    @Override
                    public void onCompleted() {
                        cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().loadMoreFailed(e);
                        }
                    }

                    @Override
                    public void onNext(UserList userList) {
                        if (isViewAttached()){
                            hasMore = userList.hasMore;
                        }
                        getView().loadMoreSuccess(userList.userBaseList);
                        page ++;
                    }
                });
    }

    @Override
    public void unbindView() {
        super.unbindView();
        cancel();
    }

    private void cancel(){
        if (subscription != null){
            subscription.unsubscribe();
        }

        subscription = null;
    }
}
