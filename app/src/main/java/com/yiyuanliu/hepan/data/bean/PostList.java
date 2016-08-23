package com.yiyuanliu.hepan.data.bean;

import android.content.Context;

import com.yiyuanliu.hepan.data.model.Topic;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/23.
 * 这个bean略复杂，而且单个topic返回的数据并不能够代表整体情况，
 * 所以手动处理
 */
public class PostList extends NormalBean {
    public int page;
    public int has_next;
    public int total_num;

    public List<TopicReply> list;
    public TopicContent topic;

    public static class TopicReply{
        public int reply_id;
        public List<Content> reply_content;
        public String reply_name;
        public int reply_posts_id;
        public int position;
        public long posts_date;
        public String icon;
        public String userTitle;
        public int is_quote;
        public String quote_content;
        public String quote_user_name;
    }

    public static class TopicContent{
        public int topic_id;
        public String title;
        public int user_id;
        public String user_nick_name;
        public int replies;
        public int hits;
        public int gender;
        public long create_date;
        public String icon;
        public String userTitle;

        public PollInfo poll_info;
        public List<Content> content;

        //还没见过不投票就可见的情况，所以我懒得管这情况了==
        public static class PollInfo{
            public long deadline;
            public int is_visible;
            //这个好像就是最大可选项
            public int type;
            //为1时已经投票，为2时没投？
            public int poll_status;
            public List<PollItem> poll_item_list;

            public static class PollItem{
                public String name;
                public int poll_item_id;
                public int total_num;
                public int totalNum;
            }
        }
    }
}
