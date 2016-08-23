package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/13.
 */
public class ForumList extends NormalBean {

    /**
     * externInfo : {"padding":""}
     */

    private BodyBean body;
    /**
     * body : {"externInfo":{"padding":""}}
     * list : [{"board_category_id":273,"board_category_name":"成电校园","board_category_type":2,"board_list":[{"board_id":174,"board_name":"就业创业","description":"点击进入→求职日历","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/bf/common_174_icon.png","last_posts_date":"1468416083000","board_content":1,"forumRedirect":"","favNum":285,"td_posts_num":223,"topic_total_num":73820,"posts_total_num":776741,"is_focus":0},{"board_id":20,"board_name":"学术交流","description":"书籍交易请到→二手书场","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/98/common_20_icon.png","last_posts_date":"1468416107000","board_content":1,"forumRedirect":"","favNum":44,"td_posts_num":41,"topic_total_num":103973,"posts_total_num":919989,"is_focus":0},{"board_id":219,"board_name":"出国留学","description":"","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/c0/common_219_icon.png","last_posts_date":"1468404987000","board_content":1,"forumRedirect":"","favNum":86,"td_posts_num":4,"topic_total_num":8459,"posts_total_num":98993,"is_focus":0},{"board_id":382,"board_name":"考试专区","description":"","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/4f/common_382_icon.png","last_posts_date":"1468416478000","board_content":1,"forumRedirect":"","favNum":3,"td_posts_num":33,"topic_total_num":2293,"posts_total_num":39050,"is_focus":0},{"board_id":326,"board_name":"新生专区","description":"<div align=\"center\"><img src=\"http://www.uestc.edu.cn/public/2014/01/01_26_20_31_39_7362.jpg\" /><\/div>","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/a6/common_326_icon.png","last_posts_date":"1468412717000","board_content":1,"forumRedirect":"","favNum":5,"td_posts_num":12,"topic_total_num":958,"posts_total_num":12682,"is_focus":0}]},{"board_category_id":95,"board_category_name":"科技交流","board_category_type":2,"board_list":[{"board_id":316,"board_name":"自然科学","description":"","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/3f/common_316_icon.png","last_posts_date":"1468402792000","board_content":1,"forumRedirect":"","favNum":6,"td_posts_num":6,"topic_total_num":423,"posts_total_num":6219,"is_focus":0},{"board_id":258,"board_name":"前端之美","description":"","board_child":0,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/50/common_258_icon.png","last_posts_date":"1468226992000","board_content":1,"forumRedirect":"","favNum":22,"td_posts_num":0,"topic_total_num":334,"posts_total_num":3420,"is_focus":0},{"board_id":211,"board_name":"科技资讯","description":"","board_child":0,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/eb/common_211_icon.png","last_posts_date":"1467967347000","board_content":1,"forumRedirect":"","favNum":5,"td_posts_num":0,"topic_total_num":9477,"posts_total_num":65921,"is_focus":0}]},{"board_category_id":203,"board_category_name":"休闲娱乐","board_category_type":2,"board_list":[{"board_id":244,"board_name":"成电骑迹","description":"","board_child":0,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/91/common_244_icon.png","last_posts_date":"1468327490000","board_content":1,"forumRedirect":"","favNum":52,"td_posts_num":0,"topic_total_num":8612,"posts_total_num":153951,"is_focus":0},{"board_id":334,"board_name":"情系舞缘","description":"","board_child":0,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/2f/common_334_icon.png","last_posts_date":"1468405360000","board_content":1,"forumRedirect":"","favNum":22,"td_posts_num":5,"topic_total_num":10185,"posts_total_num":427543,"is_focus":0},{"board_id":312,"board_name":"跑步公园","description":"","board_child":0,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/95/common_312_icon.png","last_posts_date":"1468410506000","board_content":1,"forumRedirect":"","favNum":47,"td_posts_num":10,"topic_total_num":853,"posts_total_num":26449,"is_focus":0}]},{"board_category_id":1,"board_category_name":"站务管理","board_category_type":2,"board_list":[{"board_id":2,"board_name":"站务公告","description":"","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/c8/common_2_icon.png","last_posts_date":"1468308915000","board_content":1,"forumRedirect":"","favNum":4,"td_posts_num":0,"topic_total_num":1486,"posts_total_num":14011,"is_focus":0},{"board_id":46,"board_name":"站务综合","description":"","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/d9/common_46_icon.png","last_posts_date":"1468243555000","board_content":1,"forumRedirect":"","favNum":13,"td_posts_num":0,"topic_total_num":8948,"posts_total_num":116098,"is_focus":0}]}]
     * online_user_num : 0
     * td_visitors : 0
     */

    private int online_user_num;
    private int td_visitors;
    /**
     * board_category_id : 273
     * board_category_name : 成电校园
     * board_category_type : 2
     * board_list : [{"board_id":174,"board_name":"就业创业","description":"点击进入→求职日历","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/bf/common_174_icon.png","last_posts_date":"1468416083000","board_content":1,"forumRedirect":"","favNum":285,"td_posts_num":223,"topic_total_num":73820,"posts_total_num":776741,"is_focus":0},{"board_id":20,"board_name":"学术交流","description":"书籍交易请到→二手书场","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/98/common_20_icon.png","last_posts_date":"1468416107000","board_content":1,"forumRedirect":"","favNum":44,"td_posts_num":41,"topic_total_num":103973,"posts_total_num":919989,"is_focus":0},{"board_id":219,"board_name":"出国留学","description":"","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/c0/common_219_icon.png","last_posts_date":"1468404987000","board_content":1,"forumRedirect":"","favNum":86,"td_posts_num":4,"topic_total_num":8459,"posts_total_num":98993,"is_focus":0},{"board_id":382,"board_name":"考试专区","description":"","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/4f/common_382_icon.png","last_posts_date":"1468416478000","board_content":1,"forumRedirect":"","favNum":3,"td_posts_num":33,"topic_total_num":2293,"posts_total_num":39050,"is_focus":0},{"board_id":326,"board_name":"新生专区","description":"<div align=\"center\"><img src=\"http://www.uestc.edu.cn/public/2014/01/01_26_20_31_39_7362.jpg\" /><\/div>","board_child":1,"board_img":"http://bbs.uestc.edu.cn/data/attachment/common/a6/common_326_icon.png","last_posts_date":"1468412717000","board_content":1,"forumRedirect":"","favNum":5,"td_posts_num":12,"topic_total_num":958,"posts_total_num":12682,"is_focus":0}]
     */

    private List<ListBean> list;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public int getOnline_user_num() {
        return online_user_num;
    }

    public void setOnline_user_num(int online_user_num) {
        this.online_user_num = online_user_num;
    }

    public int getTd_visitors() {
        return td_visitors;
    }

    public void setTd_visitors(int td_visitors) {
        this.td_visitors = td_visitors;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class BodyBean {
        /**
         * padding :
         */

        private ExternInfoBean externInfo;

        public ExternInfoBean getExternInfo() {
            return externInfo;
        }

        public void setExternInfo(ExternInfoBean externInfo) {
            this.externInfo = externInfo;
        }

        public static class ExternInfoBean {
            private String padding;

            public String getPadding() {
                return padding;
            }

            public void setPadding(String padding) {
                this.padding = padding;
            }
        }
    }

    public static class ListBean {
        private int board_category_id;
        private String board_category_name;
        private int board_category_type;
        /**
         * board_id : 174
         * board_name : 就业创业
         * description : 点击进入→求职日历
         * board_child : 1
         * board_img : http://bbs.uestc.edu.cn/data/attachment/common/bf/common_174_icon.png
         * last_posts_date : 1468416083000
         * board_content : 1
         * forumRedirect :
         * favNum : 285
         * td_posts_num : 223
         * topic_total_num : 73820
         * posts_total_num : 776741
         * is_focus : 0
         */

        private List<BoardListBean> board_list;

        public int getBoard_category_id() {
            return board_category_id;
        }

        public void setBoard_category_id(int board_category_id) {
            this.board_category_id = board_category_id;
        }

        public String getBoard_category_name() {
            return board_category_name;
        }

        public void setBoard_category_name(String board_category_name) {
            this.board_category_name = board_category_name;
        }

        public int getBoard_category_type() {
            return board_category_type;
        }

        public void setBoard_category_type(int board_category_type) {
            this.board_category_type = board_category_type;
        }

        public List<BoardListBean> getBoard_list() {
            return board_list;
        }

        public void setBoard_list(List<BoardListBean> board_list) {
            this.board_list = board_list;
        }

        public static class BoardListBean {
            private int board_id;
            private String board_name;
            private String description;
            private int board_child;
            private String board_img;
            private String last_posts_date;
            private int board_content;
            private String forumRedirect;
            private int favNum;
            private int td_posts_num;
            private int topic_total_num;
            private int posts_total_num;
            private int is_focus;

            public int getBoard_id() {
                return board_id;
            }

            public void setBoard_id(int board_id) {
                this.board_id = board_id;
            }

            public String getBoard_name() {
                return board_name;
            }

            public void setBoard_name(String board_name) {
                this.board_name = board_name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getBoard_child() {
                return board_child;
            }

            public void setBoard_child(int board_child) {
                this.board_child = board_child;
            }

            public String getBoard_img() {
                return board_img;
            }

            public void setBoard_img(String board_img) {
                this.board_img = board_img;
            }

            public String getLast_posts_date() {
                return last_posts_date;
            }

            public void setLast_posts_date(String last_posts_date) {
                this.last_posts_date = last_posts_date;
            }

            public int getBoard_content() {
                return board_content;
            }

            public void setBoard_content(int board_content) {
                this.board_content = board_content;
            }

            public String getForumRedirect() {
                return forumRedirect;
            }

            public void setForumRedirect(String forumRedirect) {
                this.forumRedirect = forumRedirect;
            }

            public int getFavNum() {
                return favNum;
            }

            public void setFavNum(int favNum) {
                this.favNum = favNum;
            }

            public int getTd_posts_num() {
                return td_posts_num;
            }

            public void setTd_posts_num(int td_posts_num) {
                this.td_posts_num = td_posts_num;
            }

            public int getTopic_total_num() {
                return topic_total_num;
            }

            public void setTopic_total_num(int topic_total_num) {
                this.topic_total_num = topic_total_num;
            }

            public int getPosts_total_num() {
                return posts_total_num;
            }

            public void setPosts_total_num(int posts_total_num) {
                this.posts_total_num = posts_total_num;
            }

            public int getIs_focus() {
                return is_focus;
            }

            public void setIs_focus(int is_focus) {
                this.is_focus = is_focus;
            }
        }
    }
}
