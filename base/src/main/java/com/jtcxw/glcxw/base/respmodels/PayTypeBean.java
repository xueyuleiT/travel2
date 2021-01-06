package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class PayTypeBean {
    private List<TypeArrayBean> TypeArray;

    public List<TypeArrayBean> getTypeArray() {
        return TypeArray;
    }

    public void setTypeArray(List<TypeArrayBean> TypeArray) {
        this.TypeArray = TypeArray;
    }

    public static class TypeArrayBean {
        /**
         * ItemName : 余额支付
         * ItemValue : 1
         */

        private boolean checked = false;
        private String ItemName;
        private String ItemValue;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

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
