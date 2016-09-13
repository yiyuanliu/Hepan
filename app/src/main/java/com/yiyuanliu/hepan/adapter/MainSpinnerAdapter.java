package com.yiyuanliu.hepan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;

/**
 * Created by yiyuan on 2016/8/6.
 */
public class MainSpinnerAdapter extends BaseAdapter {
    private Spinner spinner;

    public MainSpinnerAdapter(Spinner spinner) {
        this.spinner = spinner;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int p = spinner.getSelectedItemPosition();

        if (convertView == null || !convertView.getTag().equals("View")){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar_spinner_item,parent,false);
        }

        String str = null;
        switch (p) {
            case 0:
                str = "最新发表";
                break;
            case 1:
                str = "最新回复";
                break;
            case 2:
                str = "今日热门";
                break;
        }

        TextView textView = (TextView)convertView.findViewById(R.id.textView);
        textView.setText(str);
        convertView.setTag("View");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !convertView.getTag().equals("DropView")) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar_spinner_item_drop, parent, false);
        }

        String str = null;
        switch (position) {
            case 0:
                str = "最新发表";
                break;
            case 1:
                str = "最新回复";
                break;
            case 2:
                str = "今日热门";
                break;
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(str);
        convertView.setTag("DropView");

        return convertView;
    }
}
