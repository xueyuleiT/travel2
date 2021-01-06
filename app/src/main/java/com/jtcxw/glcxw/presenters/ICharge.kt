package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface ICharge {
    fun getGoodsInfo()
    fun payRecharge(jsonObject: JsonObject)
    fun getMisceAneous(jsonObject: JsonObject)
}