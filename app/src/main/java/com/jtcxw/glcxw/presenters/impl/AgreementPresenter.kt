package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.dialogs.BaseDialogFragment
import com.jtcxw.glcxw.base.respmodels.AgreementBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IAgreement
import com.jtcxw.glcxw.views.AgreementView
import models.BaseBean
import retrofit2.Response

class AgreementPresenter:IAgreement {

    var iView: AgreementView?= null
    constructor(view: AgreementView){
        iView = view
    }

    override fun getMemberTreaty(jsonObject: JsonObject) {
        var fragment: Any?
        if (iView is BaseFragment<*, *>) {
            fragment =  (iView as BaseFragment<*, *>)
            HttpUtil.addSubscription(ApiClient.retrofit().getMemberTreaty(jsonObject),object :
                ApiCallback<AgreementBean, Response<BaseBean<AgreementBean>>>(){
                override fun onSuccess(model: BaseBean<AgreementBean>) {
                    if (model.Code == 200){
                        iView?.onMemberTreatySucc(model.Data!!)
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
                    iView?.onMemberTreatyFailed()
                }

                override fun onFinish() {
                }

            }, fragment, null)
        } else {
            fragment = (iView as BaseDialogFragment)
            HttpUtil.addSubscription(ApiClient.retrofit().getMemberTreaty(jsonObject),object :
                ApiCallback<AgreementBean, Response<BaseBean<AgreementBean>>>(){
                override fun onSuccess(model: BaseBean<AgreementBean>) {
                    if (model.Code == 200){
                        iView?.onMemberTreatySucc(model.Data!!)
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
                    iView?.onMemberTreatyFailed()
                }

                override fun onFinish() {
                }

            }, fragment, null)
        }
    }
}