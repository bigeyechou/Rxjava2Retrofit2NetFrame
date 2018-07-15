package com.zhenggzh.dream.retrofitandrxjavademo.netapi;

import com.zhenggzh.dream.retrofitandrxjavademo.bean.DouBanMovieRequest;

import io.reactivex.Observable;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 眼神 on 2018/3/27.
 *
 * 存放所有的Api
 */

public interface HttpApi {
    //请填写自己的接口名
    @POST("")
    Observable<ResponseBody> getDataForBean(@Body DouBanMovieRequest bean);
    //请填写自己的接口名
    @GET("")
    Observable<ResponseBody> getDataForMap(@QueryMap Map<String,Integer> map);

    /**
     * 通过地址下载一个文件
     */
    @GET()
    @Streaming
    Call<ResponseBody> downloadFile(@Url String fileUrl);

}
