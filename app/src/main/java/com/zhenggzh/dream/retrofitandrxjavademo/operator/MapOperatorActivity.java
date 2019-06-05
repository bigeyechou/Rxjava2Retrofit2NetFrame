package com.zhenggzh.dream.retrofitandrxjavademo.operator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * RxJava Map操作符(变换操作符)
 */
public class MapOperatorActivity extends AppCompatActivity {
    private static String TAG = "MapOperatorActivity";
    private final String mRxJavaExplain = "map操作符:\n" +
            "将一个被观察者(Observable)对象通过某种关系转换为另一个被观察者（Observable）对象。\n" +
            "严格按照顺序来发送" +
            "应用实例：网络请求的Response转换成相应的Bean";

    @Bind(R.id.btn_map_explain)
    AppCompatButton btnMapExplain;
    @Bind(R.id.btn_map)
    AppCompatButton btnMap;
    @Bind(R.id.tv_result)
    AppCompatButton tvResult;
    @Bind(R.id.btn_map_no_retrofit_backpressure)
    AppCompatButton btnMapNoRetrofitBackPressure;
    @Bind(R.id.btn_map_no_retrofit)
    AppCompatButton btnMapNoRetrofit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_operator_layout);
        ButterKnife.bind(this);
        tvResult.setText(mRxJavaExplain);
    }

    @OnClick({R.id.btn_map_explain, R.id.btn_map, R.id.btn_map_no_retrofit_backpressure, R.id.btn_map_no_retrofit})
    public void onClickButton(AppCompatButton button) {
        switch (button.getId()) {
            case R.id.btn_map_explain:
                tvResult.setText(mRxJavaExplain);
                break;
            case R.id.btn_map:
                rxMap();
                break;
            case R.id.btn_map_no_retrofit_backpressure:
                rxMapNoRetrofitBackPressure();
                break;
            case R.id.btn_map_no_retrofit:
                rxMapNoRetrofit();
                break;
            default:
                tvResult.setText(mRxJavaExplain);
                break;
        }
    }
/**
 * 解释Observer Subscriber Consumer 关系
 * Subscriber 对 Observer 接口进行了一些扩展， Consumer是简易版的Observer，他有多重重载，可以自定义你需要处理的信息
 * -------------------------------------------------------------------------------------------------------
 * 解释Schedulers.io() Schedulers.newThread() AndroidSchedulers.mainThread()
 * newThread() 会为每个实例创建一个新线程，io() 使用的是线程池来管理，mainThread()运行在主线程中
 */
    /**
     * 不使用封装好的retrofit
     */
    @SuppressLint("CheckResult")
    private void rxMapNoRetrofit() {
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
                        tvResult.setText("不使用retrofit：" + weatherResponseBean.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "失败：" + throwable.getMessage() + "\n");
                        tvResult.setText("不使用retrofit:失败");
                    }
                });
    }

    /**
     * 不使用封装好的retrofit但支持背压的
     * <p>
     * 什么是背压？
     * {当上下游在不同的线程中，通过Observable发射，处理，响应数据流时，如果上游发射数据的速度快于下游接收处理数据的速度，
     * 这样对于那些没来得及处理的数据就会造成积压，这些数据既不会丢失，也不会被垃圾回收机制回收，而是存放在一个异步缓存池中，
     * 如果缓存池中的数据一直得不到处理，越积越多，最后就会造成内存溢出，这便是响应式编程中的背压（backpressure）问题。}
     * <p>
     * BackpressureStrategy状态：
     * MISSING:OnNext发出的事件不做任何缓冲或删除操作
     * ERROR:抛出一个MissingBackpressureException异常，以防下游无法跟上
     * BUFFER:缓冲所有next值，直到下游使用它,容易oom
     * DROP:如果下游无法跟上，则会下跌最近的onNext值
     * LATEST:只保留最新的onNext值，如果下游无法跟上，则覆盖以前的任何值
     */
    private void rxMapNoRetrofitBackPressure() {
        Flowable.create(new FlowableOnSubscribe<Response>() {
            @Override
            public void subscribe(FlowableEmitter<Response> emitter) throws Exception {
                Request.Builder builder = new Request.Builder().url("https://www.tianqiapi.com/api")
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }, BackpressureStrategy.BUFFER)//create方法中多了一个BackpressureStrategy类型的参数
                .map(new Function<Response, WeatherResponseBean>() {
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
                })
                .subscribeOn(Schedulers.io())//subscribeOn顾名思义，改变了上游的subscribe所在的线程，而在 Flowable 中不仅如此,还同样的改变了Subscription.request所在的线程
                // 调度上游的Flowable的subscribe方法，可能会调度上游Subscription的request 方法，运行在io线程中。
                .unsubscribeOn(Schedulers.io())//调度上游的Subscription的cancel方法，运行在io线程中。
                .observeOn(AndroidSchedulers.mainThread())//调度下游Subscriber 的 onNext / onError / onComplete 方法，运行在主线程中。
                .subscribe(new Subscriber<WeatherResponseBean>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e(TAG, "开始订阅");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(WeatherResponseBean weatherResponseBean) {
                        Log.e(TAG, "结果：" + weatherResponseBean.toString());
                        tvResult.setText("不使用retrofit但支持背压：" + weatherResponseBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "失败");
                        tvResult.setText("不使用retrofit但支持背压:失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "完成");
                    }
                });
    }

    /**
     * 不支持背压
     */
    private void rxMap() {
        Observable<ResponseBody> observable = RetrofitFactory.getInstance().getHttpApi().getWeatherDataForQuery("v1", "北京");
        observable.map(new Function<ResponseBody, WeatherResponseBean>() {
            @Override
            public WeatherResponseBean apply(ResponseBody responseBody) throws Exception {
                String result = CompressUtils.decompress(responseBody.byteStream());
                WeatherResponseBean weatherResponseBean = GsonUtils.fromJson(result, WeatherResponseBean.class);
                return weatherResponseBean;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherResponseBean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "开始订阅");
                    }

                    @Override
                    public void onNext(WeatherResponseBean weatherResponseBean) {
                        tvResult.setText("不支持背压：" + weatherResponseBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "失败");
                        tvResult.setText("不支持背压:失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "完成");
                    }
                });

    }
}
