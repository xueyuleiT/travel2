package com.jtcxw.glcxw.base.api;


public enum Environment {

    PRODUCT( "http://glapi.bitshare.cn:8080/", "https://htkactvi3.lakala.com/", "https://htkactvi.lakala.com/registration/"),
    DEV_OUTER( "https://api.dycxw.com/", "http://static.wsmsd.cn/", "http://htkactvi-sit.lakala.sh.in/registration/api/registration/");
    //https://api.dycxw.com/

    public String path;
    public String webPath;
    public String registerPath;

    Environment(String path, String webPath, String registerPath) {
        this.path = path;
        this.webPath = webPath;
        this.registerPath = registerPath;
    }
}
