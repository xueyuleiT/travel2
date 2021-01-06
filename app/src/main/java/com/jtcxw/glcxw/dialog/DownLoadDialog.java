package com.jtcxw.glcxw.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.glcxw.lib.util.FileProvider7;
import com.google.android.material.bottomsheet.BaseBottomSheetDialogFragment;
import com.jtcxw.glcxw.R;
import com.jtcxw.glcxw.base.utils.ScreenUtil;
import com.jtcxw.glcxw.base.views.LoadingView;
import com.jtcxw.glcxw.listeners.OrderCancelCallback;
import com.jtcxw.glcxw.utils.AppUpdateUtils;

import java.io.File;

import static com.jtcxw.glcxw.utils.AppUpdateUtils.DOWNLOAD;
import static com.jtcxw.glcxw.utils.AppUpdateUtils.DOWNLOAD_FINISH;

public class DownLoadDialog extends DialogFragment {

    private String mTitle;
    private String mUrl;
    private LoadingView mLoadingView;

    public DownLoadDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public DownLoadDialog setUrl(String url) {
        this.mUrl = url;
        return this;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_layout, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BaseBottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = (int) (ScreenUtil.getScreenWidth(getContext()) * 0.7);
        window.setAttributes(wlp);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(mTitle)) {
            ((TextView) view.findViewById(R.id.tv_title)).setText("更新 "+mTitle);
        }

        setCancelable(false);
        mLoadingView = view.findViewById(R.id.loading_view);

        File file = new File(getContext().getExternalFilesDir("apk"),mTitle+".apk");
       new AppUpdateUtils(file.getAbsolutePath(),mUrl,new Handler(){
           @Override
           public void handleMessage(@NonNull Message msg) {
               super.handleMessage(msg);
               if (msg.what == DOWNLOAD) {
                   mLoadingView.setProgress(msg.arg1);
               } else if (msg.what == DOWNLOAD_FINISH) {
                   mLoadingView.setProgress(100);
                   Intent intent = new Intent(Intent.ACTION_VIEW);
                   if (Build.VERSION.SDK_INT >= 24) {
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                       intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                       Uri uri = FileProvider7.getUriForFile(getContext(), file);
                       Log.d("uri === >7",uri.toString());
                       intent.setDataAndType(uri, "application/vnd.android.package-archive");
                   } else  {
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       Uri uri = FileProvider7.getUriForFile(getContext(), file);
                       Log.d("uri === >",uri.toString());
                       intent.setDataAndType(uri, "application/vnd.android.package-archive");
                   }
                   startActivity(intent);
                   dismiss();
               }
           }
       }).startDownLoad();

    }
}
