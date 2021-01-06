package com.jtcxw.glcxw.base.views

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView
import com.glcxw.lib.util.CacheUtil
import com.glcxw.lib.util.constants.SPKeys
import com.jtcxw.glcxw.base.constant.Constant

class TimingTextView : TextView {



    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        if (TextUtils.isEmpty(text)){
            text = "获取验证码"
        }
    }
    //时长
    open var currentTime = Constant.SMS_TIME
    //倒计时结束回调
    var onStopTime: (TimingTextView) -> Unit = {
        text = "获取验证码"
    }
    //倒计时开始回调
    var onStartTime: (TimingTextView) -> Unit = {}
    val timeHandler = Handler()
    //计时任务
    val task = Task()
    //控件文本前缀
    var prefix = "重新获取("
    //后缀
    var suffix = "s)"
    //运行状态
    var isRunning = false

    inner class Task : Runnable {
        override fun run() {
            if (currentTime == 0L) {
                currentTime = Constant.SMS_TIME
                //计时结束
                onStopTime(this@TimingTextView)
                isRunning = false
                isEnabled = true
                return
            }
            //更新计时文本
            text = StringBuilder().append(prefix).append(currentTime).append(suffix).toString()
            currentTime--
            //每间隔一秒更新文本
            timeHandler.postDelayed(this, 1000)
        }
        //开始计时
        fun startWithType(type: Int) {
            if(isRunning){
                return
            }
            CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_SMS_TIME + type,System.currentTimeMillis())
            isEnabled = false
            onStartTime(this@TimingTextView)
            isRunning = true
            timeHandler.post(this)
        }

        //结束
        fun stop() {
            isRunning = false
            timeHandler.removeCallbacks(this)

        }

        //开始计时
        fun start(time :Long) {
            if(isRunning){
                return
            }
            isEnabled = false
            onStartTime(this@TimingTextView)
            isRunning = true
            currentTime = time
            timeHandler.post(this)
        }
    }



    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        task.stop()
    }
}