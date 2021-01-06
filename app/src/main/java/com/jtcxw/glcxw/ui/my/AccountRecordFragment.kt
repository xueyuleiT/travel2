package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.AccountRecordAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.MemberAccountHistoryBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentAccountRecordBinding
import com.jtcxw.glcxw.presenters.impl.AccountRecordPresenter
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.AccountRecordView
import me.yokeyword.fragmentation.SupportFragment

class AccountRecordFragment:BaseFragment<FragmentAccountRecordBinding,CommonModel>(),AccountRecordView {
    override fun onGetMemberAccountHistory(memberAccountHistoryBean: MemberAccountHistoryBean) {
        if (mPage == 1) {
            mDatas.clear()
        }
        if (memberAccountHistoryBean.memberAccountHistoryList != null) {
            mDatas.addAll(memberAccountHistoryBean.memberAccountHistoryList)
            mBinding.recyclerView.setNewData(mDatas,memberAccountHistoryBean.memberAccountHistoryList.size == mRecords)
        } else {
            mBinding.recyclerView.setNewData(mDatas,false)
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?){
            val accountRecordFragment = AccountRecordFragment()
            accountRecordFragment.arguments = bundle
            fragment.startForResult(accountRecordFragment,99)

        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_account_record
    }

    var mDatas = ArrayList<MemberAccountHistoryBean.MemberAccountHistoryListBean>()
    var mPage = 1
    val mRecords = 10
    var mPresenter:AccountRecordPresenter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("钱包明细")

        mPresenter = AccountRecordPresenter(this)
        SwipeUtil.initHeader(mBinding.header)
        mBinding.swipeLayout.setOnRefreshListener {
            mPage = 1
            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            json.addProperty("Records",mRecords)
            json.addProperty("Page",mPage)
            mPresenter!!.getMemberAccountHistory(json,mBinding.swipeLayout)

        }

        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        val adapter = AccountRecordAdapter(context!!,mDatas)
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<MemberAccountHistoryBean.MemberAccountHistoryListBean>{
            override fun onItemClick(
                view: View?,
                model: MemberAccountHistoryBean.MemberAccountHistoryListBean?,
                position: Int
            ) {
                if (model!!.changeType == 98 || model!!.changeType == 99) {
                    return
                }
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_ORDER_TYPE,model!!.changeType.toString())
                bundle.putString(BundleKeys.KEY_BUSINESS_ID,model!!.businessId)
                OrderDetailFragment.newInstance(this@AccountRecordFragment,bundle)
            }

            override fun onItemLongClick(
                view: View?,
                model: MemberAccountHistoryBean.MemberAccountHistoryListBean?,
                position: Int
            ) {
            }

        })
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.setOnLoadNextPageListener {
            mPage ++

            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            json.addProperty("Records",mRecords)
            json.addProperty("Page",mPage)
            mPresenter!!.getMemberAccountHistory(json,mBinding.swipeLayout)
        }

        mBinding.swipeLayout.autoRefresh()
    }

    override fun doAfterAnim() {
    }
}