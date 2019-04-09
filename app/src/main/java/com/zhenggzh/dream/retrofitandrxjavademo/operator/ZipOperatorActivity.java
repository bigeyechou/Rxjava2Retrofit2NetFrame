package com.zhenggzh.dream.retrofitandrxjavademo.operator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.zhenggzh.dream.retrofitandrxjavademo.R;
import com.zhenggzh.dream.retrofitandrxjavademo.bean.WeatherResponseBean;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.RetrofitFactory;
import com.zhenggzh.dream.retrofitandrxjavademo.utils.CompressUtils;
import com.zhenggzh.dream.retrofitandrxjavademo.utils.GsonUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * RxJava zip操作符（组合操作符）
 */
public class ZipOperatorActivity extends AppCompatActivity {

    private static String TAG = "RxJavaMainActivity";
    private final String mRxJavaExplain = "zip操作符:\n" +
            "按照自己的规则发射与发射数据项最少的相同的数据。\n" +
            "合并多个被观察者(Observable)的数据流然后发送合并后的数据流。\n" +
            "严格按照顺序来发送\n" +
            "应用实例：多用于合并多个网络请求成一个，实现多个接口数据共同更新UI，如List插入头数据、多个接口里面的数据合并成一个你想要的数据。";

    @Bind(R.id.btn_zip_explain)
    Button btnZipExplain;
    @Bind(R.id.btn_zip)
    Button btnZip;
    @Bind(R.id.btn_zip_more)
    Button btnZipMore;
    @Bind(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_operator_layout);
        ButterKnife.bind(this);

        tvResult.setText(mRxJavaExplain);
    }

    @OnClick({R.id.btn_zip_explain, R.id.btn_zip, R.id.btn_zip_more})
    public void onClickButton(Button button) {
        switch (button.getId()) {
            case R.id.btn_zip:
                rxZip();
                break;
            case R.id.btn_zip_more:
                rxZipMore();
                break;
            case R.id.btn_zip_explain:
                tvResult.setText(mRxJavaExplain);
                break;
        }
    }

    /**
     * RxJava：zip操作符
     * 把北京和上海的天气WeatherResponseBean 成 List<WeatherResponseBean>
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
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<WeatherResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<WeatherResponseBean> weatherResponseBeans) {
                        Log.e(TAG, "zip合并后：" + weatherResponseBeans.toString());
                        tvResult.setText("zip合并后：" + weatherResponseBeans.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        tvResult.setText("zip合并失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void rxZipMore() {
        Observable.zip(Observable.just(1, 8, 7), Observable.just(2, 5),
                Observable.just(3, 6), Observable.just(4, 9, 0), new Function4<Integer, Integer, Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2, Integer integer3, Integer integer4) {
                        return integer < integer2 ? integer3 : integer4;
                    }
                }).subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer integer) {
                Log.e(TAG, "onNext--> " + integer);
                tvResult.setText("zip合并成功");
            }

        }, new Consumer<Throwable>() {

            @Override
            public void accept(Throwable throwable) {
                Log.e(TAG, "onError--> " + throwable.getMessage());
                tvResult.setText("zip合并失败");
            }
        });
    }
}
