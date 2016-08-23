package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/17.
 */
public class PmSession extends NormalBean {

    /**
     * externInfo : {"padding":""}
     * list : [{"plid":3978029,"pmid":3978029,"lastUserId":175143,"lastUserName":"禾禾呵","lastSummary":"好久没水河畔，才看到","lastDateline":"1468574768000","toUserId":185372,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185372&size=middle","toUserName":"巴格威尔","toUserIsBlack":0,"isNew":0}]
     * hasNext : 0
     * count : 1
     */

    public BodyBean body;

    public static class BodyBean {
        /**
         * padding :
         */

        public ExternInfoBean externInfo;
        public int hasNext;
        public int count;
        /**
         * plid : 3978029
         * pmid : 3978029
         * lastUserId : 175143
         * lastUserName : 禾禾呵
         * lastSummary : 好久没水河畔，才看到
         * lastDateline : 1468574768000
         * toUserId : 185372
         * toUserAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185372&size=middle
         * toUserName : 巴格威尔
         * toUserIsBlack : 0
         * isNew : 0
         */

        public List<ListBean> list;

        public static class ExternInfoBean {
            public String padding;
        }

        public static class ListBean {
            public int plid;
            public int pmid;
            public int lastUserId;
            public String lastUserName;
            public String lastSummary;
            public long lastDateline;
            public int toUserId;
            public String toUserAvatar;
            public String toUserName;
            public int toUserIsBlack;
            public int isNew;
        }
    }
}
