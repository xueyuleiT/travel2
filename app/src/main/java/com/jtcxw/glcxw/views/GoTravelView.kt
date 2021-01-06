package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.BusArriveListBean
import com.jtcxw.glcxw.base.respmodels.MisceAneousBean
import com.jtcxw.glcxw.base.respmodels.ModuleConfigBean


interface GoTravelView:BusView,BannerView {
    fun onGetMisceAneousBeanSucc(misceAneousBean: MisceAneousBean)
    fun onForcastArriveQuerySucc(busArriveListBean: BusArriveListBean)
    fun onModuleConfigSucc(moduleConfigBean: ModuleConfigBean)
}