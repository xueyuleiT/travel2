package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.CommonFQABean

interface FQAView {
    fun onGetFQASucc(fqaBean: CommonFQABean)
    fun onGetFQAFailed()
}