package com.jtcxw.glcxw.ui.customized

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.RecruitAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.RecruitBean
import com.jtcxw.glcxw.base.respmodels.RecruitResultBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentRecruitBinding
import com.jtcxw.glcxw.presenters.impl.RecruitPresenter
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.RecruitView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment

class RecruitFragment:BaseFragment<FragmentRecruitBinding,CommonModel>(),RecruitView {
    override fun onCancelRejoinRecruitSucc(recruitResultBean: RecruitResultBean) {
        ToastUtil.toastSuccess(recruitResultBean.rtrurnMsg)
        mBinding.swipeLayout.autoRefresh()
    }

    override fun onJoinRecruit(recruitResultBean: RecruitResultBean) {
        ToastUtil.toastSuccess(recruitResultBean.rtrurnMsg)
        mBinding.swipeLayout.autoRefresh()
    }


    var mPlanningInfoBean: RecruitBean.PlanningInfoBean? = null
    override fun onGetRecruitSucc(recruitBean: RecruitBean) {
        mPlanningInfoBean = recruitBean.planning_info
        mData.clear()
        mData.addAll(recruitBean.recruit_list)
        mBinding.recyclerView.setNewData(mData,false)
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recruit
    }

    var mPresenter:RecruitPresenter ?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    var mData = ArrayList<RecruitBean.RecruitListBean>()

    override fun doAfterAnim() {
        val tvRight = mBinding.root.findViewById<TextView>(com.jtcxw.glcxw.base.R.id.tv_right)
        tvRight.text = "开始招募"
        tvRight.setTextColor(resources.getColor(R.color.green_light))
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f)
        tvRight.setOnClickListener {
            if (mPlanningInfoBean == null) {
                return@setOnClickListener
            }
            val bundle = Bundle()
            bundle.putString(BundleKeys.KEY_PLANNING_ID,mPlanningInfoBean!!.planning_id.toString())
            CustomizedCarFragment.newInstance(parentFragment as SupportFragment,bundle)
        }
        initToolBar("定制用车")


        SwipeUtil.initHeader(mBinding.header)

        mPresenter = RecruitPresenter(this)

        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.setSupportLoadNextPage(true)

        mBinding.swipeLayout.setOnRefreshListener {
            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            json.addProperty("TripType",4)
            mPresenter!!.getRecruitLine(json,mBinding.swipeLayout)
        }

        mBinding.recyclerView.setSupportLoadNextPage(true)
        val adapter = RecruitAdapter(context!!,this,mData)
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<RecruitBean.RecruitListBean> {
            override fun onItemClick(
                view: View?,
                model: RecruitBean.RecruitListBean?,
                position: Int
            ) {

            }

            override fun onItemLongClick(
                view: View?,
                model: RecruitBean.RecruitListBean?,
                position: Int
            ) {
            }

        })
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.setNewData(mData)
        mBinding.swipeLayout.autoRefresh()
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == ISupportFragment.RESULT_OK) {
            mBinding.swipeLayout.autoRefresh()
        }
    }

    var hasInit = false

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (hasInit || ismBindingInitialized()) {
            initToolBar("定制用车")
        }
        hasInit = true
    }
}