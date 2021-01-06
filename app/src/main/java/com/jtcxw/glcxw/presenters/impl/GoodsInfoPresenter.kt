package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.GoodsInfoBean
import com.jtcxw.glcxw.base.respmodels.VerifySmsBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IGoodsInfo
import com.jtcxw.glcxw.views.GoodsInfoView
import models.BaseBean
import retrofit2.Response

class GoodsInfoPresenter:IGoodsInfo {
    var iView:GoodsInfoView? = null
    constructor(view:GoodsInfoView) {
        iView = view
    }
    override fun getAccountInfo(jsonObject: JsonObject) {
        val fragment = (iView as BaseFragment<*, *>)
        val dialog =  DialogUtil.getLoadingDialog(fragment.fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().getAccountInfo(jsonObject),object :
            ApiCallback<GoodsInfoBean, Response<BaseBean<GoodsInfoBean>>>(){
            override fun onSuccess(model: BaseBean<GoodsInfoBean>) {
                if (model.Code == 200){
                    iView?.onGetGoodsInfoSucc(model.Data!!)
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