package com.yiyuanliu.hepan.presenter;

import android.util.Log;

import com.yiyuanliu.hepan.base.BasePresenter;
import com.yiyuanliu.hepan.base.MoreLoadPresenter;
import com.yiyuanliu.hepan.contract.PostListView;
import com.yiyuanliu.hepan.data.Api;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.BaseBean;
import com.yiyuanliu.hepan.data.bean.Content;
import com.yiyuanliu.hepan.data.bean.NormalBean;
import com.yiyuanliu.hepan.data.bean.PostList;
import com.yiyuanliu.hepan.data.bean.TopicAdmin;
import com.yiyuanliu.hepan.data.bean.VoteRs;
import com.yiyuanliu.hepan.utils.HepanException;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/7/27.
 */
public class PostListPresenter extends BasePresenter<PostListView> implements MoreLoadPresenter {
    private DataManager dataManager;
    private Subscription subscription;
    private boolean hasMore;
    private int topicId;
    private int page;
    private int order;

    public PostListPresenter(DataManager dataManager, int topicId){
        this.topicId = topicId;
        this.dataManager = dataManager;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void loadNew(){
        cancel();
        page = 1;
        subscription = dataManager.getApi()
                .loadPostList(topicId, page, order, dataManager.getAccountManager().getUserMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostList>() {
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
                    public void onNext(PostList postList) {
                        if (postList.getRs() != 1){
                            throw new HepanException(postList.getErrorInfo());
                        }

                        if (isViewAttached()){
                            getView().loadNewSuccess(postList.topic);
                            getView().loadNewSuccess(postList.list);
                            hasMore = postList.has_next == 1;
                            page ++;
                        }
                    }
                });
    }

    @Override
    public void loadMore() {
        subscription = dataManager.getApi()
                .loadPostList(topicId, page, order, dataManager.getAccountManager().getUserMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostList>() {
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
                    public void onNext(PostList postList) {
                        if (isViewAttached()){
                            hasMore = postList.has_next == 1;
                            getView().loadMoreSuccess(postList.list);
                            page ++;
                        }
                    }
                });

    }

    @Override
    public boolean isLoading() {
        return subscription != null && !subscription.isUnsubscribed();
    }


    public void replyTopic(int replyId, String content) {
        TopicAdmin topicAdmin = new TopicAdmin();
        topicAdmin.getBody().getJson().setTid(topicId);
        topicAdmin.getBody().getJson().setReplyId(replyId);
        if (replyId != 0){
            topicAdmin.getBody().getJson().setIsQuote(1);
        }

        Content content1 = new Content();
        content1.type = Content.TYPE_NORMAL;
        content1.infor = content;

        topicAdmin.getBody().getJson().addContent(content1);

        dataManager.getApi().getWebApi()
                .topicAdmin(Api.TOPIC_ADMIN_REPLY, topicAdmin, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NormalBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().replyFailed(e);
                        }
                    }

                    @Override
                    public void onNext(NormalBean baseBean) {
                        page --;
                        hasMore = true;
                        if (isViewAttached()){
                            HepanException.detectRespon(baseBean);
                            getView().replySuccess();
                        }
                    }
                });
    }

    public void vote(List<Integer> integerList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer integer:integerList) {
            stringBuilder.append(integer);
            stringBuilder.append(",");
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());

        dataManager.getApi().getWebApi()
                .vote(topicId, stringBuilder.toString(), dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<VoteRs>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().voteFailed(e);
                        }
                    }

                    @Override
                    public void onNext(VoteRs voteRs) {
                        if (isViewAttached()) {
                            HepanException.detectRespon(voteRs);
                            getView().voteSuccessful(voteRs.vote_rs);
                        }
                    }
                });
    }

    public void cancel(){
        if (isLoading()){
            subscription.unsubscribe();
        }

        subscription = null;
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public void unbindView() {
        super.unbindView();
        cancel();
    }

}
