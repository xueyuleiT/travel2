package com.jtcxw.glcxw.listeners

import com.jtcxw.glcxw.base.respmodels.FrequencyBean
import com.jtcxw.glcxw.localbean.OrderPriceBean
import java.util.*

interface OrderDateListener {
    fun notifyDateChange(date: Date)
    fun getDate():Date
    fun getSelectedDate(): FrequencyBean.scheduleListBean
    fun checkOrderInfo():Boolean
    fun checkTicketCount():Boolean
    fun getOrderPriceInfo(): OrderPriceBean
    fun getFrequencyBean():FrequencyBean
}