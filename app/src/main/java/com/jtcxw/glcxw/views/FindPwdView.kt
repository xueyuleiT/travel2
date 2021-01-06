package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.SmsBean
import com.jtcxw.glcxw.base.respmodels.VerifySmsBean

interface FindPwdView {
    fun onSendSmsCodeSucc(smsBean: SmsBean)
    fun onVerifySmsCodeSucc(verifySmsBean: VerifySmsBean)
}