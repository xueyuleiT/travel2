package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.dialogs.LoadingDialog

interface IPayList {
    fun setDefaultPayList(jsonObject: JsonObject)
    fun getDefaultPayList(jsonObject: JsonObject,loadingDialog: LoadingDialog?)
}