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
