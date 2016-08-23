package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/30.
 */
public class AttachmentRs extends NormalBean {
    public BodyBean body;

    public static class BodyBean{
        public List<Attachment> attachment;

        public static class Attachment{
            public int id;
            public String urlName;
        }
    }
}
