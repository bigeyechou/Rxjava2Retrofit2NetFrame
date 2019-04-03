package com.zhenggzh.dream.retrofitandrxjavademo.rxjavatest;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhenggzh.dream.retrofitandrxjavademo.MainActivity;
import com.zhenggzh.dream.retrofitandrxjavademo.R;
import com.zhenggzh.dream.retrofitandrxjavademo.app.BaseConstant;
import com.zhenggzh.dream.retrofitandrxjavademo.bean.WeatherRequestBean;
import com.zhenggzh.dream.retrofitandrxjavademo.bean.WeatherResponseBean;
import com.zhenggzh.dream.retrofitandrxjavademo.netsubscribe.MovieSubscribe;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultListener;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultSub;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.RetrofitFactory;
import com.zhenggzh.dream.retrofitandrxjavademo.utils.CompressUtils;
import com.zhenggzh.dream.retrofitandrxjavademo.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * RxJava测验
 */
public class RxJavaMainActivity extends AppCompatActivity {

    private static String TAG = "RxJavaMainActivity.class";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_layout);
    }


    public void rx1(View v) {
//        rxText();
//        rxFlatMap();
        rxZip();
    }

    /**
     * RxJava：zip操作符
     *
     * 示例：实现多接口共同更新UI
     */
    private void rxZip() {
        Observable<ResponseBody> observable1 = RetrofitFactory.getInstance().getHttpApi().getWeatherDataForQuery("v1", "北京");
        Observable<ResponseBody> observable2 = RetrofitFactory.getInstance().getHttpApi().getWeatherDataForQuery("v1", "上海");
        Observable.zip(observable1, observable2, new BiFunction<ResponseBody, ResponseBody, List<WeatherResponseBean>>() {
            @Override
            public List<WeatherResponseBean> apply(ResponseBody responseBody1, ResponseBody responseBody2) throws Exception {
                String result1 = CompressUtils.decompress(responseBody1.byteStream());
                String result2 = CompressUtils.decompress(responseBody2.byteStream());
                WeatherResponseBean data1 = GsonUtils.fromJson(result1, WeatherResponseBean.class);
                WeatherResponseBean data2 = GsonUtils.fromJson(result2, WeatherResponseBean.class);

                List<WeatherResponseBean> listWeather = new ArrayList<>();
                listWeather.add(data1);
                listWeather.add(data2);
                return listWeather;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<WeatherResponseBean>>() {
            @Override
            public void accept(List<WeatherResponseBean> weatherResponseBeans) throws Exception {
                Log.e(TAG,"zip合并后："+weatherResponseBeans.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG,"zip合并失败");
            }
        });
    }

    /**
     * 请求数据
     */
    private void getDouBanData() {
        MovieSubscribe.getWeatherDataForBody("北京", new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                Toast.makeText(RxJavaMainActivity.this, "请求成功：", Toast.LENGTH_SHORT).show();
                WeatherResponseBean weather = GsonUtils.fromJson(result,
                        WeatherResponseBean.class);
            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(RxJavaMainActivity.this, "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void rxFlatMap() {

    }

    private void rxText() {

        Observable.create(new ObservableOnSubscribe<Response>() {

            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {

                Request.Builder builder = new Request.Builder().url("https://www.tianqiapi.com/api")
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, WeatherResponseBean>() {
            @Override
            public WeatherResponseBean apply(Response response) throws Exception {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Log.e(TAG, "map：转换前：" + response.body());
                        return GsonUtils.fromJson(body.string(),
                                WeatherResponseBean.class);
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<WeatherResponseBean>() {
                    @Override
                    public void accept(WeatherResponseBean weatherResponseBean) throws Exception {
                        Log.e(TAG, "doOnNext: 保存成功：" + weatherResponseBean.toString() + "\n");
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResponseBean>() {
                    @Override
                    public void accept(WeatherResponseBean weatherResponseBean) throws Exception {
                        Log.e(TAG, "成功:" + weatherResponseBean.toString() + "\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "失败：" + throwable.getMessage() + "\n");
                    }
                });

    }


}
