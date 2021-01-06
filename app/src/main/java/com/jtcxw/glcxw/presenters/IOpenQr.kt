package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IOpenQr {
    fun verifySmsCode(jsonObject: JsonObject)
    fun sendSmsCode(params: JsonObject)
    fun openQRCode(jsonObject: JsonObject)
}