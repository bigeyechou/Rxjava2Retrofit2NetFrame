package com.zhenggzh.dream.retrofitandrxjavademo.operator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zhenggzh.dream.retrofitandrxjavademo.R;
import com.zhenggzh.dream.retrofitandrxjavademo.bean.WeatherResponseBean;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * 变换操作符buffer
 */
public class BufferOperatorActivity extends AppCompatActivity {

  private static String TAG = "BufferOperatorActivity";
  private final String mRxJavaExplain = "buffer操作符:\n" +
      "定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值。\n" +
      "其实就是将要发射的数据封装成多个缓冲区，每次发射一个缓冲区。\n" +
      "严格按照顺序来发送\n" +
      "应用实例：多用于合并多个网络请求成一个，实现多个接口数据共同更新UI，如List插入头数据、多个接口里面的数据合并成一个你想要的数据。";
  @Bind(R.id.btn_buffer_explain) AppCompatButton btnBufferExplain;
  @Bind(R.id.btn_buffer) AppCompatButton btnBuffer;
  @Bind(R.id.tv_result) AppCompatTextView tvResult;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_buffer_operator_layout);
    ButterKnife.bind(this);
    tvResult.setText(mRxJavaExplain);
  }

  @OnClick({R.id.btn_buffer_explain, R.id.btn_buffer})
  public void onClickButton(AppCompatButton button) {
    switch (button.getId()) {
      case R.id.btn_buffer_explain:
        tvResult.setText(mRxJavaExplain);
        break;
      case R.id.btn_buffer:
        rxBuffer();
        break;
    }
  }

  private void rxBuffer() {
    Observable.just("one", "two", "three", "four", "five")//创建了一个有5个数字的被观察者
        // 3 means,  it takes max of three from its start index and create list
        // 1 means, it jumps one step every time
        .buffer(3, 1)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<String>>() {

          @Override public void onSubscribe(Disposable d) {

            Log.d(TAG, " onSubscribe : " + d.isDisposed());
          }

          @Override public void onNext(List<String> stringList) {
            tvResult.setText(" onNext size : " + stringList.size());
            Log.d(TAG, " onNext : size :" + stringList.size());
            for (String value : stringList) {
              tvResult.setText(" value : " + value);
              Log.d(TAG, " : value :" + value);
            }
          }

          @Override public void onError(Throwable e) {
            tvResult.setText(" onError : " + e.getMessage());
            Log.d(TAG, " onError : " + e.getMessage());
          }

          @Override public void onComplete() {
            Log.d(TAG, " onComplete");

          }
        });
  }
}
