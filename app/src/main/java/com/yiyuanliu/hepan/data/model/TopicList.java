package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.data.bean.NewsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyuan on 2016/7/13.
 */
public class TopicList {
    public List<Topic> topicList = new ArrayList<>();
    public boolean hasMore;

    public TopicList(com.yiyuanliu.hepan.data.bean.TopicList topicBean){
        hasMore = topicBean.getHas_next() == 1;
        for (com.yiyuanliu.hepan.data.bean.TopicList.ListBean item:topicBean.getList()){
            topicList.add(new Topic(item));
        }
    }

    public TopicList(NewsList newsList) {
        hasMore = newsList.getHas_next() == 1;
        for (NewsList.ListBean listBean:newsList.getList()){
            topicList.add(new Topic(listBean));
        }
    }
}
