package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface ITicket {
    fun ticketChecking(jsonObject: JsonObject)
}