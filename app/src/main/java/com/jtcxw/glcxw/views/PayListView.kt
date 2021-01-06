package com.jtcxw.glcxw.views

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.respmodels.PayListBean

interface PayListView {
    fun onGetDefaultPayListSucc(payListBean: PayListBean)
    fun onSetDefaultPayListSucc(jsonObject: JsonObject)
}