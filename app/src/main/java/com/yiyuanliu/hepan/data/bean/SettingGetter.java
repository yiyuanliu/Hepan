package com.yiyuanliu.hepan.data.bean;

import com.google.gson.Gson;

/**
 * Created by yiyuan on 2016/5/27.
 */
public class SettingGetter {

    public SettingGetter(){
        body = new BodyBean();
    }

    /**
     * postInfo : {"forumIds":"0"}
     */

    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        /**
         * forumIds : 0
         */

        private PostInfoBean postInfo = new PostInfoBean();

        public PostInfoBean getPostInfo() {
            return postInfo;
        }

        public void setPostInfo(PostInfoBean postInfo) {
            this.postInfo = postInfo;
        }

        public static class PostInfoBean {
            private int forumIds;

            public int getForumIds() {
                return forumIds;
            }

            public void setForumIds(int forumIds) {
                this.forumIds = forumIds;
            }
        }
    }

    @Override
    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

