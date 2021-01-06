package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IRegister {
    fun sendSmsCode(jsonObject: JsonObject)
    fun smsRegister(jsonObject: JsonObject)
}