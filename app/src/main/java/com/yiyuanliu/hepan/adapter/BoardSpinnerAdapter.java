package com.yiyuanliu.hepan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.data.model.Forum;

/**
 * Created by yiyuan on 2016/7/25.
 */
public class BoardSpinnerAdapter extends BaseAdapter {
    private Forum.Board board;
    private Spinner spinner;

    public BoardSpinnerAdapter(Forum.Board board, Spinner spinner){
        this.board = board;
        this.spinner = spinner;
    }

    @Override
    public int getCount() {
        return board.childBoard.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position < 1 ? board : board.childBoard.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int p = spinner.getSelectedItemPosition();

        if (convertView == null || !convertView.getTag().equals("View")){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar_spinner_item,parent,false);
        }

        TextView textView = (TextView)convertView.findViewById(R.id.textView);
        textView.setText(((Forum.Board)getItem(p)).name);
        convertView.setTag("View");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !convertView.getTag().equals("DropView")) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar_spinner_item_drop, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(((Forum.Board) getItem(position)).name);
        convertView.setTag("DropView");

        return convertView;
    }

}

