package com.yiyuanliu.hepan.data;

import android.net.Uri;
import android.util.Log;

import com.yiyuanliu.hepan.App;
import com.yiyuanliu.hepan.data.bean.AtUser;
import com.yiyuanliu.hepan.data.bean.AttachmentRs;
import com.yiyuanliu.hepan.data.bean.AvatarBean;
import com.yiyuanliu.hepan.data.bean.Content;
import com.yiyuanliu.hepan.data.bean.ForumList;
import com.yiyuanliu.hepan.data.bean.HeartBeat;
import com.yiyuanliu.hepan.data.bean.NewsList;
import com.yiyuanliu.hepan.data.bean.NormalBean;
import com.yiyuanliu.hepan.data.bean.NotifyListPost;
import com.yiyuanliu.hepan.data.bean.NotifyListSys;
import com.yiyuanliu.hepan.data.bean.PmAdmin;
import com.yiyuanliu.hepan.data.bean.PmJson;
import com.yiyuanliu.hepan.data.bean.PmList;
import com.yiyuanliu.hepan.data.bean.PmListJson;
import com.yiyuanliu.hepan.data.bean.PmSession;
import com.yiyuanliu.hepan.data.bean.PostList;
import com.yiyuanliu.hepan.data.bean.SettingGetter;
import com.yiyuanliu.hepan.data.bean.SettingRs;
import com.yiyuanliu.hepan.data.bean.TopicAdmin;
import com.yiyuanliu.hepan.data.bean.TopicList;
import com.yiyuanliu.hepan.data.bean.UserInfo;
import com.yiyuanliu.hepan.data.bean.UserList;
import com.yiyuanliu.hepan.data.bean.UserLogin;
import com.yiyuanliu.hepan.data.bean.VoteRs;
import com.yiyuanliu.hepan.data.model.AtUserList;
import com.yiyuanliu.hepan.data.model.Forum;
import com.yiyuanliu.hepan.data.model.NotifyPost;
import com.yiyuanliu.hepan.data.model.NotifySys;
import com.yiyuanliu.hepan.data.model.Pm;
import com.yiyuanliu.hepan.data.model.PmMessage;
import com.yiyuanliu.hepan.data.model.Rate;
import com.yiyuanliu.hepan.data.model.RateInfo;
import com.yiyuanliu.hepan.data.model.UserBase;
import com.yiyuanliu.hepan.span.ImageTag;
import com.yiyuanliu.hepan.utils.HepanException;
import com.yiyuanliu.hepan.utils.PathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Func1;

/**
 * Created by yiyuan on 2016/7/10.
 */
public class Api {
    public static final String BASE_URL = "http://bbs.uestc.edu.cn/mobcent/app/web/";

    public static final String USER_LOGIN = "login";
    public static final String USER_LOGOUT = "logout";

    public static final String TOPIC_SORT_NEW = "new";
    public static final String TOPIC_SORT_ALL = "all";

    public static final String USER_TOKEN = "accessToken";
    public static final String USER_SECRET = "accessSecret";
    public static final String USER_HASH = "apphash";

    public static final String NOTIFY_POST = "post";
    public static final String NOTIFY_SYSTEM = "system";
    public static final String NOTIFY_AT = "at";

    public static final String ATTACHMENT_MODULE_FORUM = "forum";
    public static final String ATTACHMENT_MODULE_ALBUM = "album";

    public static final String ATTACHMENT_TYPE_IMAGE = "image";
    public static final String ATTACHMENT_TYPE_AUDIO = "audio";

    public static final String TOPIC_ADMIN_NEW = "new";
    public static final String TOPIC_ADMIN_REPLY = "reply";

    public static final int ORDER_DEF = 0;
    public static final int ORDER_LAST = 1;

    public WebApi getWebApi() {
        return webApi;
    }

    private WebApi webApi;

    private volatile Forum forum;
    private volatile com.yiyuanliu.hepan.data.model.UserInfo myInfo;

    private volatile SettingRs mSettingRs;

    private volatile LoginListener loginListener;

    private volatile AtUserList atUserList;

    public Api(){
        webApi = new Retrofit.Builder().baseUrl(BASE_URL)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(WebApi.class);
    }

    public void setLoginListener(LoginListener loginListener){
        this.loginListener = loginListener;
    }

    public Observable<Boolean> userLogin(String username, String password){
        return webApi.userLogin(username, password ,USER_LOGIN)
                .map(new Func1<UserLogin, Boolean>() {
                    @Override
                    public Boolean call(UserLogin userLogin) {
                        Log.d("TAG",userLogin.getRs() + " ");
                        if (userLogin.getRs() == 1){
                            loginListener.onLoginSuccess(userLogin);
                        }
                        return userLogin.getRs() == 1;
                    }
                });
    }

    public Observable<com.yiyuanliu.hepan.data.model.TopicList> topicList(int boardId,
                                                                          int page,
                                                                          String sortby,
                                                                          Map<String, String> userMap,
                                                                          boolean isUserTopic){
        if (isUserTopic){
            return webApi.userTopic(boardId, page, userMap)
                    .map(new Func1<TopicList, com.yiyuanliu.hepan.data.model.TopicList>() {
                        @Override
                        public com.yiyuanliu.hepan.data.model.TopicList call(TopicList topicList) {
                            HepanException.detectRespon(topicList);
                            return new com.yiyuanliu.hepan.data.model.TopicList(topicList);
                        }
                    });

        }else if (boardId == -1){
            return webApi.newsList(page, userMap)
                    .map(new Func1<NewsList, com.yiyuanliu.hepan.data.model.TopicList>() {
                        @Override
                        public com.yiyuanliu.hepan.data.model.TopicList call(NewsList newsList) {
                            HepanException.detectRespon(newsList);
                            return new com.yiyuanliu.hepan.data.model.TopicList(newsList);
                        }
                    });
        }else {
            return webApi.topicList(boardId, page, sortby, userMap)
                    .map(new Func1<TopicList, com.yiyuanliu.hepan.data.model.TopicList>() {
                        @Override
                        public com.yiyuanliu.hepan.data.model.TopicList call(TopicList topicList) {
                            HepanException.detectRespon(topicList);
                            return new com.yiyuanliu.hepan.data.model.TopicList(topicList);
                        }
                    });
        }
    }

    public Observable<Forum> loadForum(Map<String, String> userMap, boolean update){
        if (forum == null){
            update = true;
        }

        if (!update){
            return Observable.just(forum);
        } else {
            return webApi.forumList(0, userMap)
                    .map(new Func1<ForumList, Forum>() {
                        @Override
                        public Forum call(ForumList forumList) {
                            HepanException.detectRespon(forumList);
                            forum = new Forum(forumList);
                            return forum;
                        }
                    });
        }
    }

    public Observable<Forum.Board> loadBoard(final Forum.Board board, Map<String, String> userMap){
        if (board == null){
            throw new RuntimeException("board can't be a null object");
        }

        if (board.childBoard.size() != 0 || !board.hasChild){
            Log.d("jjj","just");
            return Observable.just(board);
        } else {
            return webApi.forumList(board.boardId,userMap)
                    .map(new Func1<ForumList, Forum.Board>() {
                        @Override
                        public Forum.Board call(ForumList forumList) {
                            HepanException.detectRespon(forumList);
                            board.setChildBoard(forumList);
                            return board;
                        }
                    });
        }
    }

    //TODO: 2016/8/7 通过boardId来获取board
//
//    public Observable<Forum.Board> loadBoard(int boardId, Map<String, String> userMap){
//
//            return webApi.forumList(boardId,userMap)
//                    .map(new Func1<ForumList, Forum.Board>() {
//                        @Override
//                        public Forum.Board call(ForumList forumList) {
//                            Forum.Board board = new Forum.Board(forumList);
//                            return board;
//                        }
//                    });
//
//    }

    public Observable<NotifySys.NotifySysList> loadNotifySys(int page, Map<String, String> userMap) {
        return webApi.notifyListSys(page, userMap)
                .map(new Func1<NotifyListSys, NotifySys.NotifySysList>() {
                    @Override
                    public NotifySys.NotifySysList call(NotifyListSys notifyListSys) {
                        HepanException.detectRespon(notifyListSys);
                        return new NotifySys.NotifySysList(notifyListSys);
                    }
                });
    }

    public Observable<NotifyPost.NotifyPostList> loadNotifyPost(int page, Map<String, String> userMap){
        return webApi.notifyListPost(page, userMap)
                .map(new Func1<NotifyListPost, NotifyPost.NotifyPostList>() {
                    @Override
                    public NotifyPost.NotifyPostList call(NotifyListPost notifyListPost) {
                        HepanException.detectRespon(notifyListPost);
                        return new NotifyPost.NotifyPostList(notifyListPost);
                    }
                });
    }

    public Observable<NotifyPost.NotifyPostList> loadNotifyAt(int page, Map<String, String> userMap) {
        return webApi.notifyListAt(page, userMap)
                .map(new Func1<NotifyListPost, NotifyPost.NotifyPostList>() {
                    @Override
                    public NotifyPost.NotifyPostList call(NotifyListPost notifyListPost) {
                        HepanException.detectRespon(notifyListPost);
                        return new NotifyPost.NotifyPostList(notifyListPost);
                    }
                });
    }

    public Observable<Pm.PmList> loadPmList(PmJson pmJson, Map<String, String> userMap){
        return webApi.pmSessionList(pmJson, userMap)
                .map(new Func1<PmSession, Pm.PmList>() {
                    @Override
                    public Pm.PmList call(PmSession pmSession) {
                        HepanException.detectRespon(pmSession);
                        return new Pm.PmList(pmSession);
                    }
                });
    }

    public Observable<com.yiyuanliu.hepan.data.model.UserInfo>
                    loadUserInfo(final UserBase userBase, Map<String, String> userMap){
        return webApi.userInfo(userBase.userId, userMap)
                .map(new Func1<UserInfo, com.yiyuanliu.hepan.data.model.UserInfo>() {
                    @Override
                    public com.yiyuanliu.hepan.data.model.UserInfo call(UserInfo userInfo) {
                        HepanException.detectRespon(userInfo);
                        return com.yiyuanliu.hepan.data.model.UserInfo.newInstance(userInfo, userBase);
                    }
                });
    }

    public Observable<PostList> loadPostList(int topicId, int page, int order, Map<String, String> userMap){
        return webApi.postList(topicId, page, order, userMap);
    }

    public Observable<com.yiyuanliu.hepan.data.model.UserList> loadUserList(int page, String type, Map<String, String> userMap){
        return webApi.userList(page, type, userMap)
                .map(new Func1<UserList, com.yiyuanliu.hepan.data.model.UserList>() {
                    @Override
                    public com.yiyuanliu.hepan.data.model.UserList call(UserList userList) {
                        HepanException.detectRespon(userList);
                        return com.yiyuanliu.hepan.data.model.UserList.newInstance(userList);
                    }
                });
    }

    public Observable<Map<Uri, String>> sendImg(String module, final List<Uri> uriList, Map<String, String> userMap){
        List<MultipartBody.Part> partList = new ArrayList<>();

        for (Uri uri:uriList){
            String path = PathUtil.getPath(App.getApp(), uri);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"),new File(path));
            MultipartBody.Part part = MultipartBody.Part.createFormData("uploadFile[]", path, requestBody);
            partList.add(part);
        }

        MultipartBody.Part[] parts = new MultipartBody.Part[partList.size()];
        partList.toArray(parts);


        return webApi.sendAttachment(ATTACHMENT_TYPE_IMAGE, module, userMap, parts)
                .map(new Func1<AttachmentRs, Map<Uri, String>>() {
                    @Override
                    public Map<Uri, String> call(AttachmentRs attachmentRs) {
                        if (attachmentRs.getRs() != 1){
                            throw new HepanException(attachmentRs.getErrorInfo());
                        }

                        Map<Uri, String> map = new HashMap<>();

                        List<AttachmentRs.BodyBean.Attachment> attachments = attachmentRs.body.attachment;
                        for (int i = 0;i < attachments.size();i ++){
                            map.put(uriList.get(i), attachments.get(i).urlName);
                        }
                        return map;
                    }
                });
    }

    public Observable<NormalBean> newTopic(final int fid, final int classificationId,
                                           final String title,
                                           final String content, final List<Uri> uriList,
                                           final Map<String, String> userMap) {
        if (uriList == null || uriList.size() == 0) {
            TopicAdmin topicAdmin = new TopicAdmin();

            topicAdmin.getBody().getJson().setFid(fid);
            topicAdmin.getBody().getJson().setTitle(title);
            topicAdmin.getBody().getJson().setTypeId(classificationId);

            Content content1 = new Content();
            content1.type = Content.TYPE_NORMAL;
            content1.infor = content;
            topicAdmin.getBody().getJson().addContent(content1);

            return webApi.topicAdmin("new", topicAdmin, userMap);
        }

        return sendImg(ATTACHMENT_MODULE_FORUM, uriList, userMap)
                .flatMap(new Func1<Map<Uri, String>, Observable<NormalBean>>() {
                    @Override
                    public Observable<NormalBean> call(Map<Uri, String> uriStringMap) {
                        TopicAdmin topicAdmin = new TopicAdmin();

                        topicAdmin.getBody().getJson().setFid(fid);
                        topicAdmin.getBody().getJson().setTitle(title);
                        topicAdmin.getBody().getJson().setTypeId(classificationId);

                        Content content1 = new Content();
                        content1.type = Content.TYPE_NORMAL;
                        content1.infor = content;
                        topicAdmin.getBody().getJson().addContent(content1);

                        for (Uri uri:uriList) {
                            Content content2 = new Content();
                            content2.type = Content.TYPE_PICTURE;
                            content2.infor = uriStringMap.get(uri);

                            topicAdmin.getBody().getJson().addContent(content2);
                        }

                        return webApi.topicAdmin("new", topicAdmin, userMap);
                    }
                });
    }

    public Observable<NormalBean> newTopic(final int fid, final int classificationId,
                                           final String title,
                                           final List<Object> contentList,
                                           final Map<String, String> userMap) {
        final List<Uri> uriList = new ArrayList<>();
        for (Object o: contentList) {
            if (o instanceof Uri) {
                uriList.add((Uri) o);
            }
        }

        if (uriList.size() == 0) {
            TopicAdmin topicAdmin = new TopicAdmin();

            topicAdmin.getBody().getJson().setFid(fid);
            topicAdmin.getBody().getJson().setTitle(title);
            topicAdmin.getBody().getJson().setTypeId(classificationId);

            for (Object o: contentList) {
                String s = o.toString();
                Content content1 = new Content();
                content1.type = Content.TYPE_NORMAL;
                content1.infor = s;
                topicAdmin.getBody().getJson().addContent(content1);
            }

            return webApi.topicAdmin("new", topicAdmin, userMap);
        }

        return sendImg(ATTACHMENT_MODULE_FORUM, uriList, userMap)
                .flatMap(new Func1<Map<Uri, String>, Observable<NormalBean>>() {
                    @Override
                    public Observable<NormalBean> call(Map<Uri, String> uriStringMap) {
                        TopicAdmin topicAdmin = new TopicAdmin();

                        topicAdmin.getBody().getJson().setFid(fid);
                        topicAdmin.getBody().getJson().setTitle(title);
                        topicAdmin.getBody().getJson().setTypeId(classificationId);

                        for (Object o: contentList) {
                            if (o instanceof String) {
                                Content content1 = new Content();
                                content1.type = Content.TYPE_NORMAL;
                                content1.infor = o.toString();
                                topicAdmin.getBody().getJson().addContent(content1);
                            }

                            if (o instanceof Uri) {
                                Uri uri = (Uri) o;
                                Content content2 = new Content();
                                content2.type = Content.TYPE_PICTURE;
                                content2.infor = uriStringMap.get(uri);

                                topicAdmin.getBody().getJson().addContent(content2);
                            }
                        }
                        return webApi.topicAdmin("new", topicAdmin, userMap);
                    }
                });
    }

    public Observable<NormalBean> reply(final int tid,
                                        final int replyId,
                                        final List<Object> contentList,
                                        final Map<String, String> userMap) {
        final List<Uri> uriList = new ArrayList<>();
        for (Object o: contentList) {
            if (o instanceof ImageTag) {
                uriList.add(((ImageTag) o).getUri());
            }
        }

        if (uriList.size() == 0) {
            TopicAdmin topicAdmin = new TopicAdmin();
            topicAdmin.getBody().getJson().setTid(tid);
            topicAdmin.getBody().getJson().setReplyId(replyId);
            if (replyId != 0){
                topicAdmin.getBody().getJson().setIsQuote(1);
            }

            for(Object o:contentList) {
                String s = (String) o;

                Content content1 = new Content();
                content1.type = Content.TYPE_NORMAL;
                content1.infor = s;

                topicAdmin.getBody().getJson().addContent(content1);
            }

            return webApi.topicAdmin("reply", topicAdmin, userMap);
        }

        return sendImg(ATTACHMENT_MODULE_FORUM, uriList, userMap)
                .flatMap(new Func1<Map<Uri, String>, Observable<NormalBean>>() {
                    @Override
                    public Observable<NormalBean> call(Map<Uri, String> uriStringMap) {
                        TopicAdmin topicAdmin = new TopicAdmin();
                        topicAdmin.getBody().getJson().setTid(tid);
                        topicAdmin.getBody().getJson().setReplyId(replyId);
                        if (replyId != 0){
                            topicAdmin.getBody().getJson().setIsQuote(1);
                        }

                        for (Object o:contentList) {
                            if (o instanceof String) {
                                String s = (String) o;

                                Content content1 = new Content();
                                content1.type = Content.TYPE_NORMAL;
                                content1.infor = s;

                                topicAdmin.getBody().getJson().addContent(content1);
                            }

                            if (o instanceof ImageTag) {
                                Content content2 = new Content();
                                content2.type = Content.TYPE_PICTURE;
                                content2.infor = uriStringMap.get(((ImageTag) o).getUri());

                                topicAdmin.getBody().getJson().addContent(content2);
                            }
                        }

                        return webApi.topicAdmin("reply", topicAdmin, userMap);
                    }
                });

    }

    public Observable<Void> updateAvatar(final Map<String, String> stringMap) {
        File file = new File(App.getApp().getExternalCacheDir(), "avatar");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("userAvatar", file.getName(), requestBody);

        return webApi.uploadAvatar(part, stringMap)
                .map(new Func1<NormalBean, Void>() {
                    @Override
                    public Void call(NormalBean normalBean) {
                        HepanException.detectRespon(normalBean);
                        return null;
                    }
                });
    }

    public Observable<SettingRs> loadSetting(Map<String, String> userMap) {
        if (mSettingRs == null) {
            return webApi.getSetting(new SettingGetter(), userMap)
                    .map(new Func1<SettingRs, SettingRs>() {
                        @Override
                        public SettingRs call(SettingRs settingRs) {
                            HepanException.detectRespon(settingRs);
                            mSettingRs = settingRs;
                            return settingRs;
                        }
                    });
        } else {
            return Observable.just(mSettingRs);
        }
    }

    public Observable<PmMessage.PmMessageList> loadPmList(int from, long endTime,int cache, Map<String, String> userMap) {
        PmListJson pmListJson = new PmListJson();
        pmListJson.setBody(new PmListJson.BodyBean());
        PmListJson.BodyBean.PmInfosBean pmInfosBean = new PmListJson.BodyBean.PmInfosBean();
        pmListJson.getBody().setPmInfos(new ArrayList<PmListJson.BodyBean.PmInfosBean>());
        pmListJson.getBody().getPmInfos().add(pmInfosBean);
        pmInfosBean.setFromUid(from);
        pmInfosBean.setStartTime(0);
        pmInfosBean.setStopTime(endTime);
        pmInfosBean.setPmLimit(25);
        pmInfosBean.setCacheCount(cache);

        return webApi.getPmList(pmListJson, userMap)
                .map(new Func1<PmList, PmMessage.PmMessageList>() {
                    @Override
                    public PmMessage.PmMessageList call(PmList pmList) {
                        HepanException.detectRespon(pmList);
                        return new PmMessage.PmMessageList(pmList);
                    }
                });
    }

    public Observable<Void> sendMessage(int toUser, String content, Map<String, String> userMap) {
        PmAdmin pmAdmin = new PmAdmin();
        pmAdmin.setAction("send");
        pmAdmin.setMsg(new PmAdmin.MsgBean());
        pmAdmin.getMsg().setContent(content);
        pmAdmin.getMsg().setType("text");
        pmAdmin.setToUid(toUser);

        return webApi.sendMessage(pmAdmin, userMap)
                .map(new Func1<NormalBean, Void>() {
                    @Override
                    public Void call(NormalBean normalBean) {
                        HepanException.detectRespon(normalBean);
                        return null;
                    }
                });
    }

    public Observable<Void> updateUserSign(final String sign, Map<String, String> userMap) {
        return webApi.updateUserSign(sign, userMap)
                .map(new Func1<NormalBean, Void>() {
                    @Override
                    public Void call(NormalBean normalBean) {
                        HepanException.detectRespon(normalBean);
                        myInfo.sign = sign;
                        return null;
                    }
                });
    }

    public Observable<com.yiyuanliu.hepan.data.model.UserInfo> loadMyInfo() {
        if (myInfo != null) {
            return Observable.just(myInfo);
        } else {
            DataManager dataManager = DataManager.getInstance(App.getApp());
            final UserBase userBase = new UserBase(dataManager.getAccountManager().getUserName(),
                    dataManager.getAccountManager().getUid(),
                    dataManager.getAccountManager().getUserAvatar());
            return webApi.userInfo(dataManager.getAccountManager().getUid(), dataManager.getAccountManager().getUserMap())
                    .map(new Func1<UserInfo, com.yiyuanliu.hepan.data.model.UserInfo>() {
                        @Override
                        public com.yiyuanliu.hepan.data.model.UserInfo call(UserInfo userInfo) {
                            myInfo = com.yiyuanliu.hepan.data.model.UserInfo.newInstance(userInfo, userBase);
                            return myInfo;
                        }
                    });
        }
    }

    public Observable<AtUserList> loadAtUser(Map<String, String> userMap) {
        if (atUserList != null) {
            return Observable.just(atUserList);
        } else {
            return webApi.getAtUser(100, userMap)
                    .map(new Func1<AtUser, AtUserList>() {
                        @Override
                        public AtUserList call(AtUser atUser) {
                            atUserList = new AtUserList(atUser);
                            return atUserList;
                        }
                    });
        }
    }

    public Observable<RateInfo> loadRateInfo(final String url) {
        return Observable.create(new Observable.OnSubscribe<RateInfo>() {
            @Override
            public void call(Subscriber<? super RateInfo> subscriber) {
                subscriber.onNext(RateInfo.loadRateInfo(url));
            }
        });
    }

    public Observable<Rate> rate(final String url, final int score, final String info, final boolean notifyUser) {
        return Observable.create(new Observable.OnSubscribe<Rate>() {
            @Override
            public void call(Subscriber<? super Rate> subscriber) {
                subscriber.onNext(Rate.rate(url, score, info, notifyUser));
            }
        });
    }

    public interface WebApi{
        @FormUrlEncoded
        @POST("index.php?r=user/login")
        Observable<UserLogin> userLogin(@Field("username") String username,
                                        @Field("password") String password,
                                        @Field("type") String type);

        @FormUrlEncoded
        @POST("index.php?r=forum/topiclist&pageSize=25")
        Observable<TopicList> topicList(@Field("boardId") int boardId,
                                        @Field("page") int page,
                                        @Field("sortby") String sortBy,
                                        @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=forum/forumlist")
        Observable<ForumList> forumList(@Field("fid") int fid,
                                        @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=message/notifylistex&type=post")
        Observable<NotifyListPost> notifyListPost(@Field("page") int page,
                                                  @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=message/notifylistex&type=system")
        Observable<NotifyListSys> notifyListSys(@Field("page") int page,
                                                @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=message/notifylistex&type=at")
        Observable<NotifyListPost> notifyListAt(@Field("page") int page,
                                                @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=message/pmsessionlist")
        Observable<PmSession> pmSessionList(@Field("json") PmJson pmJson,
                                            @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=user/userinfo")
        Observable<UserInfo> userInfo(@Field("userId") int userId,
                                      @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=user/topiclist&type=topic")
        Observable<TopicList> userTopic(@Field("uid") int userId,
                                        @Field("page") int page,
                                        @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=forum/postlist&pageSize=25")
        Observable<PostList> postList(@Field("topicId") int topicId,
                                      @Field("page") int page,
                                      @Field("order") int order,
                                      @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=portal/newslist&moduleId=2")
        Observable<NewsList> newsList(@Field("page") int page,
                                      @FieldMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=user/userlist")
        Observable<UserList> userList(@Field("page") int page,
                                      @Field("type") String type,
                                      @FieldMap Map<String, String> userMap);

        @Multipart
        @POST("index.php?r=forum/sendattachmentex")
        Observable<AttachmentRs> sendAttachment(@Query("type") String type,
                                                @Query("module") String module,
                                                 @QueryMap Map<String,String> map,
                                                @Part MultipartBody.Part... files);

        @FormUrlEncoded
        @POST("index.php?r=forum/topicadmin")
        Observable<NormalBean> topicAdmin(@Field("act") String act,
                                        @Field("json")TopicAdmin topicAdmin,
                                        @FieldMap Map<String,String> map);

        @FormUrlEncoded
        @POST("index.php?r=forum/vote")
        Observable<VoteRs> vote(@Field("tid") int tid,
                                @Field("options") String option,
                                @FieldMap Map<String, String> map);

        @FormUrlEncoded
        @POST("index.php?r=user/getsetting")
        Observable<SettingRs> getSetting(@Field("getSetting") SettingGetter settingGetter,
                                         @FieldMap Map<String,String> map);

        @FormUrlEncoded
        @POST("index.php?r=message/pmlist")
        Observable<PmList> getPmList(@Field("pmlist") PmListJson pmJson,
                                     @FieldMap Map<String, String> map);

        @FormUrlEncoded
        @POST("index.php?r=message/pmadmin")
        Observable<NormalBean> sendMessage(@Field("json") PmAdmin pmAdmin,
                                           @FieldMap Map<String, String> map);

        @FormUrlEncoded
        @POST("index.php?r=message/heart")
        Call<HeartBeat> heart(@FieldMap Map<String, String> map);

        @FormUrlEncoded
        @POST("index.php?r=user/updateuserinfo&type=info")
        Observable<NormalBean> updateUserSign(@Field("sign") String sign, @FieldMap Map<String, String> userMap);

        @Multipart
        @POST("index.php?r=user/uploadavatarex")
        Observable<AvatarBean> uploadAvatar(@Part MultipartBody.Part file, @QueryMap Map<String, String> userMap);

        @FormUrlEncoded
        @POST("index.php?r=forum/atuserlist")
        Observable<AtUser> getAtUser(@Field("pageSize") int pageSize, @FieldMap Map<String,String> map);


    }

    public interface LoginListener{
        void onLoginSuccess(UserLogin userLogin);
        void onAccountFailed();
    }
}
