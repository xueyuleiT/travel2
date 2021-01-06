package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IBusQuery
import com.jtcxw.glcxw.views.BusQueryView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class BusQueryPresenter:IBusQuery {
    override fun clearQueryHistory(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().clearQueryHistory(jsonObject),object :
            ApiCallback<JsonObject, Response<BaseBean<JsonObject>>>(){
            override fun onSuccess(model: BaseBean<JsonObject>) {
                if (model.Code == 200){
                    iView?.onClearQueryHistorySucc(model.Data!!)
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
            }

            override fun onFinish() {
                dialog.dismiss()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                }
            }

        })
    }

    override fun listQueryHistory(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().listQueryHistory(jsonObject),object :
            ApiCallback<SiteOrLineBean, Response<BaseBean<SiteOrLineBean>>>(){
            override fun onSuccess(model: BaseBean<SiteOrLineBean>) {
                if (model.Code == 200){
                    iView?.onListHistorySucc(model.Data!!)
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
            }

            override fun onFinish() {
                dialog.dismiss()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                }
            }

        })
    }

    override fun saveQueryHistory(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().saveQueryHistory(jsonObject),object :
            ApiCallback<JsonObject, Response<BaseBean<JsonObject>>>(){
            override fun onSuccess(model: BaseBean<JsonObject>) {
            }

            override fun onFailure(msg: String?) {
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

    var iView:BusQueryView ?= null
    constructor(view: BusQueryView){
        iView = view
    }

    override fun querySiteOrLine(
        jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().querySiteOrLine(jsonObject),object :
            ApiCallback<SiteOrLineBean, Response<BaseBean<SiteOrLineBean>>>(){
            override fun onSuccess(model: BaseBean<SiteOrLineBean>) {
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
               smartRefreshLayout.finishRefresh(0)
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