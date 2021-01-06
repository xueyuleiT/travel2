package com.jtcxw.glcxw.utils

import android.os.Handler
import android.text.TextUtils
import android.view.View
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.toptechs.libaction.action.Action
import com.toptechs.libaction.action.Valid
import me.yokeyword.fragmentation.SupportFragment

class ClickValid: Valid,Action {
    override fun call() {
        Handler().postDelayed({
            mTagetView!!.performClick()
        }, 500)
    }

    var mTagetView:View?= null
    constructor(view: View){
        mTagetView = view
    }
    override fun check(): Boolean {
        return !TextUtils.isEmpty(UserUtil.getUserInfoBean().memberId)
    }

    override fun doValid() {
    }
}