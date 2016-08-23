package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.data.bean.NotifyListSys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyuan on 2016/7/17.
 */
public class NotifySys {
    public String note;
    public UserBase userBase;
    public long date;
    public boolean isNew;
    public List<NotifyListSys.BodyBean.DataBean.Action> actionList;

    public NotifySys(NotifyListSys.BodyBean.DataBean data) {
        note = data.getNote();
        date = data.getReplied_date();
        isNew = data.getIs_read() == 1;
        actionList = data.actions;

        userBase = new UserBase(data.getUser_name(), data.getUser_id(), data.getIcon());
    }

    public static class NotifySysList {
        public boolean hasMore;
        public List<NotifySys> notifySysList;

        public NotifySysList(NotifyListSys notifyListSys) {
            hasMore = notifyListSys.getHas_next() == 1;

            this.notifySysList = new ArrayList<>();

            for (NotifyListSys.BodyBean.DataBean data:notifyListSys.getBody().getData()){
                this.notifySysList.add(new NotifySys(data));
            }
        }
    }
}
