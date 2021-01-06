package com.jtcxw.glcxw.ui.travel

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.HotAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.ScenicBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentHotBinding
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.SupportFragment

class HotSpotFragment: BaseFragment<FragmentHotBinding, CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_hot
    }

    private val mDatas = ArrayList<ScenicBean.ScenicInfoBean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.recyclerView.layoutManager = GridLayoutManager(context,2)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        val adapter = HotAdapter(context!!,mDatas)
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<ScenicBean.ScenicInfoBean>{
            override fun onItemClick(
                view: View?,
                model: ScenicBean.ScenicInfoBean?,
                position: Int
            ) {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_SCENIC_ID,model!!.scenicId)
                ScenicDetailFragment.newInstance(parentFragment!!.parentFragment as SupportFragment,bundle)
            }

            override fun onItemLongClick(
                view: View?,
                model: ScenicBean.ScenicInfoBean?,
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

    fun onDataChange(scenicBean: ScenicBean) {
        mDatas.clear()
        mDatas.addAll(scenicBean.scenicInfoList)
        if (ismBindingInitialized()) {
            mBinding.recyclerView.setNewData(mDatas, false)
        }
    }
}