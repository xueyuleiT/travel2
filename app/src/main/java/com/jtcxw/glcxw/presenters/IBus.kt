package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IBus {
    fun busInquiryAnnexBus(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
}