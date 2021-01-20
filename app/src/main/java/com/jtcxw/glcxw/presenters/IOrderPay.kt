package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.dialogs.LoadingDialog

interface IOrderPay {
    fun pay( params: JsonObject,dialog: LoadingDialog)
    fun getPayType( params: JsonObject,loadingDialog: LoadingDialog?)
    fun payRecharge( params: JsonObject)
}