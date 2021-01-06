package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.respmodels.BusArriveListBean
import com.jtcxw.glcxw.base.respmodels.MisceAneousBean
import com.jtcxw.glcxw.base.respmodels.ModuleConfigBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IBanner
import com.jtcxw.glcxw.presenters.IGoTravel
import com.jtcxw.glcxw.views.GoTravelView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class GoTravelPresenter:IGoTravel ,BannerPresenter{
    override fun h5ModuleConfig(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().h5ModuleConfig(jsonObject),object :
            ApiCallback<ModuleConfigBean, Response<BaseBean<ModuleConfigBean>>>(){
            override fun onSuccess(model: BaseBean<ModuleConfigBean>) {
                if (model.Code == 200){
                    (iView as GoTravelView).onModuleConfigSucc(model.Data!!)
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
            }

            override fun onFinish() {
                dialog.dismiss()
            }

        }, fragment,object :RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                dialog.dismiss()
            }

        })
    }

    override fun getMisceAneous(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getMisceAneous(jsonObject),object :
            ApiCallback<MisceAneousBean, Response<BaseBean<MisceAneousBean>>>(){
            override fun onSuccess(model: BaseBean<MisceAneousBean>) {
                if (model.Code == 200){
                    (iView as GoTravelView).onGetMisceAneousBeanSucc(model.Data!!)
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
            }

            override fun onFinish() {
            }

        }, fragment,null)
    }

    override fun forcastArriveQuery(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
         HttpUtil.addSubscription(ApiClient.retrofit().forcastArriveQuery(jsonObject),object :
            ApiCallback<BusArriveListBean, Response<BaseBean<BusArriveListBean>>>(){
            override fun onSuccess(model: BaseBean<BusArriveListBean>) {
                if (model.Code == 200){
                    (iView as GoTravelView).onForcastArriveQuerySucc(model.Data!!)
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
            }

            override fun onFinish() {
            }

        }, fragment,null)
    }

    constructor(view: GoTravelView){
        iView = view
    }
    override fun busInquiryAnnexBus(
        jsonObject: JsonObject,
        smartRefreshLayout: SmartRefreshLayout
    ) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().annexBus(jsonObject),object :
            ApiCallback<AnnexBusBean, Response<BaseBean<AnnexBusBean>>>(){
            override fun onSuccess(model: BaseBean<AnnexBusBean>) {
                if (model.Code == 200){
                    (iView as GoTravelView).onBusInquiryAnnexBusSucc(model.Data!!)
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
                (iView as GoTravelView).onBusInquiryAnnexBusFinish()
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