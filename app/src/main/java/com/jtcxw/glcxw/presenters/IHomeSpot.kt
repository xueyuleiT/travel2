package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IHomeSpot {
    fun getContentList(jsonObject: JsonObject)
}