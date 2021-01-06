package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.TicketBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IOrderTicketGo
import com.jtcxw.glcxw.views.OrderTicketGoView
import models.BaseBean
import retrofit2.Response

class OrderTicketGoPresenter:IOrderTicketGo {

    var iView:OrderTicketGoView ?= null
    constructor(view: OrderTicketGoView){
        iView = view
    }
    override fun getRoundTikmodelList(params: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getRoundTikmodelList(params),object :
            ApiCallback<TicketBean, Response<BaseBean<TicketBean>>>(){
            override fun onSuccess(model: BaseBean<TicketBean>) {
                if (model.Code == 200){
                    iView?.onRoundTikmodelListSucc(model.Data!!,params.get("ScheduleId").asString)
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
//                if (!TextUtils.isEmpty(msg)) {
//                    ToastUtil.toastError(msg!!)
//                }
                iView?.onRoundTikmodelListFailed(params.get("ScheduleId").asString)
            }

            override fun onFinish() {
            }

        }, fragment, null)
    }
}