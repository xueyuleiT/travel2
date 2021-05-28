package com.jtcxw.glcxw.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jtcxw.glcxw.BuildConfig
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseActivity
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.utils.RxBus
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.events.MessageEvent
import com.jtcxw.glcxw.fragment.MainFragment
import com.jtcxw.glcxw.localbean.MessageBean
import com.jtcxw.glcxw.ui.my.OrderDetailFragment
import com.jtcxw.glcxw.utils.DaoUtilsStore
import com.zss.cardview.CardView
import me.yokeyword.fragmentation.Fragmentation
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {
    var mCardView:CardView?= null
    var mRunnable = Runnable {
        mCardView!!.visibility = View.GONE
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fragmentation.builder()
            .debug(BuildConfig.DEBUG)
            .stackViewMode(Fragmentation.BUBBLE)
            .install()

        loadRootFragment(R.id.host_fragment, MainFragment())
        mCardView = findViewById(R.id.cd_view)
        fragmentAnimator = DefaultHorizontalAnimator()


    }

    fun showNotifycation(messageEvent: MessageBean) {
        runOnUiThread {
            mCardView!!.visibility = View.VISIBLE
            val tvType = mCardView!!.findViewById<TextView>(R.id.tv_type)
            val tvTime = mCardView!!.findViewById<TextView>(R.id.tv_time)
            val tvDetail = mCardView!!.findViewById<TextView>(R.id.tv_detail)
            val vType = mCardView!!.findViewById<ImageView>(R.id.v_type)

            tvType.text = messageEvent.title
            tvTime.text = messageEvent.time
            tvDetail.text = messageEvent.detail
            mCardView!!.removeCallbacks(mRunnable)
            mCardView!!.postDelayed(mRunnable,5000)
            mCardView!!.setOnClickListener {
                mCardView!!.removeCallbacks(mRunnable)
                mCardView!!.visibility = View.GONE

                if (messageEvent.pushType == "2") {
                    messageEvent.read = 1
                    DaoUtilsStore.getInstance().userDaoUtils.update(messageEvent)
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_ORDER_TYPE,messageEvent.MessageType)
                    bundle.putString(BundleKeys.KEY_BUSINESS_ID,messageEvent.BusinessId)
                    OrderDetailFragment.newInstance(topFragment!! as SupportFragment,bundle)
                } else {
                    MessageFragment.newInstance(topFragment as SupportFragment,null)
                }
            }

            RxBus.getDefault().post(MessageEvent())
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent != null && intent.hasExtra("type") && intent.getStringExtra("type") == "message") {
            if (intent.getStringExtra("pushType") == "2") {
                val businessId = intent.getStringExtra("businessId")
                var list = DaoUtilsStore.getInstance().userDaoUtils.queryAll()
                var model:MessageBean?= null
                run label@ {
                    list.forEach {
                        if (it.businessId == businessId) {
                            model = it
                            return@label
                        }
                    }
                }
                model!!.read = 1
                DaoUtilsStore.getInstance().userDaoUtils.update(model)
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_ORDER_TYPE,model!!.MessageType)
                bundle.putString(BundleKeys.KEY_BUSINESS_ID,model!!.BusinessId)
                OrderDetailFragment.newInstance(topFragment!! as SupportFragment,bundle)
            } else {
                MessageFragment.newInstance(topFragment as SupportFragment,null)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==android.R.id.home){
            return (topFragment as SupportFragment).onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private var mLastTime = 0L

    override fun onBackPressedSupport() {
        if(supportFragmentManager.backStackEntryCount > 1){
            if(topFragment is QueryMainFragment) {
                topFragment.onBackPressedSupport()
                return
            }
            if ((topFragment as BaseFragment<*, *>).canBack()){
                pop()
            }
        }else {
            if (System.currentTimeMillis() - mLastTime < 2000 ){
                exitProcess(0)
            }else{
                ToastUtil.toastWaring("再点一次退出" )
                mLastTime = System.currentTimeMillis()
                return
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        (topFragment as SupportFragment).onActivityResult(requestCode,resultCode,data)
    }
}
