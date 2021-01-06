package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.BusArriveListBean

interface BusMapView {
    fun onForcastArriveQuerySucc(busArriveListBean: BusArriveListBean)

}