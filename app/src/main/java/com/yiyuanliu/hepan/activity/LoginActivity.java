package com.yiyuanliu.hepan.activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.contract.LoginView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {
    @BindView(R.id.username) TextInputLayout userName;
    @BindView(R.id.password) TextInputLayout passWord;
    @BindView(R.id.actions_container) FrameLayout actionContainer;
    @BindView(R.id.progress_loading) ProgressBar progressBar;
    @BindView(R.id.container) LinearLayout container;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        userName.getEditText().setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT && isLoginValid()){
                    passWord.getEditText().requestFocus();
                    return true;
                }
                return false;
            }
        });

        passWord.getEditText().setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE){
                            login();
                            return true;
                        }
                        return false;
                    }
                }
        );

        loginPresenter = new LoginPresenter(DataManager.getInstance(this));
        loginPresenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.unbindView();
    }

    @OnClick(R.id.login)
    public void login(){
        TransitionManager.beginDelayedTransition(container);

        userName.setVisibility(View.GONE);
        passWord.setVisibility(View.GONE);
        actionContainer.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);

        loginPresenter.login(userName.getEditText().getText().toString(),
                passWord.getEditText().getText().toString());
    }

    @OnClick(R.id.signup)
    public void signUp(){
        TransitionManager.beginDelayedTransition(container);

        userName.setVisibility(View.GONE);
        passWord.setVisibility(View.GONE);
        actionContainer.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);
    }

    public boolean isLoginValid(){
        return !(!userName.getEditText().getText().toString().equals("") ||
                !passWord.getEditText().getText().toString().equals(""));
    }

    @Override
    public void showSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showError(boolean isNetwork) {
        TransitionManager.beginDelayedTransition(container);

        userName.setVisibility(View.VISIBLE);
        passWord.setVisibility(View.VISIBLE);
        actionContainer.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.GONE);

        passWord.setError(isNetwork ?
                getString(R.string.wrong_network) : getString(R.string.wrong_account));
    }
}
