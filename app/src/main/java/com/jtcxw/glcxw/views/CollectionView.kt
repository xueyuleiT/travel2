package com.jtcxw.glcxw.views

import com.google.gson.JsonObject

interface CollectionView {
    fun onAddCollectionSucc(jsonObject: JsonObject)
    fun onCancelCollectionSucc(jsonObject: JsonObject)
}