package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/17.
 */
public class NotifyListSys extends NormalBean {

    /**
     * externInfo : {"padding":""}
     * data : [{"replied_date":"1465346885000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"您在主题 【2015成电杰出学子（研究生）经验分享楼】（持续更新） 的帖子被 采妹纸的蘑菇菌 评分 水滴 -10 滴 啥也没有啊对此如有疑问或意见请联系 采妹纸的蘑菇菌 或在 站务综合提出。","is_read":"0"},{"replied_date":"1447073011000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"您的用户组升级为 河蟹 (Lv.3)   看看我能做什么 \u203a","is_read":"0"}]
     */

    private BodyBean body;
    /**
     * body : {"externInfo":{"padding":""},"data":[{"replied_date":"1465346885000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"您在主题 【2015成电杰出学子（研究生）经验分享楼】（持续更新） 的帖子被 采妹纸的蘑菇菌 评分 水滴 -10 滴 啥也没有啊对此如有疑问或意见请联系 采妹纸的蘑菇菌 或在 站务综合提出。","is_read":"0"},{"replied_date":"1447073011000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"您的用户组升级为 河蟹 (Lv.3)   看看我能做什么 \u203a","is_read":"0"}]}
     * page : 1
     * has_next : 0
     * total_num : 2
     */

    private int page;
    private int has_next;
    private int total_num;

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

    public static class BodyBean {
        /**
         * padding :
         */

        private ExternInfoBean externInfo;
        /**
         * replied_date : 1465346885000
         * type : system
         * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle
         * user_name : 系统
         * user_id : 0
         * mod : admin
         * note : 您在主题 【2015成电杰出学子（研究生）经验分享楼】（持续更新） 的帖子被 采妹纸的蘑菇菌 评分 水滴 -10 滴 啥也没有啊对此如有疑问或意见请联系 采妹纸的蘑菇菌 或在 站务综合提出。
         * is_read : 0
         */

        private List<DataBean> data;

        public ExternInfoBean getExternInfo() {
            return externInfo;
        }

        public void setExternInfo(ExternInfoBean externInfo) {
            this.externInfo = externInfo;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
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

        public static class DataBean {
            private long replied_date;
            private String type;
            private String icon;
            private String user_name;
            private int user_id;
            private String mod;
            private String note;
            private int is_read;
            public List<Action> actions;

            public long getReplied_date() {
                return replied_date;
            }

            public void setReplied_date(long replied_date) {
                this.replied_date = replied_date;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getMod() {
                return mod;
            }

            public void setMod(String mod) {
                this.mod = mod;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public int getIs_read() {
                return is_read;
            }

            public void setIs_read(int is_read) {
                this.is_read = is_read;
            }

            public static class Action {
                public String redirect;
                public String title;

                @Override
                public String toString() {
                    return title;
                }
            }
        }
    }
}
