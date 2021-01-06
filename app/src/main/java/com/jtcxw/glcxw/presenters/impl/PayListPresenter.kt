package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.PayListBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IPayList
import com.jtcxw.glcxw.views.PayListView
import models.BaseBean
import retrofit2.Response

class PayListPresenter:IPayList {
    override fun setDefaultPayList(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().setDefaultPayList(jsonObject),object :
            ApiCallback<JsonObject, Response<BaseBean<JsonObject>>>(){
            override fun onSuccess(model: BaseBean<JsonObject>) {
                if (model.Code == 200){
                    if (model.Data != null) {
                        iView?.onSetDefaultPayListSucc(model.Data!!)
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
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                }
            }

        })
    }

    var iView:PayListView ?= null
    constructor(view: PayListView){
        iView = view
    }
    override fun getDefaultPayList(jsonObject: JsonObject,loadingDialog:LoadingDialog?) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getDefaultPayList(jsonObject),object :
            ApiCallback<PayListBean, Response<BaseBean<PayListBean>>>(){
            override fun onSuccess(model: BaseBean<PayListBean>) {
                if (model.Code == 200){
                    if (model.Data != null) {
                        iView?.onGetDefaultPayListSucc(model.Data!!)
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
                loadingDialog?.dismiss()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    loadingDialog?.dismiss()
                }
            }

        })
    }
}