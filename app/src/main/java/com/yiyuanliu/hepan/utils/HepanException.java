package com.yiyuanliu.hepan.utils;

import com.yiyuanliu.hepan.data.bean.BaseBean;

/**
 * Created by yiyuan on 2016/7/22.
 */
public class HepanException extends RuntimeException {
    public HepanException(String msg){
        super(msg);
    }

    public static void detectRespon(BaseBean baseBean){
        if (baseBean.getRs() == 0){
            throw new HepanException(baseBean.getErrorInfo());
        }
    }
}
