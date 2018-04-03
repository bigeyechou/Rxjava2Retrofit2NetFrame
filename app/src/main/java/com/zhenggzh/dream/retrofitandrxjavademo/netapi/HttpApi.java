package com.zhenggzh.dream.retrofitandrxjavademo.netapi;

import com.zhenggzh.dream.retrofitandrxjavademo.bean.DouBanMovieRequest;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by 眼神 on 2018/3/27.
 *
 * 存放所有的Api
 */

public interface HttpApi {
    /**
     * 豆瓣电影
     */
    @POST("top250")
    Observable<ResponseBody> getDoubanData(@Body DouBanMovieRequest bean);

    @GET("top250")
    Observable<ResponseBody> getDoubanDatanew(@QueryMap Map<String,Integer> map);

    /**
     * 通过地址下载一个文件
     */
    @GET()
    @Streaming
    Call<ResponseBody> downloadFile(@Url String fileUrl);

}
