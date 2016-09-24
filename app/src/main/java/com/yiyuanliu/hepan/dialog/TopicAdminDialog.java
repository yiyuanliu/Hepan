package com.yiyuanliu.hepan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.contract.TopicAdminView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.AtUserList;
import com.yiyuanliu.hepan.presenter.TopicAdminPresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;
import com.yiyuanliu.hepan.utils.ImagePermissionUtil;
import com.yiyuanliu.hepan.utils.SimpleEditWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yiyuan on 2016/9/24.
 */

public class TopicAdminDialog extends DialogFragment
        implements TopicAdminView, SimpleEditWatcher.EditListener {

    private static String TAG = "TopicAdminDialog";

    public static void show(AppCompatActivity activity, int tid, int replyId) {
        TopicAdminDialog dialog = new TopicAdminDialog();
        Bundle args = new Bundle();
        args.putInt("tid", tid);
        args.putInt("replyId", replyId);
        dialog.setArguments(args);
        dialog.show(activity.getSupportFragmentManager(), "reply");
    }

    private int tid;
    private int replyId;
    private List<Uri> uriList;
    private TopicAdminPresenter presenter;

    @BindView(R.id.edit_content)
    TextInputEditText content;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.pic_num)
    Button picNum;
    @BindView(R.id.at_user)
    ImageButton atUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        replyId = args.getInt("replyId");
        tid = args.getInt("tid");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AppCompatDialog appCompatDialog = new AppCompatDialog(getContext(), R.style.AppTheme_Dialog);

        appCompatDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        appCompatDialog.setContentView(R.layout.dialog_topic_admin);
        appCompatDialog.setTitle("评论");
        ButterKnife.bind(this, appCompatDialog);
        content.addTextChangedListener(new SimpleEditWatcher(this));
        presenter = new TopicAdminPresenter(getContext());
        presenter.bindView(this);

        return appCompatDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        presenter.unbindView();
    }

    @OnClick(R.id.send)
    protected void send() {
        presenter.doReply(tid, replyId, content.getText().toString(), uriList);
    }

    @OnClick(R.id.add_pic)
    protected void addPic() {
        if (ImagePermissionUtil.checkReadPermission(getContext())) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 0);
        } else {
            ImagePermissionUtil.requestPermission(this, 1);
        }
    }

    private void addPic(Uri uri) {
        if (uriList == null) {
            uriList = new ArrayList<>();
        }

        uriList.add(uri);
        picNum.setVisibility(View.VISIBLE);
        picNum.setText(String.valueOf(uriList.size()) + " 张图片");

        if (!send.isEnabled()) {
            send.setEnabled(true);
        }
    }

    @OnClick(R.id.pic_num)
    protected void clearPic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("是否清空已选择的图片")
                .setCancelable(true)
                .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (uriList != null) {
                            uriList.clear();
                            picNum.setVisibility(View.GONE);
                            picNum.setText("0");
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();

        if (send.isEnabled() && content.getText().length() == 0) {
            send.setEnabled(false);
        }
    }

    @OnClick(R.id.at_user)
    protected void atUser() {
        presenter.loadAtUser();
    }

    @Override
    public void onTextChange(boolean hasText) {
        if (hasText) {
            send.setEnabled(true);
        } else if (uriList == null || uriList.size() == 0){
            send.setEnabled(false);
        }
    }

    @Override
    public void onSendFailed(Throwable throwable) {
        content.setError(ExceptionHandle.getMsg(TAG, throwable));
    }

    @Override
    public void onSendSuccessful() {
        dismiss();
        // TODO: 2016/9/24 Notify activity for this
    }

    @Override
    public void showAtUserList(final AtUserList atUserList) {
        atUser.setEnabled(true);
        if (atUserList.stringList == null || atUserList.stringList.size() == 0) {
            return;
        }
        AtUserDialog.getInstance(getContext(), atUserList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                content.append("@" + atUserList.stringList.get(which) + " ");
                content.moveCursorToVisibleOffset();
            }
        }).show();
    }

    @Override
    public void loadAtUserFailed() {
        atUser.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    addPic(uri);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                for (int i = 0;i < permissions.length;i ++){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        addPic();
                    }
                }
                break;
        }
    }
}
