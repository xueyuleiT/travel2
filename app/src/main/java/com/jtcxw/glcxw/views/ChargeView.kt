package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.GoodListBean
import com.jtcxw.glcxw.base.respmodels.MisceAneousBean
import com.jtcxw.glcxw.base.respmodels.PayRechargeBean

interface ChargeView {
    fun onGetGoodsInfoSucc(goodListBean: GoodListBean)
    fun onPayRechargeSucc(payRechargeBean: PayRechargeBean)
    fun onGetMisceAneousBeanSucc(misceAneousBean: MisceAneousBean)

}