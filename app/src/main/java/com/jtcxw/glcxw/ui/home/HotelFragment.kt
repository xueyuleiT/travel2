package com.jtcxw.glcxw.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.HotHotelAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.HotelBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentHotelBinding
import com.jtcxw.glcxw.presenters.impl.HotelPresenter
import com.jtcxw.glcxw.ui.travel.HotelDetailFragment
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.HotelView
import me.yokeyword.fragmentation.SupportFragment

class HotelFragment: BaseFragment<FragmentHotelBinding, CommonModel>(),HotelView {
    override fun onHotelInfoListSucc(hotelBean: HotelBean) {
        mDatas.clear()
        mDatas.addAll(hotelBean.hotelInfoList)
        mBinding.recyclerView.setNewData(mDatas,false)
    }

    override fun onHotelInfoListFinish() {
        mBinding.swipeLayout.finishRefresh(0)
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_hotel
    }

    private val mDatas = ArrayList<HotelBean.HotelInfoListBean>()

    var mPresenter:HotelPresenter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initToolBar("酒店")

        SwipeUtil.initHeader(mBinding.header)

        mPresenter = HotelPresenter(this)

        mBinding.recyclerView.setSupportLoadNextPage(true)
        mBinding.recyclerView.layoutManager = GridLayoutManager(context,2)
        val adapter = HotHotelAdapter(context!!,mDatas)
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<HotelBean.HotelInfoListBean>{
            override fun onItemClick(
                view: View?,
                model: HotelBean.HotelInfoListBean?,
                position: Int
            ) {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_HOTEL_ID,model!!.hotelId)
                HotelDetailFragment.newInstance(this@HotelFragment,bundle)
            }

            override fun onItemLongClick(
                view: View?,
                model: HotelBean.HotelInfoListBean?,
                position: Int
            ) {
            }

        })

        mBinding.swipeLayout.setOnRefreshListener {
            var json = JsonObject()
            json.addProperty("Longitude", UserUtil.getUser().longitude)
            json.addProperty("Latitude", UserUtil.getUser().latitude)
            mPresenter!!.getHotelInfoList(json,mBinding.swipeLayout)
        }

        mBinding.recyclerView.adapter = adapter
        mBinding.swipeLayout.autoRefresh()
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val hotelFragment = HotelFragment()
            hotelFragment.arguments = bundle
            fragment.start(hotelFragment)
        }
    }

    override fun doAfterAnim() {

    }
}