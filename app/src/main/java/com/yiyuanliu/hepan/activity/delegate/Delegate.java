package com.yiyuanliu.hepan.activity.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

/**
 * Created by yiyuan on 2016/8/24.
 */
public interface Delegate {
    void onCreate(Bundle savedInstanceState) ;
    void onResume();
    void onDestroy();
    boolean onBackPressed();
    boolean onCreateOptionsMenu(Menu menu);
    void onActivityResult(int requestCode, int resultCode, Intent data) ;
}
