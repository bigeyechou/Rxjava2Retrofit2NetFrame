package com.zhenggzh.dream.retrofitandrxjavademo.operator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhenggzh.dream.retrofitandrxjavademo.R;
import com.zhenggzh.dream.retrofitandrxjavademo.bean.WeatherResponseBean;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultSub;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.RetrofitFactory;
import com.zhenggzh.dream.retrofitandrxjavademo.utils.CompressUtils;
import com.zhenggzh.dream.retrofitandrxjavademo.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
 * RxJava zip操作符
 */
public class ZipOperatorActivity extends AppCompatActivity {

    private final String mRxJavaExplain = "zip操作符:\n" +
            "合并多个被观察者的数据流然后发送合并后的数据流。\n" +
            "严格按照顺序来发送";
    private static String TAG = "RxJavaMainActivity.class";
    @Bind(R.id.btn_zip_explain)
    Button btnZipExplain;
    @Bind(R.id.btn_zip)
    Button btnZip;
    @Bind(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_operator_layout);
        ButterKnife.bind(this);

        tvResult.setText(mRxJavaExplain);
    }

    @OnClick({R.id.btn_zip_explain, R.id.btn_zip})
    public void onClickButton(Button button) {
        switch (button.getId()) {
            case R.id.btn_zip:
                rxZip();
                break;
            case R.id.btn_zip_explain:
                break;
        }
    }

    /**
     * RxJava：zip操作符
     */
    @SuppressLint("CheckResult")
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
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<WeatherResponseBean>>() {
            @Override
            public void accept(List<WeatherResponseBean> weatherResponseBeans) throws Exception {
                Log.e(TAG, "zip合并后：" + weatherResponseBeans.toString());
                tvResult.setText("zip合并后：" + weatherResponseBeans.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "zip合并失败");
                tvResult.setText("zip合并失败");
            }
        });
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
