package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class PayListBean {
    private List<PayBean> PayList;

    public List<PayBean> getPayList() {
        return PayList;
    }

    public void setPayList(List<PayBean> payList) {
        PayList = payList;
    }

    public static class PayBean{
        private int PayType;
        private String PayTypeName;
        private int PaySort;

        public int getPayType() {
            return PayType;
        }

        public void setPayType(int payType) {
            PayType = payType;
        }

        public String getPayTypeName() {
            return PayTypeName;
        }

        public void setPayTypeName(String payTypeName) {
            PayTypeName = payTypeName;
        }

        public int getPaySort() {
            return PaySort;
        }

        public void setPaySort(int paySort) {
            PaySort = paySort;
        }
    }
}
