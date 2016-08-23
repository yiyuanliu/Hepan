package com.yiyuanliu.hepan.data.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by yiyuan on 2016/8/19.
 */
public class PmListJson {


    /**
     * pmInfos : [{"fromUid":185372,"startTime":"0","stopTime":"0","cacheCount":0,"pmLimit":10,"plid":3978029,"pmid":3978029}]
     * externInfo : {"onlyFromUid":0}
     */

    private BodyBean body;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        /**
         * onlyFromUid : 0
         */

        private ExternInfoBean externInfo;
        /**
         * fromUid : 185372
         * startTime : 0
         * stopTime : 0
         * cacheCount : 0
         * pmLimit : 10
         * plid : 3978029
         * pmid : 3978029
         */

        private List<PmInfosBean> pmInfos;

        public ExternInfoBean getExternInfo() {
            return externInfo;
        }

        public void setExternInfo(ExternInfoBean externInfo) {
            this.externInfo = externInfo;
        }

        public List<PmInfosBean> getPmInfos() {
            return pmInfos;
        }

        public void setPmInfos(List<PmInfosBean> pmInfos) {
            this.pmInfos = pmInfos;
        }

        public static class ExternInfoBean {
            private int onlyFromUid;

            public int getOnlyFromUid() {
                return onlyFromUid;
            }

            public void setOnlyFromUid(int onlyFromUid) {
                this.onlyFromUid = onlyFromUid;
            }
        }

        public static class PmInfosBean {
            private int fromUid;
            private long startTime;
            private long stopTime;
            private int cacheCount;
            private int pmLimit;
            private int plid;
            private int pmid;

            public int getFromUid() {
                return fromUid;
            }

            public void setFromUid(int fromUid) {
                this.fromUid = fromUid;
            }

            public long getStartTime() {
                return startTime;
            }

            public void setStartTime(long startTime) {
                this.startTime = startTime;
            }

            public long getStopTime() {
                return stopTime;
            }

            public void setStopTime(long stopTime) {
                this.stopTime = stopTime;
            }

            public int getCacheCount() {
                return cacheCount;
            }

            public void setCacheCount(int cacheCount) {
                this.cacheCount = cacheCount;
            }

            public int getPmLimit() {
                return pmLimit;
            }

            public void setPmLimit(int pmLimit) {
                this.pmLimit = pmLimit;
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
        }
    }
}
