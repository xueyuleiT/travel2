package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.CommonFQABean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IFQA
import com.jtcxw.glcxw.views.FQAView
import models.BaseBean
import retrofit2.Response

class FQAPresenter:IFQA {
    var iView: FQAView? = null
    constructor(view:FQAView) {
        iView = view
    }
    override fun getFQA(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getFQA(jsonObject),object :
            ApiCallback<CommonFQABean, Response<BaseBean<CommonFQABean>>>(){
            override fun onSuccess(model: BaseBean<CommonFQABean>) {
                if (model.Code == 200){
                    iView?.onGetFQASucc(model.Data!!)
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
                iView?.onGetFQAFailed()
            }

            override fun onFinish() {
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    iView?.onGetFQAFailed()
                }
            }

        })
    }
}