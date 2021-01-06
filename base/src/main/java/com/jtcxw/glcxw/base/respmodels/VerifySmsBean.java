package com.jtcxw.glcxw.base.respmodels;

public class VerifySmsBean {
    /**
     * Notice : 验证通过
     * VerifyFlag : true
     * MemberId : 95de1dc6-2003-11eb-916a-00155d37fc0f
     */

    private String Notice;
    private boolean VerifyFlag;
    private String MemberId;

    public String getNotice() {
        return Notice;
    }

    public void setNotice(String Notice) {
        this.Notice = Notice;
    }

    public boolean isVerifyFlag() {
        return VerifyFlag;
    }

    public void setVerifyFlag(boolean VerifyFlag) {
        this.VerifyFlag = VerifyFlag;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String MemberId) {
        this.MemberId = MemberId;
    }
}
