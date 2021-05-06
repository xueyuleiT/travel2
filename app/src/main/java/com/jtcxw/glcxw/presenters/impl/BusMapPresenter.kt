package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiCallbackWithCode
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

    override fun forcastArriveQuery(jsonObject: JsonObject,type:Int) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().forcastArriveQuery(jsonObject),object :
            ApiCallbackWithCode<BusArriveListBean, Response<BaseBean<BusArriveListBean>>>(){
            override fun onSuccess(model: BaseBean<BusArriveListBean>) {
                if (model.Code == 200){
                    iView?.onForcastArriveQuerySucc(model.Data!!,type)
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(code:Int,innerCode:Int,msg: String?) {
                if (innerCode == 400) {
                    iView?.onForcastArriveQuerySucc(BusArriveListBean(),type)
                }
//                if (!TextUtils.isEmpty(msg)) {
//                    ToastUtil.toastError(msg!!)
//                }
            }

            override fun onFinish() {
            }

        }, fragment,null)
    }

}