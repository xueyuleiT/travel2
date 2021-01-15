package com.jtcxw.glcxw.router

import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.glcxw.router.IAppRouter
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.utils.*
import com.jtcxw.glcxw.events.LogoutEvent
import com.jtcxw.glcxw.fragment.MainFragment
import com.jtcxw.glcxw.ui.login.LoginFragment
import me.yokeyword.fragmentation.SupportFragment

class AppRouter:IAppRouter {

    override fun goLogin(warning: String) {
        if (UserUtil.isShowLogin) {
            return
        }
        UserUtil.getUser().clear()
        UserUtil.isShowLogin = true
        MaterialDialog(BaseUtil.sTopAct!!)
            .title(null,"提示")
            .message(null, warning)
            .positiveButton(null, SpannelUtil.getSpannelStr("确定", BaseUtil.sTopAct!!.resources.getColor(
                R.color.blue_3A75F3)),
                object : DialogCallback {
                    override fun invoke(p1: MaterialDialog) {
                        (BaseUtil.sTopAct!!.topFragment as SupportFragment).popTo(MainFragment::class.java,false)
                        RxBus.getDefault().postDelay(LogoutEvent(),100)
                        LoginFragment.newInstance((BaseUtil.sTopAct!!.topFragment as SupportFragment),null)
                    }
                }
            )
            .cornerRadius(DimensionUtil.dpToPx(2), null)
            .cancelable(false)
            .show()
    }
}