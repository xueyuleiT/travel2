package com.jtcxw.glcxw.base.respmodels

class StationBean {


    var viewType = 0
    var StationId: String? = null
    var StationName: String? = null
    var Distance: Int = 0
    var Lon: Double = 0.toDouble()
    var Lat: Double = 0.toDouble()

    var LineId: String? = null
    var LineName: String? = null
    var DirectionLineName: String? = null
    var StartTime: String? = null
    var LastTime: String? = null
    var isOpen = false
}