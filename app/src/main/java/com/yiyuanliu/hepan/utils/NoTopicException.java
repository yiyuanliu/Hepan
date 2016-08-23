package com.yiyuanliu.hepan.utils;

/**
 * Created by yiyuan on 2016/7/22.
 */
public class NoTopicException extends RuntimeException {
    public NoTopicException() {
        super("没有帖子");
    }
}
