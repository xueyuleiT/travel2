package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.glcxw.lib.util.AmountUtil
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.GoodsInfoBean
import com.jtcxw.glcxw.databinding.FragmentOrderDetailBinding
import com.jtcxw.glcxw.presenters.impl.GoodsInfoPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.GoodsInfoView
import me.yokeyword.fragmentation.SupportFragment
// 订单详情页面
class OrderDetailFragment:BaseFragment<FragmentOrderDetailBinding,CommonModel>(),GoodsInfoView {
    override fun onGetGoodsInfoSucc(goodsInfoBean: GoodsInfoBean) {
        mGoodsInfoBean = goodsInfoBean
        when {
            goodsInfoBean.changeType == 1 -> {// 支付宝
                mBinding.vType.setImageResource(R.mipmap.icon_pay_ali)
                mBinding.tvTransAmount.setTextColor(resources.getColor(R.color.red_ff3737))
            }
            goodsInfoBean.changeType == 8 -> {// 微信
                mBinding.vType.setImageResource(R.mipmap.icon_pay_wx_recharge)
                mBinding.tvTransAmount.setTextColor(resources.getColor(R.color.red_ff3737))
            }

            goodsInfoBean.changeType == 7 -> { // 微信
                mBinding.vType.setImageResource(R.mipmap.icon_pay_wx_recharge)
                mBinding.tvTransAmount.setTextColor(resources.getColor(R.color.red_ff3737))
            }

            goodsInfoBean.changeType == 2 -> { // 出行
                mBinding.tvTransAmount.setTextColor(resources.getColor(R.color.red_ff3737))
                mBinding.vType.setImageResource(R.mipmap.icon_order_bus)
            }
            goodsInfoBean.changeType == 3 -> mBinding.vType.setImageResource(R.mipmap.icon_order_qr) // 出行
            goodsInfoBean.changeType == 4 -> mBinding.vType.setImageResource(R.mipmap.icon_order_bus) // 出行

            goodsInfoBean!!.changeType == 98 -> { // 老版本充值
                mBinding.tvTransAmount.setTextColor(mBinding.tvTransAmount.resources.getColor(R.color.red_ff3737))
                mBinding.vType.setImageResource(R.mipmap.icon_recharge_old)
            }

            goodsInfoBean!!.changeType == 99 -> { // 老版本消费
                mBinding.tvTransAmount.setTextColor(mBinding.tvTransAmount.resources.getColor(R.color.black_263238))
                mBinding.vType.setImageResource(R.mipmap.icon_consumer_old)
            }

            goodsInfoBean!!.changeType == 101 -> { // 老版本客服处理
                mBinding.tvTransAmount.setTextColor(mBinding.tvTransAmount.resources.getColor(R.color.black_263238))
                mBinding.vType.setImageResource(R.mipmap.icon_pay_kefu)
            }
        }

        when {
            goodsInfoBean.orderAmount == 0.0 ->  mBinding.tvTransAmount.text = AmountUtil.formatTosepara(goodsInfoBean.orderAmount.toString())
            goodsInfoBean.orderAmount > 0 ->  mBinding.tvTransAmount.text = "+" + AmountUtil.formatTosepara(goodsInfoBean.orderAmount.toString())
            else ->  mBinding.tvTransAmount.text = AmountUtil.formatTosepara(goodsInfoBean.orderAmount.toString())
        }

        mBinding.tvTransType.text = goodsInfoBean.title
        mBinding.tvTransStatus.text = goodsInfoBean.subTitle


        if (goodsInfoBean.names != null && goodsInfoBean.names.isNotEmpty()) {
            for (i in 0 until goodsInfoBean.names.size) {
                val contentView = LayoutInflater.from(context).inflate(R.layout.item_order_detail,null)
                val tvTitle = contentView.findViewById<TextView>(R.id.tv_title)
                val tvValue = contentView.findViewById<TextView>(R.id.tv_value)

                tvTitle.text = goodsInfoBean.names[i]
                tvValue.text = goodsInfoBean.values[i]

                if (i == goodsInfoBean.names.size - 1) {
                    val divider = contentView.findViewById<View>(R.id.v_divider)
                    divider.visibility = View.GONE
                }
                mBinding.llDetail.addView(contentView)

            }

        } else {
            mBinding.llDetail.visibility = View.GONE
        }



    }


    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?){
            val orderDetailFragment = OrderDetailFragment()
            orderDetailFragment.arguments = bundle
            fragment.start(orderDetailFragment)

        }
    }


    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_order_detail
    }
    var mGoodsInfoBean:GoodsInfoBean?= null
    var mPresenter: GoodsInfoPresenter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("交易详情")

        mPresenter = GoodsInfoPresenter(this)

    }

    // 获取页面数据
    override fun doAfterAnim() {
        val json = JsonObject()
        json.addProperty("BusinessId",arguments!!.getString(BundleKeys.KEY_BUSINESS_ID))
        json.addProperty("ChangeType",arguments!!.getString(BundleKeys.KEY_ORDER_TYPE)!!.toInt())
        mPresenter!!.getAccountInfo(json)
    }
}