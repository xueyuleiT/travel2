package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class BannerBean {
    private List<BannerListBean> BannerList;

    public List<BannerListBean> getBannerList() {
        return BannerList;
    }

    public void setBannerList(List<BannerListBean> BannerList) {
        this.BannerList = BannerList;
    }

    public static class BannerListBean {
        /**
         * OrderNo : 3
         * BannerUrl :
         * Type : 1
         * JumpMark : 0
         * JumpMethod : 0
         * JumpRoute :
         */

        private int OrderNo;
        private String BannerUrl;
        private String Type;
        private int JumpMark;
        private int JumpMethod;
        private String JumpRoute;

        public int getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(int OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getBannerUrl() {
            return BannerUrl;
        }

        public void setBannerUrl(String BannerUrl) {
            this.BannerUrl = BannerUrl;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public int getJumpMark() {
            return JumpMark;
        }

        public void setJumpMark(int JumpMark) {
            this.JumpMark = JumpMark;
        }

        public int getJumpMethod() {
            return JumpMethod;
        }

        public void setJumpMethod(int JumpMethod) {
            this.JumpMethod = JumpMethod;
        }

        public String getJumpRoute() {
            return JumpRoute;
        }

        public void setJumpRoute(String JumpRoute) {
            this.JumpRoute = JumpRoute;
        }
    }
}
