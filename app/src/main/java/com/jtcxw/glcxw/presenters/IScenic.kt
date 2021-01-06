package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IScenic {
    fun getScenicInfoList(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout)

}