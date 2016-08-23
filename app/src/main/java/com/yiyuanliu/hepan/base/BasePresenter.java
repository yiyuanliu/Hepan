package com.yiyuanliu.hepan.base;

import java.lang.ref.WeakReference;

/**
 * Created by yiyuan on 2016/7/22.
 */
public abstract class BasePresenter<V> {
    WeakReference<V> weakReference;

    public void bindView(V view){
        weakReference = new WeakReference<V>(view);
    }

    public void unbindView(){
        weakReference.clear();
        weakReference = null;
    }

    public boolean isViewAttached(){
        if (weakReference == null){
            return false;
        } else if (weakReference.get() == null){
            return false;
        } else {
            return true;
        }
    }

    public V getView() {
        return weakReference.get();
    }

}
