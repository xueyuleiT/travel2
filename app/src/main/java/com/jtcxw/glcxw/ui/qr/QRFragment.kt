package com.jtcxw.glcxw.ui.qr

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.PayListBean
import com.jtcxw.glcxw.base.respmodels.SmsBean
import com.jtcxw.glcxw.base.respmodels.VerifySmsBean
import com.jtcxw.glcxw.base.utils.*
import com.jtcxw.glcxw.databinding.FragmentQrBinding
import com.jtcxw.glcxw.presenters.impl.OpenQrPresenter
import com.jtcxw.glcxw.presenters.impl.PayListPresenter
import com.jtcxw.glcxw.ui.FQAFragment
import com.jtcxw.glcxw.ui.LocationFragment
import com.jtcxw.glcxw.ui.my.AuthFragment
import com.jtcxw.glcxw.ui.my.ChargeFragment
import com.jtcxw.glcxw.ui.my.OrdersFragment
import com.jtcxw.glcxw.ui.my.PayListFragment
import com.jtcxw.glcxw.utils.QRCodeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.OpenQrView
import com.jtcxw.glcxw.views.PayListView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import models.BaseBean
import retrofit2.Response
import java.util.*
import kotlin.concurrent.fixedRateTimer

class QRFragment: LocationFragment<FragmentQrBinding, CommonModel>() , OpenQrView, PayListView {
    override fun onGetDefaultPayListSucc(payListBean: PayListBean) {
        mBinding.tvBucket.text = payListBean.payList[0].payTypeName
    }

    override fun onSetDefaultPayListSucc(jsonObject: JsonObject) {
    }

    override fun onSendSmsCodeSucc(smsBean: SmsBean) {
    }

    override fun onVerifySmsCodeSucc(verifySmsBean: VerifySmsBean) {
    }

    override fun onOpenQRCodeSucc(jsonObject: JsonObject) {
        if (jsonObject.has("Flag") && jsonObject.get("Flag").asString == "true") {
            ToastUtil.toastSuccess("开通成功")
            checkQr(DialogUtil.getLoadingDialog(fragmentManager))
        }
    }

    override fun onLogout() {
    }

    override fun refresh() {

    }
    override fun onLocationChange(aMapLocation: AMapLocation) {
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_qr
    }

    var mPresenter:OpenQrPresenter?= null
    var payListPresenter: PayListPresenter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BaseUtil.isDarkMode()) {
            mBinding.rlBg.setBackgroundResource(0)
        }

        val tvTitle = mBinding.root.findViewById<TextView>(R.id.tv_center_title)
        tvTitle!!.text = "一码通"
        tvTitle.visibility = View.VISIBLE
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        tvTitle.setTextColor(resources.getColor(backColor()))

//        init()
        mPresenter = OpenQrPresenter(this)
        payListPresenter = PayListPresenter(this)

        mBinding.llBucket.visibility = View.GONE
        mBinding.llOpen.visibility = View.VISIBLE
        mBinding.tvOpen.visibility = View.GONE
        mBinding.llCharge.visibility = View.GONE
        mBinding.llQr.visibility = View.GONE
        mBinding.ivWelcome.visibility = View.VISIBLE


        mBinding.tvRefresh.setOnClickListener(this)
        mBinding.tvOpen.setOnClickListener(this)
        mBinding.tvHelp.setOnClickListener(this)
        mBinding.llBucket.setOnClickListener(this)

        if (BaseUtil.isDarkMode()) {
            mBinding.llBucket.setBackgroundResource(R.mipmap.pic_qr_bottom_dark)
        } else {
            mBinding.llBucket.setBackgroundResource(R.mipmap.pic_qr_bottom)
        }


    }


    var timer: Timer?= null
    private fun startTimer() {
        if (mTimerRunning || !isSupportVisible) {
            return
        }
        mTimerRunning = true
        timer = fixedRateTimer("", false, 15000, 15000) {
            activity!!.runOnUiThread {
                if (isSupportVisible && mTimerRunning) {
                    refreshQr(null)
                }
            }
        }
    }

    var mTimerRunning = false
    private fun stopTimer() {
        mTimerRunning = false
        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.ll_bucket -> {
                PayListFragment.newInstance(parentFragment as SupportFragment,null)
            }
            R.id.tv_help -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TITLE,"乘车码帮助")
                bundle.putString(BundleKeys.KEY_FQA_TYPE,"1")
                FQAFragment.newInstance(parentFragment as SupportFragment,bundle)
            }
            R.id.tv_refresh -> {
                refreshQr(DialogUtil.getLoadingDialog(fragmentManager))
            }
            R.id.tv_open -> {
                if (isOpen && UserUtil.getUserInfoBean().realNameVerifyFlag == "0") {
                    if (UserUtil.getUserInfoBean().ownerAmount < 2) {//充值
                        ChargeFragment.newInstance(parentFragment as SupportFragment, null)
                    }
                } else {
                    if (UserUtil.getUserInfoBean().realNameVerifyFlag == "1") {
                        if (UserUtil.getUserInfoBean().ownerAmount < 2 && isOpen) {//充值
                            ChargeFragment.newInstance(parentFragment as SupportFragment, null)
                        } else {
                            val json = JsonObject()
                            json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
                            mPresenter!!.openQRCode(json)
//                        OpenQrFragment.newInstance(parentFragment as SupportFragment,null)
                        }
                    } else {
                        AuthFragment.newInstance(parentFragment as SupportFragment, null)
                    }
                }
            }
        }
    }

    var isOpen = false

    private fun refreshQr(dialog: LoadingDialog?) {
        val json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        HttpUtil.addSubscription(ApiClient.retrofit().getQRCode(json),object :
            ApiCallback<JsonObject, Response<BaseBean<JsonObject>>>(){
            override fun onSuccess(model: BaseBean<JsonObject>) {
                if (model.Code == 200){
                    when {
                        model.Data!!["Status"].asInt == 0 -> {//0-账户未开通
                            isOpen = false
                            if (UserUtil.getUserInfoBean().realNameVerifyFlag == "1") {
                                mBinding.ivWelcome.visibility = View.VISIBLE
                                mBinding.llOpen.visibility = View.VISIBLE
                                mBinding.tvOpen.visibility = View.VISIBLE
                                mBinding.tvOpen.text = "立即开启"
                                mBinding.llBucket.visibility = View.GONE
                                mBinding.llQr.visibility = View.GONE
                                mBinding.llCharge.visibility = View.GONE
                            } else {
                                mBinding.ivWelcome.visibility = View.VISIBLE
                                mBinding.llOpen.visibility = View.VISIBLE
                                mBinding.tvOpen.visibility = View.VISIBLE
                                mBinding.tvOpen.text = "立即实名"
                                mBinding.llBucket.visibility = View.GONE
                                mBinding.llQr.visibility = View.GONE
                                mBinding.llCharge.visibility = View.GONE
                            }
                        }
                        model.Data!!["Status"].asInt == 1 -> {//1-账户余额不足;
                            isOpen = true
                            mBinding.llBucket.visibility = View.GONE
                            mBinding.llOpen.visibility = View.VISIBLE
                            mBinding.tvOpen.visibility = View.VISIBLE
                            mBinding.tvOpen.text = "请去充值"
                            mBinding.llCharge.visibility = View.VISIBLE
                            mBinding.llQr.visibility = View.GONE
                            mBinding.ivWelcome.visibility = View.GONE
                        }
                        model.Data!!["Status"].asInt == 2 -> {//2-生成乘车码成功
                            isOpen = true
                            mBinding.ivWelcome.visibility = View.GONE
                            mBinding.llBucket.visibility = View.VISIBLE
                            mBinding.llOpen.visibility = View.GONE
                            mBinding.llCharge.visibility = View.GONE
                            mBinding.llQr.visibility = View.VISIBLE

                            val decodeArr =
                                Base64.decode(model.Data!!.get("Base64").asString, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(decodeArr, 0, decodeArr.size)
                            mBinding.ivQr.setImageBitmap(bitmap)
                        }
                        else -> {//3-生成乘车码失败;
                            isOpen = true
                            mBinding.ivWelcome.visibility = View.GONE
                            mBinding.llBucket.visibility = View.VISIBLE
                            mBinding.llOpen.visibility = View.GONE
                            mBinding.llCharge.visibility = View.GONE
                            mBinding.llQr.visibility = View.VISIBLE
                            ToastUtil.toastWaring("二维码获取失败")
                        }
                    }
                } else {
                    isOpen = true
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
                mBinding.tvOpen.text = "立即开启"
                if (!TextUtils.isEmpty(msg)) {
                    ToastUtil.toastError(msg!!)
                }
            }

            override fun onFinish() {
                dialog?.dismiss()
            }

        }, this, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    dialog?.dismiss()
                }
            }

        })
    }

    private fun checkQr(dialog: LoadingDialog?) {
        val json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        HttpUtil.addSubscription(ApiClient.retrofit().getQRCode(json),object :
            ApiCallback<JsonObject, Response<BaseBean<JsonObject>>>(){
            override fun onSuccess(model: BaseBean<JsonObject>) {
                if (model.Code == 200){
                    when {
                        model.Data!!["Status"].asInt == 0 -> {//0-账户未开通
                            isOpen = false
                            if (UserUtil.getUserInfoBean().realNameVerifyFlag == "1") {
                                mBinding.ivWelcome.visibility = View.VISIBLE
                                mBinding.llOpen.visibility = View.VISIBLE
                                mBinding.tvOpen.visibility = View.VISIBLE
                                mBinding.tvOpen.text = "立即开启"
                                mBinding.llBucket.visibility = View.GONE
                                mBinding.llQr.visibility = View.GONE
                                mBinding.llCharge.visibility = View.GONE
                            } else {
                                mBinding.ivWelcome.visibility = View.VISIBLE
                                mBinding.llOpen.visibility = View.VISIBLE
                                mBinding.tvOpen.visibility = View.VISIBLE
                                mBinding.tvOpen.text = "立即实名"
                                mBinding.llBucket.visibility = View.GONE
                                mBinding.llQr.visibility = View.GONE
                                mBinding.llCharge.visibility = View.GONE
                            }
                        }
                        model.Data!!["Status"].asInt == 1 -> {//1-账户余额不足;
                            isOpen = true
                            mBinding.llBucket.visibility = View.GONE
                            mBinding.llOpen.visibility = View.VISIBLE
                            mBinding.tvOpen.visibility = View.VISIBLE
                            mBinding.tvOpen.text = "请去充值"
                            mBinding.llCharge.visibility = View.VISIBLE
                            mBinding.llQr.visibility = View.GONE
                            mBinding.ivWelcome.visibility = View.GONE
                        }
                        model.Data!!["Status"].asInt == 2 -> {//2-生成乘车码成功
                            isOpen = true
                            startTimer()
                            mBinding.ivWelcome.visibility = View.GONE
                            mBinding.llBucket.visibility = View.VISIBLE
                            mBinding.llOpen.visibility = View.GONE
                            mBinding.llCharge.visibility = View.GONE
                            mBinding.llQr.visibility = View.VISIBLE

                            val decodeArr =
                                Base64.decode(model.Data!!.get("Base64").asString, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(decodeArr, 0, decodeArr.size)
                            mBinding.ivQr.setImageBitmap(bitmap)
                        }
                        else -> {//3-生成乘车码失败;
                            isOpen = true
                            mBinding.ivWelcome.visibility = View.GONE
                            mBinding.llBucket.visibility = View.VISIBLE
                            mBinding.llOpen.visibility = View.GONE
                            mBinding.llCharge.visibility = View.GONE
                            mBinding.llQr.visibility = View.VISIBLE
                            ToastUtil.toastWaring("二维码获取失败")
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
                if (!TextUtils.isEmpty(msg)) {
                    ToastUtil.toastError(msg!!)
                }
            }

            override fun onFinish() {
                dialog?.dismiss()
            }

        }, this, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    dialog?.dismiss()
                }
            }

        })
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (ismBindingInitialized()){
            checkQr(null)
            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            payListPresenter!!.getDefaultPayList(json,null)
        }
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        stopTimer()
    }

    override fun doAfterAnim() {
    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }
}