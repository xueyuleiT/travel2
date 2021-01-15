package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.view.View
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.databinding.FragmentSafeBinding
import com.jtcxw.glcxw.ui.login.AgreementFragment1
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.SupportFragment

class SafeFragment: BaseFragment<FragmentSafeBinding, CommonModel>()  {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_safe
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("安全隐私")
        mBinding.llAgreement.setOnClickListener(this)
        mBinding.llPolicy.setOnClickListener(this)
        mBinding.tvPermission.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.ll_agreement -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TREATY_TYPE,"1")
                AgreementFragment1.newInstance(this,bundle)
            }

            R.id.ll_policy -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TREATY_TYPE,"2")
                AgreementFragment1.newInstance(this,bundle)
            }

            R.id.tv_permission -> {
                PermissionFragment.newInstance(this,null)
            }
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val safeFragment = SafeFragment()
            safeFragment.arguments = bundle
            fragment.start(safeFragment)
        }
    }

    override fun doAfterAnim() {
    }
}