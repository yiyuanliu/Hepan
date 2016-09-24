package com.yiyuanliu.hepan.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.SimpleAdapter;

import com.yiyuanliu.hepan.data.model.AtUserList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yiyuan on 2016/9/24.
 */

public class AtUserDialog {
    public static AlertDialog getInstance(Context context, AtUserList atUserList,
                                          DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        List<Map<String, String>> mapList = new ArrayList<>();
        for (String str : atUserList.stringList) {
            Map<String, String > stringStringMap = new HashMap<>();
            stringStringMap.put("name", str);
            mapList.add(stringStringMap);
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(context, mapList,
                android.R.layout.simple_list_item_1, new String[]{"name"}, new int[]{android.R.id.text1});
        builder.setTitle("选择好友")
                .setAdapter(simpleAdapter, listener);
        builder.setCancelable(true);
        return builder.create();
    }
}
