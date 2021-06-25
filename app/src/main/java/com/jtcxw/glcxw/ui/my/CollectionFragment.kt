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
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.respmodels.CollectionInfoBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentCollectionBinding
import com.jtcxw.glcxw.listeners.CollectCancelCallback
import com.jtcxw.glcxw.listeners.StationClick
import com.jtcxw.glcxw.localbean.MyCollectionBean
import com.jtcxw.glcxw.presenters.impl.CollectionListPresenter
import com.jtcxw.glcxw.presenters.impl.CollectionPresenter
import com.jtcxw.glcxw.ui.QueryMainFragment
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
            if (mList.size > 0) {
                mTvRight!!.visibility = View.VISIBLE
            } else {
                mTvRight!!.visibility = View.GONE
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
    // 获取收藏的景点酒店线路和站点数据
    override fun onCollectionInfoSucc(collectInfoBean: CollectionInfoBean) {
        mCollectInfoBean.addAll(collectInfoBean.collectInfo)
    }

    // 查询站点信息数据成功
    override fun onQuerySiteSucc(
        s: List<AnnexBusBean.StationListBean>,
        stationId: String
    ) {
        val bundle = Bundle()
        s.forEach {
            it.stationLineInfo.forEach { it_ ->
                it_.isCollection = it_.collectionFlag
            }
        }
        bundle.putParcelableArrayList(BundleKeys.KEY_STATION_BEAN, ArrayList(s))
        bundle.putString(BundleKeys.KEY_STATION_ID,stationId)
        QueryMainFragment.newInstance(this@CollectionFragment as SupportFragment,bundle)
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
    var mTvRight:TextView?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("收藏")

        mTvRight = mBinding.root.findViewById<TextView>(R.id.tv_right)
        mTvRight!!.text = "编辑"
        mTvRight!!.visibility = View.GONE
        mTvRight!!.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        mTvRight!!.setTextColor(resources.getColor(R.color.green_light))
        mTvRight!!.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f)
        mTvRight!!.setOnClickListener {
            if (mList.isEmpty()) {
                return@setOnClickListener
            }
            mList.forEach {
                it.isEdit = !it.isEdit
            }

            if (mTvRight!!.text.toString() == "编辑") {
                mTvRight!!.text = "完成"
            } else {
                mTvRight!!.text = "编辑"
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
                // adapter内部点击取消收藏回调
                val json = JsonObject()
                json.addProperty("CollectionId",id)
                mCollectPresenter!!.cancelCollection(json)
            }

        },object :StationClick {
            override fun onStationClick(id: String) {
                // adapter内部点击站点回调
                val json = JsonObject()
                json.addProperty("Longitude",UserUtil.getUser().longitude)
                json.addProperty("Latitude",UserUtil.getUser().latitude)
                json.addProperty("StationId",id)
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                mPresenter!!.querySite(json,id)
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