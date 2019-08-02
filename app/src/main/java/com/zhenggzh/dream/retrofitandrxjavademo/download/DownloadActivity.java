package com.zhenggzh.dream.retrofitandrxjavademo.download;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zhenggzh.dream.retrofitandrxjavademo.R;
import com.zhenggzh.dream.retrofitandrxjavademo.netsubscribe.DownloadSubscribe;
import java.io.File;

/**
 * 下载相关待完善
 */
public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

  @Bind(R.id.btn_download_app) AppCompatButton btnDownloadApp;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_download_layout);
    ButterKnife.bind(this);
  }

  @OnClick({ R.id.btn_download_app })
  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_download_app:
        download("下载地址xxx");
        break;
    }
  }

  private ProgressDialog progressDialog;
  private void download(String fileUrl) {
    progressDialog = new ProgressDialog(DownloadActivity.this);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setTitle("正在下载");
    progressDialog.setMessage("请稍候...");
    progressDialog.setProgress(0);
    progressDialog.show();

    final File file = new File(getApkPath(),"rxjava.apk");


    DownloadSubscribe.downloadFile(fileUrl, file.getPath(), file.getName(), new FileDownloadObserver<File>() {
      @Override
      public void onDownLoadSuccess(File file) {
        Log.e("==============>","下载成功");
      }
      @Override
      public void onDownLoadFail(Throwable throwable) {
        Log.e("==============>","下载失败");
      }

      @Override
      public void onProgress(int progress,long total) {
        progressDialog.setProgress(progress);
      }
    });

  }


  //文件路径
  public String getApkPath() {
    String directoryPath="";
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
      directoryPath =getExternalFilesDir("apk").getAbsolutePath();
    }else{//没外部存储就使用内部存储
      directoryPath=getFilesDir()+File.separator+"apk";
    }
    File file = new File(directoryPath);
    Log.e("测试路径",directoryPath);
    if(!file.exists()){//判断文件目录是否存在
      file.mkdirs();
    }
    return directoryPath;
  }
}
