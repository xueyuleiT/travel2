package com.glcxw.lib.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtil {

    private static Context sContext;
    private SharedPreferences mSecurePrefs;

    public static void init(Application context){
        sContext = context;
    }

    private CacheUtil() {
        mSecurePrefs = sContext.getSharedPreferences("htk",Context.MODE_PRIVATE);
    }
    private volatile static CacheUtil INSTANCE = null;
    public static CacheUtil getInstance() {
        if (INSTANCE == null){
            synchronized (CacheUtil.class){
                if (INSTANCE == null) {
                    INSTANCE = new CacheUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void setProperty(String key, String value) {
        if (null == value)
            value = "";

        mSecurePrefs.edit().putString(key, value).commit();
    }

    public void setProperty(String key, int value) {
        mSecurePrefs.edit().putInt(key, value).commit();
    }

    public void setProperty(String key, boolean value) {
        mSecurePrefs.edit().putBoolean(key, value).commit();
    }

    public void setProperty(String key, long value) {
        mSecurePrefs.edit().putLong(key, value).commit();
    }


    public Long getProperty(String key, Long value) {
        return mSecurePrefs.getLong(key, value);
    }

    public boolean getProperty(String key, boolean value) {
        return mSecurePrefs.getBoolean(key, value);
    }

    public String getProperty(String key, String value) {
        return mSecurePrefs.getString(key, value);
    }

    public int getProperty(String key, int value) {
        return mSecurePrefs.getInt(key, value);
    }

    public String getProperty(String key) {
        return getProperty(key, "");
    }

    /**
     * 清空所有数据
     */
    public void clearAllData() {
        mSecurePrefs.edit().clear().commit();
    }


}
