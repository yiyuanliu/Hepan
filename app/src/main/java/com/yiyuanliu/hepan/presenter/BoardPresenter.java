package com.yiyuanliu.hepan.presenter;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.contract.BoardView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.Forum;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/7/25.
 */
public class BoardPresenter extends BasePresenter<BoardView> {
    private DataManager dataManager;
    public BoardPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    public void load(Forum.Board board){
        dataManager.getApi().loadBoard(board, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Forum.Board>() {
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
                    public void onNext(Forum.Board board) {
                        if (isViewAttached()){
                            getView().showBoard(board);
                        }
                    }
                });
    }
}
