package com.jtcxw.glcxw.base.respmodels;

public class PayBean {
    /**
     * order_id : 1000211344
     * order_name : null
     * order_price : 0
     */

    private String MemberId;
    private String AliOrderInfo;
    private String Payment = "1";
    private String order_id;
    private String order_name;
    private String order_price;

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getAliOrderInfo() {
        return AliOrderInfo;
    }

    public void setAliOrderInfo(String aliOrderInfo) {
        AliOrderInfo = aliOrderInfo;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }
}
