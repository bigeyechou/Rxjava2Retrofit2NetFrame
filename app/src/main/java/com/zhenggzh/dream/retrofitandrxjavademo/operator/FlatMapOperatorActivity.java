package com.zhenggzh.dream.retrofitandrxjavademo.operator;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * RxJava flatmap操作符（变换操作符）
 */
public class FlatMapOperatorActivity extends AppCompatActivity {
    private static String TAG = "FlatMapOperatorActivity";
    private final String mRxJavaExplain = "flatmap操作符:\n" +
            "将一个发送事件的上游Observable变换为多个发送事件的Observables,然后将它们发射的事件合并后放进一个单独的observable里\n" +
            "flatMap 并不能保证事件的顺序；可以保证顺序的操作符concatMap，用法相当";

    @Bind(R.id.btn_flatmap_explain)
    Button btnFlatmapExplain;
    @Bind(R.id.btn_flatmap1)
    Button btnFlatmap1;
    @Bind(R.id.btn_flatmap2)
    Button btnFlatmap2;
    @Bind(R.id.btn_concatmap)
    Button btnConcatMap;
    @Bind(R.id.tv_result)
    TextView tvResult;

    private List<String> resultString = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flatmap_operator_layout);
        ButterKnife.bind(this);
        tvResult.setText(mRxJavaExplain);
    }


    @OnClick({R.id.btn_flatmap_explain, R.id.btn_flatmap1, R.id.btn_flatmap2,R.id.btn_concatmap})
    public void onClickButton(Button button) {
        switch (button.getId()) {
            case R.id.btn_flatmap_explain:
                tvResult.setText(mRxJavaExplain);
                break;
            case R.id.btn_flatmap1:
                rxFlatMap1();
                break;
            case R.id.btn_flatmap2:
                rxFlatMap2();
                break;
            case R.id.btn_concatmap:
                rxConcatMap();
                break;
            default:
                tvResult.setText(mRxJavaExplain);
                break;
        }
    }

    private void rxFlatMap1() {
        resultString.clear();
        final Observable<ResponseBody> observable = RetrofitFactory.getInstance().getHttpApi().getWeatherDataForQuery("v1", "北京");
        observable.flatMap(new Function<ResponseBody, ObservableSource<WeatherResponseBean.DataBean>>() {
            @Override
            public ObservableSource<WeatherResponseBean.DataBean> apply(ResponseBody responseBody) throws Exception {
                String result = CompressUtils.decompress(responseBody.byteStream());
                WeatherResponseBean weatherResponseBean = GsonUtils.fromJson(result, WeatherResponseBean.class);
                return Observable.fromIterable(weatherResponseBean.getData());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WeatherResponseBean.DataBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "rxFlatMap1开始订阅");
            }

            @Override
            public void onNext(WeatherResponseBean.DataBean dataBean) {
                Log.e(TAG, "rxFlatMap1成功" + dataBean.getDate());
                resultString.add(dataBean.getDate());
                tvResult.setText("rxFlatMap1结果：" + resultString);
            }


            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "rxFlatMap1失败");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "rxFlatMap1完成");
            }
        });

    }

    /**
     * flatMap无序
     */
    private void rxFlatMap2() {
        resultString.clear();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> strings = new ArrayList<>();
                String s = integer.toString();
                strings.add(s);
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(strings).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, "rxFlatMap2成功:" + s);
                resultString.add(s);
                tvResult.setText("rxFlatMap2结果：" + resultString);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "rxFlatMap2失败:"+throwable.getMessage());
                tvResult.setText("rxFlatMap2失败：" + throwable.getMessage());
            }
        });

    }

    /**
     * concatMap有序
     */
    private void rxConcatMap() {
        resultString.clear();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> strings = new ArrayList<>();
                String s = integer.toString();
                strings.add(s);
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(strings).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, "rxCancatMap成功:" + s);
                resultString.add(s);
                tvResult.setText("rxCancatMap结果：" + resultString);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "rxCancatMap失败:"+throwable.getMessage());
                tvResult.setText("rxCancatMap失败：" + throwable.getMessage());
            }
        });

    }

}
