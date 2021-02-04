package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class SiteDataBean {
    private List<AnnexBusBean.StationListBean> SiteData;

    public List<AnnexBusBean.StationListBean> getSiteData() {
        return SiteData;
    }

    public void setSiteData(List<AnnexBusBean.StationListBean> siteData) {
        SiteData = siteData;
    }
}
