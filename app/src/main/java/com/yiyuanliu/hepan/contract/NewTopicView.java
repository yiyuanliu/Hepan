package com.yiyuanliu.hepan.contract;

import com.yiyuanliu.hepan.data.bean.SettingRs;
import com.yiyuanliu.hepan.data.model.Forum;

/**
 * Created by yiyuan on 2016/7/29.
 */
public interface NewTopicView {
    void onSendSuccessful();
    void onSendFailed(Throwable throwable);
    void onLoaded(Forum forum, SettingRs settingRs);
    void onLoadFailed(Throwable throwable);
}
