package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.VersionBean

interface AppVersionView {
    fun onAppVersionSucc(versionBean: VersionBean)
}