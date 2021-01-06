package com.jtcxw.glcxw.views

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean

interface BusQueryView {
    fun onBusInquiryAnnexBusSucc(siteOrLineBean: SiteOrLineBean)
    fun onListHistorySucc(siteOrLineBean: SiteOrLineBean)
    fun onClearQueryHistorySucc(jsonObject: JsonObject)
}