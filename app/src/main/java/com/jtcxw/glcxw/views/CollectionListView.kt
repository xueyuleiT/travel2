package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.CollectionInfoBean

interface CollectionListView {
    fun onCollectionInfoSucc(collectInfoBean: CollectionInfoBean)
    fun onCollectionInfoFinish()
}