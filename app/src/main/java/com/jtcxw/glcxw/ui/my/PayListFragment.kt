package com.jtcxw.glcxw.ui.my

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.PayListAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.PayListBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentPayListBinding
import com.jtcxw.glcxw.listeners.CommonItemTouchHelper
import com.jtcxw.glcxw.listeners.ItemTouchCallBack
import com.jtcxw.glcxw.presenters.impl.PayListPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.PayListView
import me.yokeyword.fragmentation.SupportFragment
import java.util.*
import kotlin.collections.ArrayList

class PayListFragment:BaseFragment<FragmentPayListBinding,CommonModel>() , PayListView{
    override fun onSetDefaultPayListSucc(jsonObject: JsonObject) {

    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?){
            val payListFragment = PayListFragment()
            payListFragment.arguments = bundle
            fragment.start(payListFragment)

        }
    }

    override fun onGetDefaultPayListSucc(payListBean: PayListBean) {
        mList.clear()
        mList.addAll(payListBean.payList)
        mBinding.recyclerView.setNewData(mList)
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pay_list
    }
    private var mHelper: ItemTouchHelper?= null
    private var mIsOrderChanged = false
    private var mAdapter:PayListAdapter?= null
    var mList = ArrayList<PayListBean.PayBean>()
    var mPresenter:PayListPresenter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("扣款顺序")

        mPresenter = PayListPresenter(this)

        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)

        val itemTouchCallBack = ItemTouchCallBack()
        itemTouchCallBack.setOnItemTouchListener(object :ItemTouchCallBack.OnItemTouchListener{
            override fun onMove(fromPosition: Int, toPosition: Int) {
            }

            override fun onSwiped(position: Int) {
            }

        })
        /**
         * 实例化ItemTouchHelper对象,然后添加到RecyclerView
         */
        mHelper = CommonItemTouchHelper.Builder()
            .canDrag(true)
            .isLongPressDragEnable(false)
            .selectedBackgroundColor(resources.getColor(R.color.bg_white))
            .unSelectedBackgroundColor(Color.TRANSPARENT)
            .itemTouchCallbackListener(object :
                CommonItemTouchHelper.OnItemTouchCallbackListener {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPosition = viewHolder.adapterPosition
                    val toPosition = target.adapterPosition
                    val temp = mList[fromPosition].paySort
                    mList[fromPosition].paySort = mList[toPosition].paySort
                    mList[toPosition].paySort = temp
                    Collections.swap(mList, fromPosition, toPosition)
                    mAdapter!!.outerAdapter.notifyItemMoved(fromPosition, toPosition)
                    mIsOrderChanged = true
                    val json = JsonObject()
                    json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                    val jsonArray = JsonArray()
                    var sort = 1
                    mList.forEach {
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("PayType",it.payType)
                        jsonObject.addProperty("PayTypeName",it.payTypeName)
                        jsonObject.addProperty("PaySort",sort)
                        jsonArray.add(jsonObject)
                        sort ++
                    }
                    json.add("PayList",jsonArray)
                    mPresenter!!.setDefaultPayList(json)
                    return true
                }

                override fun onSwipe(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                }

                override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {

                }

                override fun onSwapped(fromIndex: Int, toIndex: Int) {

                }

                override fun onRemoved(index: Int) {
                }
            })
            .build()
        mHelper!!.attachToRecyclerView(mBinding.recyclerView)

        mAdapter = PayListAdapter(context!!,mList,mHelper)
        mBinding.recyclerView.adapter = mAdapter

        val json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        mPresenter!!.getDefaultPayList(json,DialogUtil.getLoadingDialog(fragmentManager))
    }

    override fun doAfterAnim() {
    }
}