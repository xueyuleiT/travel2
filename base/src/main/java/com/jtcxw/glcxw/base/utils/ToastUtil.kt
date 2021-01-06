package com.jtcxw.glcxw.base.utils

import android.text.TextUtils
import com.jtcxw.glcxw.base.R
import es.dmoral.toasty.Toasty

class ToastUtil {
    companion object{
        fun toastSuccess(msg : String?){
            if (!TextUtils.isEmpty(msg)) {
                Toasty.success(BaseUtil.sTopAct!!, msg!!).show()
            }
        }

        fun toastSuccess(id : Int){
            Toasty.success(
                BaseUtil.sTopAct!!,
                BaseUtil.sTopAct!!.resources.getString(id)).show()
        }

        fun toastWaring(msg : String?){
            if (!TextUtils.isEmpty(msg)) {
                Toasty.warning(BaseUtil.sTopAct!!, msg!!).show()
            }
        }

        fun toastWarning(id : Int){
            Toasty.warning(
                BaseUtil.sTopAct!!,
                BaseUtil.sTopAct!!.resources.getString(id)).show()
        }

        fun toastError(msg : String?){
            if (!TextUtils.isEmpty(msg)) {
                Toasty.custom(BaseUtil.sTopAct!!,msg!!,null, BaseUtil.sTopAct!!.resources.getColor(R.color.gray_9),BaseUtil.sTopAct!!.resources.getColor(R.color.white),1000,false,true).show()
//                Toasty.error(BaseUtil.sTopAct!!, msg!!).show()
            }
        }


    }
}