package com.yiyuanliu.hepan.data.bean;

import java.util.List;

/**
 * Created by yiyuan on 2016/8/19.
 */
public class PmList extends NormalBean {

    /**
     * externInfo : {"padding":""}
     * userInfo : {"uid":175143,"name":"禾禾呵","avatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=175143&size=small"}
     * pmList : [{"fromUid":185372,"name":"巴格威尔","avatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185372&size=small","msgList":[{"sender":185372,"mid":4161140,"content":" 关于您在\u201cGSON解析求助\u201d的帖子 http://bbs.uestc.edu.cn/forum.php?mod=redirect&goto=findpost&pid=28949782&ptid=1617647 \nHI，你好！我是河畔上JSON发帖求助的，能加你QQ吗，河畔不太会用，不够方便，我的Q43990740","type":"text","time":"1467734936000"},{"sender":175143,"mid":4162736,"content":"[mobcent_phiz=http://bbs.uestc.edu.cn/mobcent/app/data/phiz/default/31.png][mobcent_phiz=http://bbs.uestc.edu.cn/mobcent/app/data/phiz/default/31.png]好久没水河畔，才看到","type":"text","time":"1468574768000"}],"plid":3978029,"hasPrev":0}]
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
         * padding :
         */

        private ExternInfoBean externInfo;
        /**
         * uid : 175143
         * name : 禾禾呵
         * avatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=175143&size=small
         */

        private UserInfoBean userInfo;
        /**
         * fromUid : 185372
         * name : 巴格威尔
         * avatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185372&size=small
         * msgList : [{"sender":185372,"mid":4161140,"content":" 关于您在\u201cGSON解析求助\u201d的帖子 http://bbs.uestc.edu.cn/forum.php?mod=redirect&goto=findpost&pid=28949782&ptid=1617647 \nHI，你好！我是河畔上JSON发帖求助的，能加你QQ吗，河畔不太会用，不够方便，我的Q43990740","type":"text","time":"1467734936000"},{"sender":175143,"mid":4162736,"content":"[mobcent_phiz=http://bbs.uestc.edu.cn/mobcent/app/data/phiz/default/31.png][mobcent_phiz=http://bbs.uestc.edu.cn/mobcent/app/data/phiz/default/31.png]好久没水河畔，才看到","type":"text","time":"1468574768000"}]
         * plid : 3978029
         * hasPrev : 0
         */

        private List<PmListBean> pmList;

        public ExternInfoBean getExternInfo() {
            return externInfo;
        }

        public void setExternInfo(ExternInfoBean externInfo) {
            this.externInfo = externInfo;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public List<PmListBean> getPmList() {
            return pmList;
        }

        public void setPmList(List<PmListBean> pmList) {
            this.pmList = pmList;
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

        public static class UserInfoBean {
            private int uid;
            private String name;
            private String avatar;

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

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }

        public static class PmListBean {
            private int fromUid;
            private String name;
            private String avatar;
            private int plid;
            private int hasPrev;
            /**
             * sender : 185372
             * mid : 4161140
             * content :  关于您在“GSON解析求助”的帖子 http://bbs.uestc.edu.cn/forum.php?mod=redirect&goto=findpost&pid=28949782&ptid=1617647
             HI，你好！我是河畔上JSON发帖求助的，能加你QQ吗，河畔不太会用，不够方便，我的Q43990740
             * type : text
             * time : 1467734936000
             */

            private List<MsgListBean> msgList;

            public int getFromUid() {
                return fromUid;
            }

            public void setFromUid(int fromUid) {
                this.fromUid = fromUid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getPlid() {
                return plid;
            }

            public void setPlid(int plid) {
                this.plid = plid;
            }

            public int getHasPrev() {
                return hasPrev;
            }

            public void setHasPrev(int hasPrev) {
                this.hasPrev = hasPrev;
            }

            public List<MsgListBean> getMsgList() {
                return msgList;
            }

            public void setMsgList(List<MsgListBean> msgList) {
                this.msgList = msgList;
            }

            public static class MsgListBean {
                private int sender;
                private int mid;
                private String content;
                private String type;
                private long time;

                public int getSender() {
                    return sender;
                }

                public void setSender(int sender) {
                    this.sender = sender;
                }

                public int getMid() {
                    return mid;
                }

                public void setMid(int mid) {
                    this.mid = mid;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public long getTime() {
                    return time;
                }

                public void setTime(long time) {
                    this.time = time;
                }
            }
        }
    }
}
