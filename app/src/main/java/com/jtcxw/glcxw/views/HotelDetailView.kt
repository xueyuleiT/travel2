package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.HotelDetailBean

interface HotelDetailView {
    fun onGetHotelDetailSucc(hotelDetailBean: HotelDetailBean)
}