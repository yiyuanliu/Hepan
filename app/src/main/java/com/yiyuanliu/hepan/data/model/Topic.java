package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.data.bean.NewsList;
import com.yiyuanliu.hepan.data.bean.TopicList;

import java.io.Serializable;

/**
 * Created by yiyuan on 2016/7/10.
 */
public class Topic implements Serializable {
    public int topicId;
    public String title;
    public String content;
    public String board;
    public long time;

    public UserBase userBase;

    public Topic(TopicList.ListBean item) {
        topicId = item.getTopic_id();
        title = item.getTitle();
        content = item.getSubject();
        time = item.getLast_reply_date();

        board = item.getBoard_name();

        userBase = new UserBase(item);
    }

    public Topic(NewsList.ListBean listBean) {
        topicId = listBean.getSource_id();
        title = listBean.getTitle();
        content = listBean.getSummary();
        time = listBean.getLast_reply_date();

        board = listBean.getBoard_name();

        userBase = new UserBase(listBean.getUser_nick_name(), listBean.getUser_id(), listBean.getUserAvatar());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Topic){
            return ((Topic) o).topicId == topicId;
        } else {
            return false;
        }
    }
}
