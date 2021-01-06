package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.TicketBean

interface OrderTicketGoView {
    fun onRoundTikmodelListSucc(ticketBean: TicketBean,id:String)
    fun onRoundTikmodelListFailed(id:String)
}