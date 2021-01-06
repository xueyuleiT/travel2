package com.jtcxw.glcxw.base.basic

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.BaseObservable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.baidu.ocr.ui.camera.CameraActivity
import com.jtcxw.glcxw.base.R
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.StatusBarUtil
import kotlinx.android.synthetic.main.tool_bar.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.lang.reflect.ParameterizedType


open abstract class BaseFragment<VB : ViewDataBinding,VM : BaseObservable> : AbstractFragment(),View.OnClickListener {

    private var mCompositeSubscription: CompositeSubscription = CompositeSubscription()
    protected lateinit var mBinding: VB
    fun ismBindingInitialized() = ::mBinding.isInitialized
    var mRootView : View? = null
    var mViewModel :VM? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideKeyboard()
        if (Build.VERSION.SDK_INT >= 21) {
            val window = activity!!.window
            activity!!.window.navigationBarColor = navigationBarColor()
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        if (mRootView != null) {
            if (app_bar_layout != null){
                app_bar_layout?.setPadding(app_bar_layout!!.paddingLeft,
                    app_bar_layout!!.paddingTop + if (needSetStatusHeight()) StatusBarUtil.getStatusBarHeight(activity!!) else 0,
                    app_bar_layout!!.paddingRight,
                    app_bar_layout!!.paddingBottom)
                app_bar_layout.setBackgroundColor(resources.getColor(statusBarColor()))

            }else if (needSetStatusHeight()){
                mRootView?.setPadding(mRootView!!.paddingLeft,
                    mRootView!!.paddingTop + if (needSetStatusHeight()) StatusBarUtil.getStatusBarHeight(activity!!) else 0,
                    mRootView!!.paddingRight,
                    mRootView!!.paddingBottom)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (isBarStausDark()) {
            activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (isBarStausDark()) {
            activity!!.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            activity!!.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    open fun navigationBarColor():Int {
        return Color.TRANSPARENT
    }

    abstract fun getVariableId():Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mViewModel == null) {
            mViewModel = ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>).newInstance()
        }
        if (mRootView == null) {
            mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false)
            mBinding.setVariable(getVariableId(),mViewModel)
            mRootView = mBinding.root
        }

        if (mRootView!!.parent != null) {
            val parent = mRootView!!.parent as ViewGroup
            parent?.endViewTransition(mRootView)//主动调用清除动画
            parent?.removeView(mRootView)
        }


        return mRootView
    }

    open fun needSetStatusHeight():Boolean{
        return true
    }

    protected abstract fun getLayoutId(): Int


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userVisibleHint) {
            mHaveLoadData = true
            mRootView!!.postDelayed({

                if (!isDetached) {
                    doAfterAnim()
                }

            }, 600)
        }
    }

    open fun initToolBar(title : String){

        val toolBar = mBinding.root.findViewById<Toolbar>(R.id.tool_bar)
        if (toolBar != null && activity != null) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
            val tvTitle = mBinding.root.findViewById<TextView>(R.id.tv_center_title)
            toolBar!!.title = ""
            tvTitle!!.text = title
            tvTitle.setTextColor(resources.getColor(backColor()))
            toolBar.setBackgroundColor(resources.getColor(statusBarColor()))
            val upArrow = ContextCompat.getDrawable(context!!,
                R.drawable.abc_ic_ab_back_material
            )
            if(upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(context!!, backColor()), PorterDuff.Mode.SRC_ATOP)
                if( (activity as AppCompatActivity).supportActionBar != null) {
                    (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(upArrow)
                }
            }
        }

    }

    fun setTitle(title : String) {
        val toolBar = mBinding.root.findViewById<Toolbar>(R.id.tool_bar)
        if (toolBar != null && activity != null) {
            val tvTitle = mBinding.root.findViewById<TextView>(R.id.tv_center_title)
            tvTitle!!.text = title
        }

    }

    protected fun initToolBar(title : Int){
        val toolBar = mBinding.root.findViewById<Toolbar>(R.id.tool_bar)
        if (toolBar != null) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
            val tvTitle = mBinding.root.findViewById<TextView>(R.id.tv_title)
            toolBar!!.title = ""
            tvTitle!!.setText(title)
            tvTitle.setTextColor(resources.getColor(backColor()))

            val upArrow = ContextCompat.getDrawable(context!!,
                R.drawable.abc_ic_ab_back_material
            )
            if(upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(context!!, backColor()), PorterDuff.Mode.SRC_ATOP)
                if( (activity as AppCompatActivity).supportActionBar != null) {
                    (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(upArrow)
                }
            }
        }
    }


    open fun <M> addSubscription(observable: Observable<M>, subscriber: Subscriber<M>) {
        mCompositeSubscription!!.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber))
    }

    override fun onDestroy() {
        if (mCompositeSubscription!!.hasSubscriptions()) {
            //取消注册，以避免内存泄露
            mCompositeSubscription!!.unsubscribe()
        }
        hideSoftInput()
        super.onDestroy()
    }


    open fun isBarStausDark(): Boolean{
        return !BaseUtil.isDarkMode()
    }

    open fun statusBarColor(): Int{
        return R.color.back_white
    }

    /**
     * 隐藏软键盘
     */
    fun hideKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (imm != null && imm.isActive && activity!!.currentFocus != null) {
            if (activity!!.window != null) {
                imm.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    abstract fun doAfterAnim()

    override fun onClick(v: View?) {

    }


    open fun canBack(): Boolean{
        return true
    }

    open fun backColor():Int{
        return R.color.back
    }


    private var mHaveLoadData = false


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (!mHaveLoadData) {
            mHaveLoadData = true
            doAfterAnim()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            pop()
        }
        return super.onOptionsItemSelected(item)
    }


    open fun showConfirmDialog(title: String,content:String,ok:String,cancel:String,positiveCallback:DialogCallback,negativeCallback:DialogCallback?) {
        MaterialDialog(context!!)
            .title(null,title)
            .message(null, content)
            .negativeButton(null, SpannelUtil.getSpannelStr(cancel, context!!.resources.getColor(R.color.gray_9)),
                negativeCallback)
            .positiveButton(null, SpannelUtil.getSpannelStr(ok, context!!.resources.getColor(R.color.blue_3A75F3)),
                positiveCallback
            )
            .lifecycleOwner(activity!!)
            .cornerRadius(DimensionUtil.dpToPx(2), null)
            .cancelable(true)
            .show()
    }


}