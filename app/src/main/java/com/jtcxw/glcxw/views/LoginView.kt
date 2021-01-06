package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.respmodels.PicVerifyCodeBean

interface LoginView {
    fun onLoginSucc(userInfoBean: UserInfoBean)
    fun onLoginFailed(msg:String)
    fun onLoginVerifyCodeSucc(json: PicVerifyCodeBean)
    fun onLoginVerifyCodeFailed(msg:String)
    fun onLoginVerifyCodeFinish()
}