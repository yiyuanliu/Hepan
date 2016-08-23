package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.App;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.PmList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yiyuan on 2016/8/19.
 */
public class PmMessage {
    public String content;
    public boolean fromMe;
    public long time;

    public PmMessage(){}

    public PmMessage(PmList.BodyBean.PmListBean.MsgListBean msg){
        content = msg.getContent();
        fromMe = msg.getSender() == DataManager.getInstance(App.getApp()).getAccountManager().getUid();
        time = msg.getTime();
    }

    public static class PmMessageList{
        public List<PmMessage> pmMessages = new ArrayList<>();
        public boolean hasMore;
        public long start = Long.MAX_VALUE;

        public PmMessageList(PmList pmList) {
            for (PmList.BodyBean.PmListBean pmListBean:pmList.getBody().getPmList()) {
                for (PmList.BodyBean.PmListBean.MsgListBean msgListBean :pmListBean.getMsgList()){
                    pmMessages.add(new PmMessage(msgListBean));

                    if (msgListBean.getTime() < start && msgListBean.getTime() != 0) {
                        start = msgListBean.getTime();
                    }
                }

                if (pmListBean.getHasPrev() == 1 && pmMessages.size() > 0){
                    hasMore = true;
                }
            }
            hasMore = start != Long.MAX_VALUE && hasMore;

            Collections.sort(pmMessages, new Comparator<PmMessage>() {
                @Override
                public int compare(PmMessage lhs, PmMessage rhs) {
                    return lhs.time > rhs.time ? -1 : 1;
                }
            });
        }
    }
}
