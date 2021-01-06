package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.BusArriveListBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IBusMap
import com.jtcxw.glcxw.views.BusMapView
import models.BaseBean
import retrofit2.Response

class BusMapPresenter:IBusMap {

    var iView:BusMapView?= null
    constructor(view: BusMapView){
        iView = view
    }

    override fun forcastArriveQuery(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().forcastArriveQuery(jsonObject),object :
            ApiCallback<BusArriveListBean, Response<BaseBean<BusArriveListBean>>>(){
            override fun onSuccess(model: BaseBean<BusArriveListBean>) {
                if (model.Code == 200){
                    iView?.onForcastArriveQuerySucc(model.Data!!)
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
            }

            override fun onFinish() {
            }

        }, fragment,null)
    }

}