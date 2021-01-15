package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.view.View
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.databinding.FragmentPermissionBinding
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.SupportFragment

class PermissionFragment: BaseFragment<FragmentPermissionBinding, CommonModel>()  {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_permission
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("权限管理")
        mBinding.llCamera.setOnClickListener(this)
        mBinding.llLocation.setOnClickListener(this)
        mBinding.tvSave.setOnClickListener(this)
    }

    override fun doAfterAnim() {
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val permissionFragment = PermissionFragment()
            permissionFragment.arguments = bundle
            fragment.start(permissionFragment)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.ll_location -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_PERMISSION_TYPE,"position")
                PermissionDetailFragment.newInstance(this,bundle)
            }

            R.id.ll_camera -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_PERMISSION_TYPE,"camera")
                PermissionDetailFragment.newInstance(this,bundle)
            }

            R.id.tv_save -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_PERMISSION_TYPE,"photo")
                PermissionDetailFragment.newInstance(this,bundle)
            }
        }
    }
}