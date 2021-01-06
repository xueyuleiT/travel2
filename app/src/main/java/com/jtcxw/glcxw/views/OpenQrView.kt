package com.jtcxw.glcxw.views

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.respmodels.SmsBean
import com.jtcxw.glcxw.base.respmodels.VerifySmsBean

interface OpenQrView {
    fun onSendSmsCodeSucc(smsBean: SmsBean)
    fun onVerifySmsCodeSucc(verifySmsBean: VerifySmsBean)
    fun onOpenQRCodeSucc(jsonObject: JsonObject)
}