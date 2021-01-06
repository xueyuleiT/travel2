package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.PayBean
import com.jtcxw.glcxw.base.respmodels.PayRechargeBean
import com.jtcxw.glcxw.base.respmodels.PayTypeBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IOrderPay
import com.jtcxw.glcxw.views.OrderPayView
import models.BaseBean
import retrofit2.Response

class OrderPayPresenter:IOrderPay {
    override fun payRecharge(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().payRecharge(jsonObject),object :
            ApiCallback<PayRechargeBean, Response<BaseBean<PayRechargeBean>>>(){
            override fun onSuccess(model: BaseBean<PayRechargeBean>) {
                if (model.Code == 200){
                    iView?.onPayRechargeSucc(model.Data!!,dialog)
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
                dialog.dismiss()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    dialog.dismiss()
                }
            }

        })
    }
    override fun pay(params: JsonObject,dialog: LoadingDialog) {

        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().pay(params),object :
            ApiCallback<PayBean, Response<BaseBean<PayBean>>>(){
            override fun onSuccess(model: BaseBean<PayBean>) {
                if (model.Code == 200) {
                    iView?.onPaySucc(model.Data!!,dialog)
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
                dialog.dismiss()
            }

            override fun onFinish() {
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    dialog.dismiss()
                }
            }

        })
    }

    var iView:OrderPayView ?= null
    constructor(view: OrderPayView){
        iView = view
    }


    override fun getPayType(params: JsonObject,loadingDialog: LoadingDialog?) {

        val fragment = (iView as BaseFragment<*, *>)
        var dialog = loadingDialog
        if (dialog == null) {
            dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        }
        HttpUtil.addSubscription(ApiClient.retrofit().getPayType(params),object :
            ApiCallback<PayTypeBean, Response<BaseBean<PayTypeBean>>>(){
            override fun onSuccess(model: BaseBean<PayTypeBean>) {
                if (model.Code == 200) {
                    iView?.onGetPayTypeSucc(model.Data!!)
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

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    dialog?.dismiss()
                }
            }

        })

    }
}