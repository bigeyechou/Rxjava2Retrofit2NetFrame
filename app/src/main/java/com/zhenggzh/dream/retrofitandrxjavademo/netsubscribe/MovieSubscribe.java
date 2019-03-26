package com.zhenggzh.dream.retrofitandrxjavademo.netsubscribe;

import com.zhenggzh.dream.retrofitandrxjavademo.netutils.HttpMethods;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

import java.util.HashMap;
import java.util.Map;
import okhttp3.ResponseBody;

/**
 * Created by 眼神 on 2018/3/27.
 * 建议：把功能模块来分别存放不同的请求方法，比如登录注册类LoginSubscribe、电影类MovieSubscribe
 */

public class MovieSubscribe {
    /**
     * 获取数据
     */
    public static void getData(int pageNumber, int userId,DisposableObserver<ResponseBody> subscriber) {
        Map<String,Integer> map = new HashMap<>();
        map.put("start",pageNumber);
        map.put("count",userId);
        Observable<ResponseBody> observable =  HttpMethods.getInstance().getHttpApi().getDataForMap(map);
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取数据
     */
    public static void getDouBanData(String cityName,DisposableObserver<ResponseBody> subscriber) {
        Map<String,String> map = new HashMap<>();
        map.put("version","v1");
        map.put("city",cityName);
        Observable<ResponseBody> observable =  HttpMethods.getInstance().getHttpApi().getDataForBean(map);
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }
}
