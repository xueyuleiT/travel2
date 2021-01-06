package com.jtcxw.glcxw.ui.login

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.AgreementBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.databinding.FragmentAgreementBinding
import com.jtcxw.glcxw.presenters.impl.AgreementPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.AgreementView
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import me.yokeyword.fragmentation.SupportFragment

class AgreementFragment:
    BaseFragment<FragmentAgreementBinding, CommonModel>(),AgreementView {
    override fun onMemberTreatyFailed() {
        mBinding.progressBar.visibility = View.GONE
        mBinding.webView.visibility = View.VISIBLE
    }

    override fun onMemberTreatySucc(agreementBean: AgreementBean) {
        mBinding.webView.loadDataWithBaseURL(null, agreementBean.TreatyRichContent, "text/html", "utf-8", null)

    }

    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?) {
            val agreementFragment = AgreementFragment()
            agreementFragment.arguments = bundle
            fragment.start(agreementFragment)

        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_agreement
    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BaseUtil.isDarkMode()) {
            mBinding.vBg.setBackgroundResource(0)
        }

        initWebViewSettings()

        val treatyType = arguments!!.getString(BundleKeys.KEY_TREATY_TYPE,"1")
        if (treatyType == "1") {
            initToolBar("用户协议")
        } else if (treatyType == "2"){
            initToolBar("隐私政策")
        } else {
            initToolBar("关于桂林出行网")
        }

        val json = JsonObject()
        json.addProperty("TreatyType",arguments!!.getString("TreatyType","1"))
        AgreementPresenter(this).getMemberTreaty(json)
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

        mBinding.webView.setBackgroundColor(0) // 设置背景色
        mBinding.webView.background.alpha = 0 // 设置填充透明度 范围：0-255

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