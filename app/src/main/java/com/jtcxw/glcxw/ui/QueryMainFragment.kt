package com.jtcxw.glcxw.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.databinding.FragmentQueryMainBinding
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator

class QueryMainFragment:BaseFragment<FragmentQueryMainBinding,CommonModel>() {
    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val queryMainFragment = QueryMainFragment()
            queryMainFragment.arguments = bundle
            fragment.start(queryMainFragment)
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_query_main
    }

    var mQueryStr = ""

    private var mCurrentFragment:SupportFragment?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            if (arguments!!.containsKey(BundleKeys.KEY_LINE_ID) || arguments!!.containsKey(BundleKeys.KEY_STATION_ID)) {
                mCurrentFragment = BusMapFragment()
                mCurrentFragment!!.arguments = arguments
                childFragmentManager!!.beginTransaction()
                    .add(R.id.container,mCurrentFragment!!)
                    .commitNow()
            } else {
                mQueryStr = arguments!!.getString(BundleKeys.KEY_QUERY_TEXT,"")
                mCurrentFragment = BusQueryFragment()
                childFragmentManager!!.beginTransaction()
                    .add(R.id.container,mCurrentFragment!!)
                    .commitNow()
            }
        } else {
            mCurrentFragment = BusQueryFragment()
            childFragmentManager!!.beginTransaction()
                .add(R.id.container,mCurrentFragment!!)
                .commitNow()
        }
        fragmentAnimator = DefaultHorizontalAnimator()

    }

    fun replace(showFragment: SupportFragment){
//        childFragmentManager!!.beginTransaction()
//            .setCustomAnimations(fragmentAnimator.enter,fragmentAnimator.exit,fragmentAnimator.popEnter,fragmentAnimator.popExit)
//            .hide(mCurrentFragment!!)
//            .commitNow()
        if (childFragmentManager!!.fragments.size == 1){
            childFragmentManager!!.beginTransaction()
                .setCustomAnimations(fragmentAnimator.enter,fragmentAnimator.exit,fragmentAnimator.popEnter,fragmentAnimator.popExit)
                .add(R.id.container,showFragment)
                .commitNow()
        } else {
            childFragmentManager!!.beginTransaction()
                .remove(childFragmentManager.fragments[0])
                .commitNow()
            childFragmentManager!!.beginTransaction()
                .setCustomAnimations(
                    fragmentAnimator.enter,
                    fragmentAnimator.exit,
                    fragmentAnimator.popEnter,
                    fragmentAnimator.popExit
                )
                .add(R.id.container,showFragment)
                .commitNow()
        }
        mCurrentFragment = showFragment
    }

    override fun doAfterAnim() {
    }

    override fun needSetStatusHeight(): Boolean {
        return false
    }


    override fun onBackPressedSupport(): Boolean {
        if (childFragmentManager!!.fragments.size == 1) {
            BaseUtil.sTopAct!!.pop()
        } else {
            childFragmentManager!!.beginTransaction()
                .setCustomAnimations(
                    fragmentAnimator.enter,
                    fragmentAnimator.exit,
                    fragmentAnimator.popEnter,
                    fragmentAnimator.popExit
                )
                .remove(mCurrentFragment!!)
                .commitNow()
            val fragment = childFragmentManager!!.fragments[childFragmentManager!!.fragments.size - 1]
            if (fragment is BusQueryFragment) {
                fragment.onSupportVisible()
            }
            childFragmentManager!!.beginTransaction()
                .show(fragment)
                .commitNow()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            if (childFragmentManager!!.fragments.size == 1) {
                return super.onOptionsItemSelected(item)
            } else {
                childFragmentManager!!.beginTransaction()
                    .setCustomAnimations(
                        fragmentAnimator.enter,
                        fragmentAnimator.exit,
                        fragmentAnimator.popEnter,
                        fragmentAnimator.popExit
                    )
                    .remove(mCurrentFragment!!)
                    .commitNow()
                val fragment = childFragmentManager!!.fragments[childFragmentManager!!.fragments.size - 1]
                if (fragment is BusQueryFragment) {
                    fragment.onSupportVisible()
                }
                childFragmentManager!!.beginTransaction()
                    .show(fragment)
                    .commitNow()
            }
        }
        return true
    }
}