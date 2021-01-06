package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.ExtraOrderBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean

interface MyView {
    fun onOrderStatisticsSucc(extraOrderBean: ExtraOrderBean)
    fun onOrderStatisticsFinish()

    fun onMemberInfoSucc(userInfoBean: UserInfoBean)
    fun onMemberInfoFailed(msg:String)
    fun onMemberInfoFinish()
}