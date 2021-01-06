package com.jtcxw.glcxw.ui.my

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.ServiceAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.CusServerBean
import com.jtcxw.glcxw.base.respmodels.MisceAneousBean
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentServiceBinding
import com.jtcxw.glcxw.presenters.impl.ServicePresenter
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.ServiceView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment

class ServiceFragment:BaseFragment<FragmentServiceBinding,CommonModel>() ,ServiceView{
    override fun onGetMisceAneousBeanSucc(misceAneousBean: MisceAneousBean) {
        if (misceAneousBean.itemType == 2) {
            mBinding.tvPhone.text = misceAneousBean.itemValue
        } else {
            mBinding.tvTime.text = misceAneousBean.itemValue
        }
    }

    override fun onGetMisceAneousBeanFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }

    override fun onGetCusServerInfoSucc(list: List<CusServerBean>) {
        mList.clear()
        mList.addAll(list)
        mBinding.recyclerView.setNewData(mList,false)
    }

    override fun onGetCusServerInfoFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_service
    }

    var mFinishCount = 0
    var mPresenter:ServicePresenter?= null
    var mList = ArrayList<CusServerBean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("客服中心")

        mPresenter = ServicePresenter(this)

        SwipeUtil.initHeader(mBinding.header)
        mBinding.swipeLayout.setOnRefreshListener {
            mFinishCount = 3
            var json = JsonObject()
            json.addProperty("ItemType","2")
            json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
            mPresenter!!.getMisceAneous(json,mBinding.swipeLayout)

            json = JsonObject()
            json.addProperty("ItemType","3")
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            mPresenter!!.getMisceAneous(json,mBinding.swipeLayout)

            json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            mPresenter!!.getCusServerInfo(json,mBinding.swipeLayout)
        }

        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        mBinding.recyclerView.adapter = ServiceAdapter(context!!,mList)
        mBinding.recyclerView.setNewData(mList)

        mBinding.swipeLayout.autoRefresh()

        mBinding.tvPhone.setOnClickListener {
            if (TextUtils.isEmpty(mBinding.tvPhone.text.toString())) {
                return@setOnClickListener
            }
            val text = mBinding.tvPhone.text.toString()
            MaterialDialog(context!!)
                .title(null, "提示")
                .message(null, SpannelUtil.getSpannelStr(text,context!!.resources.getColor(R.color.red_ff3737)))
                .negativeButton(null, SpannelUtil.getSpannelStr("取消",context!!.resources.getColor(R.color.blue_3a75f3)))
                .positiveButton(null, SpannelUtil.getSpannelStr("拨打",context!!.resources.getColor(R.color.blue_3a75f3)), object : DialogCallback {
                    override fun invoke(p1: MaterialDialog) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        val data = Uri.parse("tel:$text")
                        intent.data = data
                        startActivity(intent)
                    }
                })
                .cornerRadius(DimensionUtil.dpToPx(2), null)
                .cancelable(false)
                .show()
        }

        mBinding.tvComplaint.setOnClickListener {
            ComplaintFragment.newInstance(this,null)
        }
    }


    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (resultCode == ISupportFragment.RESULT_OK) {
            mBinding.swipeLayout.autoRefresh()
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val serviceFragment = ServiceFragment()
            serviceFragment.arguments = bundle
            fragment.start(serviceFragment)
        }
    }

    override fun doAfterAnim() {
    }
}