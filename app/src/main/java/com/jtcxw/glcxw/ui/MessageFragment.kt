package com.jtcxw.glcxw.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.MessageAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentMessageBinding
import com.jtcxw.glcxw.localbean.MessageBean
import com.jtcxw.glcxw.ui.my.AccountRecordFragment
import com.jtcxw.glcxw.ui.my.OrderDetailFragment
import com.jtcxw.glcxw.utils.DaoUtilsStore
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment

class MessageFragment:BaseFragment<FragmentMessageBinding,CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_message
    }

    var mList = ArrayList<MessageBean>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("消息中心")

        val tvRight = mBinding.root!!.findViewById<TextView>(R.id.tv_right)
        tvRight.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        tvRight.setTextColor(resources.getColor(R.color.black_263238))
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f)
        tvRight.setOnClickListener {

            if (mList.isEmpty()) {
                return@setOnClickListener
            }

            MaterialDialog(context!!)
                .title(null,"提示")
                .message(null,"是否全部标记为已读")
                .positiveButton(null, SpannelUtil.getSpannelStr("确定", context!!.resources.getColor(R.color.blue_3A75F3)),
                    object : DialogCallback {
                        override fun invoke(p1: MaterialDialog) {
                            mList.forEach {
                                if (it.read == 0) {
                                    it.read = 1
                                    DaoUtilsStore.getInstance().userDaoUtils.update(it)
                                }
                            }
                            mBinding.recyclerView.innerAdapter.notifyAllItems()
                        }
                    })
                .negativeButton(null, SpannelUtil.getSpannelStr("取消", context!!.resources.getColor(R.color.blue_3A75F3)),null)
                .lifecycleOwner(activity!!)
                .cornerRadius(DimensionUtil.dpToPx(2), null)
                .cancelable(true)
                .show()

        }

        mBinding.swipeLayout.setOnRefreshListener {
            var list = DaoUtilsStore.getInstance().userDaoUtils.queryAll()
            if (list == null) {
                list = ArrayList<MessageBean>()
            }
            mList.clear()
            list.forEach {
                if (it.phone == UserUtil.getUserInfoBean().realTelphoneNo) {
                    mList.add(it)
                }
            }
            if(mList.isNotEmpty()) {
                val temp = ArrayList<MessageBean>()
                mList.reversed().forEach {
                    temp.add(it)
                }
                mList = temp
                tvRight.visibility = View.VISIBLE
                tvRight.text = "全部已读"
            } else {
                tvRight.visibility = View.GONE
            }
            mBinding.recyclerView.setNewData(mList,false)
            mBinding.swipeLayout.finishRefresh(0)
        }

        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        val result =  ArrayList<MessageBean>()

        val adapter = MessageAdapter(context!!,result)
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<MessageBean>{
            override fun onItemClick(view: View?, model: MessageBean?, position: Int) {
                if (model!!.PushType == "2") {
                    model!!.read = 1
                    DaoUtilsStore.getInstance().userDaoUtils.update(model)
                    mBinding.recyclerView.innerAdapter.notifyAllItems()
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_ORDER_TYPE,model!!.MessageType)
                    bundle.putString(BundleKeys.KEY_BUSINESS_ID,model!!.BusinessId)
                    OrderDetailFragment.newInstance(this@MessageFragment,bundle)

                }
            }

            override fun onItemLongClick(view: View?, model: MessageBean?, position: Int) {

                MaterialDialog(context!!)
                    .title(null,"提示")
                    .negativeButton(null, SpannelUtil.getSpannelStr("已读", context!!.resources.getColor(R.color.blue_3A75F3)),
                        object : DialogCallback {
                            override fun invoke(p1: MaterialDialog) {
                                model!!.read = 1
                                DaoUtilsStore.getInstance().userDaoUtils.update(model)
                                mBinding.recyclerView.innerAdapter.notifyAllItems()
                            }
                        })
                    .positiveButton(null, SpannelUtil.getSpannelStr("删除", context!!.resources.getColor(R.color.blue_3A75F3)),
                        object : DialogCallback {
                            override fun invoke(p1: MaterialDialog) {
                                DaoUtilsStore.getInstance().userDaoUtils.delete(model)
                                adapter.data.remove(model)
                                mBinding.recyclerView.innerAdapter.notifyAllItems()
                            }
                        }
                    )
                    .lifecycleOwner(activity!!)
                    .cornerRadius(DimensionUtil.dpToPx(2), null)
                    .cancelable(true)
                    .show()
            }

        })
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.setNewData(result,false)

        mBinding.swipeLayout.autoRefresh()

    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val messageFragment = MessageFragment()
            messageFragment.arguments = bundle
            fragment.start(messageFragment, ISupportFragment.SINGLETOP)
        }
    }


    override fun doAfterAnim() {
    }
}