package com.zhenggzh.dream.retrofitandrxjavademo.operator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenggzh.dream.retrofitandrxjavademo.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 创建操作符相关
 * RxJava创建被观察者对象Observable
 */
public class CreateOperatorActivity extends AppCompatActivity {

    private final String TAG = "CreateOperatorActivity";
    private String mRxJavaExplain = "点击相应操作符显示相关解释，测试数据请看log日志~\n";

    @Bind(R.id.tv_result)
    TextView tvResult;
    Observable observable;

    /**
     * 基本创建：create()
     * 快速创建：just();fromArray();fromIterable
     * 延迟创建：defer();timer();intervalRange();range();
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_operator_layout);
        ButterKnife.bind(this);
        tvResult.setText(mRxJavaExplain);
    }


    @OnClick({R.id.btn_create, R.id.btn_just, R.id.btn_fromarray, R.id.btn_fromiterable, R.id.btn_defer, R.id.btn_timer, R.id.btn_interval,R.id.btn_intervalrange, R.id.btn_range})
    public void onClickButton(Button button) {
        switch (button.getId()) {
            case R.id.btn_create:
                rxCreate();
                break;
            case R.id.btn_just:
                rxJust();
                break;
            case R.id.btn_fromarray:
                rxFromArray();
                break;
            case R.id.btn_fromiterable:
                rxFromIterable();
                break;
            case R.id.btn_defer:
                rxDefer();
                break;
            case R.id.btn_timer:
                rxTimer();
                break;
            case R.id.btn_interval:
                rxInterval();
                break;
            case R.id.btn_intervalrange:
                rxIntervalRange();
                break;
            case R.id.btn_range:
                rxRange();
                break;
            default:
                break;
        }
    }


    /**
     * create 最基本的操作符，创建被观察者（observable）对象
     */

    private void rxCreate() {
        tvResult.setText(mRxJavaExplain+" create 最基本的操作符，创建被观察者（observable）对象");
        observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        });
        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "create开始连接");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "create成功：" + integer.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "create错误：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "create完成");
            }
        });

    }

    /**
     * just 操作符：创建被观察者（observable）对象，可以发送最多10个相同类型的参数
     * 传入多少个参数就会执行多少次onNext（）
     */
    private void rxJust() {
        tvResult.setText(mRxJavaExplain+"just 操作符：创建被观察者（observable）对象，可以发送最多10个相同类型的参数\n" +
                "传入多少个参数就会执行多少次onNext（）");
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "just开始连接");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "just成功：" + integer.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "just错误：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "just完成");
            }
        });
    }


    /**
     * fromArray 操作符：创建被观察者（observable）对象
     * 传入 数组类型 的数据，会将数组类型转换为Observable对象
     * 应用场景：想要发送10个以上事件或遍历数组
     */
    private void rxFromArray() {
        tvResult.setText(mRxJavaExplain+"fromArray 操作符：创建被观察者（observable）对象\n" +
                "传入 数组类型 的数据，会将数组类型转换为Observable对象\n" +
                "应用场景：想要发送10个以上事件或遍历数组");
        Integer[] items = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        Observable.fromArray(items).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "fromArray开始连接");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "fromArray成功：" + integer.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "fromArray错误：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "fromArray完成");
            }
        });
    }


    /**
     * fromIterable 操作符：创建被观察者（observable）对象
     * 传入 集合list 数据，会将集合类型转换为Observable对象
     * 应用场景：想要发送10个以上事件或遍历集合
     */
    private void rxFromIterable() {
        tvResult.setText(mRxJavaExplain+"fromIterable 操作符：创建被观察者（observable）对象\n" +
                "传入 集合list 数据，会将集合类型转换为Observable对象\n" +
                "应用场景：想要发送10个以上事件或遍历集合");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Observable.fromIterable(list).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "fromIterable开始连接");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "fromIterable成功：" + integer.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "fromIterable错误：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "fromIterable完成");
            }
        });
    }

    /**
     * defer 操作符：延迟创建，直到有观察者（Observer）订阅才动态创建被观察者(observable)对象并发送事件
     * 可以保证被观察者的数据是最新的
     */
    private Integer i = 1;

    private void rxDefer() {
        tvResult.setText(mRxJavaExplain+"defer 操作符：延迟创建，直到有观察者（Observer）订阅才动态创建被观察者(observable)对象并发送事件\n" +
                "可以保证被观察者的数据是最新的");
        Observable observable = Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                return Observable.just(i);
            }
        });

        i = 99;

        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "defer开始连接");
            }

            @Override
            public void onNext(Integer i) {
                Log.e(TAG, "defer成功：" + i);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "defer错误：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "defer完成");
            }
        });

    }


    /**
     * timer 操作符：创建被观察者（observable）对象，延时发送（指定延迟时间）
     * 延迟指定事件，发送一个0，一般用于检测
     */
    private void rxTimer() {
        tvResult.setText(mRxJavaExplain+"timer 操作符：创建被观察者（observable）对象，延时发送（指定延迟时间）\n" +
                "延迟指定事件，发送一个0，一般用于检测");
        Observable.timer(3, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "timer开始连接");
            }

            @Override
            public void onNext(Long value) {
                Log.e(TAG, "timer接收事件：" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "timer错误");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "timer完成");
            }
        });
    }


    /**
     * interval 操作符：创建被观察者（observable）对象
     * 每隔指定时间就发送该事件
     * 发送的事件序列 = 从0开始、无限递增1的的整数序列
     * 可以用作检查，比如检测多设备登录
     */
    private void rxInterval() {
        tvResult.setText(mRxJavaExplain+"interval 操作符：创建被观察者（observable）对象\n" +
                "每隔指定时间就发送该事件\n" +
                "发送的事件序列 = 从0开始、无限递增1的的整数序列\n" +
                "可以用作检查，比如检测多设备登录");
        /**
         * 参数1 = 第1次延迟时间;
         * 参数2 = 间隔时间数字;
         * 参数3 = 时间单位 -SECONDS-秒 ;
         */
        Observable.interval(3, 1, TimeUnit.SECONDS)
                // 该例子发送的事件序列特点：延迟3s后发送事件，每隔1秒产生1个数字（从0开始递增1，无限个）
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "interval开始连接");
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.d(TAG, "interval接收事件：" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "interval错误");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "interval完成");
                    }

                });
    }

    /**
     * intervalRange 操作符：创建被观察者对象（observable）
     * 每隔指定时间发送事件，可指定发送数据的次数
     * 发送的事件序列 = 从0开始、无限递增1的的整数序列，作用类似于interval（），但可指定发送的数据的数量
     */
    private void rxIntervalRange() {
        tvResult.setText(mRxJavaExplain+"intervalRange 操作符：创建被观察者对象（observable）\n" +
                "每隔指定时间发送事件，可指定发送数据的次数\n" +
                "发送的事件序列 = 从0开始、无限递增1的的整数序列，作用类似于interval（），但可指定发送的数据的数量");
        /**
         * 参数1 = 事件序列起始点;
         * 参数2 = 事件发送数量;
         * 参数3 = 第1次事件延迟发送时间;
         * 参数4 = 间隔时间数字;
         * 参数5 = 时间单位;
         */
        Observable.intervalRange(3, 10, 2, 1, TimeUnit.SECONDS)
                // 该例子发送的事件序列特点：
                // 1. 从3开始，一共发送10个事件；
                // 2. 第1次延迟2s发送，之后每隔2秒产生1个数字（从0开始递增1，无限个）
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "intervalRange开始连接");
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.d(TAG, "intervalRange接收事件：" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "intervalRange错误");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "intervalRange完成");
                    }
                });
    }


    /**
     * range 操作符：创建被观察者对象（observable）
     * 连续发送一个时间序列，可以指定范围
     * 发送的事件序列 = 从0开始、无限递增1的的整数序列，作用类似于intervalRange（），但区别在于：无延迟发送事件
     */
    private void rxRange() {
        tvResult.setText(mRxJavaExplain+"range 操作符：创建被观察者对象（observable）\n" +
                "连续发送一个时间序列，可以指定范围\n" +
                "发送的事件序列 = 从0开始、无限递增1的的整数序列，作用类似于intervalRange（），但区别在于：无延迟发送事件");
        /**
         * 参数1 = 事件序列起始点；
         * 参数2 = 事件数量；
         * 注：若设置为负数，则会抛出异常
         */
        Observable.range(3, 10)
                // 该例子发送的事件序列特点：从3开始发送，每次发送事件递增1，一共发送10个事件
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "range开始连接");
                    }
                    // 默认最先调用复写的 onSubscribe（）

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "range接收事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "range错误");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "range完成");
                    }

                });
    }
}
