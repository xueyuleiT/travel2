package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.AddOrderBean
import com.jtcxw.glcxw.base.respmodels.FrequencyBean

interface BusLineGoOrderView {
    fun onFrequencySucc(frequencyBean: FrequencyBean,routeId: String)
    fun onFrequencyFinish()

    fun onAddOrderSucc(addOrderBean: AddOrderBean)

}