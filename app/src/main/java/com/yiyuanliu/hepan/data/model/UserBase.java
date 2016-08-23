package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.data.bean.TopicList;

import java.io.Serializable;

/**
 * Created by yiyuan on 2016/7/10.
 */
public class UserBase implements Serializable {
    public static final String GENDER_MALE = "男";
    public static final String GENDER_FEMALE = "女";
    public static final String GENDER_UNKNOWN = "未知";

    public int userId;
    public String userName;
    public String userAvatar;
    public String userGender;

    public UserBase(TopicList.ListBean item) {
        userId = item.getUser_id();
        userName = item.getUser_nick_name();
        userAvatar = item.getUserAvatar();

        //我猜是这样子
        switch (item.getGender()){
            case 0:
                userGender = GENDER_UNKNOWN;
                break;
            case 1:
                userGender = GENDER_MALE;
                break;
            case 2:
                userGender = GENDER_FEMALE;
                break;
        }
    }

    public UserBase(String userName, int userId, String icon) {
        this.userId = userId;
        this.userName = userName;
        this.userAvatar = icon;
        userGender = GENDER_UNKNOWN;
    }

    public void setGender(int gender) {
        switch (gender){
            case 0:
                userGender = GENDER_UNKNOWN;
                break;
            case 1:
                userGender = GENDER_MALE;
                break;
            case 2:
                userGender = GENDER_FEMALE;
                break;
        }
    }
}
