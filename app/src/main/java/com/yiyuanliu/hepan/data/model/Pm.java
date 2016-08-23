package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.data.bean.PmSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyuan on 2016/7/17.
 */
public class Pm {
    public int plid;
    public int pmid;

    public UserBase lastUserBase;
    public long lastDate;
    public String lastSummary;

    public UserBase toUserBase;

    public boolean isNew;

    public Pm(PmSession.BodyBean.ListBean data) {
        plid = data.plid;
        pmid = data.pmid;

        // TODO: 2016/7/17 显然有bug
        lastUserBase = new UserBase(data.lastUserName, data.lastUserId, data.toUserAvatar);
        lastDate = data.lastDateline;
        lastSummary = data.lastSummary;

        toUserBase = new UserBase(data.toUserName, data.toUserId, data.toUserAvatar);

        isNew = data.isNew == 1;
    }

    public static class PmList{

        public boolean hasMore;
        public List<Pm> pmList;

        public PmList(PmSession pmSession) {
            hasMore = pmSession.body.hasNext == 1;
            pmList = new ArrayList<>();

            for (PmSession.BodyBean.ListBean data:pmSession.body.list){
                pmList.add(new Pm(data));
            }
        }
    }
}
