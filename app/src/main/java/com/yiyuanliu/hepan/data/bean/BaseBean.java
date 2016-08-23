package com.yiyuanliu.hepan.data.bean;

/**
 * Created by yiyuan on 2016/7/11.
 */
public abstract class BaseBean {

    public static final int SUCCESS = 1;

    /*
     * rs = 1时相应成功否则相应失败
     */
    private int rs;

    /*
     * 通常为空，否则响应失败
     */
    private String errcode;

    public abstract String getErrorCode();

    public int getRs() {
        return rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public abstract String getErrorInfo();
}
