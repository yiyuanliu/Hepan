package com.yiyuanliu.hepan.data;

import android.content.Context;

import com.yiyuanliu.hepan.App;
import com.yiyuanliu.hepan.data.bean.UserLogin;
import com.yiyuanliu.hepan.notify.HeartService;

import im.fir.sdk.FIR;

/**
 * Created by yiyuan on 2016/7/9.
 */
public class DataManager implements Api.LoginListener {
    public static final String TAG = "DataManager";

    private static DataManager dataManager;

    private Api api;
    private AccountManager accountManager;

    public DataManager(Context context) {
        context = context.getApplicationContext();

        this.api = new Api();
        api.setLoginListener(this);
        this.accountManager = new AccountManager(context);
    }

    public static DataManager getInstance(Context context){
        if (dataManager == null){
            synchronized (DataManager.class){
                dataManager = new DataManager(context);

                AccountManager manager = dataManager.getAccountManager();
                if (manager.hasAccount()) {
                    String stringBuilder = "userId:" + manager.getUid() + ",userName:" + manager.getUserName();
                    FIR.addCustomizeValue("userdata", stringBuilder);
                } else {
                    FIR.addCustomizeValue("userdata", "游客");
                }
            }
        }

        return dataManager;
    }

    @Override
    public void onLoginSuccess(UserLogin userLogin) {
        accountManager.loginSuccess(userLogin);
        HeartService.startService(App.getApp());

        AccountManager manager = accountManager;
    }

    @Override
    public void onAccountFailed() {
        accountManager.logout();
    }

    public Api getApi() {
        return api;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }


}
