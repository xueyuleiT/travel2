package com.jtcxw.glcxw.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.ViewDataBinding
import com.amap.api.location.AMapLocation
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.utils.RxBus
import com.jtcxw.glcxw.events.LoginEvent
import com.jtcxw.glcxw.events.LogoutEvent
import rx.Subscription

open abstract class LocationFragment<VB : ViewDataBinding,VM : BaseObservable>: BaseFragment<VB, VM>() {
    open abstract fun onLocationChange(aMapLocation: AMapLocation)

    override fun backColor(): Int {
        return R.color.white
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterEvent()
    }
    abstract fun refresh()
    abstract fun onLogout()

    private val mRxList = ArrayList<Subscription>()
    private fun registerEvent(){
        mRxList.add(
            RxBus.getDefault().toObservable(LoginEvent::class.java)
                .subscribe {
                    refresh()
                }
        )

        mRxList.add(
            RxBus.getDefault().toObservable(LogoutEvent::class.java)
                .subscribe {
                    onLogout()
                }
        )
    }

    private fun unRegisterEvent(){
        mRxList.forEach {
            it.unsubscribe()
        }
        mRxList.clear()
    }
}