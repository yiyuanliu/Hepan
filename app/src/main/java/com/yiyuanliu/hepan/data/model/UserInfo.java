package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.data.bean.*;
import com.yiyuanliu.hepan.data.bean.UserList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yiyuan on 2016/7/19.
 * 还有许多功能可以添加，不过我懒得写 =.=
 */
public class UserInfo implements Serializable {

    public UserBase userBase;
    public boolean isFriend;
    public String userTitle;
    public String sign;
    public int topicNum;

    //水滴，就这样喽
    public int waterNum;

    public List<Item> itemList = new ArrayList<>();

    public static UserInfo newInstance(com.yiyuanliu.hepan.data.bean.UserInfo userInfo, UserBase userBase) {
        UserInfo user = new UserInfo();

        user.userBase = userBase;
        userBase.userName = userInfo.getName();
        userBase.userAvatar = userInfo.getIcon();
        userBase.setGender(userInfo.getGender());

        user.isFriend = userInfo.getIsFriend() == 1;
        user.waterNum = userInfo.getGold_num();
        user.userTitle = userInfo.getUserTitle();
        user.topicNum = userInfo.getTopic_num();
        user.sign = userInfo.getSign();

        if (userInfo.getBody().getProfileList()!= null) {
            for (com.yiyuanliu.hepan.data.bean.UserInfo.BodyBean.InfoItemBean item:userInfo.getBody().getProfileList()){
                Item item1 = new Item();
                item1.key = item.getTitle();
                item1.value = item.getData();

                user.itemList.add(item1);
            }
        }

        if (userInfo.getBody().getCreditList()!= null) {
            for (com.yiyuanliu.hepan.data.bean.UserInfo.BodyBean.InfoItemBean item:userInfo.getBody().getCreditList()){
                Item item1 = new Item();
                item1.key = item.getTitle();
                item1.value = item.getData();

                user.itemList.add(item1);
            }
        }

        if (userInfo.getBody().getCreditShowList()!= null) {
            for (com.yiyuanliu.hepan.data.bean.UserInfo.BodyBean.InfoItemBean item:userInfo.getBody().getCreditShowList()){
                Item item1 = new Item();
                item1.key = item.getTitle();
                item1.value = item.getData();

                user.itemList.add(item1);
            }
        }

        return user;
    }

    public static class Item {
        public String key;
        public String value;
    }
}
