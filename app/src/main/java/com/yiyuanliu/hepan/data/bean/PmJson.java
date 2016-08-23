package com.yiyuanliu.hepan.data.bean;

import com.google.gson.Gson;

/**
 * Created by yiyuan on 2016/7/17.
 */
public class PmJson {
    int page = 1;
    int pageSize = 50;

    public PmJson(int page){
        this.page = page;
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
