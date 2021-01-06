package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class GoodListBean {
    private List<GoodsListBean> GoodsList;

    public List<GoodsListBean> getGoodsList() {
        return GoodsList;
    }

    public void setGoodsList(List<GoodsListBean> GoodsList) {
        this.GoodsList = GoodsList;
    }

    public static class GoodsListBean {
        /**
         * Id : 70c9ddbb-445b-417f-a8bd-53a9a4e2b4c9
         * GoodsNo : 00122
         * GoodsName : 酒店
         * Price : 12.9
         * GoodsType : 常规
         * Sort : 2
         * Remark : 2
         */
        private boolean checked = false;
        private String Id;
        private String GoodsNo;
        private String GoodsName;
        private double Price;
        private String GoodsType;
        private int Sort;
        private String Remark;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getGoodsNo() {
            return GoodsNo;
        }

        public void setGoodsNo(String GoodsNo) {
            this.GoodsNo = GoodsNo;
        }

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String GoodsName) {
            this.GoodsName = GoodsName;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public String getGoodsType() {
            return GoodsType;
        }

        public void setGoodsType(String GoodsType) {
            this.GoodsType = GoodsType;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int Sort) {
            this.Sort = Sort;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }
    }
}
