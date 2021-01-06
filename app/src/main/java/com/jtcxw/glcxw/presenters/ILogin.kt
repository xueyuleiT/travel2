package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface ILogin {
    fun loginVerifyCode(id: String)
    fun login(jsonObject: JsonObject)
}