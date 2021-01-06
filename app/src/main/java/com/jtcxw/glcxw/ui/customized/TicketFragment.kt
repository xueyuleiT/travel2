package com.jtcxw.glcxw.ui.customized

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.OrderConfirmBean
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentTicketBinding
import com.jtcxw.glcxw.presenters.impl.TicketPresenter
import com.jtcxw.glcxw.utils.QRCodeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.TicketView

class TicketFragment:BaseFragment<FragmentTicketBinding,CommonModel>,TicketView {
    override fun onTicketCheckingSucc(jsonObject: JsonObject) {
        ToastUtil.toastSuccess("验票成功")
//        mBinding.tvStatus.text = "已乘车"
//        mBinding.tvStatus.visibility = View.VISIBLE
//        mBinding.vQr.visibility = View.GONE
//        mBinding.tvCheck.visibility = View.GONE
        (parentFragment as OrderConfirmFragment).refresh()
    }
    var mIndex = 1
    var mTikmodelId = 0L
    var mTakeNum = 1
    var mPassengerInfoBean:OrderConfirmBean.PassengerInfoBean? =null
    constructor(i: Int){
        mIndex = i
    }


    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ticket
    }

    var mPresenter:TicketPresenter?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = TicketPresenter(this)
        mBinding.llLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mBinding.llLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mBinding.llLayout.setPadding(0,0,0,0)
            }

        })
        refresh()
    }

    fun refresh() {
        if (parentFragment == null) {
            return
        }
        mTikmodelId = (parentFragment as OrderConfirmFragment).getTikmodelId()
        mTakeNum = (parentFragment as OrderConfirmFragment).getTakeNum()
        mPassengerInfoBean = (parentFragment as OrderConfirmFragment).getPassengerInfo(mIndex)

//        mBinding.tvStartTime.text = mPassengerInfoBean!!.check_time
        mBinding.tvSeatNo.text = mPassengerInfoBean!!.seat_no
        mBinding.tvTicketNo.text = mPassengerInfoBean!!.ticket_no


        mBinding.tvTakeNum.text = "当前订单有${mTakeNum}位乘客"
        if (mPassengerInfoBean!!.tikcet_state == "N") {//未检票
            mBinding.vQr.visibility = View.VISIBLE
            mBinding.tvStatus.visibility = View.GONE
            mBinding.tvCheck.visibility = View.VISIBLE
            mBinding.tvCheck.setOnClickListener{
                MaterialDialog(context!!)
                    .title(null, "检票确认")
                    .message(null, "确定要检票(" + mPassengerInfoBean!!.ticket_no + ")吗?")
                    .positiveButton(null, SpannelUtil.getSpannelStr("确定", context!!.resources.getColor(R.color.blue_3A75F3)),
                        object :DialogCallback{
                            override fun invoke(p1: MaterialDialog) {
                                val json = JsonObject()
                                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                                json.addProperty("TikmId",mTikmodelId)
                                json.addProperty("TikNo",mPassengerInfoBean!!.ticket_no)
                                mPresenter!!.ticketChecking(json)
                            }
                        })
                    .negativeButton(null,
                        SpannelUtil.getSpannelStr("取消", context!!.resources.getColor(R.color.blue_3A75F3)), null)
                    .lifecycleOwner(activity)
                    .cornerRadius(DimensionUtil.dpToPx(2), null)
                    .cancelable(true)
                    .show()

            }

            mBinding.vQr.setImageBitmap(QRCodeUtil.createQRCodeBitmap(mPassengerInfoBean!!.ticket_no,
                DimensionUtil.dpToPx(200).toInt(), DimensionUtil.dpToPx(200).toInt())
            )
        } else if (mPassengerInfoBean!!.tikcet_state == "Y") {
            mBinding.tvStatus.text = "已乘车"
            mBinding.tvStatus.visibility = View.VISIBLE
            mBinding.vQr.visibility = View.GONE
            mBinding.tvCheck.visibility = View.GONE
        } else {
            mBinding.tvStatus.text = "已退票"
            mBinding.tvStatus.visibility = View.VISIBLE
            mBinding.vQr.visibility = View.GONE
            mBinding.tvCheck.visibility = View.GONE
        }
    }

    override fun doAfterAnim() {
    }
}