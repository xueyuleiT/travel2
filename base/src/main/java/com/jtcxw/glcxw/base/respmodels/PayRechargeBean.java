package com.jtcxw.glcxw.base.respmodels;

public class PayRechargeBean {
    /**
     * MemberId : 15f2dfa2-27af-11eb-916a-00155d37fc0f
     * ProductId : 8bbada58-9789-4d32-8c66-c2f2e9150030
     * Payment : 2
     * glcx_mine_order : {"Id":"1eca2c31-27cb-4129-9892-a4c09996892c","OrderNo":"202011161348480105169089684645012622","OrderName":"商品充值cz00009","OrderPrice":1,"OrderStatus":1,"OrderTime":"2020-11-16T13:48:48.2350891+08:00"}
     * AliOrderInfo : app_id=2021002100614175&biz_content=...
     */

    private String MemberId;
    private String ProductId;
    private int Payment;
    private GlcxMineOrderBean glcx_mine_order;
    private String AliOrderInfo;
    private WeChatAPPResultBean WeChatAPPResult;

    public WeChatAPPResultBean getWeChatAPPResult() {
        return WeChatAPPResult;
    }

    public void setWeChatAPPResult(WeChatAPPResultBean weChatAPPResult) {
        WeChatAPPResult = weChatAPPResult;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String MemberId) {
        this.MemberId = MemberId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public int getPayment() {
        return Payment;
    }

    public void setPayment(int Payment) {
        this.Payment = Payment;
    }

    public GlcxMineOrderBean getGlcx_mine_order() {
        return glcx_mine_order;
    }

    public void setGlcx_mine_order(GlcxMineOrderBean glcx_mine_order) {
        this.glcx_mine_order = glcx_mine_order;
    }

    public String getAliOrderInfo() {
        return AliOrderInfo;
    }

    public void setAliOrderInfo(String AliOrderInfo) {
        this.AliOrderInfo = AliOrderInfo;
    }

    public static class WeChatAPPResultBean {
        String AppId;
        String Partnerid;
        String Prepayid;
        String Package;
        String Noncestr;
        String Timestamp;
        String Sign;

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

    public static class GlcxMineOrderBean {
        /**
         * Id : 1eca2c31-27cb-4129-9892-a4c09996892c
         * OrderNo : 202011161348480105169089684645012622
         * OrderName : 商品充值cz00009
         * OrderPrice : 1
         * OrderStatus : 1
         * OrderTime : 2020-11-16T13:48:48.2350891+08:00
         */

        private String Id;
        private String OrderNo;
        private String OrderName;
        private double OrderPrice;
        private int OrderStatus;
        private String OrderTime;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getOrderName() {
            return OrderName;
        }

        public void setOrderName(String OrderName) {
            this.OrderName = OrderName;
        }

        public double getOrderPrice() {
            return OrderPrice;
        }

        public void setOrderPrice(double orderPrice) {
            OrderPrice = orderPrice;
        }

        public int getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(int OrderStatus) {
            this.OrderStatus = OrderStatus;
        }

        public String getOrderTime() {
            return OrderTime;
        }

        public void setOrderTime(String OrderTime) {
            this.OrderTime = OrderTime;
        }
    }
}
