package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.AliSignBean
import com.jtcxw.glcxw.base.respmodels.AliSignStatusBean
import com.jtcxw.glcxw.base.respmodels.AliUnSignBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IPayAli
import com.jtcxw.glcxw.views.PayAliView
import models.BaseBean
import retrofit2.Response

class PayAliPresenter:IPayAli {
    var iView: PayAliView?= null
    constructor(view: PayAliView){
        iView = view
    }
    override fun useragreementPageSign(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().useragreementPageSign(jsonObject),object :
            ApiCallback<AliSignBean, Response<BaseBean<AliSignBean>>>(){
            override fun onSuccess(model: BaseBean<AliSignBean>) {
                if (model.Code == 200){
                    if (model.Data != null) {
                        iView?.onUseragreementPageSignSucc(model.Data!!)
                    }
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

    override fun useragreementUnSign(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().useragreementUnSign(jsonObject),object :
            ApiCallback<AliUnSignBean, Response<BaseBean<AliUnSignBean>>>(){
            override fun onSuccess(model: BaseBean<AliUnSignBean>) {
                if (model.Code == 200){
                    if (model.Data != null) {
                        iView?.onUseragreementUnSignSucc(model.Data!!)
                    }
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

    override fun useragreementQuery(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().useragreementQuery(jsonObject),object :
            ApiCallback<AliSignStatusBean, Response<BaseBean<AliSignStatusBean>>>(){
            override fun onSuccess(model: BaseBean<AliSignStatusBean>) {
                if (model.Code == 200){
                    if (model.Data != null) {
                        iView?.onUseragreementQuerySucc(model.Data!!)
                    }
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