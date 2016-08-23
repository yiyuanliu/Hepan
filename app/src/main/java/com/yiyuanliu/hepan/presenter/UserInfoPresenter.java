package com.yiyuanliu.hepan.presenter;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.contract.UserInfoView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.data.model.UserInfo;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/8/8.
 */
public class UserInfoPresenter extends BasePresenter<UserInfoView> {
    private DataManager dataManager;

    public UserInfoPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void load(UserBase userBase) {
        dataManager.getApi().loadUserInfo(userBase, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onLoadFailed(e);
                        }
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        if (isViewAttached()){
                            getView().onLoaded(userInfo);
                        }
                    }
                });
    }
}
