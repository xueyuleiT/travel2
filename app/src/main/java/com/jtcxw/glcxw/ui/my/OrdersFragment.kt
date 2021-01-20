package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cunoraz.tagview.Tag
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.OrdersAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.DictionaryInfoBean
import com.jtcxw.glcxw.base.respmodels.OrderMixBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentOrdersBinding
import com.jtcxw.glcxw.presenters.impl.OrdersPresenter
import com.jtcxw.glcxw.ui.customized.OrderConfirmFragment
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.OrdersView
import me.yokeyword.fragmentation.SupportFragment

class OrdersFragment:BaseFragment<FragmentOrdersBinding,CommonModel>() ,OrdersView{
    override fun onGetMemberOrderFailed() {
        if (mPage == 1) {
            mDatas.clear()
        }
        mBinding.recyclerView.setNewData(mDatas,false)
    }

    override fun onGetOrderTypeListFailed() {
        getMemberOrder()
    }


    override fun onGetMemberOrderSucc(orderMixBean: OrderMixBean) {
        if (mPage == 1) {
            mDatas.clear()
        }
        if (orderMixBean.memberOrderList != null) {
            mDatas.addAll(orderMixBean.memberOrderList)
            mBinding.recyclerView.setNewData(mDatas, orderMixBean.memberOrderList.size == mRecords)
        } else {
            mBinding.recyclerView.setNewData(mDatas, false)
        }
    }

    override fun onGetMemberOrderFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }

    private val mRecords = 10
    var mPage = 1
    var mTagValue = 0
    override fun onGetOrderTypeListSucc(dictionaryInfoBean: DictionaryInfoBean) {
        mTags.clear()
        val bean = DictionaryInfoBean.DictionaryBean()
        bean.itemName = "全部"
        bean.itemValue = "0"
        mTags.add(bean)
        mTags.addAll(dictionaryInfoBean.dictionaryInfo)

        mBinding.tagView.removeAll()
        val list = ArrayList<Tag>()
        mTags.forEach {
            val tag = Tag(it.itemName)
            if (mTagValue == it.itemValue.toInt()) {
                tag.background = resources.getDrawable(R.drawable.shape_r2_cgreen)
                tag.tagTextColor = resources.getColor(R.color.white)
            } else {
                tag.background = resources.getDrawable(R.drawable.shape_r2_c_bdc8ce)
                tag.tagTextColor = resources.getColor(R.color.gray_6)
            }
            tag.tagTextSize = 12f
            list.add(tag)
        }
        mBinding.tagView.addTags(list)
        mBinding.tagView.setOnTagClickListener {  tag,position ->

            if (mBinding.swipeLayout.isRefreshing) {
                return@setOnTagClickListener
            }

            if (mTagValue == mTags[position].itemValue.toInt()){
                return@setOnTagClickListener
            }
            mTagValue = mTags[position].itemValue.toInt()

            mBinding.tagView.removeAll()
            val list = ArrayList<Tag>()
            mTags.forEach {
                val tag = Tag(it.itemName)
                if (it.itemValue.toInt() == mTagValue) {
                    tag.background = resources.getDrawable(R.drawable.shape_r2_cgreen)
                    tag.tagTextColor = resources.getColor(R.color.white)
                } else {
                    tag.background = resources.getDrawable(R.drawable.shape_r2_c_bdc8ce)
                    tag.tagTextColor = resources.getColor(R.color.gray_6)
                }
                tag.tagTextSize = 12f
                list.add(tag)
            }
            mBinding.tagView.addTags(list)

            mBinding.swipeLayout.autoRefresh()
        }

        getMemberOrder()

    }

    override fun onGetOrderTypeLisFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_orders
    }

    var mDatas = ArrayList<OrderMixBean.MemberOrderListBean>()

    var mPresenter:OrdersPresenter?= null
    var mFinishCount = 0
    var mTags = ArrayList<DictionaryInfoBean.DictionaryBean>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null && !TextUtils.isEmpty(arguments!!.getString(BundleKeys.KEY_TITLE))) {
            initToolBar(arguments!!.getString(BundleKeys.KEY_TITLE,""))
        } else {
            initToolBar("全部订单")
        }
        SwipeUtil.initHeader(mBinding.header)

        mPresenter = OrdersPresenter(this)

        mBinding.swipeLayout.setOnRefreshListener {
            mPage = 1
            if (mTags.isEmpty()) {
                mFinishCount = 2
                mPresenter!!.getOrderTypeList(JsonObject(),mBinding.swipeLayout)
            } else {
                mFinishCount = 1
                getMemberOrder()
            }

        }

        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        mBinding.recyclerView.setOnLoadNextPageListener {
            mPage ++
            getMemberOrder()
        }

        val adapter = OrdersAdapter(context!!,mDatas)
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<OrderMixBean.MemberOrderListBean>{
            override fun onItemLongClick(
                view: View?,
                model: OrderMixBean.MemberOrderListBean?,
                position: Int
            ) {
            }

            override fun onItemClick(
                view: View?,
                model: OrderMixBean.MemberOrderListBean?,
                position: Int
            ) {
                if (model!!.orderType == 4) {
                    return
                }
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_ORDER_ID, model!!.orderNo)
                OrderConfirmFragment.newInstance(this@OrdersFragment, bundle)
            }

        })
        mBinding.recyclerView.adapter = adapter

        mBinding.swipeLayout.autoRefresh()

    }

    private fun getMemberOrder() {
        val json = JsonObject()
        val orderClassType = arguments!!.getString(BundleKeys.KEY_ORDER_CLASS_TYPE,"")
        if (arguments != null && !TextUtils.isEmpty(orderClassType)) {
            json.addProperty("OrderClassType",orderClassType)
        }
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        json.addProperty("OrderType", mTagValue)
        json.addProperty("Records",mRecords)
        json.addProperty("Page",mPage)
        mPresenter!!.getMemberOrder(json,mBinding.swipeLayout)
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?){
            val ordersFragment = OrdersFragment()
            ordersFragment.arguments = bundle
            fragment.start(ordersFragment)
        }
    }

    override fun doAfterAnim() {
    }

    var hasInit = false

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (hasInit || ismBindingInitialized()) {
            if (!mBinding.swipeLayout.isRefreshing) {
                mBinding.swipeLayout.autoRefresh()
            }
        }
        hasInit = true
    }
}