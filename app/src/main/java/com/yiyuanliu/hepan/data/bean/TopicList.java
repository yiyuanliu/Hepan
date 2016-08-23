package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/13.
 */
public class TopicList extends NormalBean {

    /**
     * externInfo : {"padding":""}
     */

    private BodyBean body;
    /**
     * body : {"externInfo":{"padding":""}}
     * newTopicPanel : [{"type":"normal","action":"","title":"发表帖子"}]
     * classificationTop_list : []
     * classificationType_list : []
     * isOnlyTopicType : 0
     * anno_list : []
     * list : [{"board_id":61,"board_name":"二手专区","topic_id":1618573,"type":"normal","title":"出一台办公用主机","user_id":125402,"user_nick_name":"一路小跑sss","userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=125402&size=middle","last_reply_date":"1468340118000","vote":0,"hot":0,"hits":12,"replies":1,"essence":0,"top":0,"status":0,"subject":"从师兄那里收的，只能看看网页，办办公。150。另外收一台","pic_path":"","ratio":"1","gender":0,"recommendAdd":0,"special":0,"isHasRecommendAdd":0,"imageList":[],"sourceWebUrl":"http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1618573"}]
     * page : 1
     * has_next : 1
     * total_num : 1474726
     */

    private int isOnlyTopicType;
    private int page;
    private int has_next;
    private int total_num;
    /**
     * type : normal
     * action :
     * title : 发表帖子
     */

    private List<NewTopicPanelBean> newTopicPanel;
    private List<?> classificationTop_list;
    private List<?> classificationType_list;
    private List<?> anno_list;
    /**
     * board_id : 61
     * board_name : 二手专区
     * topic_id : 1618573
     * type : normal
     * title : 出一台办公用主机
     * user_id : 125402
     * user_nick_name : 一路小跑sss
     * userAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=125402&size=middle
     * last_reply_date : 1468340118000
     * vote : 0
     * hot : 0
     * hits : 12
     * replies : 1
     * essence : 0
     * top : 0
     * status : 0
     * subject : 从师兄那里收的，只能看看网页，办办公。150。另外收一台
     * pic_path :
     * ratio : 1
     * gender : 0
     * recommendAdd : 0
     * special : 0
     * isHasRecommendAdd : 0
     * imageList : []
     * sourceWebUrl : http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1618573
     */

    private List<ListBean> list;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public int getIsOnlyTopicType() {
        return isOnlyTopicType;
    }

    public void setIsOnlyTopicType(int isOnlyTopicType) {
        this.isOnlyTopicType = isOnlyTopicType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getHas_next() {
        return has_next;
    }

    public void setHas_next(int has_next) {
        this.has_next = has_next;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public List<NewTopicPanelBean> getNewTopicPanel() {
        return newTopicPanel;
    }

    public void setNewTopicPanel(List<NewTopicPanelBean> newTopicPanel) {
        this.newTopicPanel = newTopicPanel;
    }

    public List<?> getClassificationTop_list() {
        return classificationTop_list;
    }

    public void setClassificationTop_list(List<?> classificationTop_list) {
        this.classificationTop_list = classificationTop_list;
    }

    public List<?> getClassificationType_list() {
        return classificationType_list;
    }

    public void setClassificationType_list(List<?> classificationType_list) {
        this.classificationType_list = classificationType_list;
    }

    public List<?> getAnno_list() {
        return anno_list;
    }

    public void setAnno_list(List<?> anno_list) {
        this.anno_list = anno_list;
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

    public static class NewTopicPanelBean {
        private String type;
        private String action;
        private String title;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class ListBean {
        private int board_id;
        private String board_name;
        private int topic_id;
        private String type;
        private String title;
        private int user_id;
        private String user_nick_name;
        private String userAvatar;
        private long last_reply_date;
        private int vote;
        private int hot;
        private int hits;
        private int replies;
        private int essence;
        private int top;
        private int status;
        private String subject;
        //private String pic_path;
        private String ratio;
        private int gender;
        private int recommendAdd;
        private int special;
        private int isHasRecommendAdd;
        private String sourceWebUrl;
        private List<?> imageList;

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

        public int getTopic_id() {
            return topic_id;
        }

        public void setTopic_id(int topic_id) {
            this.topic_id = topic_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_nick_name() {
            return user_nick_name;
        }

        public void setUser_nick_name(String user_nick_name) {
            this.user_nick_name = user_nick_name;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public long getLast_reply_date() {
            return last_reply_date;
        }

        public void setLast_reply_date(long last_reply_date) {
            this.last_reply_date = last_reply_date;
        }

        public int getVote() {
            return vote;
        }

        public void setVote(int vote) {
            this.vote = vote;
        }

        public int getHot() {
            return hot;
        }

        public void setHot(int hot) {
            this.hot = hot;
        }

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }

        public int getReplies() {
            return replies;
        }

        public void setReplies(int replies) {
            this.replies = replies;
        }

        public int getEssence() {
            return essence;
        }

        public void setEssence(int essence) {
            this.essence = essence;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

//        public String getPic_path() {
//            return pic_path;
//        }
//
//        public void setPic_path(String pic_path) {
//            this.pic_path = pic_path;
//        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getRecommendAdd() {
            return recommendAdd;
        }

        public void setRecommendAdd(int recommendAdd) {
            this.recommendAdd = recommendAdd;
        }

        public int getSpecial() {
            return special;
        }

        public void setSpecial(int special) {
            this.special = special;
        }

        public int getIsHasRecommendAdd() {
            return isHasRecommendAdd;
        }

        public void setIsHasRecommendAdd(int isHasRecommendAdd) {
            this.isHasRecommendAdd = isHasRecommendAdd;
        }

        public String getSourceWebUrl() {
            return sourceWebUrl;
        }

        public void setSourceWebUrl(String sourceWebUrl) {
            this.sourceWebUrl = sourceWebUrl;
        }

        public List<?> getImageList() {
            return imageList;
        }

        public void setImageList(List<?> imageList) {
            this.imageList = imageList;
        }
    }
}
