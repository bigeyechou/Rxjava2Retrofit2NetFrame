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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_main_layout);
        ButterKnife.bind(this);
    }

    /**
     * 操作符种类：
     * 创建操作符：create{};just{};fromArray{};timer...
     * 变换操作符：map{};flatMap{};concatMap{}...
     * 组合操作符：zip{};concat{}
     * 功能操作符：subscribe{};subscribeOn{};observeOn{};delay{}...
     * 过滤操作符：
     * 条件操作符：contains{};exists{};isEmpty{}...
     */

    @OnClick({R.id.btn_map, R.id.btn_zip, R.id.btn_flatmap, R.id.btn_create_operator})
    public void onClickButton(Button button) {
        switch (button.getId()) {
            case R.id.btn_create_operator:
                startActivity(new Intent(RxOperatorMainActivity.this, CreateOperatorActivity.class));
                break;
            case R.id.btn_map:
                startActivity(new Intent(RxOperatorMainActivity.this, MapOperatorActivity.class));
                break;
            case R.id.btn_flatmap://包含flatMap和concatMap
                startActivity(new Intent(RxOperatorMainActivity.this, FlatMapOperatorActivity.class));
                break;
            case R.id.btn_zip:
                startActivity(new Intent(RxOperatorMainActivity.this, ZipOperatorActivity.class));
                break;
            default:
                break;
        }
    }
}
