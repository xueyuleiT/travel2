package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.AgreementBean

interface AgreementView {
    fun onMemberTreatySucc(agreementBean: AgreementBean)
    fun onMemberTreatyFailed()
}