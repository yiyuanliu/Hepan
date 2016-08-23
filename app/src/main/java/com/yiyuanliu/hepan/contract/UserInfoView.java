package com.yiyuanliu.hepan.contract;

import com.yiyuanliu.hepan.data.model.UserInfo;

/**
 * Created by yiyuan on 2016/8/8.
 */
public interface UserInfoView  {
    void onLoaded(UserInfo userInfo);
    void onLoadFailed(Throwable throwable);
}
