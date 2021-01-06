package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.respmodels.PicVerifyCodeBean
import com.jtcxw.glcxw.base.utils.DeviceUtil
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.ILogin
import com.jtcxw.glcxw.views.LoginView
import models.BaseBean
import retrofit2.Response

class LoginPresenter : ILogin {
    override fun login(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().login(jsonObject),object :
            ApiCallback<UserInfoBean, Response<BaseBean<UserInfoBean>>>(){
            override fun onSuccess(model: BaseBean<UserInfoBean>) {
                if (model.Code == 200) {
                    iView?.onLoginSucc(model.Data!!)
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
                if (!TextUtils.isEmpty(msg)) {
                    ToastUtil.toastError(msg!!)
                    iView?.onLoginFailed(msg)
                } else {
                    iView?.onLoginFailed("")
                }

            }

            override fun onFinish() {
                dialog.dismiss()
            }

        }, fragment, object : RefreshCallback{
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    dialog.dismiss()
                }
            }

        })
    }

    var iView:LoginView ?= null
    constructor(view: LoginView){
        iView = view
    }

    override fun loginVerifyCode(id: String) {
        val fragment = (iView as BaseFragment<*, *>)
        val json = JsonObject()
        json.addProperty("LoginGuid", DeviceUtil.getDeviceId(fragment.context))
        HttpUtil.addSubscription(ApiClient.retrofit().loginVerifyCode(json),object :
            ApiCallback<PicVerifyCodeBean, Response<BaseBean<PicVerifyCodeBean>>>(){
            override fun onSuccess(model: BaseBean<PicVerifyCodeBean>) {
                if (model.Code == 200){
                    iView?.onLoginVerifyCodeSucc(model.Data!!)
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
                if (!TextUtils.isEmpty(msg)) {
                    iView?.onLoginVerifyCodeFailed(msg!!)
                }
            }

            override fun onFinish() {
                iView?.onLoginVerifyCodeFinish()
            }

        }, fragment, null)
    }
}