package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.respmodels.MemberAccountHistoryBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.IAccountRecord
import com.jtcxw.glcxw.views.AccountRecordView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class AccountRecordPresenter:IAccountRecord {

    var iView: AccountRecordView?= null
    constructor(view: AccountRecordView){
        iView = view
    }
    override fun getMemberAccountHistory(
        jsonObject: JsonObject,
        smartRefreshLayout: SmartRefreshLayout
    ) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().getMemberAccountHistory(jsonObject),object :
            ApiCallback<MemberAccountHistoryBean, Response<BaseBean<MemberAccountHistoryBean>>>(){
            override fun onSuccess(model: BaseBean<MemberAccountHistoryBean>) {
                if (model.Code == 200){
                    iView?.onGetMemberAccountHistory(model.Data!!)
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
                iView?.onGetMemberAccountHistory(MemberAccountHistoryBean())
            }

            override fun onFinish() {
                smartRefreshLayout.finishRefresh(0)
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