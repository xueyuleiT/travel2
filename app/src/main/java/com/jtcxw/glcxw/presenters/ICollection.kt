package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface ICollection {
    fun addCollection(jsonObject: JsonObject)
    fun cancelCollection(jsonObject: JsonObject)
}