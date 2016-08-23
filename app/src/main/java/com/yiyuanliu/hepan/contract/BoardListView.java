package com.yiyuanliu.hepan.contract;

import com.yiyuanliu.hepan.data.model.Forum;

/**
 * Created by yiyuan on 2016/7/25.
 */
public interface BoardListView {
    void showBoardList(Forum forum);
    void showError(Throwable throwable);
}
