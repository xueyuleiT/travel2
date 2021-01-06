package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class ContentListBean {
    private List<ContentListInfoBean> ContentList;

    public List<ContentListInfoBean> getContentList() {
        return ContentList;
    }

    public void setContentList(List<ContentListInfoBean> ContentList) {
        this.ContentList = ContentList;
    }

    public static class ContentListInfoBean {
        /**
         * ContentType : 1
         * ContentId : 0b394a37-4084-4099-b90d-4fc498bd8b14
         * ContentTitle : 桂林香格里拉 大酒店
         * ContentDesc : 环城北二路
         * OrderNum : 1
         * DefaultPhotoUrl : http://localhost:8005/Files/images/Hotel/0b394a37-4084-4099-b90d-4fc498bd8b14/IMG_10002.jpg
         */

        private int ContentType;
        private String ContentId;
        private String ContentTitle;
        private String ContentDesc;
        private int OrderNum;
        private String DefaultPhotoUrl;

        public int getContentType() {
            return ContentType;
        }

        public void setContentType(int ContentType) {
            this.ContentType = ContentType;
        }

        public String getContentId() {
            return ContentId;
        }

        public void setContentId(String ContentId) {
            this.ContentId = ContentId;
        }

        public String getContentTitle() {
            return ContentTitle;
        }

        public void setContentTitle(String ContentTitle) {
            this.ContentTitle = ContentTitle;
        }

        public String getContentDesc() {
            return ContentDesc;
        }

        public void setContentDesc(String ContentDesc) {
            this.ContentDesc = ContentDesc;
        }

        public int getOrderNum() {
            return OrderNum;
        }

        public void setOrderNum(int OrderNum) {
            this.OrderNum = OrderNum;
        }

        public String getDefaultPhotoUrl() {
            return DefaultPhotoUrl;
        }

        public void setDefaultPhotoUrl(String DefaultPhotoUrl) {
            this.DefaultPhotoUrl = DefaultPhotoUrl;
        }
    }
}
