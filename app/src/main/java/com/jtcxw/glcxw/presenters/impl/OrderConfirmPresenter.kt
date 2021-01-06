package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.ComplimentaryTicketBean
import com.jtcxw.glcxw.base.respmodels.FrequencyBean
import com.jtcxw.glcxw.base.respmodels.OrderCancelBean
import com.jtcxw.glcxw.base.respmodels.OrderConfirmBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IOrderConfirm
import com.jtcxw.glcxw.views.OrderConfirmView
import models.BaseBean
import retrofit2.Response

class OrderConfirmPresenter:IOrderConfirm {
    override fun complimentaryTicket(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().complimentaryTicket(jsonObject),object :
            ApiCallback<ComplimentaryTicketBean, Response<BaseBean<ComplimentaryTicketBean>>>(){
            override fun onSuccess(model: BaseBean<ComplimentaryTicketBean>) {
                if (model.Code == 200){
                    iView?.onComplimentaryTicketSucc(model.Data!!)
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

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    dialog.dismiss()
                }
            }

        })
    }

    override fun orderCancel(params: JsonObject, dialog: LoadingDialog) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().orderCancel(params),object :
            ApiCallback<OrderCancelBean, Response<BaseBean<OrderCancelBean>>>(){
            override fun onSuccess(model: BaseBean<OrderCancelBean>) {
                if (model.Code == 200){
                    iView?.onOrderCancelSucc(model.Data!!)
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

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    dialog.dismiss()
                }
            }

        })
    }

    var iView:OrderConfirmView
    constructor(view: OrderConfirmView){
        iView = view
    }
    override fun getOrderDetail(params: JsonObject, dialog: LoadingDialog) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getOrderDetail(params),object :
            ApiCallback<OrderConfirmBean, Response<BaseBean<OrderConfirmBean>>>(){
            override fun onSuccess(model: BaseBean<OrderConfirmBean>) {
                if (model.Code == 200){
                    iView?.onOrderDetailSucc(model.Data!!)
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

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    dialog.dismiss()
                }
            }

        })
    }
}