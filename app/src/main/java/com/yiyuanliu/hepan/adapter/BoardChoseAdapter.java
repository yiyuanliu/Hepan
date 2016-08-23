package com.yiyuanliu.hepan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.data.model.Forum;

/**
 * Created by yiyuan on 2016/7/29.
 */
public class BoardChoseAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {
    private Forum forum;
    private Forum.Category category;
    private Forum.Board board;
    private Spinner spinner;
    private boolean isFirst = true;

    private OnBoardChangedListener onBoardChangedListener;

    public BoardChoseAdapter(Spinner spinner, Forum forum, OnBoardChangedListener onBoardChangedListener){
        this.spinner = spinner;
        this.forum = forum;
        spinner.setOnItemSelectedListener(this);
        this.onBoardChangedListener = onBoardChangedListener;
    }

    public boolean isBoardSelected(){
        return board != null;
    }

    public int getBoardId(){
        return board.boardId;
    }

    @Override
    public int getCount() {
        if (category != null){
            return category.boardList.size() + 2;
        } else {
            return forum.categories.size() + 1;
        }
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

        if (convertView == null || !convertView.getTag().equals("DropView")) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar_spinner_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        String str = "选择板块";

        if (board != null){
            str = board.name;
        }
        textView.setText(str);
        convertView.setTag("DropView");
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        int p = spinner.getSelectedItemPosition();

        if (convertView == null || !convertView.getTag().equals("View")){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar_spinner_item_drop,parent,false);
        }
        convertView.setVisibility(View.VISIBLE);

        TextView textView = (TextView)convertView.findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);
        String str;
        if (category != null){
            if (position == 0){
                str = null;
                textView.setVisibility(View.GONE);
            } else {
                str = position == 1 ? "上级菜单" : category.boardList.get(position - 2).name;
            }

        } else {
            if (position == 0){
                str = null;
                textView.setVisibility(View.GONE);
            } else {
                str = forum.categories.get(position - 1).categoryName;
            }

        }
        textView.setText(str);

        convertView.setTag("View");

        return convertView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0){
            return;
        }

        if (category != null){
            if (position == 1){
                category = null;
                notifyDataSetChanged();
                spinner.performClick();
            } else {
                board = category.boardList.get(position - 2);
                onBoardChangedListener.onBoardChanged(board.boardId);
                notifyDataSetChanged();
            }
        } else {
            category = forum.categories.get(position - 1);
            notifyDataSetChanged();
            spinner.performClick();
        }

        spinner.setSelection(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public interface OnBoardChangedListener {
        void onBoardChanged(int fid);
    }
}
