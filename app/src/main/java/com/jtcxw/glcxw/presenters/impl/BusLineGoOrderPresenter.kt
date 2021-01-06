package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.AddOrderBean
import com.jtcxw.glcxw.base.respmodels.FrequencyBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IBusLineGoOrder
import com.jtcxw.glcxw.views.BusLineGoOrderView
import models.BaseBean
import retrofit2.Response

class BusLineGoOrderPresenter: IBusLineGoOrder {

    var iView:BusLineGoOrderView
    constructor(view: BusLineGoOrderView){
        iView = view
    }

    override fun addOrder(params: JsonObject, dialog: LoadingDialog) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().addOrder(params),object :
            ApiCallback<AddOrderBean, Response<BaseBean<AddOrderBean>>>(){
            override fun onSuccess(model: BaseBean<AddOrderBean>) {
                if (model.Code == 200){
                    iView?.onAddOrderSucc(model.Data!!)
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

        }, fragment, null)
    }



    override fun getFrequencys(params: JsonObject,dialog: LoadingDialog) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getFrequencys(params),object :
            ApiCallback<FrequencyBean, Response<BaseBean<FrequencyBean>>>(){
            override fun onSuccess(model: BaseBean<FrequencyBean>) {
                if (model.Code == 200){
                    iView?.onFrequencySucc(model.Data!!,params.get("RouteId").asString)
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
                iView?.onFrequencyFinish()
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