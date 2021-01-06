package com.jtcxw.glcxw.views

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.respmodels.IDTypeBean

interface AuthView:MyView {
    fun onAuthenticationImageSucc(jsonObject: JsonObject,type: Int)
    fun onAuthenticationSucc(jsonObject: JsonObject)
    fun onGetIdTypeListSucc(idTypeBean: IDTypeBean)
}