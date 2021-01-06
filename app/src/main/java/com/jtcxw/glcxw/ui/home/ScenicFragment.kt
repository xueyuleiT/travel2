package com.jtcxw.glcxw.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.HotAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.ScenicBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentHotelBinding
import com.jtcxw.glcxw.presenters.impl.ScenicPresenter
import com.jtcxw.glcxw.ui.travel.ScenicDetailFragment
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.ScenicView
import me.yokeyword.fragmentation.SupportFragment

class ScenicFragment: BaseFragment<FragmentHotelBinding, CommonModel>(), ScenicView {
    override fun onScenicInfoListFinish() {
        mBinding.swipeLayout.finishRefresh(0)
    }

    override fun onScenicInfoListSucc(scenicBean: ScenicBean) {
        mDatas.clear()
        mDatas.addAll(scenicBean.scenicInfoList)
        mBinding.recyclerView.setNewData(mDatas,false)
    }


    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_hotel
    }

    private val mDatas = ArrayList<ScenicBean.ScenicInfoBean>()

    var mPresenter: ScenicPresenter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initToolBar("景点")

        SwipeUtil.initHeader(mBinding.header)

        mPresenter = ScenicPresenter(this)

        mBinding.recyclerView.setSupportLoadNextPage(true)
        mBinding.recyclerView.layoutManager = GridLayoutManager(context,2)
        val adapter = HotAdapter(context!!,mDatas)
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<ScenicBean.ScenicInfoBean>{
            override fun onItemClick(
                view: View?,
                model: ScenicBean.ScenicInfoBean?,
                position: Int
            ) {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_SCENIC_ID,model!!.scenicId)
                ScenicDetailFragment.newInstance(this@ScenicFragment,bundle)
            }

            override fun onItemLongClick(
                view: View?,
                model: ScenicBean.ScenicInfoBean?,
                position: Int
            ) {
            }

        })

        mBinding.swipeLayout.setOnRefreshListener {
            var json = JsonObject()
            json.addProperty("Longitude",UserUtil.getUser().longitude)
            json.addProperty("Latitude",UserUtil.getUser().latitude)
            mPresenter!!.getScenicInfoList(json,mBinding.swipeLayout)
        }

        mBinding.recyclerView.adapter = adapter
        mBinding.swipeLayout.autoRefresh()
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val scenicFragment = ScenicFragment()
            scenicFragment.arguments = bundle
            fragment.start(scenicFragment)
        }
    }

    override fun doAfterAnim() {

    }
}