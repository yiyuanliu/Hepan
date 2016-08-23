package com.yiyuanliu.hepan.span;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yiyuanliu.hepan.activity.BoardActivity;
import com.yiyuanliu.hepan.activity.FragmentActivity;
import com.yiyuanliu.hepan.activity.PostListActivity;
import com.yiyuanliu.hepan.activity.UserInfoActivity;
import com.yiyuanliu.hepan.data.model.Forum;
import com.yiyuanliu.hepan.data.model.Topic;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.utils.IntentUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yiyuan on 2016/8/3.
 */
public class LinkSpan extends ClickableSpan {
    public static final String TAG = "LinkSpan";

    private static final String AT_USER = "^http://bbs\\.uestc\\.edu\\.cn/home\\.php\\?mod=space&uid=(\\d+).*$";
    private static final String TO_TOPIC = "^http://bbs\\.uestc\\.edu\\.cn/forum\\.php\\?mod=viewthread&tid=(\\d+).*$";
    private static final String TO_FORUM = "^http://bbs\\.uestc\\.edu\\.cn/forum\\.php\\?mod=forumdisplay&fid=(\\d+).*$";


    private String link;
    private String content;

    public LinkSpan(String content, String link) {
        this.link = link;
        this.content = content;
    }

    @Override
    public void onClick(View widget) {
        final Context context = widget.getContext();

        Pattern userPattern = Pattern.compile(AT_USER);
        Matcher userMatcher = userPattern.matcher(link);
        if (userMatcher.find()){
            String uid = userMatcher.group(1);
            toUser(context, Integer.parseInt(uid), content.replaceFirst("@", ""));

            return;
        }

        Pattern topicPattern = Pattern.compile(TO_TOPIC);
        Matcher topicMatcher = topicPattern.matcher(link);
        if (topicMatcher.find()){
            String topicId = topicMatcher.group(1);
            toTopic(context, Integer.parseInt(topicId));

            return;
        }

        Pattern forumPattern = Pattern.compile(TO_FORUM);
        Matcher forumMatcher = forumPattern.matcher(link);
        if (forumMatcher.find()){
            String forumId = forumMatcher.group(1);
            //toForum(context, Integer.parseInt(forumId));
            toLink(context, link);
            return;
        }

        toLink(context, link);
    }

    private static void toForum(Context context, int fid) {
        BoardActivity.startActivity(context, fid);
    }

    private static void toUser(Context context, int userId, String username) {
        Log.d(TAG, "user " + userId);
        UserBase userBase = new UserBase(username.trim() , userId, "");
        UserInfoActivity.startActivity(context, userBase);
    }

    private static void toTopic(Context context, int topicId) {
        Log.d(TAG, "topic " + topicId);
        PostListActivity.startActivity(context, topicId);
    }

    private static void toLink(Context context, String url) {
        Log.d(TAG, "clicked " + url);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (!IntentUtil.hasActivity(context, intent)){
            Toast.makeText(context, "没有可以启动链接的程序", Toast.LENGTH_SHORT).show();
        } else {
            context.startActivity(intent);
        }
    }
}
