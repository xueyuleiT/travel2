package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.HotelDetailBean
import com.jtcxw.glcxw.base.respmodels.ScenicDetailBean

interface HotelDetailView {
    fun onGetHotelDetailSucc(hotelDetailBean: HotelDetailBean)
}