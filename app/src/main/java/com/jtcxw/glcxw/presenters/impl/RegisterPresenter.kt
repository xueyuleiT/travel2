package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.RegisterBean
import com.jtcxw.glcxw.base.respmodels.SmsBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IRegister
import com.jtcxw.glcxw.views.RegisterView
import models.BaseBean
import retrofit2.Response

class RegisterPresenter:IRegister {
    var iView:RegisterView?= null
    constructor(view: RegisterView) {
        iView = view
    }
    override fun smsRegister(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().smsRegister(jsonObject),object :
            ApiCallback<RegisterBean, Response<BaseBean<RegisterBean>>>(){
            override fun onSuccess(model: BaseBean<RegisterBean>) {
                if (model.Code == 200){
                    iView?.onSmsRegisterSucc(model.Data!!)
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

    override fun sendSmsCode(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().sendSmsCode(jsonObject),object :
            ApiCallback<SmsBean, Response<BaseBean<SmsBean>>>(){
            override fun onSuccess(model: BaseBean<SmsBean>) {
                if (model.Code == 200){
                    iView?.onSendSmsCodeSucc(model.Data!!)
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
}