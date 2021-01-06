package com.jtcxw.glcxw.ui.travel

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.HotAdapter
import com.jtcxw.glcxw.adapter.HotHotelAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.HotelBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentHotBinding
import com.jtcxw.glcxw.localbean.HotBean
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.SupportFragment

class HotHotelFragment: BaseFragment<FragmentHotBinding, CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_hot
    }

    private val mDatas = ArrayList<HotelBean.HotelInfoListBean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.recyclerView.setSupportLoadNextPage(true)
        mBinding.recyclerView.layoutManager = GridLayoutManager(context,2)
        val adapter = HotHotelAdapter(context!!,mDatas)
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<HotelBean.HotelInfoListBean>{
            override fun onItemClick(
                view: View?,
                model: HotelBean.HotelInfoListBean?,
                position: Int
            ) {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_HOTEL_ID,model!!.hotelId)
                HotelDetailFragment.newInstance(parentFragment!!.parentFragment as SupportFragment,bundle)
            }

            override fun onItemLongClick(
                view: View?,
                model: HotelBean.HotelInfoListBean?,
                position: Int
            ) {
            }

        })

        mBinding.recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mBinding.recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mBinding.recyclerView.setPadding(0,0,0,0)
            }

        })
        mBinding.recyclerView.adapter = adapter
    }

    override fun doAfterAnim() {

    }

    fun onDataChange(hotelBean: HotelBean){
        mDatas.clear()
        mDatas.addAll(hotelBean.hotelInfoList)
        if (ismBindingInitialized()) {
            mBinding.recyclerView.setNewData(mDatas, false)
        }

    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        if (ismBindingInitialized()) {
            mBinding.recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mBinding.recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    mBinding.recyclerView.setPadding(0, 0, 0, 0)
                }

            })
        }

    }
}