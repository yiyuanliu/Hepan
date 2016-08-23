package com.yiyuanliu.hepan.contract;

import com.yiyuanliu.hepan.data.model.Forum;

/**
 * Created by yiyuan on 2016/7/25.
 */
public interface BoardView {
    void showBoard(Forum.Board board);
    void showError(Throwable throwable);
}
