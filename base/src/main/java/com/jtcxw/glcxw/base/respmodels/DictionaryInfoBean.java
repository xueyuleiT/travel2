package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class DictionaryInfoBean {
    private List<DictionaryBean> DictionaryInfo;

    public List<DictionaryBean> getDictionaryInfo() {
        return DictionaryInfo;
    }

    public void setDictionaryInfo(List<DictionaryBean> DictionaryInfo) {
        this.DictionaryInfo = DictionaryInfo;
    }

   public static class DictionaryBean {
        /**
         * ItemName : 定制公交
         * ItemValue : 2
         */

        private String ItemName;
        private String ItemValue;

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String ItemName) {
            this.ItemName = ItemName;
        }

        public String getItemValue() {
            return ItemValue;
        }

        public void setItemValue(String ItemValue) {
            this.ItemValue = ItemValue;
        }
    }
}
