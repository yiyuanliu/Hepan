package com.yiyuanliu.hepan.contract;

import com.yiyuanliu.hepan.base.MoreLoadView;
import com.yiyuanliu.hepan.data.model.PmMessage;

/**
 * Created by yiyuan on 2016/8/19.
 */
public interface PmMessageView extends MoreLoadView<PmMessage> {
    void sendFailed(Throwable e);
    void sendSuccess();
}
