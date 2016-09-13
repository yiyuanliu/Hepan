package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/8/20.
 */
public class HeartBeat extends NormalBean {

    public Body body;

    public static class Body {
        public HeartInfoModel atMeInfo;
        public List<HeartMsgModel> pmInfos;
        public HeartInfoModel replyInfo;
        public HeartInfoModel systemInfo;

        public class HeartInfoModel {
            public int count;
            public long time;
        }

        public class HeartMsgModel {
            public long fromUid;
            public int plid;
            public int pmid;
            public long time;
        }
    }

}
