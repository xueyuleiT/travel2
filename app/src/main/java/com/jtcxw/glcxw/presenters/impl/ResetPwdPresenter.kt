package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.ResetPwdBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IResetPwd
import com.jtcxw.glcxw.views.ResetPwdView
import models.BaseBean
import retrofit2.Response

class ResetPwdPresenter:IResetPwd {
    override fun changePwd(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().changePwd(jsonObject),object :
            ApiCallback<ResetPwdBean, Response<BaseBean<ResetPwdBean>>>(){
            override fun onSuccess(model: BaseBean<ResetPwdBean>) {
                if (model.Code == 200) {
                    iView?.onResetPwdSucc(model.Data!!)
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

    var iView:ResetPwdView ?= null
    constructor(view:ResetPwdView) {
        iView = view
    }


}