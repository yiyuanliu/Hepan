package com.yiyuanliu.hepan.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyuan on 2016/7/29.
 */
public class UserList {
    public List<UserBase> userBaseList = new ArrayList<>();
    public boolean hasMore;

    public static UserList newInstance(com.yiyuanliu.hepan.data.bean.UserList userList) {
        UserList userList1 = new UserList();
        for (com.yiyuanliu.hepan.data.bean.UserList.ListBean item:userList.getList()){
            userList1.userBaseList.add(new UserBase(item.getName(), item.getUid(), item.getIcon()));
        }
        userList1.hasMore = userList.getHas_next() == 1;
        return userList1;
    }
}
