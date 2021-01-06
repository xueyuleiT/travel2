package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.dialogs.LoadingDialog

interface IBusRecruitLineAdd {
    fun busRecruitLineAdd(jsonObject: JsonObject,dialog: LoadingDialog)
}