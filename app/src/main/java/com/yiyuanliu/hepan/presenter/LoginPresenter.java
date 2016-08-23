package com.yiyuanliu.hepan.presenter;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.contract.LoginView;
import com.yiyuanliu.hepan.data.DataManager;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/7/22.
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    private DataManager dataManager;

    public LoginPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    public void login(String username, String password){
        dataManager.getApi().userLogin(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().showError(true);
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (isViewAttached()){
                            if (aBoolean){
                                getView().showSuccess();
                            } else {
                                getView().showError(aBoolean);
                            }
                        }
                    }
                });
    }
}
