package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.IDTypeBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IAuth
import com.jtcxw.glcxw.views.AuthView
import models.BaseBean
import retrofit2.Response

class AuthPresenter:IAuth {
    override fun getOrderStatistics(jsonObject: JsonObject) {
    }

    override fun getMemberInfo(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getMemberInfo(jsonObject),object :
            ApiCallback<UserInfoBean, Response<BaseBean<UserInfoBean>>>(){
            override fun onSuccess(model: BaseBean<UserInfoBean>) {
                if (model.Code == 200){
                    iView?.onMemberInfoSucc(model.Data!!)
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
                iView?.onMemberInfoFailed( if(TextUtils.isEmpty(msg))  "" else msg!!)
            }

            override fun onFinish() {
                iView?.onMemberInfoFinish()
            }

        }, fragment, object : RefreshCallback{
            override fun onRefreshBack(refreshSucc: Boolean) {
                iView?.onMemberInfoFinish()
            }

        })
    }

    override fun getIdTypeList() {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().getIdTypeList(JsonObject()),object :
            ApiCallback<IDTypeBean, Response<BaseBean<IDTypeBean>>>(){
            override fun onSuccess(model: BaseBean<IDTypeBean>) {
                if (model.Code == 200){
                    (iView as AuthView).onGetIdTypeListSucc(model.Data!!)
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
    var iView:AuthView?= null
    constructor(view:AuthView) {
        iView = view
    }
    override fun authentication(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().authentication(jsonObject),object :
            ApiCallback<JsonObject, Response<BaseBean<JsonObject>>>(){
            override fun onSuccess(model: BaseBean<JsonObject>) {
                if (model.Code == 200){
                    (iView as AuthView).onAuthenticationSucc(model.Data!!)
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

    override fun authenticationImage(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().authenticationImage(jsonObject),object :
            ApiCallback<JsonObject, Response<BaseBean<JsonObject>>>(){
            override fun onSuccess(model: BaseBean<JsonObject>) {
                if (model.Code == 200){
                    (iView as AuthView).onAuthenticationImageSucc(model.Data!!,jsonObject.get("ProsAndCons").asInt)
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