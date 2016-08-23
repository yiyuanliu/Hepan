package com.yiyuanliu.hepan.presenter;

import android.net.Uri;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.contract.NewTopicView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.NormalBean;
import com.yiyuanliu.hepan.data.bean.SettingRs;
import com.yiyuanliu.hepan.data.model.Forum;

import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/7/29.
 */
public class NewTopicPresenter extends BasePresenter<NewTopicView> {
    private DataManager dataManager;

    public NewTopicPresenter(DataManager instance) {
        dataManager = instance;
    }

    public void load(){
        dataManager.getApi().loadForum(dataManager.getAccountManager().getUserMap(), false)
                .withLatestFrom(dataManager.getApi().loadSetting(dataManager.getAccountManager().getUserMap()),
                        new Func2<Forum, SettingRs, Data>() {
                            @Override
                            public Data call(Forum forum, SettingRs settingRs) {
                                Data data = new Data();
                                data.forum = forum;
                                data.settingRs = settingRs;
                                return data;
                            }
                        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Data>() {
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
                    public void onNext(Data data) {
                        if (isViewAttached()) {
                            getView().onLoaded(data.forum, data.settingRs);
                        }
                    }
                });
    }

    public void doSend(int fid, int classificationId, String title, String str, List<Uri> uriList){
        dataManager.getApi().newTopic(fid, classificationId, title, str, uriList,
                dataManager.getAccountManager().getUserMap())
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

    public static class Data{
        public SettingRs settingRs;
        public Forum forum;
    }

}
