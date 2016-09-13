package com.yiyuanliu.hepan.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyuanliu.hepan.R;

/**
 * Created by yiyuan on 2016/7/22.
 */
public abstract class MoreLoadAdapter extends RecyclerView.Adapter {
    protected boolean isLoading;

    public abstract void setLoading(boolean isLoading);

    public boolean isLoading(){
        return isLoading;
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public static LoadingViewHolder newInstance(ViewGroup viewGroup) {
            LoadingViewHolder loading = new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loading, viewGroup, false));

            return loading;
        }

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
