package com.jtcxw.glcxw.base.respmodels;

public class AliUnSignBean {
    private String MemberId;
    private int IsSendToUnSign;
    private String Message;

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public int getIsSendToUnSign() {
        return IsSendToUnSign;
    }

    public void setIsSendToUnSign(int isSendToUnSign) {
        IsSendToUnSign = isSendToUnSign;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
