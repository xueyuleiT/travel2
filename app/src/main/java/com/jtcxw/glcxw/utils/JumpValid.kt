package com.jtcxw.glcxw.utils

import android.os.Handler
import android.text.TextUtils
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.toptechs.libaction.action.Action
import com.toptechs.libaction.action.Valid
import me.yokeyword.fragmentation.SupportFragment

class JumpValid: Valid,Action {
    override fun call() {
        Handler().postDelayed({
            (BaseUtil.sTopAct!!.topFragment as SupportFragment).start(mTagetFragment)
        }, 500)
    }

    var mTagetFragment:SupportFragment?= null
    constructor(fragment: SupportFragment){
        mTagetFragment = fragment
    }
    override fun check(): Boolean {
        return !TextUtils.isEmpty(UserUtil.getUserInfoBean().memberId)
    }

    override fun doValid() {
    }
}