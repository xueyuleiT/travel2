package com.jtcxw.glcxw.views

import com.google.gson.JsonObject

interface TicketView {
    fun onTicketCheckingSucc(jsonObject: JsonObject)
}