package com.jtcxw.glcxw.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jtcxw.glcxw.base.utils.DimensionUtil;


public class TabLayoutUtil {
    public static void setTabWidth(final TabLayout tabLayout, final int padding){
            //拿到tabLayout的mTabStrip属性
            LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                View tabView = mTabStrip.getChildAt(i);

                //设置tab左右间距 注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                params.width = (int) DimensionUtil.Companion.dpToPx(30);
                params.leftMargin = padding;
                params.rightMargin = padding;
                tabView.setLayoutParams(params);

                tabView.invalidate();
            }

    }
}
