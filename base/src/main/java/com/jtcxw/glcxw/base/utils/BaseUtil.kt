package com.jtcxw.glcxw.base.utils

import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import me.yokeyword.fragmentation.SupportActivity
import java.util.concurrent.Executors

class BaseUtil {
    companion object{
        var sTopAct : SupportActivity? = null
        val sExecutorService = Executors.newSingleThreadExecutor()
        fun unDisplayViewSize(view: View):Int {
            val sizeArr =  arrayOfNulls<Int>(2)
            var width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED)
            var height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED)
            view.measure(width, height)
            sizeArr[0] = view.measuredWidth
            sizeArr[1] = view.measuredHeight
            return sizeArr[1]!!
        }

        fun unDisplayViewWidth(view: View):Int {
            val sizeArr =  arrayOfNulls<Int>(2)
            var width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED)
            var height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED)
            view.measure(width, height)
            sizeArr[0] = view.measuredWidth
            sizeArr[1] = view.measuredHeight
            return sizeArr[0]!!
        }

        fun tintDrawable(@NonNull drawable : Drawable, color:Int) : Drawable {
            drawable.setColorFilter(ContextCompat.getColor(sTopAct!!.applicationContext, color), PorterDuff.Mode.SRC_ATOP)
            return drawable
        }

        fun isDarkMode():Boolean{
            val nightModeFlags = sTopAct!!.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            return when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    true
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    false
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    false
                }
                else -> {
                    false
                }
            }
        }

    }
}