package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IGoTravel:IBus {
    fun forcastArriveQuery(jsonObject: JsonObject)
    fun getMisceAneous(jsonObject: JsonObject)
    fun h5ModuleConfig(jsonObject: JsonObject)
}