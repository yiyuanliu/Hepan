package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/29.
 */
public class UserList extends NormalBean {

    /**
     * externInfo : {"padding":""}
     */

    private BodyBean body;
    /**
     * body : {"externInfo":{"padding":""}}
     * list : [{"distance":"","location":"","is_friend":0,"isFriend":1,"isFollow":0,"uid":178886,"name":"liuchangjiang","status":0,"is_black":0,"gender":0,"icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=178886&size=middle","level":0,"lastLogin":"1465200327000","dateline":"1465200327000","signature":"","credits":4}]
     * page : 1
     * has_next : 0
     * total_num : 1
     */

    private int page;
    private int has_next;
    private int total_num;
    /**
     * distance :
     * location :
     * is_friend : 0
     * isFriend : 1
     * isFollow : 0
     * uid : 178886
     * name : liuchangjiang
     * status : 0
     * is_black : 0
     * gender : 0
     * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=178886&size=middle
     * level : 0
     * lastLogin : 1465200327000
     * dateline : 1465200327000
     * signature :
     * credits : 4
     */

    private List<ListBean> list;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
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
        private String distance;
        private String location;
        private int is_friend;
        private int isFriend;
        private int isFollow;
        private int uid;
        private String name;
        private int status;
        private int is_black;
        private int gender;
        private String icon;
        private int level;
        private String lastLogin;
        private String dateline;
        private String signature;
        private int credits;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getIs_friend() {
            return is_friend;
        }

        public void setIs_friend(int is_friend) {
            this.is_friend = is_friend;
        }

        public int getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
        }

        public int getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(int isFollow) {
            this.isFollow = isFollow;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_black() {
            return is_black;
        }

        public void setIs_black(int is_black) {
            this.is_black = is_black;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getCredits() {
            return credits;
        }

        public void setCredits(int credits) {
            this.credits = credits;
        }
    }
}
