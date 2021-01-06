package com.jtcxw.glcxw.base.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class ScreenUtil {
    public static int getScreenHeight(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getScreenWidth(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }
}
