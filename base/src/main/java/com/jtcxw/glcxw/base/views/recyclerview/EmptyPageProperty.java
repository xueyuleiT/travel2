package com.jtcxw.glcxw.base.views.recyclerview;

import android.view.Gravity;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.jtcxw.glcxw.base.R;
import com.jtcxw.glcxw.base.utils.BaseUtil;


/**
 * Created by nali on 2017/11/16.
 */

public enum EmptyPageProperty {
    //没有内容
    NO_CONTENT_DEFAULT(BaseUtil.Companion.isDarkMode() ? R.mipmap.pic_empty_dark : R.mipmap.pic_empty, R.string.strNoContent, R.string.app_name, -1, Gravity.CENTER),

    //网络未连接
    NO_INTERNET(R.mipmap.pic_error, R.string.strNetError, R.string.app_name, -1, Gravity.CENTER),

    //服务器错误
    SERVER_ERROR(R.mipmap.pic_error, R.string.strNetError, R.string.app_name, -1, Gravity.CENTER);

    private int drawableResId;
    private int titleResId;
    private int subTitleResId;
    private int titleTextColor = -1;
    private int btnResId;
    private int gravity;

    EmptyPageProperty(@DrawableRes int drawableResId, @StringRes int titleResId, @StringRes int subTitleResId, @StringRes int btnResId, int gravity) {
        this.drawableResId = drawableResId;
        this.titleResId = titleResId;
        this.subTitleResId = subTitleResId;
        this.btnResId = btnResId;
        this.gravity = gravity;
    }

    EmptyPageProperty(@DrawableRes int drawableResId, @StringRes int titleResId, @StringRes int subTitleResId, @ColorInt int titleTextColor,
                      @StringRes int btnResId, int gravity) {
        this.drawableResId = drawableResId;
        this.titleResId = titleResId;
        this.subTitleResId = subTitleResId;
        this.titleTextColor = titleTextColor;
        this.btnResId = btnResId;
        this.gravity = gravity;
    }

    public int getDrawableResId() {
        return this.drawableResId;
    }

    public int getTitleResId() {
        return this.titleResId;
    }

    public int getSubTitleResId() {
        return this.subTitleResId;
    }

    public int getTitleTextColor() {
        return this.titleTextColor;
    }

    public int getBtnResId() {
        return this.btnResId;
    }

    public int getGravity() {
        return this.gravity;
    }

}
