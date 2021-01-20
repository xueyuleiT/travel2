package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.view.View
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.databinding.FragmentPermissionDetailBinding
import com.jtcxw.glcxw.utils.PermissionUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.SupportFragment

class PermissionDetailFragment : BaseFragment<FragmentPermissionDetailBinding, CommonModel>()  {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_permission_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when {
            arguments!!.getString(BundleKeys.KEY_PERMISSION_TYPE) == "photo" -> {
                initToolBar("相册/存储权限")
                mBinding.tvQuestion.text = "为什么要获取我的相册/存储权限"
                mBinding.tvPermission.text = "基于相册的存储功能，您可以在桂林出行网APP上进行更换个人头像的功能。"
                mBinding.tvOperation.text = "如何进行权限设置？能否停止授权？"
                mBinding.tvPermissionOperation.text = "1、打开手机设置\n\n2、在应用列表内找到应用\n\n3、点击进入，查看或修改权限设置"
            }
            arguments!!.getString(BundleKeys.KEY_PERMISSION_TYPE) == "camera" -> {
                initToolBar("相机权限")
                mBinding.tvQuestion.text = "为什么要获取我的相机权限"
                mBinding.tvPermission.text = "基于摄像头（相机）的附加功能，您可以使用这个附加功能完成拍照。"
                mBinding.tvOperation.text = "如何进行权限设置？能否停止授权？"
                mBinding.tvPermissionOperation.text = "1、打开手机设置\n\n2、在应用列表内找到应用\n\n3、点击进入，查看或修改权限设置"
            }
            else -> {
                initToolBar("位置权限")

                mBinding.tvQuestion.text = "为什么要获取我的位置信息"
                mBinding.tvPermission.text = "基于位置信息的个性化功能，我们会收集您的位置信息（我们仅收集您当时所处的地理位置，但不会将您各时段的位置信息进行结合以判断您的行踪轨迹）来判断您所处的地点，自动为您推荐或提供您所在区域可以使用的服务；为您规划路径；同时向您推荐离您最近的公交站点和线路；向您推荐您所在地区的热门景点和酒店等等。"
                mBinding.tvOperation.text = "如何进行权限设置？能否停止授权？"
                mBinding.tvPermissionOperation.text = "1、打开手机设置\n\n2、在应用列表内找到应用\n\n3、点击进入，查看或修改权限设置"
            }
        }

        mBinding.tvSetting.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.tv_setting -> {
                PermissionUtil.goSetting()
            }
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val permissionDetailFragment = PermissionDetailFragment()
            permissionDetailFragment.arguments = bundle
            fragment.start(permissionDetailFragment)
        }
    }

    override fun doAfterAnim() {
    }
}