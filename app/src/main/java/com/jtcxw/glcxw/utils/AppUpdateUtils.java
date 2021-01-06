package com.jtcxw.glcxw.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zenghui on 16/2/29.
 */
public class AppUpdateUtils {

    public static final int DOWNLOAD = 0;
    public static final int DOWNLOAD_FINISH = 1;

    private String apkFilePath;
    private String apkDownloadUrl;
    Handler mHandler;
    boolean cancelUpdate;

    public String getApkFilePath() {
        return apkFilePath;
    }

    public void setApkFilePath(String apkFilePath) {
        this.apkFilePath = apkFilePath;
    }

    public boolean isCancelUpdate() {
        return cancelUpdate;
    }

    public void setCancelUpdate(boolean cancelUpdate) {
        this.cancelUpdate = cancelUpdate;
    }

    public AppUpdateUtils(String apkFilePath, String apkDownloadUrl, Handler mHandler) {
        this.apkFilePath = apkFilePath;
        this.apkDownloadUrl = apkDownloadUrl;
        this.mHandler = mHandler;
    }

    public void startDownLoad() {
        downThread.start();
    }

    Thread downThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    URL url = new URL(apkDownloadUrl);
                    // 创建连接
                    HttpURLConnection conn = null;

                    conn = (HttpURLConnection) url.openConnection();

                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File apkFile = new File(apkFilePath);

                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;

                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        int progress = (int) (((float) count / length) * 100);
                        if (mHandler != null) {
                            Message message = mHandler.obtainMessage();
                            message.what = DOWNLOAD;
                            message.arg1 = progress;
                            // 更新进度
                            mHandler.sendMessage(message);
                        }
                        if (numread <= 0) {
                            if (mHandler != null) {
                                // 下载完成
                                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            }
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

}
