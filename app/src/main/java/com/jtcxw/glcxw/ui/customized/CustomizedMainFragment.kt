package com.jtcxw.glcxw.ui.customized

import android.os.Bundle
import android.view.View
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.databinding.FragmentMainCustomizedBinding
import com.jtcxw.glcxw.ui.CustomizedBusFragment
import com.jtcxw.glcxw.viewmodel.MainModel
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator

class CustomizedMainFragment:BaseFragment<FragmentMainCustomizedBinding,MainModel>() {

    companion object {
        fun newInstance(fragment:SupportFragment, bundle: Bundle?) {
            val customizedMainFragment = CustomizedMainFragment()
            customizedMainFragment.arguments = bundle
            fragment.start(customizedMainFragment)
        }
    }

    override fun getVariableId(): Int {
        return BR.main
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_customized
    }


    private var mCurrentIndex = 0
    private var mFragments = arrayOfNulls<SupportFragment>(4)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFragments[0] = CustomizedBusFragment()
        mFragments[1] = CustomizedSchoolBusFragment()
        mFragments[2] = RecruitFragment()
        mFragments[3] = OrderListFragment()
        loadMultipleRootFragment(R.id.host_fragment, 0, mFragments[0], mFragments[1],mFragments[2],mFragments[3])
        fragmentAnimator = DefaultHorizontalAnimator()
        mBinding.radioTab.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.rb_tab_home ->{
                    showHideFragment(mFragments[0],mFragments[mCurrentIndex])
                    mCurrentIndex = 0
                }

                R.id.rb_tab_travel ->{
                    showHideFragment(mFragments[1],mFragments[mCurrentIndex])
                    mCurrentIndex = 1
                }

                R.id.rb_tab_tour ->{
                    showHideFragment(mFragments[2],mFragments[mCurrentIndex])
                    mCurrentIndex = 2
                }

                R.id.rb_tab_personal ->{
                    showHideFragment(mFragments[3],mFragments[mCurrentIndex])
                    mCurrentIndex = 3
                }

            }
        }

        if (arguments != null) {
            var id = arguments!!.getInt(BundleKeys.KEY_INDEX,0)
            when(id) {
                0 -> {
                    id = R.id.rb_tab_home
                }

                1 -> {
                    id = R.id.rb_tab_travel
                }

                2 -> {
                    id = R.id.rb_tab_tour
                }

                3 -> {
                    id = R.id.rb_tab_personal
                }

            }
            mBinding.radioTab.check(id)
        }
    }

    override fun needSetStatusHeight(): Boolean {
        return false
    }

    override fun doAfterAnim() {
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        mFragments.forEach {
            it?.onFragmentResult(requestCode,resultCode,data)
        }
    }
}