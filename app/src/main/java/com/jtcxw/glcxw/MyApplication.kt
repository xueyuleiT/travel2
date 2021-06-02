package com.jtcxw.glcxw

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import cn.com.jttravel.wxapi.WXEntryActivity
import cn.jpush.android.api.JPushInterface
import com.glcxw.lib.util.CacheUtil
import com.glcxw.router.RouterUtil
import com.jtcxw.glcxw.base.constant.Constant
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.router.AppRouter
import com.jtcxw.glcxw.utils.DaoManager
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.smtt.sdk.QbSdk
import me.yokeyword.fragmentation.SupportActivity

open class MyApplication: Application() {


    override fun onCreate() {
        super.onCreate()

        DaoManager.getInstance().init(this)

        CrashReport.initCrashReport(applicationContext, "fe64931278", BuildConfig.DEBUG)//Bugly上送
        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(this)//推送
        RouterUtil.add(AppRouter())//添加router，主要用于跨module通讯
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
        QbSdk.initX5Environment(this,cb) //X5内核
        CacheUtil.init(this)//缓存初始化
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
                if (p0 is SupportActivity && (p0 !is WXEntryActivity)) {
                    BaseUtil.sTopAct = p0
                }
            }

            override fun onActivityResumed(p0: Activity) {
                if (p0 is SupportActivity && (p0 !is WXEntryActivity)) {
                    BaseUtil.sTopAct = p0
                }
            }


        })

    }


    private fun regToWx() {
    }
}