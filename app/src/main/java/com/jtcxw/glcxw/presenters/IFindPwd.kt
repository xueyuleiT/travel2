package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IFindPwd {
    fun verifySmsCode(jsonObject: JsonObject)
    fun sendSmsCode(jsonObject: JsonObject)

}