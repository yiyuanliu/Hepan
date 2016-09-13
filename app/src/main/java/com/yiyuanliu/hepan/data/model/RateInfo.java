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
 * Created by yiyuan on 2016/8/31.
 */
public class RateInfo {

    public static RateInfo loadRateInfo(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            check(doc);

            Element element = doc.select("#rateform").first();
            String rateUrl = element.attr("action");

            Elements trs = doc.select("tr");
            Elements tds = trs.get(1).select("td");

            String range = tds.get(2).text().replace(" ", "");
            String total = tds.get(3).text().trim();
            int totalNum = Integer.parseInt(total);
            int index = range.indexOf("~");
            int min = Integer.parseInt(range.substring(0, index));
            int max = Integer.parseInt(range.substring(index + 1));

            return new RateInfo(min, max, totalNum, rateUrl);
        } catch (IOException io) {
            throw new RuntimeException(io.getMessage());
        } catch (HepanException e){
            throw e;
        } catch (Exception ex) {
            Log.e("Rate", ex.getMessage(), ex);
            throw new HepanException("jsoup解析错误");
        }
    }

    private static void check(Document doc) {
        Elements trs = doc.select("script");

        Pattern pattern = Pattern.compile("alert\\(\"([\\s\\S]*)\"\\)");

        for (Element element: trs) {
            Matcher matcher = pattern.matcher(element.data());
            if (matcher.find()) {
                throw new HepanException(matcher.group(1));
            }
        }
    }

    public RateInfo(int minScore, int maxScore, int todayTotal, String rateUrl) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.todayTotal = todayTotal;
        this.rateUrl = rateUrl;
    }

    public int minScore;
    public int maxScore;
    public int todayTotal;
    public String rateUrl;
}
