package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.HotelBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IHotel
import com.jtcxw.glcxw.views.HomeView
import com.jtcxw.glcxw.views.HotelView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class HotelPresenter:IHotel {

    var iView:HotelView?= null
    constructor(view: HotelView) {
        iView = view
    }

    override fun getHotelInfoList(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getHotelInfoList(jsonObject),object :
            ApiCallback<HotelBean, Response<BaseBean<HotelBean>>>(){
            override fun onSuccess(model: BaseBean<HotelBean>) {
                if (model.Code == 200){
                    (iView as HotelView).onHotelInfoListSucc(model.Data!!)
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
                (iView as HotelView).onHotelInfoListFinish()
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