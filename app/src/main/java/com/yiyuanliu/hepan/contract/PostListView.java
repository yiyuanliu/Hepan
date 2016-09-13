package com.yiyuanliu.hepan.contract;

import com.yiyuanliu.hepan.base.MoreLoadView;
import com.yiyuanliu.hepan.data.bean.PostList;
import com.yiyuanliu.hepan.data.model.AtUserList;
import com.yiyuanliu.hepan.data.model.Rate;
import com.yiyuanliu.hepan.data.model.RateInfo;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/27.
 */
public interface PostListView extends MoreLoadView<PostList.TopicReply> {
    void loadNewSuccess(PostList.TopicContent topic);

    void replySuccess();
    void replyFailed(Throwable throwable);

    void voteSuccessful(List<PostList.TopicContent.PollInfo.PollItem> pollItemList);
    void voteFailed(Throwable throwable);

    void showAt(AtUserList atUserList);

    void rateInfoLoaded(RateInfo rateInfo);
    void rateInfoLoadedFailed(Throwable throwable);
    void rateFinished(Rate rate);
    void rateFailed(Throwable throwable);
}
