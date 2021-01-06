package com.jtcxw.glcxw.ui.my

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.databinding.FragmentAddressBinding
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment

class AddressFragment : BaseFragment<FragmentAddressBinding, CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_address
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val addressFragment = AddressFragment()
            addressFragment.arguments = bundle
            fragment.startForResult(addressFragment,11)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("地址")
        val tvRight = mBinding.root.findViewById<TextView>(R.id.tv_right)
        tvRight.text = "确定"
        tvRight.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        tvRight.setTextColor(resources.getColor(R.color.black_263238))
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f)
        mBinding.etAddress.setText(arguments!!.getString(BundleKeys.KEY_ADDRESS,""))
        tvRight.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(BundleKeys.KEY_ADDRESS,mBinding.etAddress.text.toString())
            setFragmentResult(ISupportFragment.RESULT_OK,bundle)
            pop()
        }
    }

    override fun doAfterAnim() {
    }
}