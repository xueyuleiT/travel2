package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.CusServerBean
import com.jtcxw.glcxw.base.respmodels.MisceAneousBean

interface ServiceView {
    fun onGetMisceAneousBeanSucc(misceAneousBean: MisceAneousBean)
    fun onGetMisceAneousBeanFinish()

    fun onGetCusServerInfoSucc(list: List<CusServerBean>)
    fun onGetCusServerInfoFinish()

}