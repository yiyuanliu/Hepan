package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.data.bean.NotifyListPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyuan on 2016/7/17.
 */
public class NotifyPost {

    public int topicId;
    public String topicName;
    public String topicContent;

    public UserBase user;
    public long replyDate;
    public String replyContent;
    public String topicSubject;

    //不知道这是个干啥的，可能有用
    public int replyRemindId;

    public boolean isNew;

    public NotifyPost(NotifyListPost.BodyBean.DataBean dataBean) {
        topicId = dataBean.getTopic_id();
        topicName = dataBean.getTopic_subject();
        topicContent = dataBean.getTopic_content();
        if (topicContent != null && topicContent.lastIndexOf("\n") == topicContent.length() - 1) {
            topicContent = topicContent.substring(0, topicContent.length() - 1);
        }

        user = new UserBase(dataBean.getUser_name(), dataBean.getUser_id(), dataBean.getIcon());
        replyDate = dataBean.getReplied_date();
        replyContent = dataBean.getReply_content();
        if (replyContent != null && replyContent.lastIndexOf("\n") == replyContent.length() - 1) {
            replyContent = replyContent.substring(0, replyContent.length() - 1);
        }

        replyRemindId = dataBean.getReply_remind_id();
        topicSubject = dataBean.getTopic_subject();

        isNew = dataBean.getIs_read() == 1;
    }

    public static class NotifyPostList {

        public boolean hasMore;
        public List<NotifyPost> notifyPosts;

        public NotifyPostList(NotifyListPost notifyListPost) {
            hasMore = notifyListPost.getHas_next() == 1;

            notifyPosts = new ArrayList<>();
            for (NotifyListPost.BodyBean.DataBean dataBean:notifyListPost.getBody().getData()){
                notifyPosts.add(new NotifyPost(dataBean));
            }
        }
    }
}
