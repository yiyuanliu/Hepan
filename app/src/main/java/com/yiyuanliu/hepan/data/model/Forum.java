package com.yiyuanliu.hepan.data.model;

import com.yiyuanliu.hepan.data.bean.ForumList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyuan on 2016/7/13.
 */
public class Forum {
    public Forum(ForumList forumList) {
        categories = new ArrayList<>();

        for (ForumList.ListBean item:forumList.getList()){
            categories.add(new Category(item));
        }
    }

    public List<Category> categories;

    public static class Category{
        public Category(ForumList.ListBean item) {
            categoryName = item.getBoard_category_name();
            gid = item.getBoard_category_id();
            boardList = new ArrayList<>();

            for (ForumList.ListBean.BoardListBean board:item.getBoard_list()){
                boardList.add(new Board(board));
            }
        }

        public String categoryName;
        public int gid;
        public List<Board> boardList;
    }

    public static class Board implements Serializable{
        public Board(ForumList.ListBean.BoardListBean board) {
            name = board.getBoard_name();
            boardId = board.getBoard_id();
            hasChild = board.getBoard_child() == 1;
            hasContent = board.getBoard_content() == 1;
        }

        public String name;
        public int boardId;

        public boolean hasContent;
        public boolean hasChild;
        public List<Board> childBoard = new ArrayList<>();

        public Board() {

        }

        public void setChildBoard(ForumList forumList) {
            if (!hasChild || forumList.getList() == null || forumList.getList().size() == 0){
                return;
            } else {
                Forum forum = new Forum(forumList);
                childBoard = forum.categories.get(0).boardList;
            }
        }
    }
}
