package com.yiyuanliu.hepan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.BoardSpinnerAdapter;
import com.yiyuanliu.hepan.contract.BoardView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.Forum;
import com.yiyuanliu.hepan.fragment.TopicListFragment;
import com.yiyuanliu.hepan.presenter.BoardPresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardActivity extends AppCompatActivity implements BoardView, OnItemSelectedListener {
    public static final String TAG = "BoardActivity";

    public static final String EXTRA_BOARD_ID = "BOARD_ID";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.spinner) Spinner spinner;
    private BoardPresenter boardPresenter;
    private Forum.Board board;

    private int now;

    public static void startActivity(Context context, Forum.Board board){
        Intent intent = new Intent(context, BoardActivity.class);
        intent.putExtra(EXTRA_BOARD_ID, board);

        context.startActivity(intent);
    }

    public static void startActivity(Context context, int fid) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        if (intent != null){
            board = (Forum.Board) intent.getSerializableExtra(EXTRA_BOARD_ID);
        }

        boardPresenter = new BoardPresenter(DataManager.getInstance(this));
        boardPresenter.bindView(this);
        loadBoard();

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinner.setAdapter(new BoardSpinnerAdapter(board, spinner));
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boardPresenter.unbindView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_board, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new) {
            int p = spinner.getSelectedItemPosition();
            NewTopicActivity.startActivity(this, ((Forum.Board)spinner.getAdapter().getItem(p)).name,
                    ((Forum.Board)spinner.getAdapter().getItem(p)).boardId);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBoard(Forum.Board board) {
        this.board = board;
        spinner.setAdapter(new BoardSpinnerAdapter(board, spinner));
    }

    @Override
    public void showError(Throwable throwable) {
        Snackbar.make(spinner, ExceptionHandle.getMsg(TAG, throwable), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadBoard();
                    }
                })
                .show();
    }

    private void loadBoard(){
        boardPresenter.load(board);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (now == ((Forum.Board)spinner.getAdapter().getItem(position)).boardId) {
            return;
        }

        now = ((Forum.Board)spinner.getAdapter().getItem(position)).boardId;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, TopicListFragment.newInstance(
                        ((Forum.Board)spinner.getAdapter().getItem(position)).boardId, false))
                .commit();
        Log.d(TAG, ((Forum.Board)spinner.getAdapter().getItem(position)).name);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "nothing");
    }

}
