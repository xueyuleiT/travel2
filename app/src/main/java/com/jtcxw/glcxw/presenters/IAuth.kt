package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IAuth:IMy {
    fun authentication(jsonObject: JsonObject)
    fun authenticationImage(jsonObject: JsonObject)
    fun getIdTypeList()
}