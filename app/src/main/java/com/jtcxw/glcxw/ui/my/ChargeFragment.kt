package com.jtcxw.glcxw.ui.my

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.GoodListAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.*
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentChargeBinding
import com.jtcxw.glcxw.presenters.impl.ChargePresenter
import com.jtcxw.glcxw.presenters.impl.MyPresenter
import com.jtcxw.glcxw.ui.customized.OrderPayFragment
import com.jtcxw.glcxw.utils.DecimalInputTextWatcher
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.ChargeView
import com.jtcxw.glcxw.views.MyView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment

class ChargeFragment:BaseFragment<FragmentChargeBinding,CommonModel>() ,ChargeView,MyView{
    var mMax = 1000f
    override fun onGetMisceAneousBeanSucc(misceAneousBean: MisceAneousBean) {
        mMax = misceAneousBean.itemValue.toFloat()
    }

    override fun onOrderStatisticsSucc(extraOrderBean: ExtraOrderBean) {
    }

    override fun onOrderStatisticsFinish() {
    }

    override fun onMemberInfoFailed(msg:String) {
    }

    override fun onMemberInfoFinish() {
    }

    override fun onMemberInfoSucc(userInfoBean: UserInfoBean) {
        UserUtil.getUser().save(userInfoBean)
        if (ismBindingInitialized()) {
            mBinding.tvCash.text = UserUtil.getUserInfoBean().ownerAmount.toString()
        }

    }

    override fun onPayRechargeSucc(payRechargeBean: PayRechargeBean) {

    }

    override fun getVariableId(): Int {
        return BR.common
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?){
            val chargeFragment = ChargeFragment()
            chargeFragment.arguments = bundle
            fragment.startForResult(chargeFragment,99)

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_charge
    }
    var mAmount = 0.0
    var mPresenter: ChargePresenter? = null
    var mMyPresenter: MyPresenter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("钱包充值")
        val tvRight = mBinding.root.findViewById<TextView>(R.id.tv_right)
        tvRight.text = "明细"
        tvRight.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        tvRight.setTextColor(resources.getColor(R.color.black_263238))
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f)
        tvRight.setOnClickListener {
            AccountRecordFragment.newInstance(this,null)
        }


        mBinding.tvCash.text = UserUtil.getUserInfoBean().ownerAmount.toString()

        mBinding.etCash.addTextChangedListener(DecimalInputTextWatcher(mBinding.etCash,2))


        mMyPresenter = MyPresenter(this)

        mPresenter = ChargePresenter(this)
        mPresenter!!.getGoodsInfo()

        mBinding.tvConfirm.setOnClickListener {
            if (TextUtils.isEmpty(mBinding.etCash.text.toString())) {
                ToastUtil.toastWaring("请输入充值金额")
                return@setOnClickListener
            }

            if (mBinding.etCash.text.toString().toDouble() <= 0) {
                ToastUtil.toastWaring("请输入大于0的金额")
                return@setOnClickListener
            }

            if (mBinding.etCash.text.toString().toDouble() > mMax) {
                ToastUtil.toastWaring("充值金额不能大于${mMax}元")
                return@setOnClickListener
            }

            mAmount = mBinding.etCash.text.toString().toDouble()
            val bundle = Bundle()
            bundle.putString(BundleKeys.KEY_PAY_TYPE,"glcx_paytype")
            bundle.putString(BundleKeys.KEY_PAY_PRODUCTID,mSelf!!.id)
            bundle.putString(BundleKeys.KEY_ORDER_AMOUNT, mBinding.etCash.text.toString())
            OrderPayFragment.newInstance(this@ChargeFragment,bundle)

        }

        var json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
        mMyPresenter!!.getMemberInfo(json)

        json = JsonObject()
        json.addProperty("ItemType","5")
        json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
        mPresenter!!.getMisceAneous(json)

    }

    override fun doAfterAnim() {
    }

    var mList = ArrayList<GoodListBean.GoodsListBean>()

    var mSelf:GoodListBean.GoodsListBean ?= null
    override fun onGetGoodsInfoSucc(goodListBean: GoodListBean) {
        mList.clear()
        mList.addAll(goodListBean.goodsList)
        mBinding.recyclerView.layoutManager = GridLayoutManager(context,3)
       val adapter = GoodListAdapter(context!!,mList)
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<GoodListBean.GoodsListBean>{
            override fun onItemClick(
                view: View?,
                model: GoodListBean.GoodsListBean?,
                position: Int
            ) {
                mList.forEach {
                    it.isChecked = false
                }
                model!!.isChecked = true
                if (model.goodsType == "自主定价") {
                    mSelf = model
                    mBinding.llInput.visibility = View.VISIBLE
                } else {
                    mBinding.llInput.visibility = View.GONE
                    mAmount = model.price
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_PAY_TYPE,"glcx_paytype")
                    bundle.putString(BundleKeys.KEY_PAY_PRODUCTID,model.id)
                    bundle.putString(BundleKeys.KEY_ORDER_AMOUNT, model.price.toString())
                    OrderPayFragment.newInstance(this@ChargeFragment,bundle)
                }
                mBinding.recyclerView.adapter!!.notifyDataSetChanged()

            }

            override fun onItemLongClick(
                view: View?,
                model: GoodListBean.GoodsListBean?,
                position: Int
            ) {

            }

        })
        mBinding.recyclerView.adapter = adapter

    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if(requestCode == 10 && resultCode == ISupportFragment.RESULT_OK) {
//            val bundle = Bundle()
//            bundle.putString(BundleKeys.KEY_ORDER_AMOUNT,mAmount.toString())
//            bundle.putString("currentAmount",data!!.getString("currentAmount"))
//            bundle.putString(BundleKeys.KEY_PAY_TYPE,"支付宝支付")
//            ChargeResultFragment.newInstance(this,bundle)
        }
    }

    var hasInit = false
    override fun onSupportVisible() {
        super.onSupportVisible()
        if (ismBindingInitialized()) {
            mBinding.tvCash.text = UserUtil.getUserInfoBean().ownerAmount.toString()
        }

        if (!hasInit) {
            hasInit = true
        } else {
            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
            mMyPresenter!!.getMemberInfo(json)
        }
    }



}