package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.ExtraOrderBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IMy
import com.jtcxw.glcxw.views.MyView
import models.BaseBean
import retrofit2.Response

open class MyPresenter:IMy {
    override fun getOrderStatistics(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getOrderStatistics(jsonObject), object :
            ApiCallback<ExtraOrderBean, Response<BaseBean<ExtraOrderBean>>>() {
            override fun onSuccess(model: BaseBean<ExtraOrderBean>) {
                if (model.Code == 200) {
                    iView?.onOrderStatisticsSucc(model.Data!!)
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
                iView?.onOrderStatisticsFinish()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                iView?.onOrderStatisticsFinish()
            }

        })
    }

    var iView: MyView? = null

    constructor(view: MyView) {
        iView = view
    }

    override fun getMemberInfo(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getMemberInfo(jsonObject), object :
            ApiCallback<UserInfoBean, Response<BaseBean<UserInfoBean>>>() {
            override fun onSuccess(model: BaseBean<UserInfoBean>) {
                if (model.Code == 200) {
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
                iView?.onMemberInfoFailed(if (TextUtils.isEmpty(msg)) "" else msg!!)
            }

            override fun onFinish() {
                iView?.onMemberInfoFinish()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                iView?.onMemberInfoFinish()
            }

        })
    }
}
   