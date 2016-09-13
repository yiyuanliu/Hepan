package com.yiyuanliu.hepan.notify;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yiyuanliu.hepan.R;
import com.yiyuanliu.hepan.activity.MessageActivity;
import com.yiyuanliu.hepan.data.DataManager;
import com.yiyuanliu.hepan.data.bean.HeartBeat;
import com.yiyuanliu.hepan.utils.PreferenceHelper;

import retrofit2.Response;

public class HeartService extends IntentService {

    public HeartService() {
        super("HeartService");
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, HeartService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DataManager dataManager = DataManager.getInstance(this);

        if (PreferenceHelper.getInstance(this).isNotifyEnabled() && dataManager.getAccountManager().hasAccount()) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Intent intent1 = new Intent(this, HeartService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            long time = PreferenceHelper.getInstance(this).getNotifyDelay() * 1000 * 60;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, time, pendingIntent);
        } else {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent1 = new Intent(this, HeartService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);

        }

        int a = 0,b = 0,c = 0,d = 0;

        try {
            Response<HeartBeat> heartBeatResponse =
                    dataManager.getApi().getWebApi().heart(dataManager.getAccountManager().getUserMap()).execute();
            SharedPreferences sharedPreferences = getSharedPreferences("heart_log", MODE_PRIVATE);
            if (heartBeatResponse.isSuccessful()) {
                HeartBeat heartBeat = heartBeatResponse.body();
                //通过时间判断是否应该通知，效果待确认
                long last = sharedPreferences.getLong("last_update" + dataManager.getAccountManager().getUid(), 0);

                if (heartBeat.body.atMeInfo != null) {
                    if (heartBeat.body.atMeInfo.time > last) {
                        a = heartBeat.body.atMeInfo.count;
                    }
                }

                if (heartBeat.body.pmInfos != null) {
                    for (HeartBeat.Body.HeartMsgModel heartMsgModel:heartBeat.body.pmInfos) {
                        if (heartMsgModel.time > last) {
                            b = heartBeat.body.pmInfos.size();
                            break;
                        }
                    }
                }

                if (heartBeat.body.replyInfo != null) {
                    if (heartBeat.body.replyInfo.time > last) {
                        c = heartBeat.body.replyInfo.count;
                    }

                }

                if (heartBeat.body.systemInfo != null) {
                    if (heartBeat.body.systemInfo.time > last) {
                        d = heartBeat.body.systemInfo.count;
                    }
                }

                sharedPreferences.edit().putLong("last_update" + dataManager.getAccountManager().getUid(), System.currentTimeMillis()).apply();
            } else {
                //
            }
        } catch (Exception e) {
            //
            e.printStackTrace();
        }

        if ((a + b + c + d) == 0) {
            return;
        }

//        if (a > 0) {
//            dataManager.getApi().loadNotifyAt(1, dataManager.getAccountManager().getUserMap())
//                    .subscribe(new Action1<NotifyPost.NotifyPostList>() {
//                        @Override
//                        public void call(NotifyPost.NotifyPostList notifyPostList) {
//                        }
//                    });
//        }
//
//        if (b > 0) {
//            PmJson pmJson = new PmJson(1);
//            dataManager.getApi().loadPmList(pmJson, dataManager.getAccountManager().getUserMap())
//                    .subscribe(new Action1<Pm.PmList>() {
//                        @Override
//                        public void call(Pm.PmList pmList) {
//                        }
//                    });
//        }
//
//        if (c > 0) {
//            dataManager.getApi().loadNotifyPost(1, dataManager.getAccountManager().getUserMap())
//                    .subscribe(new Action1<NotifyPost.NotifyPostList>() {
//                        @Override
//                        public void call(NotifyPost.NotifyPostList notifyPostList) {
//                        }
//                    });
//        }
//
//        if (d > 0) {
//            dataManager.getApi().loadNotifySys(1, dataManager.getAccountManager().getUserMap())
//                    .subscribe(new Action1<NotifySys.NotifySysList>() {
//                        @Override
//                        public void call(NotifySys.NotifySysList notifySysList) {
//                        }
//                    });
//        }

        Notification.Builder builder = new Notification.Builder(this);
        Intent intent1 = MessageActivity.getIntent(this, a, b, c, d);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = builder.setContentTitle("消息提醒")
                .setContentText("你有" + String.valueOf(a +b + c + d) + "条新消息")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

}
