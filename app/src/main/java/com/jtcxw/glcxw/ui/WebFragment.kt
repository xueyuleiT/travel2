package com.jtcxw.glcxw.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.BuildConfig
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.databinding.FragmentWebBinding
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.tencent.smtt.sdk.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import wendu.dsbridge.DWebView


class WebFragment : BaseFragment<FragmentWebBinding, CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun doAfterAnim() {
//        mBinding.root.findViewById<Toolbar>(R.id.tool_bar).setNavigationOnClickListener {
//            if (mBinding.webview.canGoBack()){
//                mBinding.webview.goBack()
//            }else{
//                pop()
//            }
//        }

        if (mUrl!!.contains("?")) {
            val url = mUrl
            mBinding.webview.loadUrl(url)
        } else {
            val url = (mUrl)
            mBinding.webview.loadUrl(url)
        }

    }

    companion object {
        fun newInstance(from: SupportFragment, bundle: Bundle) {
            val fragment = WebFragment()
            fragment.arguments = bundle
            from.start(fragment,ISupportFragment.SINGLETOP)
        }
    }

    private var mUrl: String? = null
    private var mTitle: String? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_web
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebViewSettings()
        val ivClose = mBinding.root.findViewById<ImageView>(R.id.iv_close)
        ivClose.visibility = View.VISIBLE
        ivClose.setOnClickListener {
            pop()
        }
        mTitle = arguments?.getString(BundleKeys.KEY_WEB_TITLE, "")
        mUrl = arguments?.getString(BundleKeys.KEY_WEB_URL, "")
        mBinding.webview!!.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(p0: WebView?, p1: String?) {
                super.onReceivedTitle(p0, p1)
                if (!p1!!.startsWith("http")) {
                    mBinding.root.findViewById<TextView>(R.id.tv_center_title).text = p1
                }
            }

            override fun onProgressChanged(p0: WebView?, newProgress: Int) {
                super.onProgressChanged(p0, newProgress)
                if (newProgress >= 100) {
                    mBinding.progressBar!!.visibility = View.GONE
                } else {
                    mBinding.progressBar!!.progress = newProgress
                }
            }

        }

        mBinding.webview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                p0: WebView?,
                p1: String?
            ): Boolean {
                p0!!.loadUrl(p1)
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
        DWebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        initToolBar(mTitle!!)

    }



    private fun initWebViewSettings() {

        val webSetting = mBinding.webview.settings

        webSetting.allowContentAccess = true
        webSetting.javaScriptEnabled = true
        webSetting.defaultTextEncodingName = "UTF-8"
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        webSetting.domStorageEnabled = true
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE

        // settings 的设计
        webSetting.setAppCachePath(context!!.getDir("appcache", 0).path)
        webSetting.databasePath = context!!.getDir("databases", 0).path
        webSetting.setGeolocationDatabasePath(context!!.getDir("geolocation", 0).path)
        webSetting.loadWithOverviewMode = true
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding.webview.webChromeClient = null
        mBinding.webview.webViewClient = null
        mBinding.webview.settings.javaScriptEnabled = false
        mBinding.webview.clearCache(true)
        mBinding.webview.clearHistory()
        mBinding.webview.removeAllViews()
        mBinding.webview.clearFormData()

    }


}