package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.view.View
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.databinding.FragmentPayWayBinding
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.SupportFragment

class PayWayFragment:BaseFragment<FragmentPayWayBinding,CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }


    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?){
            val payWayFragment = PayWayFragment()
            payWayFragment.arguments = bundle
            fragment.start(payWayFragment)

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pay_way
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("支付方式")

        mBinding.tvPayWay.setOnClickListener {
            PayAliFragment.newInstance(this,null)
        }

        mBinding.tvPayOrder.setOnClickListener {
            PayListFragment.newInstance(this,null)
        }
    }

    override fun doAfterAnim() {
    }
}