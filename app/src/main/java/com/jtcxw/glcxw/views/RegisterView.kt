package com.jtcxw.glcxw.views

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.respmodels.RegisterBean
import com.jtcxw.glcxw.base.respmodels.SmsBean

interface RegisterView {
    fun onSmsRegisterSucc(registerBean: RegisterBean)
    fun onSendSmsCodeSucc(smsBean: SmsBean)
}