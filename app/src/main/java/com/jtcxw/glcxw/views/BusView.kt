package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.AnnexBusBean

interface BusView {
    fun onBusInquiryAnnexBusSucc(annexBusBean: AnnexBusBean)
    fun onBusInquiryAnnexBusFinish()
}