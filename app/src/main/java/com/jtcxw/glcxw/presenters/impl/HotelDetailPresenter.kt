package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.HotelDetailBean
import com.jtcxw.glcxw.base.respmodels.ScenicDetailBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IHotelDetail
import com.jtcxw.glcxw.views.HotelDetailView
import models.BaseBean
import retrofit2.Response

class HotelDetailPresenter:IHotelDetail {

    var iView:HotelDetailView?= null
    constructor(view: HotelDetailView){
        iView = view
    }

    override fun getHotelDetail(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().getHotelDetail(jsonObject),object :
            ApiCallback<HotelDetailBean, Response<BaseBean<HotelDetailBean>>>(){
            override fun onSuccess(model: BaseBean<HotelDetailBean>) {
                if (model.Code == 200){
                    iView?.onGetHotelDetailSucc(model.Data!!)
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