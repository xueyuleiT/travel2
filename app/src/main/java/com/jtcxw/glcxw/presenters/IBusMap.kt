package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IBusMap {
    fun forcastArriveQuery(jsonObject: JsonObject)
}