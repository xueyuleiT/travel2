package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.GoodsInfoBean

interface GoodsInfoView {
    fun onGetGoodsInfoSucc(goodsInfoBean: GoodsInfoBean)
}