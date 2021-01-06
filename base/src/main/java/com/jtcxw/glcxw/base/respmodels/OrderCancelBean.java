package com.jtcxw.glcxw.base.respmodels;

public class OrderCancelBean {
    private String MemberId;
    private String OrderId;

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
