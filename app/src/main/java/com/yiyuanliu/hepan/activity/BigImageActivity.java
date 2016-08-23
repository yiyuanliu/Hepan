package com.yiyuanliu.hepan.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.utils.DeviceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class BigImageActivity extends AppCompatActivity {

    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.loading) ProgressBar loading;

    private PhotoViewAttacher mAttacher;

    public static void startActivity(Context context, String url){
        Intent intent = new Intent(context, BigImageActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_big_image);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Log.d("test", url);

        ButterKnife.bind(this);

        mAttacher = new PhotoViewAttacher(imageView);

        Picasso.with(this)
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .resize(DeviceUtil.getScreenWidth(this), DeviceUtil.getScreenHeight(this))
                .centerInside()
                .onlyScaleDown()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        loading.setVisibility(View.GONE);
                        mAttacher.update();
                    }

                    @Override
                    public void onError() {
                        loading.setVisibility(View.GONE);
                    }
                });

    }

}
