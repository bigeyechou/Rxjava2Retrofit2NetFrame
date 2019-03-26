package com.zhenggzh.dream.retrofitandrxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenggzh.dream.retrofitandrxjavademo.bean.WeatherResponseBean;
import com.zhenggzh.dream.retrofitandrxjavademo.netsubscribe.MovieSubscribe;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultListener;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultSub;
import com.zhenggzh.dream.retrofitandrxjavademo.utils.GsonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.et_input_city)
    EditText etInputCity;
    @Bind(R.id.tv_city_wather)
    TextView tvCityWather;

    private String mCityName = "北京";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mCityName = etInputCity.getText().toString();
    }

    @OnClick({R.id.btn1})
    public void onClickButton(Button button) {
        switch (button.getId()) {
            case R.id.btn1:
//                Toast.makeText(this,"请配置好相关接口:URLConstant->BASE_URL 和 HttpApi->@GET()",Toast.LENGTH_SHORT);
                getDouBanData();
                break;
            default:

                break;
        }
    }


    /**
     * 请求数据
     */
    private void getDouBanData() {
        MovieSubscribe.getDouBanData(mCityName, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                Toast.makeText(MainActivity.this, "请求成功：" , Toast.LENGTH_SHORT).show();
                WeatherResponseBean weather = GsonUtils.fromJson(result,
                        WeatherResponseBean.class);
                tvCityWather.setText(weather.toString());
            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(MainActivity.this, "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }));
    }

}
