package com.jtcxw.glcxw.base.respmodels;

public class VersionBean {
    /**
     * Version : V1.0
     * ReleaseTime : 2020-12-19
     * UpdContent : sdsds
     */

    private String Version;
    private String ReleaseTime;
    private String UpdContent;
    private String UpdPackageUrl;


    public String getUpdPackageUrl() {
        return UpdPackageUrl;
    }

    public void setUpdPackageUrl(String updPackageUrl) {
        UpdPackageUrl = updPackageUrl;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getReleaseTime() {
        return ReleaseTime;
    }

    public void setReleaseTime(String ReleaseTime) {
        this.ReleaseTime = ReleaseTime;
    }

    public String getUpdContent() {
        return UpdContent;
    }

    public void setUpdContent(String UpdContent) {
        this.UpdContent = UpdContent;
    }
}
