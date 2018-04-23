package com.zhenggzh.dream.retrofitandrxjavademo.netsubscribe;

import com.zhenggzh.dream.retrofitandrxjavademo.bean.DouBanMovieRequest;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.HttpMethods;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by 眼神 on 2018/3/27.
 * 建议：把功能模块来分别存放不同的请求方法，比如登录注册类LoginSubscribe、电影类MovieSubscribe
 */

public class MovieSubscribe {
    /**
     * 获取豆瓣电影
     */
    public static void getDouBanMovie(Subscriber<ResponseBody> subscriber,int start,int count) {
        Map<String,Integer> map = new HashMap<>();
        map.put("start",start);
        map.put("count",count);

        Observable observable =  HttpMethods.getInstance().getHttpApi().getDoubanDatanew(map);
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }
}
