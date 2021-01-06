package com.jtcxw.glcxw.base.basic;

public class BaseAdapterModel {

    public boolean needBackground = true;

    public int mItemType = ItemType.DEFAULT;

    public int getItemType() {
        return mItemType;
    }

    public void setItemType(int itemType) {
        mItemType = itemType;
    }

    public static class ItemType {
        public static int ERROR = -100;
        public static int DEFAULT = 0;
        public static int EMPTY = 1;
        public static int HEADER = 2;

    }
}
