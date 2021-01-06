package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.respmodels.CityBean
import com.jtcxw.glcxw.base.respmodels.LineBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IBusCustomized
import com.jtcxw.glcxw.views.BusCustomizedView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class BusCustomizedPresenter:IBusCustomized {

    var iView:BusCustomizedView?= null
    constructor(view: BusCustomizedView) {
        iView = view
    }

    override fun getCitys(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getCitys(jsonObject),object :
            ApiCallback<CityBean, Response<BaseBean<CityBean>>>(){
            override fun onSuccess(model: BaseBean<CityBean>) {
                if (model.Code == 200){
                    iView?.onGetCitysSucc(model.Data!!)
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
                smartRefreshLayout.finishRefresh(0)
            }

            override fun onFinish() {
                iView?.onGetCitysFinish()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    smartRefreshLayout.finishRefresh(0)
                }
            }

        })
    }

    override fun getLines(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getLines(jsonObject),object :
            ApiCallback<LineBean, Response<BaseBean<LineBean>>>(){
            override fun onSuccess(model: BaseBean<LineBean>) {
                if (model.Code == 200){
                    iView?.onGetLinesSucc(model.Data!!)
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
                smartRefreshLayout.finishRefresh(0)
            }

            override fun onFinish() {
                iView?.onGetLinesFinish()
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