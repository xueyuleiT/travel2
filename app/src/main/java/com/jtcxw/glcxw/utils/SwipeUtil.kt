package com.jtcxw.glcxw.utils

import androidx.core.content.ContextCompat
import com.jtcxw.glcxw.R
import com.scwang.smartrefresh.header.MaterialHeader


class SwipeUtil {
    companion object{
        fun initHeader(header: MaterialHeader){
            header.setColorSchemeColors(ContextCompat.getColor(header.context, R.color.green_light)
                , ContextCompat.getColor(header.context,R.color.gray_6))
        }
    }
}