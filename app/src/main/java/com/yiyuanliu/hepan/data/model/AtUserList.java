package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.data.bean.AtUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyuan on 2016/8/27.
 */
public class AtUserList {
    public List<String> stringList = new ArrayList<>();

    public AtUserList(AtUser atUser) {
        int num = atUser.getTotal_num();

        if (num > 0) {
            for (AtUser.ListBean user :atUser.getList()) {
                stringList.add(user.getName());
            }
        }
    }
}
