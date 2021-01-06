package com.jtcxw.glcxw.ui.home

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.HomeHSpotAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.ContentListBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentHotBinding
import com.jtcxw.glcxw.presenters.impl.HomeSpotPresenter
import com.jtcxw.glcxw.ui.travel.HotelDetailFragment
import com.jtcxw.glcxw.ui.travel.ScenicDetailFragment
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.HomeSpotView
import me.yokeyword.fragmentation.SupportFragment

class HomeSpotFragment : BaseFragment<FragmentHotBinding, CommonModel> ,HomeSpotView{

    constructor(type: Int) {
        mType = type
    }
    constructor()

    var mType:Int?= -1
    override fun onContentListSucc(contentListBean: ContentListBean) {
        mDatas.clear()
        mDatas.addAll(contentListBean.contentList)
        mBinding.recyclerView.setNewData(mDatas,false)
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    var mPresenter: HomeSpotPresenter? =null

    override fun getLayoutId(): Int {
        return R.layout.fragment_hot
    }

    private val mDatas = ArrayList<ContentListBean.ContentListInfoBean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.recyclerView.layoutManager = GridLayoutManager(context,2)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        val adapter = HomeHSpotAdapter(context!!,mDatas)
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<ContentListBean.ContentListInfoBean>{
            override fun onItemClick(
                view: View?,
                model: ContentListBean.ContentListInfoBean?,
                position: Int
            ) {
                if (model!!.contentType == 2) {
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_SCENIC_ID,model!!.contentId)
                    ScenicDetailFragment.newInstance(BaseUtil.sTopAct!!.topFragment as SupportFragment,bundle)
                } else {
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_HOTEL_ID,model!!.contentId)
                    HotelDetailFragment.newInstance(BaseUtil.sTopAct!!.topFragment as SupportFragment,bundle)
                }
            }

            override fun onItemLongClick(
                view: View?,
                model: ContentListBean.ContentListInfoBean?,
                position: Int
            ) {
            }

        })
        mBinding.recyclerView.adapter =adapter
        mBinding.recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mBinding.recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                animPadding()
            }

        })

        mPresenter = HomeSpotPresenter(this)

        if (mType != -1) {
            val json = JsonObject()
            json.addProperty("TagId", mType)
            mPresenter!!.getContentList(json)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (valueAnimator != null && valueAnimator!!.isRunning){
            valueAnimator!!.cancel()
            valueAnimator!!.removeAllUpdateListeners()
            valueAnimator = null
        }
    }

    var valueAnimator: ValueAnimator?= null
    fun animPadding(){
        valueAnimator = ValueAnimator.ofInt(mBinding.recyclerView.paddingTop,0)
        valueAnimator!!.addUpdateListener {
            mBinding.recyclerView.setPadding(0, it.animatedValue as Int,0,0)
        }
        valueAnimator!!.interpolator = DecelerateInterpolator()
        valueAnimator!!.duration = 300
        valueAnimator!!.start()

    }

    override fun doAfterAnim() {

    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        if (ismBindingInitialized()){
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