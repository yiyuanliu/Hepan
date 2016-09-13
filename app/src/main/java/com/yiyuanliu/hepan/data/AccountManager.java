package com.yiyuanliu.hepan.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yiyuanliu.hepan.data.bean.UserLogin;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import im.fir.sdk.FIR;


/**
 * Created by yiyuan on 2016/7/11.
 */
public class AccountManager {
    private static final String SP_NAME = "AccountManager";

    private static final String SP_KEY_SECRET = "ACCESS";
    private static final String SP_KEY_TOKEN = "TOKEN";

    private static final String SP_KEY_UID = "UID";
    private static final String SP_KEY_AVATAR = "AVATAR";
    private static final String SP_KEY_NAME = "USER_NAME";

    public static final String ACTION_LOGOUT = "com.yiyuanliu.hepan.LOGOUT";

    private SharedPreferences sharedPreferences;

    private boolean hasAccount;
    private String secret;
    private String token;

    private int uid;
    private String userAvatar;
    private String userName;

    private Context application;

    public AccountManager(Context application){
        this.application = application;

        sharedPreferences = application.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        secret = sharedPreferences.getString(SP_KEY_SECRET, null);
        token = sharedPreferences.getString(SP_KEY_TOKEN, null);

        uid = sharedPreferences.getInt(SP_KEY_UID,0);
        userName = sharedPreferences.getString(SP_KEY_NAME, null);
        userAvatar = sharedPreferences.getString(SP_KEY_AVATAR, null);

        if (secret == null || token == null){
            hasAccount = false;
        } else {
            hasAccount = true;
        }
    }

    public void loginSuccess(UserLogin userLogin){
        secret = userLogin.getSecret();
        token = userLogin.getToken();

        uid = userLogin.getUid();
        userAvatar = userLogin.getAvatar();
        userName = userLogin.getUserName();

        sharedPreferences.edit()
                .putString(SP_KEY_TOKEN, token)
                .putString(SP_KEY_SECRET, secret)
                .putInt(SP_KEY_UID ,uid)
                .putString(SP_KEY_AVATAR, userAvatar)
                .putString(SP_KEY_NAME, userName)
                .apply();

        hasAccount = true;
    }

    public void logout(){

        FIR.addCustomizeValue("userdata", "游客");

        sharedPreferences.edit().clear().apply();

        Intent intent = new Intent(ACTION_LOGOUT);
        application.sendBroadcast(intent);

        hasAccount = false;
    }

    public boolean hasAccount(){
        return hasAccount;
    }

    public Map<String,String> getUserMap() {
        Map<String, String> map = new HashMap<>();

        if (hasAccount){
            map.put(Api.USER_SECRET,secret);
            map.put(Api.USER_TOKEN,token);
        }

        map.put(Api.USER_HASH,getAppHash());
        map.put("sdkVersion", "2.4.2");

        return map;
    }

    public String getAppHash() {
        try {
            String timeString = String.valueOf(System.currentTimeMillis());
            String authkey = "appbyme_key";
            String authString = timeString.substring(0, 5) + authkey;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashkey = md.digest(authString.getBytes());
            return new BigInteger(1, hashkey).toString(16).substring(8, 16);//16进制转换字符串
        } catch (Exception ex){
            return null;
        }
    }

    public int getUid() {
        return uid;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getUserName() {
        return userName;
    }
}
