package com.yiyuanliu.hepan.data.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyuan on 2016/8/3.
 */
public class TopicAdmin {

    /**
     * json : {"fid":123,"tid":123456,"typeOption":"","isAnonymous":1,"isOnlyAuthor":1,"typeId":1234,"isQuote":1,"replyId":123456,"title":"Title","aid":"1,2,3","contentList":"又是一个 JSON 字符串，格式见下面。","location":"TODO: 格式待确认"}
     */

    private BodyBean body = new BodyBean();

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        /**
         * fid : 123
         * tid : 123456
         * typeOption :
         * isAnonymous : 1
         * isOnlyAuthor : 1
         * typeId : 1234
         * isQuote : 1
         * replyId : 123456
         * title : Title
         * aid : 1,2,3
         * contentList : 又是一个 JSON 字符串，格式见下面。
         * location : TODO: 格式待确认
         */

        private JsonBean json = new JsonBean();

        public JsonBean getJson() {
            return json;
        }

        public void setJson(JsonBean json) {
            this.json = json;
        }

        public static class JsonBean {
            private int fid;
            private int tid;
            private String typeOption;
            private int isAnonymous;
            private int isOnlyAuthor;
            private int typeId;
            private int isQuote;
            private int replyId;
            private String title;
            private String aid;
            private String location;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            private String content;

            @Expose
            private List<Content> contentList = new ArrayList<>();

            public int getFid() {
                return fid;
            }

            public void setFid(int fid) {
                this.fid = fid;
            }

            public int getTid() {
                return tid;
            }

            public void setTid(int tid) {
                this.tid = tid;
            }

            public String getTypeOption() {
                return typeOption;
            }

            public void setTypeOption(String typeOption) {
                this.typeOption = typeOption;
            }

            public int getIsAnonymous() {
                return isAnonymous;
            }

            public void setIsAnonymous(int isAnonymous) {
                this.isAnonymous = isAnonymous;
            }

            public int getIsOnlyAuthor() {
                return isOnlyAuthor;
            }

            public void setIsOnlyAuthor(int isOnlyAuthor) {
                this.isOnlyAuthor = isOnlyAuthor;
            }

            public int getTypeId() {
                return typeId;
            }

            public void setTypeId(int typeId) {
                this.typeId = typeId;
            }

            public int getIsQuote() {
                return isQuote;
            }

            public void setIsQuote(int isQuote) {
                this.isQuote = isQuote;
            }

            public int getReplyId() {
                return replyId;
            }

            public void setReplyId(int replyId) {
                this.replyId = replyId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAid() {
                return aid;
            }

            public void setAid(String aid) {
                this.aid = aid;
            }

            public List<Content> getContentList() {
                return contentList;
            }

            public void setContentList(List<Content> contentList) {
                this.contentList = contentList;
            }

            public void addContent(Content content){
                this.contentList.add(content);
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }
        }
    }

    @Override
    public String toString(){
        Gson gson = new Gson();
        String str = gson.toJson(getBody().getJson().getContentList());
        gson = new Gson();
        getBody().getJson().content = str;
        return gson.toJson(this);
    }

}
