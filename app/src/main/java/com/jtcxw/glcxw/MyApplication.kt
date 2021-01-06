package com.jtcxw.glcxw

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import cn.jpush.android.api.JPushInterface
import com.glcxw.lib.util.CacheUtil
import com.glcxw.router.RouterUtil
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.router.AppRouter
import com.jtcxw.glcxw.utils.DaoManager
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.sdk.QbSdk
import me.yokeyword.fragmentation.SupportActivity

open class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        DaoManager.getInstance().init(this)

        CrashReport.initCrashReport(applicationContext, "fe64931278", BuildConfig.DEBUG)
//        Bugly.init(applicationContext,"32514a6a8a", BuildConfig.DEBUG)

        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(this)
        RouterUtil.add(AppRouter())
        val cb = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                if (Build.VERSION.SDK_INT >= 29 && QbSdk.getTbsVersion(applicationContext) < 45114){
                    QbSdk.forceSysWebView()
                }
            }

            override fun onCoreInitFinished() {
                if (Build.VERSION.SDK_INT >= 29 && QbSdk.getTbsVersion(applicationContext) < 45114){
                    QbSdk.forceSysWebView()
                }
            }
        }
        QbSdk.initX5Environment(this,cb)
        CacheUtil.init(this)
        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                if (p0 is SupportActivity) {
                    BaseUtil.sTopAct = p0
                }
            }

            override fun onActivityResumed(p0: Activity) {
                if (p0 is SupportActivity) {
                    BaseUtil.sTopAct = p0
                }
            }


        })

    }

}