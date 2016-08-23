package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/19.
 */
public class UserInfo extends NormalBean {

    /**
     * externInfo : {"padding":""}
     * repeatList : []
     * profileList : [{"type":"gender","title":"性别","data":"保密"},{"type":"birthday","title":"生日","data":""}]
     * creditList : [{"type":"credits","title":"积分","data":0},{"type":"extcredits1","title":"威望","data":0},{"type":"extcredits2","title":"水滴","data":0},{"type":"extcredits6","title":"奖励券","data":0}]
     * creditShowList : [{"type":"credits","title":"积分","data":0},{"type":"extcredits2","title":"水滴","data":0}]
     */

    private BodyBean body;
    /**
     * body : {"externInfo":{"padding":""},"repeatList":[],"profileList":[{"type":"gender","title":"性别","data":"保密"},{"type":"birthday","title":"生日","data":""}],"creditList":[{"type":"credits","title":"积分","data":0},{"type":"extcredits1","title":"威望","data":0},{"type":"extcredits2","title":"水滴","data":0},{"type":"extcredits6","title":"奖励券","data":0}],"creditShowList":[{"type":"credits","title":"积分","data":0},{"type":"extcredits2","title":"水滴","data":0}]}
     * flag : 0
     * is_black : 0
     * is_follow : 0
     * isFriend : 0
     * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185373&size=middle
     * level_url :
     * name : huangjunwei
     * email : 522301112@qq.com
     * status : 0
     * gender : 0
     * score : 0
     * credits : 0
     * gold_num : 0
     * topic_num : 0
     * photo_num : 0
     * reply_posts_num : 0
     * essence_num : 0
     * friend_num : 0
     * follow_num : 0
     * level : 0
     * sign :
     * userTitle : 蝌蚪 (Lv.1)
     * info : []
     */

    private int flag;
    private int is_black;
    private int is_follow;
    private int isFriend;
    private String icon;
    private String level_url;
    private String name;
    private String email;
    private int status;
    private int gender;
    private int score;
    private int credits;
    private int gold_num;
    private int topic_num;
    private int photo_num;
    private int reply_posts_num;
    private int essence_num;
    private int friend_num;
    private int follow_num;
    private int level;
    private String sign;
    private String userTitle;
    private List<?> info;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getIs_black() {
        return is_black;
    }

    public void setIs_black(int is_black) {
        this.is_black = is_black;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLevel_url() {
        return level_url;
    }

    public void setLevel_url(String level_url) {
        this.level_url = level_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getGold_num() {
        return gold_num;
    }

    public void setGold_num(int gold_num) {
        this.gold_num = gold_num;
    }

    public int getTopic_num() {
        return topic_num;
    }

    public void setTopic_num(int topic_num) {
        this.topic_num = topic_num;
    }

    public int getPhoto_num() {
        return photo_num;
    }

    public void setPhoto_num(int photo_num) {
        this.photo_num = photo_num;
    }

    public int getReply_posts_num() {
        return reply_posts_num;
    }

    public void setReply_posts_num(int reply_posts_num) {
        this.reply_posts_num = reply_posts_num;
    }

    public int getEssence_num() {
        return essence_num;
    }

    public void setEssence_num(int essence_num) {
        this.essence_num = essence_num;
    }

    public int getFriend_num() {
        return friend_num;
    }

    public void setFriend_num(int friend_num) {
        this.friend_num = friend_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public List<?> getInfo() {
        return info;
    }

    public void setInfo(List<?> info) {
        this.info = info;
    }

    public static class BodyBean {
        /**
         * padding :
         */

        private ExternInfoBean externInfo;
        private List<?> repeatList;
        /**
         * type : gender
         * title : 性别
         * data : 保密
         */

        private List<InfoItemBean> profileList;
        /**
         * type : credits
         * title : 积分
         * data : 0
         */

        private List<InfoItemBean> creditList;
        /**
         * type : credits
         * title : 积分
         * data : 0
         */

        private List<InfoItemBean> creditShowList;

        public ExternInfoBean getExternInfo() {
            return externInfo;
        }

        public void setExternInfo(ExternInfoBean externInfo) {
            this.externInfo = externInfo;
        }

        public List<?> getRepeatList() {
            return repeatList;
        }

        public void setRepeatList(List<?> repeatList) {
            this.repeatList = repeatList;
        }

        public List<InfoItemBean> getProfileList() {
            return profileList;
        }

        public void setProfileList(List<InfoItemBean> profileList) {
            this.profileList = profileList;
        }

        public List<InfoItemBean> getCreditList() {
            return creditList;
        }

        public void setCreditList(List<InfoItemBean> creditList) {
            this.creditList = creditList;
        }

        public List<InfoItemBean> getCreditShowList() {
            return creditShowList;
        }

        public void setCreditShowList(List<InfoItemBean> creditShowList) {
            this.creditShowList = creditShowList;
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

        public static class InfoItemBean {
            private String type;
            private String title;
            private String data;

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

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }
        }

    }
}
