package com.jtcxw.glcxw.ui.customized

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentCustomizedCarBinding
import com.jtcxw.glcxw.presenters.impl.BusRecruitLineAddPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.BusRecruitLineAddView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import com.bigkoo.pickerview.TimePickerView
import com.jtcxw.glcxw.base.constant.BundleKeys
import java.text.SimpleDateFormat
import java.util.*


class CustomizedCarFragment:BaseFragment<FragmentCustomizedCarBinding,CommonModel>(),
    BusRecruitLineAddView {
    override fun onBusRecruitLineAddSucc(jsonObject: JsonObject) {
        ToastUtil.toastSuccess(jsonObject.get("ReturnMsg").asString)
        val bundle = Bundle()
        setFragmentResult(ISupportFragment.RESULT_OK,bundle)
        pop()
    }

    companion object {
        fun newInstance(fragment: SupportFragment,bundle: Bundle?) {
            val customizedCarFragment = CustomizedCarFragment()
            customizedCarFragment.arguments = bundle
            fragment.startForResult(customizedCarFragment,10)
        }
    }


    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_customized_car
    }

    var mPresenter: BusRecruitLineAddPresenter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("定制用车")

        mBinding.rlTime.setOnClickListener(this)
        mBinding.tvOk.setOnClickListener(this)

        mPresenter = BusRecruitLineAddPresenter(this)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.tv_ok -> {
                if (TextUtils.isEmpty(mBinding.etStart.text.toString())) {
                    ToastUtil.toastWaring("请输入出发地点")
                    return
                }

                if (TextUtils.isEmpty(mBinding.etEnd.text.toString())) {
                    ToastUtil.toastWaring("请输入到达地点")
                    return
                }

                if (TextUtils.isEmpty(mBinding.tvTime.text.toString())) {
                    ToastUtil.toastWaring("请选择出发时间  ")
                    return
                }


                val json = JsonObject()
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                json.addProperty("Time", mBinding.tvTime.text.toString())
                json.addProperty("RideDate",SimpleDateFormat("yyyy-MM-dd").format(Date()))
                json.addProperty("StartStationId",0)
                json.addProperty("EndStationId",0)
                json.addProperty("PlanId",arguments!!.getString(BundleKeys.KEY_PLANNING_ID))
                json.addProperty("StartStationName",mBinding.etStart.text.toString())
                json.addProperty("EndStationName",mBinding.etEnd.text.toString())
                mPresenter!!.busRecruitLineAdd(json,DialogUtil.getLoadingDialog(fragmentManager))
            }

            R.id.rl_time -> {
                hideSoftInput()
                val timePickerView =
                    TimePickerView.Builder(context!!,
                        TimePickerView.OnTimeSelectListener { date, v ->
                            val sdf = SimpleDateFormat("HH:mm")
                            mBinding.tvTime.text = sdf.format(date)
                        }).setType(booleanArrayOf(false, false, false, true, true, false))
                timePickerView.setDate(Calendar.getInstance())
                timePickerView.build().show()
            }
        }
    }

    override fun doAfterAnim() {
    }
}