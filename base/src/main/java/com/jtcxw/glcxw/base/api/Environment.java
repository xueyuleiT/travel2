package com.jtcxw.glcxw.base.api;


public enum Environment {
    PRODUCT( "https://api.dycxw.com/"),
    DEV_OUTER( "https://devapi.dycxw.com/");

    public String path;
    Environment(String path) {
        this.path = path;
    }
}
