package com.jtcxw.glcxw.ui.my

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.CollectionAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.CollectionInfoBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentCollectionBinding
import com.jtcxw.glcxw.listeners.CollectCancelCallback
import com.jtcxw.glcxw.localbean.MyCollectionBean
import com.jtcxw.glcxw.presenters.impl.CollectionListPresenter
import com.jtcxw.glcxw.presenters.impl.CollectionPresenter
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.CollectionListView
import com.jtcxw.glcxw.views.CollectionView
import me.yokeyword.fragmentation.SupportFragment

class CollectionFragment:BaseFragment<FragmentCollectionBinding,CommonModel>(),CollectionListView,CollectionView {
    override fun onAddCollectionSucc(jsonObject: JsonObject) {
    }

    override fun onCancelCollectionSucc(jsonObject: JsonObject) {
        if (jsonObject.get("Status").asBoolean){
            ToastUtil.toastSuccess("已取消收藏")
            mBinding.swipeLayout.autoRefresh()
        } else {
            ToastUtil.toastSuccess("取消失败")
        }
    }

    override fun onCollectionInfoFinish() {
        mFinishCount ++
        if (mFinishCount > 4) {
            mBinding.swipeLayout.finishRefresh(0)
            mList.clear()
            for (i in 0 until mCollectInfoBean.size ) {
                val it = mCollectInfoBean[i]
                val myCollectionBean = MyCollectionBean()
                myCollectionBean.collectInfoBean = it
                var has = false
                mList.forEach { it1 ->
                    if (it1.collectInfoBean.type == it.type) {
                        has = true
                        return@forEach
                    }
                }
                if (!has) {
                    if (i + 1 < mCollectInfoBean.size) {
                        if (mCollectInfoBean[i + 1].type != it.type) {
                            myCollectionBean.type = 3
                        } else {
                            myCollectionBean.type = 0
                        }
                    } else if (i == mCollectInfoBean.size - 1){
                        myCollectionBean.type = 3
                    } else {
                        myCollectionBean.type = 0
                    }
                } else {
                    if (i + 1 < mCollectInfoBean.size) {
                        if (mCollectInfoBean[i + 1].type != it.type) {
                            myCollectionBean.type = 2
                        } else {
                            myCollectionBean.type = 1
                        }
                    } else if (i == mCollectInfoBean.size - 1){
                        myCollectionBean.type = 2
                    } else {
                        myCollectionBean.type = 1
                    }
                }
                mList.add(myCollectionBean)
            }
            val tvRight = mBinding.root.findViewById<TextView>(R.id.tv_right)

            if (tvRight.text.toString() == "完成") {
                mList.forEach {
                    it.isEdit = true
                }
            }
            mBinding.recyclerView.setNewData(mList,false)
        } else {
            var json = JsonObject()
            json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
            json.addProperty("Type", mFinishCount)
            mPresenter!!.getCollectionInfo(json, mBinding.swipeLayout)
        }
    }

    var mFinishCount = 1
    override fun onCollectionInfoSucc(collectInfoBean: CollectionInfoBean) {
        mCollectInfoBean.addAll(collectInfoBean.collectInfo)
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_collection
    }

    var mList = ArrayList<MyCollectionBean>()
    var mCollectInfoBean = ArrayList<CollectionInfoBean.CollectInfoBean>()
    var mPresenter:CollectionListPresenter?= null
    var mCollectPresenter:CollectionPresenter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("收藏")

        val tvRight = mBinding.root.findViewById<TextView>(R.id.tv_right)
        tvRight.text = "编辑"
        tvRight.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        tvRight.setTextColor(resources.getColor(R.color.green_light))
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f)
        tvRight.setOnClickListener {
            if (mList.isEmpty()) {
                return@setOnClickListener
            }
            mList.forEach {
                it.isEdit = !it.isEdit
            }

            if (tvRight.text.toString() == "编辑") {
                tvRight.text = "完成"
            } else {
                tvRight.text = "编辑"
            }

            mBinding.recyclerView.innerAdapter.notifyAllItems()
        }


        SwipeUtil.initHeader(mBinding.header)

        mPresenter = CollectionListPresenter(this)
        mCollectPresenter = CollectionPresenter(this)

        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.setSupportLoadNextPage(true)

        mBinding.recyclerView.adapter = CollectionAdapter(context!!, mList,object :CollectCancelCallback{
            override fun onDialogCallback(type: Int, id: String) {
                val json = JsonObject()
                json.addProperty("CollectionId",id)
                mCollectPresenter!!.cancelCollection(json)
            }

        })

        mBinding.swipeLayout.setOnRefreshListener {
            mCollectInfoBean.clear()
            mFinishCount = 1
            var json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            json.addProperty("Type",mFinishCount)
            mPresenter!!.getCollectionInfo(json,mBinding.swipeLayout)

        }

        mBinding.swipeLayout.autoRefresh()

        mBinding.recyclerView.setNewData(mList)
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val collectionFragment = CollectionFragment()
            collectionFragment.arguments = bundle
            fragment.start(collectionFragment)
        }
    }


    override fun doAfterAnim() {
    }

    var hasInit = false
    override fun onSupportVisible() {
        super.onSupportVisible()
        if (ismBindingInitialized() && hasInit) {
            mBinding.swipeLayout.autoRefresh()
        } else {
            hasInit = true
        }
    }
}