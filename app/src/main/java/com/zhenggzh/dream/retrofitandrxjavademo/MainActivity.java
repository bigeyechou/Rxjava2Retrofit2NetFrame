package com.zhenggzh.dream.retrofitandrxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import android.widget.Toast;
import com.zhenggzh.dream.retrofitandrxjavademo.netsubscribe.MovieSubscribe;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultListener;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultSub;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn1)
    Button btn1;

    private int pageNumber;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1})
    public void onClickButton(Button button){
        switch (button.getId()){
            case R.id.btn1:
                getData();
                break;
            default:

                break;
        }
    }


    /**
     * 请求数据
     */
    private void getData() {
        MovieSubscribe.getData(pageNumber,userId,new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                Toast.makeText(MainActivity.this,"请求成功："+result,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(MainActivity.this,"请求失败："+errorMsg,Toast.LENGTH_SHORT).show();
            }
        }));
    }

}
