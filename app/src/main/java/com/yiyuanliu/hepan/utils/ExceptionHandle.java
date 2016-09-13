package com.yiyuanliu.hepan.utils;

import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by yiyuan on 2016/7/27.
 */
public class ExceptionHandle {
    public static final String TIME_OUT = "网络连接超时";
    public static final String JSON_EXCEPTION = "Json解析错误";
    public static final String WRONG_NETWORK = "网络错误";

    public static String getMsg(String tag, Throwable throwable){

        if (throwable instanceof HepanException){
            return throwable.getMessage();
        }

        Log.e(tag, throwable.getMessage(), throwable);

        if (throwable instanceof SocketTimeoutException){
            return TIME_OUT;
        }

        if (throwable instanceof JsonParseException || throwable instanceof MalformedJsonException){
            //Instabug.reportException(throwable);
            return JSON_EXCEPTION;
        }

        if (throwable instanceof IOException){
            return WRONG_NETWORK;
        }

        return throwable.getMessage();
    }
}
