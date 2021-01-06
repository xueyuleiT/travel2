package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.CityBean
import com.jtcxw.glcxw.base.respmodels.LineBean

interface BusCustomizedView {
    fun onGetCitysSucc(cityBean: CityBean)
    fun onGetCitysFinish()

    fun onGetLinesSucc(lineBean: LineBean)
    fun onGetLinesFinish()
}