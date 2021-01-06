package com.jtcxw.glcxw.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.AliSignBean
import com.jtcxw.glcxw.base.respmodels.AliSignStatusBean
import com.jtcxw.glcxw.base.respmodels.AliUnSignBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentPayAliBinding
import com.jtcxw.glcxw.presenters.impl.PayAliPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.PayAliView
import me.yokeyword.fragmentation.SupportFragment
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.jtcxw.glcxw.base.utils.ToastUtil


class PayAliFragment:BaseFragment<FragmentPayAliBinding,CommonModel>() ,PayAliView{
    private var mAliSignStatusBean:AliSignStatusBean?= null
    override fun onUseragreementQuerySucc(aliSignStatusBean: AliSignStatusBean) {
        mAliSignStatusBean = aliSignStatusBean
        when {
            aliSignStatusBean.contractStatus == 0 -> {
                mBinding.tvStatus.text = "去开通"
            }
            aliSignStatusBean.contractStatus == 1 -> {
                mBinding.tvStatus.text = "支付宝处理中"
            }
            aliSignStatusBean.contractStatus == 2 -> {
                mBinding.tvStatus.text = "点击关闭"
            }
        }
    }

    override fun onUseragreementPageSignSucc(aliSignBean: AliSignBean) {
        val intent = Intent(ACTION_VIEW)
        intent.data = Uri.parse(aliSignBean.schemeUrl)
        startActivity(intent)
    }

    override fun onUseragreementUnSignSucc(aliUnSignBean: AliUnSignBean) {
        ToastUtil.toastSuccess(aliUnSignBean.message)
        val json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        mPresenter!!.useragreementQuery(json)


    }

    fun openBrowser(url: String) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val contentUrl = Uri.parse(url)
        intent.data = contentUrl
        startActivity(intent)
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pay_ali
    }

    var mPresenter:PayAliPresenter?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("支付渠道")

        mPresenter = PayAliPresenter(this)

        val json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        mPresenter!!.useragreementQuery(json)

        mBinding.tvStatus.setOnClickListener{
            if (mAliSignStatusBean == null) {
                return@setOnClickListener
            }

            when(mAliSignStatusBean!!.contractStatus) {
                0 -> {
                    if (!isAliPayInstalled(context!!)) {
                        showConfirmDialog("提示","未安装支付宝app,前往下载安装?","前往","取消",object :DialogCallback{
                            override fun invoke(p1: MaterialDialog) {
                                openBrowser("https://d.alipay.com/?nojump=true")
                            }

                        },null)
                        return@setOnClickListener
                    }
                    val json = JsonObject()
                    json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                    json.addProperty("ReturnUrl","glcxw://")
                    mPresenter!!.useragreementPageSign(json)
                }

                2 -> {
                    showConfirmDialog("提示","是否关闭支付宝快捷支付？","确定","取消",object :DialogCallback{
                        override fun invoke(p1: MaterialDialog) {
                            val json = JsonObject()
                            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                            mPresenter!!.useragreementUnSign(json)
                        }

                    },null)
                }
            }
        }

    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?){
            val payAliFragment = PayAliFragment()
            payAliFragment.arguments = bundle
            fragment.start(payAliFragment)

        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (ismBindingInitialized()) {
            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            mPresenter!!.useragreementQuery(json)
        }
    }


    fun isAliPayInstalled(context: Context): Boolean {
        val uri = Uri.parse("alipays://platformapi/startApp")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val componentName = intent.resolveActivity(context.packageManager)
        return componentName != null
    }

    override fun doAfterAnim() {
    }
}