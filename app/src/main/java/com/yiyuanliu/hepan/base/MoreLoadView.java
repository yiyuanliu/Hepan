package com.yiyuanliu.hepan.base;

import java.util.List;

/**
 * Created by yiyuan on 2016/7/22.
 */
public interface MoreLoadView<D> {
    void loadNewSuccess(List<D> dataList);
    void loadMoreSuccess(List<D> dataList);

    void loadNewFailed(Throwable throwable);
    void loadMoreFailed(Throwable throwable);

}
