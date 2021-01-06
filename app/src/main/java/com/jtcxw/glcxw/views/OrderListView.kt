package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.OrderDelBean
import com.jtcxw.glcxw.base.respmodels.OrderListBean
import com.jtcxw.glcxw.base.respmodels.PayBean

interface OrderListView {
    fun onGetListByCustomerSucc(orderListBean: OrderListBean)
    fun onDeleteBusOrderSucc(orderDelBean: OrderDelBean)
    fun onPaySucc(payBean: PayBean)
}