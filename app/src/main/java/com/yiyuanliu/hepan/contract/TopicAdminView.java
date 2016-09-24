package com.yiyuanliu.hepan.contract;

import com.yiyuanliu.hepan.data.model.AtUserList;

import java.util.List;

/**
 * Created by yiyuan on 2016/9/24.
 */

public interface TopicAdminView {
    void onSendFailed(Throwable throwable);
    void onSendSuccessful();
    void showAtUserList(AtUserList atUserList);
    void loadAtUserFailed();
}
