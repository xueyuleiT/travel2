package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class OrderMixBean {

    private List<MemberOrderListBean> MemberOrderList;

    public List<MemberOrderListBean> getMemberOrderList() {
        return MemberOrderList;
    }

    public void setMemberOrderList(List<MemberOrderListBean> memberOrderList) {
        MemberOrderList = memberOrderList;
    }

    public static class MemberOrderListBean {
        private int OrderType;
        private String OrderId;
        private String  OrderNo;
        private String OrderTime;
        private String OrderContent;
        private String OrderStatus;
        private String BusinessId;

        public String getBusinessId() {
            return BusinessId;
        }

        public void setBusinessId(String businessId) {
            BusinessId = businessId;
        }

        public String getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            OrderStatus = orderStatus;
        }

        public int getOrderType() {
            return OrderType;
        }

        public void setOrderType(int orderType) {
            OrderType = orderType;
        }

        public String getOrderId() {
            return OrderId;
        }



        public void setOrderId(String orderId) {
            OrderId = orderId;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getOrderTime() {
            return OrderTime;
        }

        public void setOrderTime(String orderTime) {
            OrderTime = orderTime;
        }

        public String getOrderContent() {
            return OrderContent;
        }

        public void setOrderContent(String orderContent) {
            OrderContent = orderContent;
        }
    }
}
