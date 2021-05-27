package cn.com.jttravel.wxapi

import android.content.Intent
import android.os.Bundle
import com.jtcxw.glcxw.base.basic.BaseActivity
import com.jtcxw.glcxw.base.constant.Constant
import com.jtcxw.glcxw.base.utils.RxBus
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.events.WxPayEvent
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WXPayEntryActivity : BaseActivity(), IWXAPIEventHandler {
    override fun onResp(p0: BaseResp?) {

        if(p0!!.type == ConstantsAPI.COMMAND_PAY_BY_WX){
            if (p0.errCode == 0) {
                ToastUtil.toastSuccess("支付完成")
                RxBus.getDefault().post(WxPayEvent())
            } else {
                ToastUtil.toastError(p0.errStr)
            }
            finish()
        }
    }


    override fun onReq(p0: BaseReq?) {
    }


    var wxAPI: IWXAPI?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wxAPI = WXAPIFactory.createWXAPI(this, Constant.APP_ID_WE_CHAT,false);
        wxAPI!!.registerApp(Constant.APP_ID_WE_CHAT);
        wxAPI!!.handleIntent(intent, this)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        wxAPI!!.handleIntent(getIntent(), this)
    }
}