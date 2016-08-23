package com.yiyuanliu.hepan.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

/**
 * Created by yiyuan on 2016/7/22.
 */
public class LayoutUtil {
    public static View inflate(ViewGroup parent, int id){
        return LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
    }
}
