package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.BannerBean

interface BannerView {
    fun onGetBannerSucc(bannerBean: BannerBean)
    fun onGetBannerFinish()
}