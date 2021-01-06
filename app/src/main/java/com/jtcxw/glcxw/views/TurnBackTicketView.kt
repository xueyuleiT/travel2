package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.TurnBackBean

interface TurnBackTicketView {
    fun onTicketRefund(turnBackBean: TurnBackBean)
}