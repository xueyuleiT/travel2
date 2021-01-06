package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.JsonObject
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.BuildConfig
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.VersionBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.databinding.FragmentVersionBinding
import com.jtcxw.glcxw.dialog.DownLoadDialog
import com.jtcxw.glcxw.presenters.impl.AppVersionPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.AppVersionView
import me.yokeyword.fragmentation.SupportFragment

class VersionFragment:BaseFragment<FragmentVersionBinding,CommonModel>() ,AppVersionView{
    var mVersionBean:VersionBean?= null
    override fun onAppVersionSucc(versionBean: VersionBean) {
        mVersionBean = versionBean
        mBinding.tvVersion.text = versionBean.version
        mBinding.tvDetail.text = versionBean.updContent
        if (versionBean.version.replace("V","").replace(".","").toInt() <= BuildConfig.VERSION_NAME.replace(".","").toInt()) {
            mBinding.tvCheck.isEnabled = false
            mBinding.tvCheck.text = "最新版本检查"
            mBinding.tvCheck.setBackgroundResource(R.drawable.shape_r5_gray)
        } else {
            mBinding.tvCheck.text = "更新到版本 ${versionBean.version}"
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_version
    }

    var mPresenter:AppVersionPresenter? =null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("当前版本")

        mPresenter = AppVersionPresenter(this)

        mBinding.tvCheck.setOnClickListener {
            if (mVersionBean != null) {
                if (mVersionBean!!.version.replace("V", "").replace(
                        ".",
                        ""
                    ).toInt() <= BuildConfig.VERSION_NAME.replace(".", "").toInt()
                ) {
                    val json = JsonObject()
                    json.addProperty("AppType", 1)
                    mPresenter!!.appVersion(json)
                } else {
                    showConfirmDialog(mBinding.tvCheck.text.toString(),mVersionBean!!.updContent,"更新","取消",object :com.afollestad.materialdialogs.DialogCallback{
                        override fun invoke(p1: MaterialDialog) {
                            //拍摄身份证反面
                            XXPermissions.with(activity)
                                .permission(Permission.Group.STORAGE) //不指定权限则自动获取清单中的危险权限
                                .request(object : OnPermission {

                                    override fun hasPermission(granted: List<String>, isAll: Boolean) {
                                        if (isAll) {
                                            DownLoadDialog().setTitle(mVersionBean!!.version)
                                                .setUrl(mVersionBean!!.updPackageUrl)
                                                .show(fragmentManager!!,"showConfirmDialog")
                                        } else {
                                            ToastUtil.toastWaring("获取权限成功，部分权限未正常授予")
                                        }
                                    }

                                    override fun noPermission(denied: List<String>, quick: Boolean) {
                                        if (quick) {
                                            XXPermissions.gotoPermissionSettings(context)
                                        } else {
                                            ToastUtil.toastWaring("获取权限失败")
                                        }
                                    }
                                })

                        }

                    },null)
                }
            } else {
                val json = JsonObject()
                json.addProperty("AppType", 1)
                mPresenter!!.appVersion(json)

            }
        }

        val json = JsonObject()
        json.addProperty("AppType",1)
        mPresenter!!.appVersion(json)
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val versionFragment = VersionFragment()
            versionFragment.arguments = bundle
            fragment.start(versionFragment)
        }
    }

    override fun doAfterAnim() {
    }
}