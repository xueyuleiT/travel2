package com.jtcxw.glcxw.utils;

import android.os.AsyncTask;

import com.lakala.wtb.listener.HandleCallBack;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, Void> {
    // 传递两个参数：URL 和 目标路径
    private String url;
    private String destPath;
    private HandleCallBack mHandleCallBack;
    public DownloadTask(HandleCallBack handleCallBack){
        mHandleCallBack = handleCallBack;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(String... params) {
        url = params[0];
        destPath = params[1];
        OutputStream out = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);
            InputStream in = urlConnection.getInputStream();
            out = new FileOutputStream(params[1]);
            byte[] buffer = new byte[10 * 1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
        } catch (IOException e) {
            mHandleCallBack.onHandle(e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mHandleCallBack.onHandle("true");
    }
}
