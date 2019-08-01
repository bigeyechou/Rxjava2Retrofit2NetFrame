package com.zhenggzh.dream.retrofitandrxjavademo.download;

public interface DownloadListener {
  void onStartDownload(long length);
  void onProgress(int progress);
  void onFail(String errorInfo);
}
