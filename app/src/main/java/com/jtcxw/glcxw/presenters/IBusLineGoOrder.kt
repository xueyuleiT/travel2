package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.dialogs.LoadingDialog

interface IBusLineGoOrder {
    fun getFrequencys(params: JsonObject,dialog: LoadingDialog)

    fun addOrder(params: JsonObject,dialog: LoadingDialog)

}