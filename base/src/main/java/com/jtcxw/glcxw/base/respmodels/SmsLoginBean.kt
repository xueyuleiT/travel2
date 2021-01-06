package com.jtcxw.glcxw.base.respmodels

class SmsLoginBean {
    /**
     * MemberId : 95de1dc6-2003-11eb-916a-00155d37fc0f
     * UserName : glcx_ltgn7l3d6795
     * NikeName : glcx_ltgn7l3d6795
     * ProfilePhoto :
     * LoginMsg : 登陆成功!
     * LoginOk : true
     */

    private var MemberId: String? = null
    private var UserName: String? = null
    private var NikeName: String? = null
    private var ProfilePhoto: String? = null
    private var HeadImageUrl: String? = null
    private var LoginMsg: String? = null
    private var LoginOk: Boolean = false
    private var Token: String?= null

    fun getHeadImageUrl():String? {
        return HeadImageUrl
    }

    fun setHeadImageUrl(headImageUrl:String) {
        this.HeadImageUrl = headImageUrl
    }

    fun getToken():String? {
        return Token
    }

    fun setToken(token:String) {
        this.Token = token
    }

    fun getMemberId(): String? {
        return MemberId
    }

    fun setMemberId(MemberId: String) {
        this.MemberId = MemberId
    }

    fun getUserName(): String? {
        return UserName
    }

    fun setUserName(UserName: String) {
        this.UserName = UserName
    }

    fun getNikeName(): String? {
        return NikeName
    }

    fun setNikeName(NikeName: String) {
        this.NikeName = NikeName
    }

    fun getProfilePhoto(): String? {
        return ProfilePhoto
    }

    fun setProfilePhoto(ProfilePhoto: String) {
        this.ProfilePhoto = ProfilePhoto
    }

    fun getLoginMsg(): String? {
        return LoginMsg
    }

    fun setLoginMsg(LoginMsg: String) {
        this.LoginMsg = LoginMsg
    }

    fun isLoginOk(): Boolean {
        return LoginOk
    }

    fun setLoginOk(LoginOk: Boolean) {
        this.LoginOk = LoginOk
    }


}