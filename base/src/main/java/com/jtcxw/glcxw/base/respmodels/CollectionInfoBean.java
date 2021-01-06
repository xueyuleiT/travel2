package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class CollectionInfoBean {
    private List<CollectInfoBean> CollectInfo;

    public List<CollectInfoBean> getCollectInfo() {
        return CollectInfo;
    }

    public void setCollectInfo(List<CollectInfoBean> CollectInfo) {
        this.CollectInfo = CollectInfo;
    }

    public static class CollectInfoBean {
        /**
         * Id : 1b3f4e11-f678-448b-afcc-032916ebcc0d
         * Type : 2
         * TypeName : 公交线路
         * MineId : 12 3
         * CollectionName :
         */

        private String Id;
        private int Type;
        private String TypeName;
        private String MineId;
        private String CollectionName;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String TypeName) {
            this.TypeName = TypeName;
        }

        public String getMineId() {
            return MineId;
        }

        public void setMineId(String MineId) {
            this.MineId = MineId;
        }

        public String getCollectionName() {
            return CollectionName;
        }

        public void setCollectionName(String CollectionName) {
            this.CollectionName = CollectionName;
        }
    }
}
