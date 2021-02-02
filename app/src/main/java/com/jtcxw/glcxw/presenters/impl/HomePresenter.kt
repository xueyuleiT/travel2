package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.*
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IHome
import com.jtcxw.glcxw.views.HomeView
import com.jtcxw.glcxw.views.TravelView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class HomePresenter:IHome , BannerPresenter{
    override fun h5ModuleConfig(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().h5ModuleConfig(jsonObject),object :
            ApiCallback<ModuleConfigBean, Response<BaseBean<ModuleConfigBean>>>(){
            override fun onSuccess(model: BaseBean<ModuleConfigBean>) {
                if (model.Code == 200){
                    (iView as HomeView).onModuleConfigSucc(model.Data!!)
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

    override fun getMemberInfo(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getMemberInfo(jsonObject), object :
            ApiCallback<UserInfoBean, Response<BaseBean<UserInfoBean>>>() {
            override fun onSuccess(model: BaseBean<UserInfoBean>) {
                if (model.Code == 200) {
                    (iView as HomeView).onMemberInfoSucc(model.Data!!)
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
                (iView as HomeView).onMemberInfoFailed(if (TextUtils.isEmpty(msg)) "" else msg!!)
            }

            override fun onFinish() {
                (iView as HomeView).onMemberInfoFinish()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                (iView as HomeView).onMemberInfoFinish()
            }

        })
    }

    override fun getContentTypeList(
        jsonObject: JsonObject,
        smartRefreshLayout: SmartRefreshLayout
    ) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getContentTypeList(jsonObject),object :
            ApiCallback<DictionaryInfoBean, Response<BaseBean<DictionaryInfoBean>>>(){
            override fun onSuccess(model: BaseBean<DictionaryInfoBean>) {
                if (model.Code == 200){
                    (iView as HomeView).onGetContentTypeListSucc(model.Data!!)
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
                (iView as HomeView).onGetContentTypeListFinish()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    smartRefreshLayout.finishRefresh(0)
                }
            }

        })
    }

    constructor(view: HomeView){
        iView = view
    }


    override fun getHotelInfoList(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getHotelInfoList(jsonObject),object :
            ApiCallback<HotelBean, Response<BaseBean<HotelBean>>>(){
            override fun onSuccess(model: BaseBean<HotelBean>) {
                if (model.Code == 200){
                    (iView as HomeView).onHotelInfoListSucc(model.Data!!)
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
                (iView as HomeView).onHotelInfoListFinish()
            }

        }, fragment, object : RefreshCallback {
            override fun onRefreshBack(refreshSucc: Boolean) {
                if (!refreshSucc) {
                    smartRefreshLayout.finishRefresh(0)
                }
            }

        })
    }

    override fun getScenicInfoList(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getScenicInfoList(jsonObject),object :
            ApiCallback<ScenicBean, Response<BaseBean<ScenicBean>>>(){
            override fun onSuccess(model: BaseBean<ScenicBean>) {
                if (model.Code == 200){
                    (iView as HomeView).onScenicInfoListSucc(model.Data!!)
                } else {
                    (iView as TravelView).onScenicInfoListFailed()
                    if (model.Code != 400 && !TextUtils.isEmpty(model.Info)) {
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
                (iView as HomeView).onScenicInfoListFinish()
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