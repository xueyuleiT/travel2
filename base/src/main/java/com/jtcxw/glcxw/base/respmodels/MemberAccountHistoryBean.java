package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class MemberAccountHistoryBean {
    private List<MemberAccountHistoryListBean> MemberAccountHistoryList;

    public List<MemberAccountHistoryListBean> getMemberAccountHistoryList() {
        return MemberAccountHistoryList;
    }

    public void setMemberAccountHistoryList(List<MemberAccountHistoryListBean> MemberAccountHistoryList) {
        this.MemberAccountHistoryList = MemberAccountHistoryList;
    }

    public static class MemberAccountHistoryListBean {
        /**
         * ChangeType : 1
         * ChangeTypeName : 充值
         * ChangeTime : 2020-11-21 15:37:04
         * Amount : 100
         */

        private int ChangeType;
        private String ChangeTypeName;
        private String ChangeTime;
        private double Amount;
        private String BusinessId;

        public String getBusinessId() {
            return BusinessId;
        }

        public void setBusinessId(String businessId) {
            BusinessId = businessId;
        }

        public int getChangeType() {
            return ChangeType;
        }

        public void setChangeType(int ChangeType) {
            this.ChangeType = ChangeType;
        }

        public String getChangeTypeName() {
            return ChangeTypeName;
        }

        public void setChangeTypeName(String ChangeTypeName) {
            this.ChangeTypeName = ChangeTypeName;
        }

        public String getChangeTime() {
            return ChangeTime;
        }

        public void setChangeTime(String ChangeTime) {
            this.ChangeTime = ChangeTime;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double amount) {
            Amount = amount;
        }
    }
}
