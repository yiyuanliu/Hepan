package com.yiyuanliu.hepan.data.bean;

/**
 * Created by yiyuan on 2016/7/23.
 */
public class Content {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_PICTURE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_MUSIC = 3;
    public static final int TYPE_LINK = 4;
    public static final int TYPE_ATTACHMENT = 5;
    //貌似默认是0，图片为1，音频为3，连接为4（只在@中见过这种写法）、5(下载附件时)
    public int type;
    public String infor;
    //待确认的写法，忘了变量名究竟是什么了
    public String url;
    public String originalInfo;
    public String desc;

}
