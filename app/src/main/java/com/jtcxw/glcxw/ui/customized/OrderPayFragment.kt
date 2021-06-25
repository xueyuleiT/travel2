package com.jtcxw.glcxw.ui.customized

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.alipay.sdk.app.PayTask
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.OrderPayAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.constant.Constant
import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.respmodels.*
import com.jtcxw.glcxw.base.utils.*
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentPayBinding
import com.jtcxw.glcxw.events.WxPayEvent
import com.jtcxw.glcxw.presenters.impl.MyPresenter
import com.jtcxw.glcxw.presenters.impl.OrderPayPresenter
import com.jtcxw.glcxw.ui.my.ChargeResultFragment
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.MyView
import com.jtcxw.glcxw.views.OrderPayView
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import rx.Subscription


class OrderPayFragment:BaseFragment<FragmentPayBinding,CommonModel>() ,OrderPayView,MyView{
    override fun onOrderStatisticsSucc(extraOrderBean: ExtraOrderBean) {
    }

    override fun onOrderStatisticsFinish() {
    }

    override fun onMemberInfoFailed(msg:String) {
        mDialog?.dismiss()
    }

    override fun onMemberInfoSucc(userInfoBean: UserInfoBean) {
        UserUtil.getUser().save(userInfoBean)
    }

    override fun onMemberInfoFinish() {
        var json = JsonObject()
        json.addProperty("PayType",arguments!!.getString(BundleKeys.KEY_PAY_TYPE))
        mPresenter!!.getPayType(json,mDialog)
    }

    override fun onPayRechargeSucc(
        payRechargeBean: PayRechargeBean,
        dialog: LoadingDialog
    ) {
        if (payRechargeBean.payment == 2) {
            aliPay(payRechargeBean.aliOrderInfo,dialog)
        } else if (payRechargeBean.payment == 4) {
            wxPay(payRechargeBean.weChatAPPResult)
        }
    }

    override fun onPaySucc(
        payBean: PayBean,
        dialog: LoadingDialog
    ) {
        if (payBean.payment == "2") {
            aliPay(payBean.aliOrderInfo, dialog)
        }  else if (payBean.payment == "4") {
            dialog.dismissAllowingStateLoss()
            wxPay(payBean.weChatOrderInfo)
        } else {
            dialog.dismiss()
            ToastUtil.toastSuccess("支付成功")
            val bundle = Bundle()
            UserUtil.getUserInfoBean().ownerAmount = UserUtil.getUserInfoBean().ownerAmount - arguments!!.getString(BundleKeys.KEY_ORDER_AMOUNT,"0.0").toDouble()
            setFragmentResult(ISupportFragment.RESULT_OK,bundle)
            pop()
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment,bundle: Bundle?) {
            val orderPayFragment = OrderPayFragment()
            orderPayFragment.arguments = bundle
            fragment.startForResult(orderPayFragment,10)
        }
    }

    // 获取后台配置的支付类型数据
    override fun onGetPayTypeSucc(payTypeBean: PayTypeBean) {
        mData!!.clear()
        mData.addAll(payTypeBean.typeArray)
        if (mData.size > 0) {
            val amount = arguments!!.getString(BundleKeys.KEY_ORDER_AMOUNT,"").toDouble()
            var hasChecked = false
            mData.forEach {
                if (amount > UserUtil.getUserInfoBean().ownerAmount) {
                    if (it.itemValue == "2") {
                        hasChecked = true;
                        it.isChecked = true
                    }
                }
            }

            if (!hasChecked) {
                mData[0].isChecked = true
            }
        }
        mBinding.recyclerView.setNewData(mData)
    }

    var mApi: IWXAPI?= null


    // 发起微信支付
    private fun wxPay(weChatAPPResult: PayRechargeBean.WeChatAPPResultBean) {
        if (!mApi!!.isWXAppInstalled) {
            ToastUtil.toastWaring("您的设备未安装微信客户端")
            return
        }
        val req = PayReq()
        req.appId = weChatAPPResult.appId
        req.partnerId = weChatAPPResult.partnerid
        req.prepayId = weChatAPPResult.prepayid
        req.packageValue = weChatAPPResult.`package`
        req.nonceStr = weChatAPPResult.noncestr
        req.timeStamp = weChatAPPResult.timestamp
        req.sign = weChatAPPResult.sign
        mApi!!.sendReq(req)
    }

    //微信支付
    private fun wxPay(weChatAPPResult: PayBean.WeChatOrderInfo) {
        if (!mApi!!.isWXAppInstalled) {
            ToastUtil.toastWaring("您的设备未安装微信客户端")
            return
        }
        val req = PayReq()
        req.appId = weChatAPPResult.appId
        req.partnerId = weChatAPPResult.partnerid
        req.prepayId = weChatAPPResult.prepayid
        req.packageValue = weChatAPPResult.`package`
        req.nonceStr = weChatAPPResult.noncestr
        req.timeStamp = weChatAPPResult.timestamp
        req.sign = weChatAPPResult.sign
        mApi!!.sendReq(req)
    }

    //支付宝支付
    private fun aliPay(orderId: String, dialog: LoadingDialog) {
        val payRunnable = Runnable {
            val payTask = PayTask(activity)
            val result = payTask.payV2(orderId, true)
            activity!!.runOnUiThread {
                dialog.dismiss()
                if (!TextUtils.isEmpty(result["resultStatus"]) && result["resultStatus"] == "9000") {
                    ToastUtil.toastSuccess("支付成功")
                    if (arguments!!.getString(BundleKeys.KEY_PAY_TYPE) == "glcx_paytype") {
                        val bundle = Bundle()
                        bundle.putString(BundleKeys.KEY_ORDER_AMOUNT,arguments!!.getString(BundleKeys.KEY_ORDER_AMOUNT,""))
                        bundle.putString(BundleKeys.KEY_PAY_TYPE,"支付宝支付")
                        bundle.putString("currentAmount",UserUtil.getUserInfoBean().ownerAmount.toString())
                        val chargeResultFragment = ChargeResultFragment()
                        chargeResultFragment.arguments = bundle
                        startWithPop(chargeResultFragment)
                    } else {
                        val bundle = Bundle()
                        setFragmentResult(ISupportFragment.RESULT_OK, bundle)
                        pop()
                    }
                } else {
                    if (!TextUtils.isEmpty(result["memo"])) {
                        ToastUtil.toastWaring(result["memo"])
                    }
                }
            }
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }


    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pay
    }

    var mPresenter:OrderPayPresenter?= null
    var mMyPresenter:MyPresenter?= null
    var mData = ArrayList<PayTypeBean.TypeArrayBean>()
    private val mRxList = ArrayList<Subscription>() //订阅列表

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("订单支付")

        //添加微信支付订阅
        mRxList.add(RxBus.getDefault().toObservable(WxPayEvent::class.java).subscribe {

            if (arguments!!.getString(BundleKeys.KEY_PAY_TYPE) == "glcx_paytype") {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_ORDER_AMOUNT,arguments!!.getString(BundleKeys.KEY_ORDER_AMOUNT,""))
                bundle.putString(BundleKeys.KEY_PAY_TYPE,"微信支付")
                bundle.putString("currentAmount",UserUtil.getUserInfoBean().ownerAmount.toString())
                val chargeResultFragment = ChargeResultFragment()
                chargeResultFragment.arguments = bundle
                startWithPop(chargeResultFragment)
            } else {
                val bundle = Bundle()
                setFragmentResult(ISupportFragment.RESULT_OK, bundle)
                pop()
            }

        })

        mApi = WXAPIFactory.createWXAPI(context, Constant.APP_ID_WE_CHAT, true)

        // 将应用的appId注册到微信
        mApi!!.registerApp(Constant.APP_ID_WE_CHAT)

        mPresenter = OrderPayPresenter(this)
        mMyPresenter = MyPresenter(this)

        mBinding.recyclerView.setSupportLoadNextPage(false)
        val adapter = OrderPayAdapter(context!!,mData)
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<PayTypeBean.TypeArrayBean>{
            override fun onItemClick(
                view: View?,
                model: PayTypeBean.TypeArrayBean?,
                position: Int
            ) {

                mData.forEach {
                    it.isChecked = false
                }
                model!!.isChecked = true
                mBinding.recyclerView.innerAdapter.notifyAllItems()
            }

            override fun onItemLongClick(
                view: View?,
                model: PayTypeBean.TypeArrayBean?,
                position: Int
            ) {
            }

        })
        mBinding.recyclerView.adapter = adapter

        mBinding.tvPay.setOnClickListener {
            var hasCheck = false
            mData.forEach {
                if (it.isChecked) {
                    hasCheck = true
                    if (TextUtils.isEmpty(arguments!!.getString(BundleKeys.KEY_ORDER_ID))) {
                        val json = JsonObject()
                        json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
                        json.addProperty("ProductId", arguments!!.getString(BundleKeys.KEY_PAY_PRODUCTID))
                        json.addProperty("Payment", it!!.itemValue)
                        json.addProperty("RechargeAmount",  arguments!!.getString(BundleKeys.KEY_ORDER_AMOUNT)!!.toDouble())
                        mPresenter!!.payRecharge(json)
                    } else {
                        val json = JsonObject()
                        json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
                        json.addProperty("OrderId", arguments!!.getString(BundleKeys.KEY_ORDER_ID))
                        json.addProperty("Payment", it!!.itemValue)
                        mPresenter!!.pay(json, DialogUtil.getLoadingDialog(fragmentManager))
                    }
                }
            }

            if (!hasCheck) {
                ToastUtil.toastWaring("请选择支付方式")
            }
        }
        val amount = arguments!!.getString(BundleKeys.KEY_ORDER_AMOUNT,"")
        val amountStr = "应付金额:¥$amount"
        mBinding.tvAmount.text = SpannelUtil.getSpannelStr(amountStr,resources.getColor(R.color.red_ff3737),amountStr.length - amount.length - 1,amountStr.length)
    }

    var mDialog:LoadingDialog? = null
    override fun doAfterAnim() {
        if (mDialog == null) {
            mDialog = DialogUtil.getLoadingDialog(fragmentManager)
        }
        var json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
        mMyPresenter!!.getMemberInfo(json)
    }

    override fun onDestroy() {
        super.onDestroy()
        mRxList.forEach {
            it.unsubscribe()
        }
        mRxList.clear()
    }
}