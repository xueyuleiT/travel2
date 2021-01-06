package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.ScenicBean

interface ScenicView {
    fun onScenicInfoListSucc(scenicBean: ScenicBean)
    fun onScenicInfoListFinish()
}