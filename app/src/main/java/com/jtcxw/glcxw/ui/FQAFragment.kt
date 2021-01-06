package com.jtcxw.glcxw.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.CommonFQABean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.databinding.FragmentFqaBinding
import com.jtcxw.glcxw.presenters.impl.FQAPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.FQAView
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import me.yokeyword.fragmentation.SupportFragment

class FQAFragment:BaseFragment<FragmentFqaBinding,CommonModel>() ,FQAView{
    override fun onGetFQAFailed() {
        mBinding.progressBar.visibility = View.GONE
        mBinding.webView.visibility = View.VISIBLE
    }

    override fun onGetFQASucc(fqaBean: CommonFQABean) {
        mBinding.webView.loadDataWithBaseURL(null, fqaBean.fqaHtml, "text/html", "utf-8", null)
        if (fqaBean.fqaTypeName != null) {
            initToolBar(fqaBean.fqaTypeName)
        }

    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val fQAFragment = FQAFragment()
            fQAFragment.arguments = bundle
            fragment.start(fQAFragment)

        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_fqa
    }

    var mPresenter:FQAPresenter?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("")
        initWebViewSettings()

        if (BaseUtil.isDarkMode()) {
            mBinding.vBg.setBackgroundResource(0)
        }

        mPresenter = FQAPresenter(this)

        val json = JsonObject()
        json.addProperty("FQAType",arguments!!.getString(BundleKeys.KEY_FQA_TYPE))
        mPresenter!!.getFQA(json)

        mBinding.webView.visibility = View.GONE
        mBinding.webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                p0: WebView?,
                p1: String?
            ): Boolean {
                p0!!.loadUrl(p1)
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mBinding.progressBar.visibility = View.GONE
                mBinding.webView.visibility = View.VISIBLE
            }
        }

    }

    private fun initWebViewSettings() {

        val webSetting = mBinding.webView.settings

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

    override fun doAfterAnim() {
    }

}