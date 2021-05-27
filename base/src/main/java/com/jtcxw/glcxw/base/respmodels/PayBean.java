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
    private WeChatOrderInfo WeChatOrderInfo;

    public PayBean.WeChatOrderInfo getWeChatOrderInfo() {
        return WeChatOrderInfo;
    }

    public void setWeChatOrderInfo(PayBean.WeChatOrderInfo weChatOrderInfo) {
        WeChatOrderInfo = weChatOrderInfo;
    }

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

    public static class WeChatOrderInfo {
        private String AppId;
        private String Partnerid;
        private String Prepayid;
        private String Package;
        private String Noncestr;
        private String Timestamp;
        private String Sign;

        public String getAppId() {
            return AppId;
        }

        public void setAppId(String appId) {
            AppId = appId;
        }

        public String getPartnerid() {
            return Partnerid;
        }

        public void setPartnerid(String partnerid) {
            Partnerid = partnerid;
        }

        public String getPrepayid() {
            return Prepayid;
        }

        public void setPrepayid(String prepayid) {
            Prepayid = prepayid;
        }

        public String getPackage() {
            return Package;
        }

        public void setPackage(String aPackage) {
            Package = aPackage;
        }

        public String getNoncestr() {
            return Noncestr;
        }

        public void setNoncestr(String noncestr) {
            Noncestr = noncestr;
        }

        public String getTimestamp() {
            return Timestamp;
        }

        public void setTimestamp(String timestamp) {
            Timestamp = timestamp;
        }

        public String getSign() {
            return Sign;
        }

        public void setSign(String sign) {
            Sign = sign;
        }
    }

}
