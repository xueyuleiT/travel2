package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IAgreement {
    fun getMemberTreaty(jsonObject: JsonObject)
}