package com.jtcxw.glcxw.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.glcxw.lib.util.CacheUtil
import com.glcxw.lib.util.constants.SPKeys
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseActivity
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.dialog.AgreementDialog
import com.jtcxw.glcxw.listeners.DialogCallback
import com.jtcxw.glcxw.listeners.OrderCancelCallback
import com.jtcxw.glcxw.ui.login.LoginFragment
import me.yokeyword.fragmentation.SupportFragment
import kotlin.system.exitProcess

class WelcomeActivity: BaseActivity() {
    var animation: AlphaAnimation?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        animation = AlphaAnimation(0f, 1.0f)
        animation!!.duration = 1000



        animation!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {

                if (CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_FIRST_RUN,true) && TextUtils.isEmpty(CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_TELEPHONE,""))) {
                   val dialog = AgreementDialog().setCancelCallback(object : DialogCallback {
                       override fun onDialogCallback(type: Int) {
                           if (type == 1) {
                               CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_FIRST_RUN,false)
                               UserUtil.getUser().userInfoBean.memberId = CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_MEMBER_ID,"")
                               UserUtil.getUser().userInfoBean.token = CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_TOKEN,"")
                               val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                               startActivity(intent)
                               findViewById<View>(R.id.iv_welcome).postDelayed({
                                   finish()
                               }, 300)
                           } else {
                               finish()
                           }
                       }

                    })
                    dialog.isCancelable = false
                    dialog.show(supportFragmentManager!!,"PermissionDialog")
                } else {
                    UserUtil.getUser().userInfoBean.realTelphoneNo = CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_TELEPHONE,"")
                    UserUtil.getUser().userInfoBean.memberId = CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_MEMBER_ID,"")
                    UserUtil.getUser().userInfoBean.token = CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_TOKEN,"")
                    val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    findViewById<View>(R.id.iv_welcome).postDelayed({
                        finish()
                    }, 300)
                }


            }

            override fun onAnimationStart(animation: Animation?) {
                findViewById<View>(R.id.iv_welcome).visibility = View.VISIBLE
            }

        })
        findViewById<View>(R.id.iv_welcome).startAnimation(animation)
    }

    override fun onBackPressedSupport() {
        if(supportFragmentManager.backStackEntryCount > 1){
            pop()
        }else {
            super.onBackPressedSupport()
            exitProcess(0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==android.R.id.home){
            return (topFragment as SupportFragment).onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}