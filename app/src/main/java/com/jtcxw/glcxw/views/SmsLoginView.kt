package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.SmsBean
import com.jtcxw.glcxw.base.respmodels.SmsLoginBean

interface SmsLoginView {
    fun onSendSmsCodeSucc(smsBean: SmsBean)
    fun onSmsLoginSucc(smsLoginBean: SmsLoginBean)
}