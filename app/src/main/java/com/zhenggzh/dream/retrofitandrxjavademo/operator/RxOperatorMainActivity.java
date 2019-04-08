package com.zhenggzh.dream.retrofitandrxjavademo.operator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.zhenggzh.dream.retrofitandrxjavademo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * RxJava操作符主页面
 * 操作符演示相关，可以删除operator包下所有内容
 */
public class RxOperatorMainActivity extends AppCompatActivity {

    @Bind(R.id.btn_zip)
    Button btnZip;
    @Bind(R.id.btn_map)
    Button btnMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_main_layout);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_map,R.id.btn_zip,R.id.btn_flatmap})
    public void onClickButton(Button button) {
        switch (button.getId()) {
            case R.id.btn_zip:
                startActivity(new Intent(RxOperatorMainActivity.this, ZipOperatorActivity.class));
                break;
            case R.id.btn_map:
                startActivity(new Intent(RxOperatorMainActivity.this,MapOperatorActivity.class));
                break;
            case R.id.btn_flatmap:
                startActivity(new Intent(RxOperatorMainActivity.this,FlatMapOperatorActivity.class));
                break;
            default:
                break;
        }
    }
}
