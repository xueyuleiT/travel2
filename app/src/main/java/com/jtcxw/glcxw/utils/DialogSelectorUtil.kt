package com.jtcxw.glcxw.utils

import android.content.Context
import com.bigkoo.pickerview.OptionsPickerView
import com.bigkoo.pickerview.TimePickerView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.listeners.CityCallback
import com.jtcxw.glcxw.localbean.ProvinceBean

class DialogSelectorUtil {
    companion object{

        fun showCityPickView(context: Context,provinceBeanList: ArrayList<ProvinceBean>,cityList:  ArrayList<ArrayList<String>>,cityListId:  ArrayList<ArrayList<Int>>, cityCallback: CityCallback){

            val pvOptions = OptionsPickerView.Builder(context, OptionsPickerView.OnOptionsSelectListener { options1, options2, _, _ ->
                run {
                    try {
                        val address = (provinceBeanList[options1].pickerViewText + " " + cityList[options1][options2])
                        cityCallback.onCityCallback(provinceBeanList[options1].getsId(),cityListId[options1][options2],cityList[options1][options2],address)
                    } catch (e: Exception) {
                    }

                }
            })
                .setTitleText("选择城市")
                .setSubmitColor(BaseUtil.sTopAct!!.resources.getColor(R.color.blue_3a75f3))
                .setCancelColor(BaseUtil.sTopAct!!.resources.getColor(R.color.blue_3a75f3))
                .setDividerColor(BaseUtil.sTopAct!!.resources.getColor(R.color.gray_6))
                .setTextColorCenter(BaseUtil.sTopAct!!.resources.getColor(R.color.black_263238))
                .setContentTextSize(16)
                .setOutSideCancelable(true)
                .build()
            pvOptions.setPicker(provinceBeanList, cityList)
            pvOptions.show()

        }

    }
}