package com.yiyuanliu.hepan.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.BoardPagerAdapter;
import com.yiyuanliu.hepan.contract.BoardListView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.Forum;
import com.yiyuanliu.hepan.presenter.BoardListPresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yiyuan on 2016/7/25.
 */
public class BoardListActivity extends AppCompatActivity implements BoardListView, BoardPagerAdapter.BoardClickedListener {
    private static final String TAG = "BoardListActivity";
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.progress_loading) ProgressBar loading;
    @BindView(R.id.text_error) TextView error;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private BoardListPresenter boardListPresenter;
    private BoardPagerAdapter boardPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        ButterKnife.bind(this);

        boardListPresenter = new BoardListPresenter(DataManager.getInstance(this));
        boardListPresenter.bindView(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startLoad();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boardListPresenter.unbindView();
    }

    @OnClick(R.id.text_error)
    protected void startLoad() {
        loading.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);

        boardListPresenter.load();
    }

    @Override
    public void showBoardList(Forum forum) {
        loading.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);

        boardPagerAdapter = new BoardPagerAdapter(forum, this);
        viewPager.setAdapter(boardPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showError(Throwable throwable) {
        loading.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);

        error.setText(ExceptionHandle.getMsg(TAG ,throwable));

    }

    @Override
    public void onForumClicked(Forum.Board board) {
        BoardActivity.startActivity(this, board);
    }
}
