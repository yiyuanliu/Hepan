package com.yiyuanliu.hepan.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.adapter.UserInfoAdapter;
import com.yiyuanliu.hepan.contract.UserInfoView;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.data.model.UserInfo;
import com.yiyuanliu.hepan.presenter.UserInfoPresenter;
import com.yiyuanliu.hepan.utils.ExceptionHandle;
import com.yiyuanliu.hepan.utils.RecyclerDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserInfoFragment extends Fragment implements UserInfoView {

    private static final String TAG = "UserInfoFragment";
    @BindView(R.id.loading) ProgressBar loading;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.error) TextView error;

    private UserInfoPresenter userInfoPresenter;
    private Unbinder unbinder;
    private UserInfoAdapter userInfoAdapter;

    private UserBase userBase;

    public UserInfoFragment() {

    }

    public static UserInfoFragment newInstance(UserBase userBase) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userBase", userBase);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userBase = (UserBase) getArguments().getSerializable("userBase");
        }

        userInfoPresenter = new UserInfoPresenter(DataManager.getInstance(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        userInfoPresenter.bindView(this);

        if (userInfoAdapter == null) {
            load();
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(userInfoAdapter);
            recyclerView.addItemDecoration(new RecyclerDivider(getContext()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
        userInfoPresenter.unbindView();
    }

    @OnClick(R.id.error)
    public void load(){
        error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        userInfoPresenter.load(userBase);
    }

    @Override
    public void onLoaded(UserInfo userInfo) {
        loading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userInfoAdapter = new UserInfoAdapter(userInfo);
        recyclerView.setAdapter(userInfoAdapter);
        recyclerView.addItemDecoration(new RecyclerDivider(getContext()));
    }

    @Override
    public void onLoadFailed(Throwable throwable) {
        if (userInfoAdapter != null) {
            return;
        }
        error.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);

        error.setText(ExceptionHandle.getMsg(TAG, throwable));
    }
}
