package com.jtcxw.glcxw.base.respmodels;

public class UserInfoBean {
    /**
     * MemberId : 95de1dc6-2003-11eb-916a-00155d37fc0f
     * UserName : glcx_ltgn7l3d6795
     * NikeName : glcx_ltgn7l3d6795
     * ProfilePhoto :
     * LoginMsg : 登陆成功!
     * LoginOk : true
     */

    private String MemberId;
    private String UserName;
    private String NikeName;
    private String ProfilePhoto;
    private String LoginMsg;
    private boolean LoginOk;
    private String Token;
    private String TelphoneNo;
    private String Sex;
    private String Nation;//国家
    private String IdCardType;//证件类型
    private String IdCardNo;//证件号码
    private String Period;//有效期
    private String Address;//地址
    private String Occupation;//职业
    private String Birthday;//生日
    private boolean isOpenQr = false;
    private String RealNameVerifyFlag;//实名认证标志0未认证1已认证
    private String RealName;//真实姓名
    private String ExpirationTime;//授权过期时间
    private String AuthorizeUnit;//授权单位
    private String AuthorizeToken;//授权token
    private double OwnerAmount; //账户余额
    private String RealTelphoneNo;//手机号
    private String HeadImageUrl;//头像

    public String getHeadImageUrl() {
        return HeadImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        HeadImageUrl = headImageUrl;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public boolean isOpenQr() {
        return isOpenQr;
    }

    public void setOpenQr(boolean openQr) {
        isOpenQr = openQr;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String MemberId) {
        this.MemberId = MemberId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getNikeName() {
        return NikeName;
    }

    public void setNikeName(String NikeName) {
        this.NikeName = NikeName;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String ProfilePhoto) {
        this.ProfilePhoto = ProfilePhoto;
    }

    public String getLoginMsg() {
        return LoginMsg;
    }

    public void setLoginMsg(String LoginMsg) {
        this.LoginMsg = LoginMsg;
    }

    public boolean isLoginOk() {
        return LoginOk;
    }

    public void setLoginOk(boolean LoginOk) {
        this.LoginOk = LoginOk;
    }

    public String getTelphoneNo() {
        return TelphoneNo;
    }

    public void setTelphoneNo(String telphoneNo) {
        TelphoneNo = telphoneNo;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getNation() {
        return Nation;
    }

    public void setNation(String nation) {
        Nation = nation;
    }

    public String getIdCardType() {
        return IdCardType;
    }

    public void setIdCardType(String idCardType) {
        IdCardType = idCardType;
    }

    public String getIdCardNo() {
        return IdCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        IdCardNo = idCardNo;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getRealNameVerifyFlag() {
        return RealNameVerifyFlag;
    }

    public void setRealNameVerifyFlag(String realNameVerifyFlag) {
        RealNameVerifyFlag = realNameVerifyFlag;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getExpirationTime() {
        return ExpirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        ExpirationTime = expirationTime;
    }

    public String getAuthorizeUnit() {
        return AuthorizeUnit;
    }

    public void setAuthorizeUnit(String authorizeUnit) {
        AuthorizeUnit = authorizeUnit;
    }

    public String getAuthorizeToken() {
        return AuthorizeToken;
    }

    public void setAuthorizeToken(String authorizeToken) {
        AuthorizeToken = authorizeToken;
    }

    public double getOwnerAmount() {
        return OwnerAmount;
    }

    public String getRealTelphoneNo() {
        return RealTelphoneNo;
    }

    public void setRealTelphoneNo(String realTelphoneNo) {
        RealTelphoneNo = realTelphoneNo;
    }

    public void setOwnerAmount(Double ownerAmount) {
        OwnerAmount = ownerAmount;
    }
}
