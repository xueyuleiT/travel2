package com.jtcxw.glcxw.base.dialogs

import androidx.fragment.app.DialogFragment
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

open class BaseDialogFragment:DialogFragment() {
    private var mCompositeSubscription: CompositeSubscription = CompositeSubscription()

    open fun <M> addSubscription(observable: Observable<M>, subscriber: Subscriber<M>) {
        mCompositeSubscription!!.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber))
    }

    override fun onDestroy() {
        if (mCompositeSubscription!!.hasSubscriptions()) {
            //取消注册，以避免内存泄露
            mCompositeSubscription!!.unsubscribe()
        }
        super.onDestroy()
    }
}