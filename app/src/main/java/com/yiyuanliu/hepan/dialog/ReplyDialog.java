package com.yiyuanliu.hepan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DialogTitle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.PostListActivity;
import com.yiyuanliu.hepan.activity.UserInfoActivity;
import com.yiyuanliu.hepan.data.Api;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.Content;
import com.yiyuanliu.hepan.data.bean.NormalBean;
import com.yiyuanliu.hepan.data.bean.TopicAdmin;
import com.yiyuanliu.hepan.data.model.NotifyPost;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.data.model.UserInfo;
import com.yiyuanliu.hepan.utils.ExceptionHandle;
import com.yiyuanliu.hepan.utils.HepanException;
import com.yiyuanliu.hepan.utils.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yiyuan on 2016/8/3.
 */
public class ReplyDialog extends Dialog {

    private static final String TAG = "ReplyDialog";

    public static void showDialog(Context context, NotifyPost notifyPost) {
        ReplyDialog replyDialog = new ReplyDialog(context, notifyPost, R.style.AppTheme_Dialog);
        replyDialog.setCancelable(true);
        replyDialog.show();
    }

    private NotifyPost notifyPost;

    @BindView(R.id.quote) TextView quote;
    @BindView(R.id.text) TextView text;
    @BindView(R.id.input) EditText input;
    @BindView(R.id.send) Button send;
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.time) TextView time;
    @BindView(R.id.username) TextView userName;
    @BindView(R.id.title) TextView dialogTitle;

    protected ReplyDialog(Context context, NotifyPost notifyPost, @StyleRes int themeResId) {
        super(context, themeResId);
        this.notifyPost = notifyPost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reply);

        ButterKnife.bind(this);
        quote.setText(notifyPost.topicContent);
        dialogTitle.setText(notifyPost.topicSubject);
        dialogTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostListActivity.startActivity(getContext(), notifyPost.topicId);
            }
        });
        text.setText(notifyPost.replyContent);
        userName.setText(notifyPost.user.userName);
        Picasso.with(getContext())
                .load(notifyPost.user.userAvatar)
                .placeholder(R.drawable.place_avatar)
                .into(avatar);
        time.setText(TimeUtil.getRelativeTime(notifyPost.replyDate));
    }

    @OnClick(R.id.send)
    void send(){
        DataManager dataManager = DataManager.getInstance(getContext());
        TopicAdmin topicAdmin = new TopicAdmin();
        topicAdmin.getBody().getJson().setTid(notifyPost.topicId);
        topicAdmin.getBody().getJson().setReplyId(notifyPost.replyRemindId);
        if (notifyPost.replyRemindId != 0){
            topicAdmin.getBody().getJson().setIsQuote(1);
        }

        Content content1 = new Content();
        content1.type = Content.TYPE_NORMAL;
        content1.infor = input.getText().toString();

        topicAdmin.getBody().getJson().addContent(content1);
        Snackbar.make(input, "发送中...", Snackbar.LENGTH_INDEFINITE).show();
        dataManager.getApi().getWebApi()
                .topicAdmin(Api.TOPIC_ADMIN_REPLY, topicAdmin, dataManager.getAccountManager().getUserMap())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NormalBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(input, ExceptionHandle.getMsg(TAG, e), Snackbar.LENGTH_SHORT).show();
                        setCancelable(true);
                    }

                    @Override
                    public void onNext(NormalBean baseBean) {
                        HepanException.detectRespon(baseBean);
                        Snackbar.make(input, "回复成功", Snackbar.LENGTH_SHORT).show();
                        cancel();
                    }
                });
    }

    @OnClick({R.id.avatar, R.id.username, R.id.time})
    void viewUser(){
        UserInfoActivity.startActivity(getContext(), notifyPost.user);
    }
}
