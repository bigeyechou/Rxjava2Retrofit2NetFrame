package com.zhenggzh.dream.retrofitandrxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.zhenggzh.dream.retrofitandrxjavademo.netsubscribe.MovieSubscribe;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultListener;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultSub;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
    @Bind(R.id.btn3)
    Button btn3;

    private int start = 0;
    private int count = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1,R.id.btn2,R.id.btn3})
    public void onClickButton(Button button){
        switch (button.getId()){
            case R.id.btn1:
                break;
            case R.id.btn2:
                getData();
                break;
            case R.id.btn3:

                break;
            default:

                break;
        }
    }

    private void getData() {
        OnSuccessAndFaultListener l = new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFault(String errorMsg) {

            }
        };
        MovieSubscribe.getDouBanMovie(new OnSuccessAndFaultSub(l),start,count);

    }

}
