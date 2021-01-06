package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IPayAli {
    fun useragreementPageSign(jsonObject: JsonObject)
    fun useragreementUnSign(jsonObject: JsonObject)
    fun useragreementQuery(jsonObject: JsonObject)
}