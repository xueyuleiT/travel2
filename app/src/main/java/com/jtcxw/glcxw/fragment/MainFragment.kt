package com.jtcxw.glcxw.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import cn.jpush.android.api.JPushInterface
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.amap.api.location.AMapLocation
import com.glcxw.lib.util.CacheUtil
import com.glcxw.lib.util.constants.SPKeys
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentMain1Binding
import com.jtcxw.glcxw.listeners.LocationCallback
import com.jtcxw.glcxw.ui.LocationFragment
import com.jtcxw.glcxw.ui.home.HomeFragment
import com.jtcxw.glcxw.ui.login.LoginFragment
import com.jtcxw.glcxw.ui.my.MyFragment
import com.jtcxw.glcxw.ui.qr.QRFragment
import com.jtcxw.glcxw.ui.travel.GoTravelFragment
import com.jtcxw.glcxw.ui.travel.TravelFragment
import com.jtcxw.glcxw.utils.LocationUtil
import com.jtcxw.glcxw.utils.MainLoginValid
import com.jtcxw.glcxw.utils.MySingleCall
import com.jtcxw.glcxw.viewmodel.MainModel
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator

class MainFragment: BaseFragment<FragmentMain1Binding, MainModel>() {
    override fun getVariableId(): Int {
        return BR.main

    }

    private val mLocationUtil = LocationUtil()
    private var mCurrentIndex = 0
    private var mFragments = arrayOfNulls<LocationFragment<*, *>>(5)

    override fun getLayoutId(): Int {
        return R.layout.fragment_main1
    }

    override fun doAfterAnim() {
    }

    fun checkHome() {
        mBinding.radioTab.check(R.id.rb_tab_home)
    }

    fun checkHotel() {
        mBinding.radioTab.check(R.id.rb_tab_tour)
        (mFragments[2] as TravelFragment).checkHotel()
    }

    fun checkIndex(index: Int) {
        when (index) {
            0 -> mBinding.radioTab.check(R.id.rb_tab_home)
            1 -> mBinding.radioTab.check(R.id.rb_tab_travel)
            2 -> mBinding.radioTab.check(R.id.rb_tab_tour)
            3 -> mBinding.radioTab.check(R.id.rb_tab_personal)
            4 -> mBinding.rbTabQr.performClick()
        }
    }

    fun checkScenic() {
        mBinding.radioTab.check(R.id.rb_tab_tour)
        (mFragments[2] as TravelFragment).checkScenic()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragments[0] = HomeFragment()
        mFragments[1] = GoTravelFragment()
        mFragments[2] = TravelFragment()
        mFragments[3] = MyFragment()
        mFragments[4] = QRFragment()

        loadMultipleRootFragment(
            R.id.host_fragment,
            0,
            mFragments[0],
            mFragments[1],
            mFragments[2],
            mFragments[3],
            mFragments[4]
        )
        fragmentAnimator = DefaultHorizontalAnimator()

        if (!CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_SHOW_GUARD,false)) {
            mBinding.radioTab.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View?,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    mBinding.radioTab.removeOnLayoutChangeListener(this)
                    val view = activity!!.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
                    val mask = LayoutInflater.from(activity!!).inflate(R.layout.layout_mask, null)
                    view.addView(mask)
                    mask.findViewById<View>(R.id.rb_mask).setOnClickListener {
                        view.removeView(mask)
                        CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_SHOW_GUARD,true)
                    }
                }

            })
        }
        mBinding.radioTab.setOnCheckedChangeListener { _, i ->
            mBinding.rbTabQr.isChecked = false
            when (i) {
                R.id.rb_tab_home -> {
                    showHideFragment(mFragments[0], mFragments[mCurrentIndex])
                    mCurrentIndex = 0
                }

                R.id.rb_tab_travel -> {
                    showHideFragment(mFragments[1], mFragments[mCurrentIndex])
                    mCurrentIndex = 1
                }

                R.id.rb_tab_tour -> {
                    showHideFragment(mFragments[2], mFragments[mCurrentIndex])
                    mCurrentIndex = 2
                }

                R.id.rb_tab_personal -> {
                    if (TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
                        LoginFragment.newInstance(this, null)
                        val mainLoginValid = MainLoginValid(3, this)
                        MySingleCall.getInstance().clear()
                        MySingleCall.getInstance().addAction(mainLoginValid).addValid(mainLoginValid)
                            .doCall()
                        when (mCurrentIndex) {
                            0 -> mBinding.radioTab.check(R.id.rb_tab_home)
                            1 -> mBinding.radioTab.check(R.id.rb_tab_travel)
                            2 -> mBinding.radioTab.check(R.id.rb_tab_tour)
                        }

                        return@setOnCheckedChangeListener
                    }
                    showHideFragment(mFragments[3], mFragments[mCurrentIndex])
                    mCurrentIndex = 3
                }

            }
        }
        mBinding.rbTabQr.setOnClickListener {
            if (TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
                LoginFragment.newInstance(this, null)
                val mainLoginValid = MainLoginValid(4, this)
                MySingleCall.getInstance().clear()
                MySingleCall.getInstance().addAction(mainLoginValid).addValid(mainLoginValid).doCall()
                return@setOnClickListener
            }
            mBinding.radioTab.clearCheck()
            mBinding.rbTabQr.isChecked = true
            showHideFragment(mFragments[4], mFragments[mCurrentIndex])
            mCurrentIndex = 4
        }
        LocationUtil.sLastUpdateTime = 0L
        checkLocation()
    }

    public fun checkLocation() {
        XXPermissions.with(activity)
            .permission(Permission.Group.LOCATION) //不指定权限则自动获取清单中的危险权限
            .request(object : OnPermission {
                override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                    if (quick) {
                    } else {
                        MaterialDialog(context!!)
                            .title(null, "提示")
                            .message(null, "未授权定位权限,可能会影响您的使用")
                            .positiveButton(
                                null,
                                SpannelUtil.getSpannelStr(
                                    "确认",
                                    context!!.resources.getColor(R.color.blue_3A75F3)
                                ),
                                null
                            )
                            .lifecycleOwner(activity)
                            .cornerRadius(DimensionUtil.dpToPx(2), null)
                            .cancelable(false)
                            .show()
                    }
                }

                override fun hasPermission(granted: List<String>, isAll: Boolean) {
                    if (isAll) {
                        mLocationUtil.init(context, object : LocationCallback {
                            override fun onLocationCallback(aMapLocation: AMapLocation) {
                                mFragments.forEach {
                                    it?.onLocationChange(aMapLocation)
                                }
                            }

                        })
                        mLocationUtil.start()
                    }
                }
            })
    }

    override fun needSetStatusHeight(): Boolean {
        return false
    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }

    override fun onResume() {
        super.onResume()
        mLocationUtil.start()

    }

    override fun onPause() {
        super.onPause()
        mLocationUtil.stopLocation()

    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationUtil.stopLocation()
        mLocationUtil.destroy()
    }

}