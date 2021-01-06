package com.jtcxw.glcxw.ui.my

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.glcxw.lib.util.RuleUtil
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.databinding.FragmentNicknameBinding
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment

class NickNameFragment:BaseFragment<FragmentNicknameBinding,CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_nickname
    }

    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?) {
            val nickNameFragment = NickNameFragment()
            nickNameFragment.arguments = bundle
            fragment.startForResult(nickNameFragment,10)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("昵称")
        val tvRight = mBinding.root.findViewById<TextView>(R.id.tv_right)
        tvRight.text = "确定"
        tvRight.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        tvRight.setTextColor(resources.getColor(R.color.black_263238))
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f)

        mBinding.etNick.setText(arguments!!.getString(BundleKeys.KEY_NICK,""))
        tvRight.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(BundleKeys.KEY_NICK,mBinding.etNick.text.toString())
            setFragmentResult(ISupportFragment.RESULT_OK,bundle)
            pop()
        }
    }

    override fun doAfterAnim() {
    }
}