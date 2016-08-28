package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/6/2.
 */
public class AtUser extends NormalBean {

    /**
     * externInfo : {"padding":""}
     */

    private BodyBean body;
    /**
     * body : {"externInfo":{"padding":""}}
     * list : [{"uid":178886,"name":"liuchangjiang","role_num":2},{"uid":134740,"name":"微软技术俱乐部","role_num":6}]
     * page : 1
     * has_next : 0
     * total_num : 2
     */

    private int page;
    private int has_next;
    private int total_num;
    /**
     * uid : 178886
     * name : liuchangjiang
     * role_num : 2
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
        private int uid;
        private String name;
        private int role_num;

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

        public int getRole_num() {
            return role_num;
        }

        public void setRole_num(int role_num) {
            this.role_num = role_num;
        }
    }
}

