package com.yiyuanliu.hepan.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yiyuan on 2016/7/22.
 */
public class TimeUtil {
    private static final long A_DAY = 24 * 60 * 60 * 1000;
    private static final long TWO_DAY = A_DAY * 2;

    private static final String YESTERDAY = "昨天 ";
    private static final String YESTERDAY_DE_YESTERDAY = "前天 ";

    public static String getRelativeTime(long time){

        long currentTimeMillis = System.currentTimeMillis();

        Calendar past = Calendar.getInstance();
        past.setTime(new Date(time));
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTimeMillis);

        if (past.get(Calendar.YEAR) != now.get(Calendar.YEAR)){

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
            return simpleDateFormat.format(past.getTime());

        } else if (now.get(Calendar.DAY_OF_YEAR) - past.get(Calendar.DAY_OF_YEAR) == 1) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINESE);
            return YESTERDAY + simpleDateFormat.format(time);

        } else if (now.get(Calendar.DAY_OF_YEAR) - past.get(Calendar.DAY_OF_YEAR) == 2) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINESE);
            return YESTERDAY_DE_YESTERDAY + simpleDateFormat.format(time);

        } else if (past.get(Calendar.MONTH) != now.get(Calendar.MONTH)
                || past.get(Calendar.DAY_OF_MONTH) != now.get(Calendar.DAY_OF_MONTH)){

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.CHINESE);
            return simpleDateFormat.format(past.getTime());

        } else {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINESE);
            return simpleDateFormat.format(past.getTime());

        }

    }
}
