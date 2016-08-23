package com.yiyuanliu.hepan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.data.bean.SettingRs;

/**
 * Created by yiyuan on 2016/8/10.
 */
public class ClassificationAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {
    private SettingRs settingRs;
    private int boardId;
    private int classificationId;

    private Spinner spinner;

    public ClassificationAdapter(Spinner spinner, SettingRs settingRs) {
        this.settingRs = settingRs;
        this.spinner = spinner;

        spinner.setOnItemSelectedListener(this);
    }

    public int getClassificationId(){
        return classificationId;
    }

    public void setFid(int fid) {
        if (boardId == fid) {
            return;
        }

        boardId = fid;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (boardId == 0 )
            return 1;

        for (SettingRs.BodyBean.PostInfoBean postInfoBean : settingRs.getBody().getPostInfo()) {
            if (postInfoBean.getFid() == boardId) {
                return postInfoBean.getTopic().getClassificationType_list().size();
            }
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (boardId == 0 )
            return null;

        for (SettingRs.BodyBean.PostInfoBean postInfoBean : settingRs.getBody().getPostInfo()) {
            if (postInfoBean.getFid() == boardId) {
                return postInfoBean.getTopic().getClassificationType_list().get(position);
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.spinner_item_classification, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        if (position == 0) {
            textView.setText("不添加标签");
        } else {
            SettingRs.BodyBean.PostInfoBean.TopicBean.ClassificationTypeListBean
                    classificationTypeListBean =
                    (SettingRs.BodyBean.PostInfoBean.TopicBean.ClassificationTypeListBean) getItem(position - 1);
            textView.setText(classificationTypeListBean.getClassificationType_name());
        }

        return convertView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            classificationId = 0;
        } else {
            SettingRs.BodyBean.PostInfoBean.TopicBean.ClassificationTypeListBean
                    classificationTypeListBean =
                    (SettingRs.BodyBean.PostInfoBean.TopicBean.ClassificationTypeListBean) getItem(position - 1);

            classificationId = classificationTypeListBean.getClassificationType_id();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
