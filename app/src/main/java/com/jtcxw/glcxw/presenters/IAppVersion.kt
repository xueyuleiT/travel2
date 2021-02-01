package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.dialogs.LoadingDialog

interface IAppVersion {
    fun appVersion(jsonObject: JsonObject,dialog: LoadingDialog?)
}