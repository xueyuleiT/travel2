package com.jtcxw.glcxw.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.HomeAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.databinding.FragmentFocusBinding
import com.jtcxw.glcxw.localbean.HomeItem
import com.jtcxw.glcxw.viewmodel.CommonModel

class FocusFragment:  BaseFragment<FragmentFocusBinding, CommonModel>()  {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_focus
    }

    private var mData = ArrayList<HomeItem>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.llLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                mBinding.llLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mBinding.llLayout.setPadding(0,0,0,0)
            }

        })


        var item = HomeItem(R.mipmap.icon_public_car, "公交查询")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_special_car, "定制公交")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_parking, "停车场")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_train, "火车票")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_plan, "飞机票")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_hotel, "酒店")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_scenic_spot, "景点门票")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_charge, "充电")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_more, "更多")
        mData.add(item)

        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.adapter = HomeAdapter(context!!, mData)
    }

    override fun doAfterAnim() {

    }
}