package com.jtcxw.glcxw.localbean;

import com.jtcxw.glcxw.base.respmodels.CollectionInfoBean;

public class MyCollectionBean {
    private int type = 1;
    private boolean edit = false;
    private CollectionInfoBean.CollectInfoBean collectInfoBean;

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CollectionInfoBean.CollectInfoBean getCollectInfoBean() {
        return collectInfoBean;
    }

    public void setCollectInfoBean(CollectionInfoBean.CollectInfoBean collectInfoBean) {
        this.collectInfoBean = collectInfoBean;
    }
}
