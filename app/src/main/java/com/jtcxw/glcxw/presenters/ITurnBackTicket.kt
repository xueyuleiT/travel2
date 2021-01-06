package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface ITurnBackTicket {
    fun turnBackTicket(jsonObject: JsonObject)
}