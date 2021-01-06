package com.jtcxw.glcxw.presenters.impl

import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.respmodels.CollectionInfoBean
import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean
import com.jtcxw.glcxw.base.utils.HttpUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.presenters.ICollectionList
import com.jtcxw.glcxw.views.CollectionListView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import models.BaseBean
import retrofit2.Response

class CollectionListPresenter:ICollectionList {
    var iView: CollectionListView? = null
    constructor(view: CollectionListView) {
        iView = view
    }
    override fun getCollectionInfo(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout) {
        val fragment = (iView as BaseFragment<*, *>)
        HttpUtil.addSubscription(ApiClient.retrofit().collectionInfo(jsonObject),object :
            ApiCallback<CollectionInfoBean, Response<BaseBean<CollectionInfoBean>>>(){
            override fun onSuccess(model: BaseBean<CollectionInfoBean>) {
                if (model.Code == 200){
                    iView?.onCollectionInfoSucc(model.Data!!)
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
                iView?.onCollectionInfoFinish()
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