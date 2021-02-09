package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.respmodels.CollectionInfoBean

interface CollectionListView {
    fun onCollectionInfoSucc(collectInfoBean: CollectionInfoBean)
    fun onQuerySiteSucc(
        s: List<AnnexBusBean.StationListBean>,
        stationId: String
    )
    fun onCollectionInfoFinish()
}