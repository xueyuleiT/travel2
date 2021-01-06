package com.jtcxw.glcxw.utils

import android.os.Handler
import android.text.TextUtils
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.RxBus
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.fragment.MainFragment
import com.toptechs.libaction.action.Action
import com.toptechs.libaction.action.Valid

class MainLoginValid:Valid ,Action{
    override fun call() {
        Handler().postDelayed({
            mMainFragment!!.checkIndex(mCheckIndex)
        }, 500)

    }

    var mCheckIndex = 0
    var mMainFragment:MainFragment ?= null
    constructor(index: Int,fragment:MainFragment) {
        mCheckIndex = index
        mMainFragment = fragment
    }
    override fun check(): Boolean {
        return !TextUtils.isEmpty(UserUtil.getUserInfoBean().memberId)
    }

    override fun doValid() {
    }
}