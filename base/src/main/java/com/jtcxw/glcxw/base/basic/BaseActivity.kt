package com.jtcxw.glcxw.base.basic

import android.os.Bundle
import android.view.Window
import me.yokeyword.fragmentation.SupportActivity
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

open class BaseActivity : SupportActivity() {
    private var mCompositeSubscription: CompositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
//        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

    }

    //添加网络请求订阅
    open fun <M> addSubscription(observable: Observable<M>, subscriber: Subscriber<M>) {
        mCompositeSubscription!!.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber))
    }

    override fun onDestroy() {
        //取消订阅
        if (mCompositeSubscription!!.hasSubscriptions()) {
            //取消注册，以避免内存泄露
            mCompositeSubscription!!.unsubscribe()
        }
        super.onDestroy()
    }

}