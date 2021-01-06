package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.respmodels.PayBean
import com.jtcxw.glcxw.base.respmodels.PayRechargeBean
import com.jtcxw.glcxw.base.respmodels.PayTypeBean

interface OrderPayView {
    fun onGetPayTypeSucc(payTypeBean: PayTypeBean)
    fun onPaySucc(
        payBean: PayBean,
        dialog: LoadingDialog
    )

    fun onPayRechargeSucc(
        payRechargeBean: PayRechargeBean,
        dialog: LoadingDialog
    )
}