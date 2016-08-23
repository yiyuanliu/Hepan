package com.yiyuanliu.hepan.data.bean;

/**
 * Created by yiyuan on 2016/7/11.
 */
public class NormalBean extends BaseBean {
    private HeadBean head;

    public static class HeadBean{
        public String getErrorCode() {
            return errCode;
        }

        public void setErrorCode(String errorCode) {
            this.errCode = errorCode;
        }

        public String getErrorInfo() {
            return errInfo;
        }

        public void setErrorInfo(String errorInfo) {
            this.errInfo = errorInfo;
        }

        public int getAlert() {
            return alert;
        }

        public void setAlert(int alert) {
            this.alert = alert;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        private String errCode;
        private String errInfo;
        private String version;
        private int alert;
    }

    @Override
    public String getErrorCode() {
        return head.getErrorCode();
    }

    @Override
    public String getErrorInfo() {
        return head.getErrorInfo();
    }
}
