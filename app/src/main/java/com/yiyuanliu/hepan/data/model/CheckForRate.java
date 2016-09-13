package com.yiyuanliu.hepan.data.model;

import android.util.Log;

import com.yiyuanliu.hepan.utils.HepanException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yiyuan on 2016/9/3.
 */
public class CheckForRate {
    private static final String TAG = "CheckForRate";

    public static Boolean check(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            Pattern pattern = Pattern.compile("alert\\(\"([\\s\\S]*)\"\\)");
            Elements trs = document.select("script");

            for (Element element: trs) {
                Matcher matcher = pattern.matcher(element.data());
                if (matcher.find()) {
                    return false;
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            if (document != null) {
                Log.e(TAG, "error in poarse:" + document.text());
            }
            throw new HepanException("Jsoup解析错误");
        }

        return false;
    }
}
