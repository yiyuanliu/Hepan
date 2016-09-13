package com.yiyuanliu.hepan.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.NotifyListSys;
import com.yiyuanliu.hepan.data.model.UserInfo;
import com.yiyuanliu.hepan.notify.HeartService;
import com.yiyuanliu.hepan.utils.AvatarTrans;
import com.yiyuanliu.hepan.utils.DeviceUtil;
import com.yiyuanliu.hepan.utils.ExceptionHandle;

import java.io.File;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    // TODO: 2016/8/23 处理可能的内存泄漏

    private static final String TAG = "SettingsActivity";
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private Preference sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.pref_setting);
        bindPreferenceSummaryToValue(findPreference("image_sim"));
        bindPreferenceSummaryToValue(findPreference("image_wifi"));
        bindPreferenceSummaryToValue(findPreference("notify_wifi"));
        bindPreferenceSummaryToValue(findPreference("notify_sim"));
        bindPreferenceSummaryToValue(findPreference("main_type"));

        findPreference("notify_wifi").setOnPreferenceChangeListener(new NotifyChangeListener());
        findPreference("notify_sim").setOnPreferenceChangeListener(new NotifyChangeListener());

        Picasso.with(this)
                .load(DataManager.getInstance(this).getAccountManager().getUserAvatar())
                .transform(new AvatarTrans())
                .into(new PreferenceTarget(findPreference("user_avatar")));

        Preference preference = findPreference("user_avatar");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getImage();
                return true;
            }
        });

        sign = findPreference("user_sign");
        sign.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final String newSign = newValue.toString();
                DataManager.getInstance(SettingsActivity.this).getApi()
                        .updateUserSign(newSign, DataManager.getInstance(SettingsActivity.this).getAccountManager().getUserMap())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Snackbar.make(getListView(), ExceptionHandle.getMsg(TAG, e), Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                sign.setSummary(newSign);
                            }
                        });
                return true;
            }
        });

        DataManager.getInstance(this)
                .getApi().loadMyInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        sign.setSummary(userInfo.sign);
                    }
                });
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void startActivity(Context c) {
        Intent intent = new Intent(c, SettingsActivity.class);
        c.startActivity(intent);
    }

    public class NotifyChangeListener implements Preference.OnPreferenceChangeListener{

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(newValue.toString());

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            }

            HeartService.startService(SettingsActivity.this);
            return true;
        }
    }

    public static class PreferenceTarget implements Target {
        private Preference preference;

        public PreferenceTarget(Preference preference) {
            this.preference = preference;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            preference.setIcon(new BitmapDrawable(bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            preference.setIcon(R.drawable.place_avatar);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            preference.setIcon(placeHolderDrawable);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    crop(uri);
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {

                    DataManager dataManager = DataManager.getInstance(this);
                    dataManager.getApi().updateAvatar(dataManager.getAccountManager().getUserMap())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Void>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Snackbar.make(getListView(), ExceptionHandle.getMsg(TAG, e), Snackbar.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(Void aVoid) {
                                    Picasso.with(SettingsActivity.this)
                                            .load(DataManager.getInstance(SettingsActivity.this).getAccountManager().getUserAvatar())
                                            .networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .transform(new AvatarTrans())
                                            .into(new PreferenceTarget(findPreference("user_avatar")));
                                }
                            });
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0:
                for (int i = 0;i < permissions.length;i ++){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        getImage();
                    } else {
                        Snackbar.make(getListView(), "没有权限无法加载图片", Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void getImage(){
        if (checkReadPermission()){

            Intent intent = new Intent();
            //ACTION_GET_CONTENT发生奇怪的错误
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                    intent, 0);
            int size = list.size();

            if (size == 0) {
                Toast.makeText(this, "没有可以响应的app", Toast.LENGTH_SHORT)
                        .show();
            } else {
                startActivityForResult(intent, 1);
            }


        } else {
            requestReadPermission();
        }
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.setData(uri);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getExternalCacheDir(), "avatar")));

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "can't find crop app", Toast.LENGTH_SHORT)
                    .show();
        } else {

            startActivityForResult(intent, 2);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkReadPermission(){
        if (!DeviceUtil.hasM()){
            return true;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestReadPermission(){
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

}
