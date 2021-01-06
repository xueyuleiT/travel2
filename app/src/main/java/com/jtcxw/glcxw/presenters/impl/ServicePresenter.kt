package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.CusServerBean
import com.jtcxw.glcxw.base.respmodels.MisceAneousBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IService
import com.jtcxw.glcxw.views.ServiceView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class ServicePresenter:IService {
    var iView:ServiceView?= null
    constructor(view:ServiceView) {
        iView = view
    }
    override fun getMisceAneous(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getMisceAneous(jsonObject),object :
            ApiCallback<MisceAneousBean, Response<BaseBean<MisceAneousBean>>>(){
            override fun onSuccess(model: BaseBean<MisceAneousBean>) {
                if (model.Code == 200){
                    (iView as ServiceView).onGetMisceAneousBeanSucc(model.Data!!)
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
            }

            override fun onFinish() {
                (iView as ServiceView).onGetMisceAneousBeanFinish()
            }

        }, fragment,null)
    }

    override fun getCusServerInfo(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getCusServerInfo(jsonObject),object :
            ApiCallback<List<CusServerBean>, Response<BaseBean<List<CusServerBean>>>>(){
            override fun onSuccess(model: BaseBean<List<CusServerBean>>) {
                if (model.Code == 200){
                    (iView as ServiceView).onGetCusServerInfoSucc(model.Data!!)
                } else {
                    if (!TextUtils.isEmpty(model.Info)) {
                        ToastUtil.toastError(model.Info!!)
                    }
                }
            }

            override fun onFailure(msg: String?) {
            }

            override fun onFinish() {
                (iView as ServiceView).onGetCusServerInfoFinish()
            }

        }, fragment,null)

    }

}