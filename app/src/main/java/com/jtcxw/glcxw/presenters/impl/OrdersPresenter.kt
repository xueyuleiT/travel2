package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.DictionaryInfoBean
import com.jtcxw.glcxw.base.respmodels.OrderMixBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IOrders
import com.jtcxw.glcxw.views.OrdersView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class OrdersPresenter:IOrders {
    var iView:OrdersView ?= null
    constructor(view: OrdersView){
        iView = view
    }
    override fun getMemberOrder(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getMemberOrder(jsonObject),object :
            ApiCallback<OrderMixBean, Response<BaseBean<OrderMixBean>>>(){
            override fun onSuccess(model: BaseBean<OrderMixBean>) {
                if (model.Code == 200){
                    if (model.Data != null) {
                        iView?.onGetMemberOrderSucc(model.Data!!)
                    } else {
                        iView?.onGetMemberOrderSucc(OrderMixBean())
                    }
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
                iView?.onGetMemberOrderFailed()
            }


            override fun onFinish() {
                iView?.onGetMemberOrderFinish()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    smartRefreshLayout.finishRefresh(0)
                }
            }

        })
    }

    override fun getOrderTypeList(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getOrderTypeList(jsonObject),object :
            ApiCallback<DictionaryInfoBean, Response<BaseBean<DictionaryInfoBean>>>(){
            override fun onSuccess(model: BaseBean<DictionaryInfoBean>) {
                if (model.Code == 200){
                    iView?.onGetOrderTypeListSucc(model.Data!!)
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
                iView?.onGetOrderTypeListFailed()
            }

            override fun onFinish() {
                iView?.onGetOrderTypeLisFinish()
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