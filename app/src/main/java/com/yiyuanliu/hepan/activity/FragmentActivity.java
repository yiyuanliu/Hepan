package com.yiyuanliu.hepan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.data.model.Topic;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.fragment.TopicListFragment;
import com.yiyuanliu.hepan.fragment.UserInfoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentActivity extends AppCompatActivity {
    public static final String KEY_TYPE = "TYPE";
    public static final int TYPE_HOT = 1;

    public static void startActivity(Context context, int  type){
        Intent intent = new Intent(context, FragmentActivity.class);
        intent.putExtra(KEY_TYPE, type);
        context.startActivity(intent);
    }

//    public static void startActivity(Context context, UserBase userBase) {
//        Intent intent = new Intent(context, FragmentActivity.class);
//        intent.putExtra(KEY_TYPE, 2);
//        intent.putExtra("userBase", userBase);
//        context.startActivity(intent);
//    }

    @BindView(R.id.toolbar) Toolbar toolbar;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent= getIntent();
        if (intent != null){
            type = intent.getIntExtra(KEY_TYPE, 0);
        }

        if (savedInstanceState == null){
            switch (type){
                case TYPE_HOT:
                    showHot();
                    break;
                case 2:
                    showUser((UserBase) intent.getSerializableExtra("userBase"));
                    break;
            }
        }
    }

    private void showHot(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, TopicListFragment.newInstance(-1, false))
                .commit();
        setTitle("今日热门");
    }

    private void showUser(UserBase userBase){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, UserInfoFragment.newInstance(userBase))
                .commit();
        setTitle("个人信息");
    }

}
