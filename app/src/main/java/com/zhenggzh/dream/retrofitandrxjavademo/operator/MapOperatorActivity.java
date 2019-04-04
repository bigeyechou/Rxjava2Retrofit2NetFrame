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
 * RxJava Map操作符
 */
public class MapOperatorActivity extends AppCompatActivity {
    private static String TAG = "MapOperatorActivity";
    private final String mRxJavaExplain = "map操作符:\n" +
            "将一个被观察者(Observable)对象通过某种关系转换为另一个被观察者（Observable）对象。\n" +
            "严格按照顺序来发送" +
            "应用实例：";

    @Bind(R.id.btn_map_explain)
    Button btnMapExplain;
    @Bind(R.id.btn_map)
    Button btnMap;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.btn_map_no_retrofit_backpressure)
    Button btnMapNoRetrofitBackPressure;
    @Bind(R.id.btn_map_no_retrofit)
    Button btnMapNoRetrofit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_operator_layout);
        ButterKnife.bind(this);
        tvResult.setText(mRxJavaExplain);
    }

    @OnClick({R.id.btn_map_explain, R.id.btn_map, R.id.btn_map_no_retrofit_backpressure, R.id.btn_map_no_retrofit})
    public void onClickButton(Button button) {
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
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherResponseBean>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e(TAG, "开始订阅");
                    }

                    @Override
                    public void onNext(WeatherResponseBean weatherResponseBean) {
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
