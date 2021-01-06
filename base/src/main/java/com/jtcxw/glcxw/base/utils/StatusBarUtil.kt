package com.jtcxw.glcxw.base.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager


class StatusBarUtil {
    companion object {
        /**
         * 设置魅族手机状态栏图标颜色风格
         * 可以用来判断是否为Flyme用户
         *
         * @param window 需要设置的窗口
         * @param dark   是否把状态栏字体及图标颜色设置为深色
         * @return boolean 成功执行返回true
         */
        fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
            var result = false
            if (window != null) {
                try {
                    val lp = window!!.getAttributes()
                    val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                    val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                    darkFlag.isAccessible = true
                    meizuFlags.isAccessible = true
                    val bit = darkFlag.getInt(null)
                    var value = meizuFlags.getInt(lp)
                    if (dark) {
                        value = value or bit
                    } else {
                        value = value and bit.inv()
                    }
                    meizuFlags.setInt(lp, value)
                    window!!.setAttributes(lp)
                    result = true
                } catch (e: Exception) {

                }

            }
            return result
        }

        /**
         * 设置小米手机状态栏字体图标颜色模式，需要MIUIV6以上
         *
         * @param window 需要设置的窗口
         * @param dark   是否把状态栏字体及图标颜色设置为深色
         * @return boolean 成功执行返回true
         */
        fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
            var result = false
            if (window != null) {
                val clazz = window!!.javaClass
                try {
                    var darkModeFlag = 0
                    val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                    val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                    darkModeFlag = field.getInt(layoutParams)
                    val extraFlagField = clazz.getMethod(
                        "setExtraFlags",
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType
                    )
                    if (dark) {//状态栏透明且黑色字体
                        extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
                    } else {//清除黑色字体
                        extraFlagField.invoke(window, 0, darkModeFlag)
                    }
                    result = true
                } catch (e: Exception) {

                }

            }
            return result
        }


        /**
         * 在不知道手机系统的情况下尝试设置状态栏字体模式为深色
         * 也可以根据此方法判断手机系统类型
         * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
         *
         * @param activity
         * @return 1:MIUUI 2:Flyme 3:android6.0 0:设置失败
         */
        open fun statusBarLightMode(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (MIUISetStatusBarLightMode(
                        activity.window,
                        true
                    )
                ) {
                    StatusBarLightMode(
                        activity,
                        1
                    )
                } else if (FlymeSetStatusBarLightMode(
                        activity.window,
                        true
                    )
                ) {
                    StatusBarLightMode(
                        activity,
                        2
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    StatusBarLightMode(
                        activity,
                        3
                    )
                }
            }

//            activity.window.decorView.setPadding(
//                0,
//                if (needSetStatusHeight) getStatusBarHeight(activity) else 0,
//                0,
//                0
//            )

        }


            /**
         * 已知系统类型时，设置状态栏字体图标为深色。
         * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
         *
         * @param activity
         * @param type     1:MIUUI 2:Flyme 3:android6.0
         */
        fun StatusBarLightMode(activity: Activity, type: Int) {
            if (type == 1) {
                MIUISetStatusBarLightMode(
                    activity.window,
                    true
                )
            } else if (type == 2) {
                FlymeSetStatusBarLightMode(
                    activity.window,
                    true
                )
            } else if (type == 3) {
                activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        /**
         * 已知系统类型时，清除MIUI或flyme或6.0以上版本状态栏字体深色模式
         * @param activity
         * @param type     1:MIUUI 2:Flyme 3:android6.0
         */
        fun StatusBarDarkMode(activity: Activity, type: Int) {
            if (type == 1) {
                MIUISetStatusBarLightMode(
                    activity.window,
                    false
                )
            } else if (type == 2) {
                FlymeSetStatusBarLightMode(
                    activity.window,
                    false
                )
            } else if (type == 3) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }

        /**
         * 获取状态栏高度
         * @param context
         * @return
         */
        fun getStatusBarHeight(context: Context): Int {
            if(sStatusBarH == 0) {
                val resources = context.getResources()
                val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
                 sStatusBarH = resources.getDimensionPixelSize(resourceId)
            }
            return sStatusBarH
        }

        var sStatusBarH :Int = 0
    }

}