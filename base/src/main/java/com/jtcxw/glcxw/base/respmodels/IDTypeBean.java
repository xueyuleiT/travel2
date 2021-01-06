package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class IDTypeBean {
    private List<DictionaryInfoBean> DictionaryInfo;

    public List<DictionaryInfoBean> getDictionaryInfo() {
        return DictionaryInfo;
    }

    public void setDictionaryInfo(List<DictionaryInfoBean> DictionaryInfo) {
        this.DictionaryInfo = DictionaryInfo;
    }

    public static class DictionaryInfoBean {
        /**
         * ItemName : 身份证
         * ItemValue : 1
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
