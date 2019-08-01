package com.zhenggzh.dream.retrofitandrxjavademo.download;

import android.util.Log;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 自定义进度的body
 */
public class DownloadResponseBody extends ResponseBody {

  private ResponseBody responseBody;
  private DownloadListener downloadListener;

  private BufferedSource bufferedSource;

  public DownloadResponseBody(ResponseBody responseBody, DownloadListener downloadListener) {
    this.responseBody = responseBody;
    this.downloadListener = downloadListener;
    downloadListener.onStartDownload(responseBody.contentLength());
  }

  @Override public MediaType contentType() {
    return responseBody.contentType();
  }

  @Override public long contentLength() {
    return responseBody.contentLength();
  }

  @Override public BufferedSource source() {
    if (bufferedSource == null){
      bufferedSource = Okio.buffer(source(responseBody.source()));
    }
    return bufferedSource;
  }

  private Source source(Source source) {
    return new ForwardingSource(source) {
      long totalBytesRead = 0L;
      @Override
      public long read(Buffer sink, long byteCount) throws IOException {
        long bytesRead = super.read(sink, byteCount);
        totalBytesRead += bytesRead != -1 ? bytesRead : 0;
        Log.e("download", "read: "+ (int) (totalBytesRead * 100 / responseBody.contentLength()));
        if (null != downloadListener) {
          if (bytesRead != -1) {
            downloadListener.onProgress((int) (totalBytesRead));
          }
        }
        return bytesRead;
      }
    };
  }
}
