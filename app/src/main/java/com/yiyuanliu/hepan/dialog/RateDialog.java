package com.yiyuanliu.hepan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.data.model.RateInfo;
import com.yiyuanliu.hepan.utils.ExceptionHandle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yiyuan on 2016/9/3.
 */
public class RateDialog extends AppCompatDialog {
    private RateInfo rateInfo;
    private RateListener rateListener;

    public static RateDialog showDialog(Context context,RateListener listener, RateInfo rateInfo) {
        RateDialog rateDialog = new RateDialog(context, R.style.AppTheme_Dialog);
        rateDialog.rateInfo = rateInfo;
        rateDialog.show();

        rateDialog.rateListener = listener;

        return rateDialog;
    }

    protected RateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @BindView(R.id.rate) Button rate;
    @BindView(R.id.reason) TextInputLayout reason;
    @BindView(R.id.notify) CheckBox notify;
    @BindView(R.id.spinner) Spinner spinner;
    int mScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rate);
        setTitle("评分");

        ButterKnife.bind(this);
        spinner.setAdapter(new RateSpinnerAdapter());
        spinner.setSelection(- rateInfo.minScore);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mScore = position + rateInfo.minScore;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mScore = 0;
            }
        });
    }

    @OnClick(R.id.rate)
    public void rate() {
        rateListener.onRate(mScore, reason.getEditText().getText().toString(), notify.isChecked());
    }

    public void showError(Throwable throwable) {
        Snackbar.make(rate, ExceptionHandle.getMsg("RateDialog", throwable), Snackbar.LENGTH_SHORT).show();
    }

    public interface RateListener {
        void onRate(int score, String reason, boolean notify);
    }

    private class RateSpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return rateInfo.maxScore - rateInfo.minScore + 1;
        }

        @Override
        public Object getItem(int position) {
            return rateInfo.minScore + position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar_spinner_item_drop,parent,false);
            }

            TextView textView = (TextView)convertView.findViewById(R.id.textView);
            textView.setText((rateInfo.minScore + position) + "水滴");

            return convertView;
        }
    }
}
