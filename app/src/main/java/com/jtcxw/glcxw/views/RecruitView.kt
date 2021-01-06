package com.jtcxw.glcxw.views

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.respmodels.RecruitBean
import com.jtcxw.glcxw.base.respmodels.RecruitResultBean

interface RecruitView {
    fun onGetRecruitSucc(recruitBean: RecruitBean)
    fun onJoinRecruit(recruitResultBean: RecruitResultBean)
    fun onCancelRejoinRecruitSucc(recruitResultBean: RecruitResultBean)
}