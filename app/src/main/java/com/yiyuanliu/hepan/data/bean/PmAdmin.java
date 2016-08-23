package com.yiyuanliu.hepan.data.bean;

import com.google.gson.Gson;

/**
 * Created by yiyuan on 2016/8/20.
 */
public class PmAdmin {

    /**
     * action : send
     * toUid : 185372
     * plid : 3978029
     * pmid : 3978029
     * msg : {"type":"text","content":"test"}
     */

    private String action;
    private int toUid;
    private int plid;
    private int pmid;
    /**
     * type : text
     * content : test
     */

    private MsgBean msg;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }

    public int getPmid() {
        return pmid;
    }

    public void setPmid(int pmid) {
        this.pmid = pmid;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String type;
        private String content;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
