package com.jtcxw.glcxw.base.utils

import com.jtcxw.glcxw.base.localmodels.UserInfo
import com.jtcxw.glcxw.base.respmodels.UserInfoBean

class UserUtil {
    companion object{
        private val user: UserInfo = UserInfo()
        var hasCheckUpdate = false
        var isShowLogin = false
        fun getUser():UserInfo{
            return user
        }

        fun getUserInfoBean():UserInfoBean {
            return user.userInfoBean
        }

    }
}