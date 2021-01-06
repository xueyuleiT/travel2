package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.dialogs.LoadingDialog

interface IOrderConfirm {
    fun getOrderDetail(params: JsonObject,dialog: LoadingDialog)
    fun orderCancel(params: JsonObject,dialog: LoadingDialog)
    fun complimentaryTicket(jsonObject: JsonObject)
}