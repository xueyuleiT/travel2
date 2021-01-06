package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IBus
import com.jtcxw.glcxw.views.BusView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class BusPresenter:IBus {
    var iView: BusView?= null
    constructor(view: BusView){
        iView = view
    }
    override fun busInquiryAnnexBus(
        jsonObject: JsonObject,
        smartRefreshLayout: SmartRefreshLayout
    ) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().annexBus(jsonObject),object :
            ApiCallback<AnnexBusBean, Response<BaseBean<AnnexBusBean>>>(){
            override fun onSuccess(model: BaseBean<AnnexBusBean>) {
                if (model.Code == 200){
                    iView?.onBusInquiryAnnexBusSucc(model.Data!!)
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
                iView?.onBusInquiryAnnexBusFinish()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    smartRefreshLayout.finishRefresh(0)
                }
            }

        })
    }
}