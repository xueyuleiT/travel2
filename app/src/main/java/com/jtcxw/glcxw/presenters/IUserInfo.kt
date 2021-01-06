package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject


interface IUserInfo {
    fun headImage(jsonObject: JsonObject)
    fun getSexList()
    fun getCountryList()
    fun getOccupationList()
    fun modifyMemberInfo(jsonObject: JsonObject)
}