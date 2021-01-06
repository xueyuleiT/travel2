package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.ComplimentaryTicketBean
import com.jtcxw.glcxw.base.respmodels.OrderCancelBean
import com.jtcxw.glcxw.base.respmodels.OrderConfirmBean

interface OrderConfirmView {
    fun onOrderDetailSucc(orderConfirmBean: OrderConfirmBean)
    fun onOrderCancelSucc(orderCancelBean: OrderCancelBean)
    fun onComplimentaryTicketSucc(complimentaryTicketBean: ComplimentaryTicketBean)
}