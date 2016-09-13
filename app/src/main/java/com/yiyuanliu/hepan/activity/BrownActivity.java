package com.yiyuanliu.hepan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yiyuanliu.hepan.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrownActivity extends AppCompatActivity {

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, BrownActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @BindView(R.id.web_view) WebView webView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private static String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
        }

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
