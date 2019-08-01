package com.zhenggzh.dream.retrofitandrxjavademo.download;

import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class FileUtils {

  public static void writeFile(InputStream inputStream, File file) {
    if (file.exists()) {
      file.delete();
    }
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);

      byte[] b = new byte[1024];

      int len;
      while ((len = inputStream.read(b)) != -1) {
        fos.write(b, 0, len);
      }
      inputStream.close();
      fos.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * 写入文件
   * 断点续传
   */
  public static void writeFile2(InputStream in, File file) throws IOException {
    RandomAccessFile saveFile = null;
    long ltest = 0;
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }
    if (file != null && file.exists()) {
      ltest = file.length();
    }

    if (in != null) {
      saveFile = new RandomAccessFile(file, "rw");
      saveFile.seek(ltest);
      byte[] buffer = new byte[1024 * 128];
      int len = -1;
      while ((len = in.read(buffer)) != -1) {
        saveFile.write(buffer, 0, len);
      }
      in.close();
    } else {
      Log.e("DOWNLOAD------->", "writeFile:下载失败");
    }
  }

  public static long readFile(File file) {
    if (file != null && file.exists()) {
      return file.length();
    } else {
      return 0;
    }
  }

}
