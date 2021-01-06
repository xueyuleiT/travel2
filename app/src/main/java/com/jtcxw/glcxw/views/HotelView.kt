package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.HotelBean

interface HotelView {
    fun onHotelInfoListSucc(hotelBean: HotelBean)
    fun onHotelInfoListFinish()
}