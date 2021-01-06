package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IResetPwd {
    fun changePwd(jsonObject: JsonObject)
}