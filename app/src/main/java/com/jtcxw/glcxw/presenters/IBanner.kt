package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IBanner {
    fun getBanner(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
}