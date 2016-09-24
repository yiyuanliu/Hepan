package com.yiyuanliu.hepan.presenter;

import android.content.Context;
import android.net.Uri;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.contract.TopicAdminView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.NormalBean;
import com.yiyuanliu.hepan.data.model.AtUserList;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/9/24.
 */

public class TopicAdminPresenter extends BasePresenter<TopicAdminView> {
    private DataManager dataManager;

    public TopicAdminPresenter(Context context) {
        dataManager = DataManager.getInstance(context);
    }

    public void doReply(int tid, int replyId, String content, List<Uri> imgList) {
        dataManager.getApi()
                .reply(tid, replyId, content, imgList, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NormalBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onSendFailed(e);
                        }
                    }

                    @Override
                    public void onNext(NormalBean normalBean) {
                        if (isViewAttached()) {
                            getView().onSendSuccessful();
                        }
                    }
                });
    }

    public void loadAtUser() {
        dataManager.getApi()
                .loadAtUser(dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AtUserList>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().loadAtUserFailed();
                        }
                    }

                    @Override
                    public void onNext(AtUserList atUserList) {
                        if (isViewAttached()) {
                            getView().showAtUserList(atUserList);
                        }
                    }
                });
    }
}
