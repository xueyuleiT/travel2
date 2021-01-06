package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IComplaint {
    fun insertCusServer(jsonObject: JsonObject)
    fun getCusServerType(jsonObject: JsonObject)
}