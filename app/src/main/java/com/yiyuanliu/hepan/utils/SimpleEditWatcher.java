package com.yiyuanliu.hepan.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by yiyuan on 2016/9/24.
 */

public class SimpleEditWatcher implements TextWatcher {
    private EditListener editListener;
    private boolean mHasText;

    public SimpleEditWatcher(EditListener editListener) {
        this.editListener = editListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean hasText;
        if (s == null || s.length() == 0) {
            hasText = false;
        } else {
            hasText = true;
        }

        if (hasText != mHasText) {
            editListener.onTextChange(hasText);
            mHasText = hasText;
        }
    }

    public interface EditListener {
        void onTextChange(boolean hasText);
    }
}
