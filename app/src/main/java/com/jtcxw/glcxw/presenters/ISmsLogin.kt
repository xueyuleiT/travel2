package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface ISmsLogin {
    fun sendSmsCode(jsonObject: JsonObject)
    fun smsLogin(jsonObject: JsonObject)

}