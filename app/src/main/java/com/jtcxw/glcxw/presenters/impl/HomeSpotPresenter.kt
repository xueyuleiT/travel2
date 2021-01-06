package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.ContentListBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IHomeSpot
import com.jtcxw.glcxw.views.HomeSpotView
import models.BaseBean
import retrofit2.Response

class HomeSpotPresenter:IHomeSpot {
    var iView:HomeSpotView?= null
    constructor(view: HomeSpotView){
        iView = view
    }
    override fun getContentList(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getContentList(jsonObject),object :
            ApiCallback<ContentListBean, Response<BaseBean<ContentListBean>>>(){
            override fun onSuccess(model: BaseBean<ContentListBean>) {
                if (model.Code == 200){
                    iView?.onContentListSucc(model.Data!!)
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
            }

        }, fragment, null)
    }
}