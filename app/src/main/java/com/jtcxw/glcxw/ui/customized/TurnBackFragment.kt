package com.jtcxw.glcxw.ui.customized

import android.os.Bundle
import android.view.View
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.OrderConfirmBean
import com.jtcxw.glcxw.base.respmodels.TurnBackBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentTurnBackTicketBinding
import com.jtcxw.glcxw.presenters.impl.TurnBackTickPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.TurnBackTicketView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import java.text.SimpleDateFormat

class TurnBackFragment:BaseFragment<FragmentTurnBackTicketBinding,CommonModel>(),TurnBackTicketView {
    override fun onTicketRefund(turnBackBean: TurnBackBean) {
        setFragmentResult(ISupportFragment.RESULT_OK, Bundle())
        pop()
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val turnBackFragment = TurnBackFragment()
            turnBackFragment.arguments = bundle
            fragment.startForResult(turnBackFragment,11)
        }
    }
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_turn_back_ticket
    }
    var mOrder: OrderConfirmBean? = null
    var mPresenter:TurnBackTickPresenter?= null
    var returnAvaibleCount = 1
    var returnCount = 0
    var checkCount = 0
    var giveCount = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("退 票")

        mPresenter = TurnBackTickPresenter(this)

        mOrder = arguments!!.getParcelable(BundleKeys.KEY_ORDER)
        mBinding.tvTime.text = SimpleDateFormat("MM-dd HH:mm").format(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mOrder!!.ride_time))
        returnCount = 0
        giveCount = 0
        if (mOrder!!.passenger_info != null) {
            mOrder!!.passenger_info.forEach {
                if (it.tikcet_state == "T") {
                    returnCount ++
                }

                if (it.tikcet_state == "Y") {
                    checkCount ++
                }

                if (it.free_ticket == "1") {
                    giveCount ++
                }
            }
            mBinding.tvAll.text = mOrder!!.passenger_info.size.toString()
            returnAvaibleCount =  mOrder!!.passenger_info.size
        }
        mBinding.tvCheck.text = "检:$checkCount"
        mBinding.tvGive.text = "赠:$giveCount"
        mBinding.tvReturn.text = "退:$returnCount"

        if (returnAvaibleCount == 0) {
            mBinding.tvAll.text = "总:0"
        } else {
            mBinding.tvAll.text = "总:$returnAvaibleCount"
        }
        if (returnAvaibleCount > 0) {
            mBinding.ivPlus.setOnClickListener {
                var value = mBinding.tvReturnCount.text.toString().toInt()
                if (value >= returnAvaibleCount  - returnCount - checkCount) {
                    mBinding.tvReturnCount.text = (returnAvaibleCount  - returnCount - checkCount).toString()
                } else {
                    value++
                    mBinding.tvReturnCount.text = value.toString()
                }

            }

            mBinding.ivDecrease.setOnClickListener {
                var value = mBinding.tvReturnCount.text.toString().toInt()
                if (value <= 1) {
                    mBinding.tvReturnCount.text = "1"
                } else {
                    value--
                    mBinding.tvReturnCount.text = value.toString()
                }
            }

            mBinding.tvConfirm.setOnClickListener {
                if (mBinding.tvReturnCount.text.toString().toInt() > mOrder!!.passenger_info.size - returnCount - checkCount){
                    ToastUtil.toastWaring("可退票数不足"+mBinding.tvReturnCount.text.toString()+"张")
                    return@setOnClickListener
                }
                val json = JsonObject()
                json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
                json.addProperty("OrderId", mOrder!!.order_id)
                json.addProperty("RegbusTikCount", mBinding.tvReturnCount.text.toString().toInt())
                mPresenter!!.turnBackTicket(json)
            }
        } else {
            mBinding.tvConfirm.setOnClickListener {
                if (mBinding.tvReturnCount.text.toString().toInt() > mOrder!!.passenger_info.size - returnCount - checkCount){
                    ToastUtil.toastWaring("可退票数不足")
                    return@setOnClickListener
                }
            }
        }

    }

    override fun doAfterAnim() {
    }
}