package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.GoodListBean
import com.jtcxw.glcxw.base.respmodels.MisceAneousBean
import com.jtcxw.glcxw.base.respmodels.PayRechargeBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.ICharge
import com.jtcxw.glcxw.presenters.IMy
import com.jtcxw.glcxw.views.ChargeView
import com.jtcxw.glcxw.views.ServiceView
import models.BaseBean
import retrofit2.Response

class ChargePresenter:ICharge{
    override fun getMisceAneous(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getMisceAneous(jsonObject),object :
            ApiCallback<MisceAneousBean, Response<BaseBean<MisceAneousBean>>>(){
            override fun onSuccess(model: BaseBean<MisceAneousBean>) {
                if (model.Code == 200){
                    (iView)?.onGetMisceAneousBeanSucc(model.Data!!)
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
            }

            override fun onFinish() {
            }

        }, fragment,null)
    }

    override fun payRecharge(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().payRecharge(jsonObject),object :
            ApiCallback<PayRechargeBean, Response<BaseBean<PayRechargeBean>>>(){
            override fun onSuccess(model: BaseBean<PayRechargeBean>) {
                if (model.Code == 200){
                    iView?.onPayRechargeSucc(model.Data!!)
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

    var iView:ChargeView? = null
    constructor(view:ChargeView) {
        iView = view
    }

    override fun getGoodsInfo() {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().getGoodsInfo(JsonObject()),object :
            ApiCallback<GoodListBean, Response<BaseBean<GoodListBean>>>(){
            override fun onSuccess(model: BaseBean<GoodListBean>) {
                if (model.Code == 200){
                    iView?.onGetGoodsInfoSucc(model.Data!!)
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