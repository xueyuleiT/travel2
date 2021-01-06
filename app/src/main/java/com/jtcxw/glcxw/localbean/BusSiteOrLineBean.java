package com.jtcxw.glcxw.localbean;

import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean;

public class BusSiteOrLineBean {
    private int type = 0; //0 site 1 line
    private boolean isFold = true;
    private SiteOrLineBean.SiteDataBean siteDataBean;
    private SiteOrLineBean.LineDateBean lineDateBean;

    public boolean isFold() {
        return isFold;
    }

    public void setFold(boolean fold) {
        isFold = fold;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SiteOrLineBean.SiteDataBean getSiteDataBean() {
        return siteDataBean;
    }

    public void setSiteDataBean(SiteOrLineBean.SiteDataBean siteDataBean) {
        this.siteDataBean = siteDataBean;
    }

    public SiteOrLineBean.LineDateBean getLineDateBean() {
        return lineDateBean;
    }

    public void setLineDateBean(SiteOrLineBean.LineDateBean lineDateBean) {
        this.lineDateBean = lineDateBean;
    }
}
