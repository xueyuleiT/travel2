package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.DictionaryInfoBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IUserInfo
import com.jtcxw.glcxw.views.UserInfoView
import models.BaseBean
import retrofit2.Response

class UserInfoPresenter:IUserInfo {
    override fun headImage(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().headImage(jsonObject),object :
            ApiCallback<JsonObject, Response<BaseBean<JsonObject>>>(){
            override fun onSuccess(model: BaseBean<JsonObject>) {
                if (model.Code == 200){
                    iView?.onHeadImage(model.Data!!)
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

    override fun modifyMemberInfo(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().modifyMemberInfo(jsonObject),object :
            ApiCallback<UserInfoBean, Response<BaseBean<UserInfoBean>>>(){
            override fun onSuccess(model: BaseBean<UserInfoBean>) {
                if (model.Code == 200){
                    iView?.onModifyMemberInfoSucc(model.Data!!)
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

    var iView:UserInfoView?= null
    constructor(view:UserInfoView) {
        iView = view
    }
    override fun getSexList() {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getSexList(JsonObject()),object :
            ApiCallback<DictionaryInfoBean, Response<BaseBean<DictionaryInfoBean>>>(){
            override fun onSuccess(model: BaseBean<DictionaryInfoBean>) {
                if (model.Code == 200){
                    iView?.onGetSexListSucc(model.Data!!.dictionaryInfo)
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
                iView?.onGetSexListFinish()
            }

        }, fragment, null)
    }

    override fun getCountryList() {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getCountryList(JsonObject()),object :
            ApiCallback<DictionaryInfoBean, Response<BaseBean<DictionaryInfoBean>>>(){
            override fun onSuccess(model: BaseBean<DictionaryInfoBean>) {
                if (model.Code == 200){
                    iView?.onGetCountryListSucc(model.Data!!.dictionaryInfo)
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
                iView?.onGetCountryListFinish()
            }

        }, fragment, null)
    }

    override fun getOccupationList() {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getOccupationList(JsonObject()),object :
            ApiCallback<DictionaryInfoBean, Response<BaseBean<DictionaryInfoBean>>>(){
            override fun onSuccess(model: BaseBean<DictionaryInfoBean>) {
                if (model.Code == 200){
                    iView?.onGetOccupationListSucc(model.Data!!.dictionaryInfo)
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
                iView?.onGetOccupationListFinish()
            }

        }, fragment,null)
    }
}