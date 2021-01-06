package com.jtcxw.glcxw.views

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.respmodels.DictionaryInfoBean

interface ComplaintView {
    fun onInsertCusServerSucc(jsonObject: JsonObject)
    fun onGetCusServerTypeSucc(dictionaryInfoBean: DictionaryInfoBean)
}