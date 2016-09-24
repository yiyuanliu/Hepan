package com.yiyuanliu.hepan.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.BoardChoseAdapter;
import com.yiyuanliu.hepan.adapter.ClassificationAdapter;
import com.yiyuanliu.hepan.adapter.NewTopicAdapter;
import com.yiyuanliu.hepan.contract.NewTopicView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.SettingRs;
import com.yiyuanliu.hepan.data.model.AtUserList;
import com.yiyuanliu.hepan.data.model.Forum;
import com.yiyuanliu.hepan.presenter.NewTopicPresenter;
import com.yiyuanliu.hepan.utils.DeviceUtil;
import com.yiyuanliu.hepan.utils.ExceptionHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiyuan on 2016/7/29.
 */
public class NewTopicActivity extends AppCompatActivity
        implements NewTopicView, BoardChoseAdapter.OnBoardChangedListener {

    public static final int REQUEST_PERMISSION = 0;
    public static final int INTENT_GET_IMAGE = 1;
    private static final String TAG = "NewTopicActivity";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.spinner) Spinner spinner;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private BoardChoseAdapter boardChoseAdapter;
    private NewTopicAdapter newTopicAdapter;
    private NewTopicPresenter newTopicPresenter;
    private ClassificationAdapter classificationAdapter;

    private boolean isFailed;

    private int sBoardId;
    private String sBoardName;

    private ProgressDialog progressDialog;

    public static void startActivity(Context context, String boardName, int boardId) {
        Intent intent = new Intent(context, NewTopicActivity.class);
        intent.putExtra("boardName", boardName);
        intent.putExtra("boardId", boardId);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Loading");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent != null) {
            sBoardName = intent.getStringExtra("boardName");
            sBoardId = intent.getIntExtra("boardId", 0);

            if (sBoardName != null && !sBoardName.equals("")){
                getSupportActionBar().setTitle(sBoardName);
            }
        }

        newTopicPresenter = new NewTopicPresenter(DataManager.getInstance(this));
        newTopicPresenter.bindView(this);
        newTopicPresenter.load();

        newTopicAdapter = new NewTopicAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newTopicAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                if (position >= 2) {
                    return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.RIGHT)
                            | makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
                }
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                newTopicAdapter.remove(position);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newTopicPresenter.unbindView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_post ,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_pic:
                getImage();
                break;
            case R.id.send:
                send();
                break;
            case R.id.at_user:
                newTopicPresenter.loadAt();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case INTENT_GET_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    newTopicAdapter.addImage(uri);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSION:
                for (int i = 0;i < permissions.length;i ++){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        getImage();

                    } else {
                        Snackbar.make(spinner, "没有权限无法加载图片", Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onSendSuccessful() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }

        Snackbar.make(recyclerView, "发送成功", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSendFailed(Throwable throwable) {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发送失败")
                .setMessage(ExceptionHandle.getMsg(TAG, throwable))
                .setCancelable(true)
                .show();
    }

    @Override
    public void onLoaded(Forum forum, SettingRs settingRs) {
        boardChoseAdapter = new BoardChoseAdapter(spinner, forum, this);
        spinner.setAdapter(boardChoseAdapter);
        if (sBoardId == 0) {
            spinner.setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        classificationAdapter = new ClassificationAdapter(
                newTopicAdapter.headerViewHolder.getClassification(), settingRs);
        newTopicAdapter.headerViewHolder.getClassification().setAdapter(classificationAdapter);
        newTopicAdapter.headerViewHolder.getClassification().setVisibility(View.VISIBLE);
        classificationAdapter.setFid(sBoardId);
    }

    @Override
    public void onLoadFailed(Throwable throwable) {
        if (isFailed) {
            return;
        }

        isFailed = true;
        Snackbar.make(recyclerView, "加载失败\n" + ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_INDEFINITE)
                .setAction("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newTopicPresenter.load();
                    }
                }).show();
    }

    @Override
    public void showAt(final AtUserList atUserList) {
        if (atUserList.stringList == null || atUserList.stringList.size() == 0) {
            Snackbar.make(toolbar, "没有可以at的好友", Snackbar.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        List<Map<String, String>> mapList = new ArrayList<>();
        for (String str : atUserList.stringList) {
            Map<String, String > stringStringMap = new HashMap<>();
            stringStringMap.put("name", str);
            mapList.add(stringStringMap);
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, mapList,
                android.R.layout.simple_list_item_1, new String[]{"name"}, new int[]{android.R.id.text1});
        builder.setTitle("选择好友")
                .setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = atUserList.stringList.get(which);
                        newTopicAdapter.textViewHolder.post.append(" @" + name + " ");
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(true);
        builder.create().show();
    }

    private void getImage(){
        if (checkReadPermission()){

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, INTENT_GET_IMAGE);

        } else {
            requestReadPermission();
        }
    }

    private void send(){

        if(sBoardId == 0 && !boardChoseAdapter.isBoardSelected()) {
            Snackbar.make(recyclerView, "请选择板块", Snackbar.LENGTH_SHORT).show();
            return;
        } else if (newTopicAdapter.headerViewHolder.getTitle() == null || newTopicAdapter.headerViewHolder.getTitle().equals("")) {
            Snackbar.make(recyclerView, "请输入标题", Snackbar.LENGTH_SHORT).show();
            return;
        } else if (
                (newTopicAdapter.textViewHolder.getContent() == null || newTopicAdapter.textViewHolder.getContent().equals(""))
                 && newTopicAdapter.list.size() == 0) {
            Snackbar.make(recyclerView, "内容为空", Snackbar.LENGTH_SHORT).show();
            return;
        }

        newTopicPresenter.doSend(sBoardId != 0 ? sBoardId : boardChoseAdapter.getBoardId(),
                classificationAdapter.getClassificationId(),
                newTopicAdapter.headerViewHolder.getTitle(), newTopicAdapter.textViewHolder.getContent(),
                newTopicAdapter.list);

        progressDialog = ProgressDialog.show(this, "发送中", "");
        progressDialog.setCancelable(false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkReadPermission(){
        if (!DeviceUtil.hasM()){
            return true;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestReadPermission(){
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
    }

    @Override
    public void onBoardChanged(int fid) {
        classificationAdapter.setFid(fid);
    }
}
