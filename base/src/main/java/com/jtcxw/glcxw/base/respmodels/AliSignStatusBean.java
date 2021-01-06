package com.jtcxw.glcxw.base.respmodels;

public class AliSignStatusBean {
    private String MemberId;
    private int ContractStatus;
    private String ContractStatusMsg;
    private String F_InvalidTime;
    private String F_SignTime;
    private String F_AlipayUserId;
    private String F_ValidTime;
    private String F_AlipayLogonId;

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public int getContractStatus() {
        return ContractStatus;
    }

    public void setContractStatus(int contractStatus) {
        ContractStatus = contractStatus;
    }

    public String getContractStatusMsg() {
        return ContractStatusMsg;
    }

    public void setContractStatusMsg(String contractStatusMsg) {
        ContractStatusMsg = contractStatusMsg;
    }

    public String getF_InvalidTime() {
        return F_InvalidTime;
    }

    public void setF_InvalidTime(String f_InvalidTime) {
        F_InvalidTime = f_InvalidTime;
    }

    public String getF_SignTime() {
        return F_SignTime;
    }

    public void setF_SignTime(String f_SignTime) {
        F_SignTime = f_SignTime;
    }

    public String getF_AlipayUserId() {
        return F_AlipayUserId;
    }

    public void setF_AlipayUserId(String f_AlipayUserId) {
        F_AlipayUserId = f_AlipayUserId;
    }

    public String getF_ValidTime() {
        return F_ValidTime;
    }

    public void setF_ValidTime(String f_ValidTime) {
        F_ValidTime = f_ValidTime;
    }

    public String getF_AlipayLogonId() {
        return F_AlipayLogonId;
    }

    public void setF_AlipayLogonId(String f_AlipayLogonId) {
        F_AlipayLogonId = f_AlipayLogonId;
    }
}
