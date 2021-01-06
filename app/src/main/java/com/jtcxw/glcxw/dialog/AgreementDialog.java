package com.jtcxw.glcxw.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BaseBottomSheetDialogFragment;
import com.google.gson.JsonObject;
import com.jtcxw.glcxw.R;
import com.jtcxw.glcxw.base.dialogs.BaseDialogFragment;
import com.jtcxw.glcxw.base.respmodels.AgreementBean;
import com.jtcxw.glcxw.listeners.DialogCallback;
import com.jtcxw.glcxw.listeners.OrderCancelCallback;
import com.jtcxw.glcxw.presenters.impl.AgreementPresenter;
import com.jtcxw.glcxw.views.AgreementView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.jetbrains.annotations.NotNull;

public class AgreementDialog extends BaseDialogFragment implements AgreementView {

    private DialogCallback mDialogCallback = null;
    private String mTitle;

    public AgreementDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public AgreementDialog setCancelCallback(DialogCallback dialogCallback) {
        mDialogCallback = dialogCallback;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_agreement, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BaseBottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    private WebView mWebView = null;
    private ProgressBar mProgressBar = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tv_confirm).setOnClickListener(view1 -> {
            if (mDialogCallback != null) {
                mDialogCallback.onDialogCallback(1);
            }
            dismiss();
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> {
            if (mDialogCallback != null) {
                mDialogCallback.onDialogCallback(0);
            }
            dismiss();
        });

        mWebView = view.findViewById(R.id.web_view);
        mProgressBar = view.findViewById(R.id.progress_bar);

        initWebViewSettings();

        JsonObject json = new JsonObject();
        json.addProperty("TreatyType","2");
        new AgreementPresenter(this).getMemberTreaty(json);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
            
        });

    }

    @Override
    public void onMemberTreatySucc(@NotNull AgreementBean agreementBean) {
        mWebView.loadDataWithBaseURL(null, agreementBean.getTreatyRichContent(), "text/html", "utf-8", null);
    }

    @Override
    public void onMemberTreatyFailed() {
        mProgressBar.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
    }

    private void initWebViewSettings() {

        mWebView.setBackgroundColor(0); // 设置背景色
        mWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255

        WebSettings webSetting = mWebView.getSettings();

        webSetting.setAllowContentAccess(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDefaultTextEncodingName("UTF-8");
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
    }



}
