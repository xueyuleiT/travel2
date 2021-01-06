package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.DictionaryInfoBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentComplaintBinding
import com.jtcxw.glcxw.dialog.KVTypeDialog
import com.jtcxw.glcxw.listeners.KVDialogCallback
import com.jtcxw.glcxw.localbean.KVBean
import com.jtcxw.glcxw.presenters.impl.ComplaintPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.ComplaintView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment

class ComplaintFragment:BaseFragment<FragmentComplaintBinding,CommonModel>(),ComplaintView {
    override fun onInsertCusServerSucc(jsonObject: JsonObject) {
        if (jsonObject.get("Status").asBoolean) {
            ToastUtil.toastSuccess("投诉提交成功")
            setFragmentResult(ISupportFragment.RESULT_OK, Bundle())
            pop()
        } else {
            ToastUtil.toastWaring("投诉提交失败")
        }
    }

    override fun onGetCusServerTypeSucc(dictionaryInfoBean: DictionaryInfoBean) {
        if (mList == null) {
            mList = ArrayList()
            dictionaryInfoBean.dictionaryInfo.forEach {
                val kvBean = KVBean(it.itemValue,it.itemName)
                mList!!.add(kvBean)
            }
            if (mList!!.size > 0) {
                mList!![0].checked = true
                mBinding.tvType.text = mList!![0].value
            }
        } else {
            mList!!.clear()
            dictionaryInfoBean.dictionaryInfo.forEach {
                val kvBean = KVBean(it.itemValue,it.itemName)
                mList!!.add(kvBean)
            }

            if (mList!!.size > 0) {
                mList!![0].checked = true
                mBinding.tvType.text = mList!![0].value
            }

            if (mList!!.size > 0) {
                mBinding.rlType.performClick()
            }
        }

    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_complaint
    }

    var mList:ArrayList<KVBean> ?= null
    var mPresenter:ComplaintPresenter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("客服中心")

        mPresenter = ComplaintPresenter(this)

        mPresenter!!.getCusServerType(JsonObject())

        mBinding.rlType.setOnClickListener(this)
        mBinding.tvConfirm.setOnClickListener(this)
        mBinding.tvCancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.tv_cancel -> {
                pop()
            }
            R.id.tv_confirm -> {
                if (TextUtils.isEmpty(mBinding.tvType.text.toString())) {
                    ToastUtil.toastWaring("请选择投诉类型")
                    return
                }

                if (mBinding.etInput.text.toString().length < 15) {
                    ToastUtil.toastWaring("投诉内容必须大于15字")
                    return
                }
                val json = JsonObject()
                mList!!.forEach {
                    if (it.checked) {
                        json.addProperty("CustomerServerType",it.key)
                    }
                }
                json.addProperty("Ask",mBinding.etInput.text.toString())
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                mPresenter!!.insertCusServer(json)
            }
            R.id.rl_type -> {
                if (mList == null || mList!!.isEmpty()) {
                    mList = ArrayList()
                    mPresenter!!.getCusServerType(JsonObject())
                    return
                }

                KVTypeDialog()
                    .setData(mList!!)
                    .setCallback(object : KVDialogCallback {
                        override fun onKVDialogCallback(i: Int) {
                            mBinding.tvType.text = mList!![i].value
                        }

                    }).show(fragmentManager!!,"KVTypeDialog")
            }
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val complaintFragment = ComplaintFragment()
            complaintFragment.arguments = bundle
            fragment.startForResult(complaintFragment,12)
        }
    }

    override fun doAfterAnim() {
    }
}