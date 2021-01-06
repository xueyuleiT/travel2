package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.DictionaryInfoBean
import com.jtcxw.glcxw.base.respmodels.OrderMixBean

interface OrdersView {
    fun onGetMemberOrderSucc(orderMixBean: OrderMixBean)
    fun onGetMemberOrderFailed()
    fun onGetMemberOrderFinish()
    fun onGetOrderTypeListSucc(dictionaryInfoBean: DictionaryInfoBean)
    fun onGetOrderTypeListFailed()
    fun onGetOrderTypeLisFinish()

}