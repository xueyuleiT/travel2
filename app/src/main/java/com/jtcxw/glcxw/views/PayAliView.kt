package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.AliSignBean
import com.jtcxw.glcxw.base.respmodels.AliSignStatusBean
import com.jtcxw.glcxw.base.respmodels.AliUnSignBean

interface PayAliView {
    fun onUseragreementPageSignSucc(aliSignBean: AliSignBean)
    fun onUseragreementUnSignSucc(aliUnSignBean: AliUnSignBean)
    fun onUseragreementQuerySucc(aliSignStatusBean: AliSignStatusBean)
}