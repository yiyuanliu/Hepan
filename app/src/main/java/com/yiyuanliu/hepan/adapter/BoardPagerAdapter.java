package com.yiyuanliu.hepan.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yiyuanliu.hepan.data.model.Forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yiyuan on 2016/7/25.
 */
public class BoardPagerAdapter extends PagerAdapter {
    private Forum forum;
    List<View> views;

    private BoardClickedListener forumClickedListner;

    public BoardPagerAdapter(Forum forum, BoardClickedListener forumClickedListner) {
        this.forum = forum;
        this.forumClickedListner = forumClickedListner;

    }

    @Override
    public int getCount() {
        return forum.categories.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return forum.categories.get(position).categoryName;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (views == null){
            views = new ArrayList<>();
            for (int i = 0;i < getCount(); i ++){
                final int now = i;
                ListView listView = new ListView(container.getContext());

                List<Map<String, String>> mapList = new ArrayList<>();
                for (Forum.Board board : forum.categories.get(i).boardList) {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", board.name);

                    mapList.add(map);
                }

                listView.setAdapter(new SimpleAdapter(container.getContext(), mapList,
                        android.R.layout.simple_list_item_1,
                        new String[]{"name"},
                        new int[]{android.R.id.text1}));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                        forumClickedListner.onForumClicked(
                                forum.categories.get(now).boardList.get(position1));
                    }
                });

                views.add(listView);
            }
        }

        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    public interface BoardClickedListener{
        void onForumClicked(Forum.Board board);
    }
}
