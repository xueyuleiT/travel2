package com.jtcxw.glcxw.ui.customized

import android.os.Bundle
import android.view.View
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.OrderListAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.OrderDelBean
import com.jtcxw.glcxw.base.respmodels.OrderListBean
import com.jtcxw.glcxw.base.respmodels.PayBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentOrderListBinding
import com.jtcxw.glcxw.presenters.impl.OrderListPresenter
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.OrderListView
import me.yokeyword.fragmentation.SupportFragment

class OrderListFragment:BaseFragment<FragmentOrderListBinding,CommonModel>(),OrderListView {

    companion object {
        fun newInstance(fragment: SupportFragment,bundle: Bundle?) {
            val orderListFragment = OrderListFragment()
            orderListFragment.arguments = bundle
            fragment.start(orderListFragment)
        }
    }


    override fun onPaySucc(payBean: PayBean) {
        ToastUtil.toastSuccess("支付成功")
        mBinding.swipeLayout.autoRefresh()
        val bundle = Bundle()
        bundle.putString(BundleKeys.KEY_ORDER_ID,payBean!!.order_id)
        if (parentFragment != null) {
            OrderConfirmFragment.newInstance(
                this@OrderListFragment.parentFragment as SupportFragment,
                bundle
            )
        } else {
            OrderConfirmFragment.newInstance(
                this@OrderListFragment,
                bundle
            )
        }
    }

    override fun onDeleteBusOrderSucc(orderDelBean: OrderDelBean) {
        ToastUtil.toastSuccess("删除成功")
        mBinding.swipeLayout.autoRefresh()
    }


    override fun onGetListByCustomerSucc(orderListBean: OrderListBean) {
        if (mPage == 1) {
            mData.clear()
        }
        mData.addAll(orderListBean.order_list)
        mBinding.recyclerView.setNewData(mData,orderListBean.order_list.size == mPageSize)
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_order_list
    }

    var mPage = 1
    val mPageSize = 20
    var mPresenter: OrderListPresenter?= null
    var mData = ArrayList<OrderListBean.OrderBean>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun doAfterAnim() {
        initToolBar("所有订单")
        SwipeUtil.initHeader(mBinding.header)

        mPresenter = OrderListPresenter(this)

        mBinding.swipeLayout.setOnRefreshListener {
            mPage = 1
            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            json.addProperty("BusiType",32)
            json.addProperty("PageIndex",mPage)
            json.addProperty("PageSize",mPageSize)
            mPresenter!!.getListByCustomer(json,mBinding.swipeLayout)
        }

        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.setSupportLoadNextPage(true)

        mBinding.recyclerView.setOnLoadNextPageListener {
            mPage ++
            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            json.addProperty("BusiType",32)
            json.addProperty("PageIndex",mPage)
            json.addProperty("PageSize",mPageSize)
            mPresenter!!.getListByCustomer(json,mBinding.swipeLayout)
        }
        val adapter = OrderListAdapter(context!!,this,mData)
        mBinding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<OrderListBean.OrderBean>{
            override fun onItemClick(view: View?, model: OrderListBean.OrderBean?, position: Int) {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_ORDER_ID,model!!.order_id)
                if (parentFragment != null) {
                    OrderConfirmFragment.newInstance(
                        this@OrderListFragment.parentFragment as SupportFragment,
                        bundle
                    )
                } else {
                    OrderConfirmFragment.newInstance(
                        this@OrderListFragment,
                        bundle
                    )
                }
            }

            override fun onItemLongClick(
                view: View?,
                model: OrderListBean.OrderBean?,
                position: Int
            ) {
            }

        })
        mBinding.recyclerView.setNewData(mData)
        mBinding.swipeLayout.autoRefresh()
    }

    override fun statusBarColor(): Int {
        return R.color.back_white
    }

    var hasInit = false

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (hasInit || ismBindingInitialized()) {
            if (!mBinding.swipeLayout.isRefreshing) {
                mBinding.swipeLayout.autoRefresh()
            }

            initToolBar("所有订单")
        }
        hasInit = true
    }
}