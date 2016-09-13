package com.yiyuanliu.hepan.data.bean;

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

        public List<TopicContent.ManagePanelBean> managePanel;
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
        /**
         * action : http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/support&sdkVersion=2.4.2.0&accessToken=576820808990db8dedc5f8960c8ae&accessSecret=2927d71a3f2cda9e756e10ecfa7fe&apphash=&tid=1624643&pid=29044611&type=post
         * title : 支持
         * recommendAdd :
         * extParams : {"beforeAction":"","recommendAdd":0,"isHasRecommendAdd":0}
         * type : support
         */

        public List<ExtraPanelBean> extraPanel;
        /**
         * action : http://bbs.uestc.edu.cn/forum.php?mod=post&action=edit&fid=48&tid=1623022&pid=29029141
         * title : 编辑
         * type : edit
         */

        public List<ManagePanelBean> managePanel;

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

        public static class ExtraPanelBean {
            public String action;
            public String title;
            public ExtParamsBean extParams;
            public String type;

            public static class ExtParamsBean {
                private String beforeAction;
                private int recommendAdd;
                private int isHasRecommendAdd;
            }
        }

        public static class ManagePanelBean {
            public String action;
            public String title;
            public String type;

        }
    }
}
