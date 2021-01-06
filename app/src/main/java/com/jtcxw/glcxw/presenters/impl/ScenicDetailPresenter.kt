package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.RecruitResultBean
import com.jtcxw.glcxw.base.respmodels.ScenicDetailBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IScenicDetail
import com.jtcxw.glcxw.views.ScenicDetailView
import models.BaseBean
import retrofit2.Response

class ScenicDetailPresenter:IScenicDetail {
    var iView:ScenicDetailView?= null
    constructor(view: ScenicDetailView){
        iView = view
    }
    override fun getScenicDetail(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog = DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().getScenicDetail(jsonObject),object :
            ApiCallback<ScenicDetailBean, Response<BaseBean<ScenicDetailBean>>>(){
            override fun onSuccess(model: BaseBean<ScenicDetailBean>) {
                if (model.Code == 200){
                    iView?.onScenicDetailSucc(model.Data!!)
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