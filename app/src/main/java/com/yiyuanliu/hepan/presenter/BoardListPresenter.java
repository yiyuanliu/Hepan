package com.yiyuanliu.hepan.presenter;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.contract.BoardListView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.Forum;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/7/25.
 */
public class BoardListPresenter extends BasePresenter<BoardListView> {
    private DataManager dataManager;

    public BoardListPresenter(DataManager data){
        this.dataManager = data;
    }

    public void load() {
        dataManager.getApi()
                .loadForum(dataManager.getAccountManager().getUserMap(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Forum>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().showError(e);
                        }
                    }

                    @Override
                    public void onNext(Forum forum) {
                        if (isViewAttached()){
                            getView().showBoardList(forum);
                        }
                    }
                });
    }
}
