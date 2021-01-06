package com.jtcxw.glcxw.base.localmodels

import android.os.Bundle
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.glcxw.lib.util.CacheUtil
import com.glcxw.lib.util.constants.SPKeys
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.respmodels.SmsLoginBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.UserUtil

class UserInfo {
    var longitude = "25.278562"
    var latitude = "110.295996"
    var city = "桂林"
    var userInfoBean = UserInfoBean()
    var LAST_REFRESH_TIME = 0L

    fun clear(){
        JPushInterface.deleteAlias(BaseUtil.sTopAct,0)
        val phone = CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_TELEPHONE)
        this.userInfoBean = UserInfoBean()
        CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_TELEPHONE,phone)
        CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_MEMBER_ID,"")
        CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_TOKEN,"")
    }

    fun save(userInfoBean: UserInfoBean) {
        LAST_REFRESH_TIME = System.currentTimeMillis()
        val token = this.userInfoBean.token
        this.userInfoBean = userInfoBean
        if (!TextUtils.isEmpty(token)) {
            this.userInfoBean.token = token
        }
        if (!TextUtils.isEmpty(userInfoBean.telphoneNo)) {
            this.userInfoBean.telphoneNo = userInfoBean.telphoneNo.replace("+86", "")
            this.userInfoBean.realTelphoneNo = userInfoBean.realTelphoneNo.replace("+86", "")
            CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_TELEPHONE,this.userInfoBean.realTelphoneNo)
        }
        if (!TextUtils.isEmpty(this.userInfoBean.memberId)) {
            JPushInterface.setAlias(BaseUtil.sTopAct, 0, this.userInfoBean.memberId.replace("-",""))
        }

        if (!TextUtils.isEmpty(userInfoBean.token)) {
            this.userInfoBean.token = userInfoBean.token
            CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_TOKEN,userInfoBean.token)
        }
        CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_MEMBER_ID,this.userInfoBean.memberId)
    }

    fun save(loginBean: SmsLoginBean) {
        this.userInfoBean.userName = loginBean.getUserName()
        this.userInfoBean.memberId = loginBean.getMemberId()
        this.userInfoBean.nikeName = loginBean.getNikeName()
        this.userInfoBean.profilePhoto = loginBean.getProfilePhoto()
        this.userInfoBean.headImageUrl = loginBean.getHeadImageUrl()

        if (!TextUtils.isEmpty(loginBean.getToken())) {
            this.userInfoBean.token = loginBean.getToken()
            CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_TOKEN,userInfoBean.token)
        }

        CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_MEMBER_ID,userInfoBean.memberId)
    }
}