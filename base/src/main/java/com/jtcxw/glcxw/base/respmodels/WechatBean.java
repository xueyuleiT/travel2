package com.jtcxw.glcxw.base.respmodels;


public class WechatBean {
    private Boolean IsPass;
    private Boolean IsNeedToRegister;
    private String WechatOpenId;
    private String Message;
    private LoginEntity LoginEntity;


    public class LoginEntity {
        private String MemberId;
        private String UserName;
        private String NikeName;
        private String ProfilePhoto;
        private Boolean LoginOk;
        private String LoginMsg;
        private String Token;


        public String getMemberId() {
            return MemberId;
        }

        public void setMemberId(String memberId) {
            MemberId = memberId;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getNikeName() {
            return NikeName;
        }

        public void setNikeName(String nikeName) {
            NikeName = nikeName;
        }

        public String getProfilePhoto() {
            return ProfilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            ProfilePhoto = profilePhoto;
        }

        public Boolean getLoginOk() {
            return LoginOk;
        }

        public void setLoginOk(Boolean loginOk) {
            LoginOk = loginOk;
        }

        public String getLoginMsg() {
            return LoginMsg;
        }

        public void setLoginMsg(String loginMsg) {
            LoginMsg = loginMsg;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String token) {
            Token = token;
        }
    }

    public Boolean getPass() {
        return IsPass;
    }

    public void setPass(Boolean pass) {
        IsPass = pass;
    }

    public Boolean getNeedToRegister() {
        return IsNeedToRegister;
    }

    public void setNeedToRegister(Boolean needToRegister) {
        IsNeedToRegister = needToRegister;
    }


    public String getWechatOpenId() {
        return WechatOpenId;
    }

    public void setWechatOpenId(String wechatOpenId) {
        WechatOpenId = wechatOpenId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public WechatBean.LoginEntity getLoginEntity() {
        return LoginEntity;
    }

    public void setLoginEntity(WechatBean.LoginEntity loginEntity) {
        LoginEntity = loginEntity;
    }
}
