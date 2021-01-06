package com.jtcxw.glcxw.views

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.respmodels.DictionaryInfoBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean

interface UserInfoView {
    fun onGetOccupationListSucc(list:List<DictionaryInfoBean.DictionaryBean>)
    fun onGetOccupationListFinish()
    fun onGetCountryListSucc(list:List<DictionaryInfoBean.DictionaryBean>)
    fun onGetCountryListFinish()
    fun onGetSexListSucc(list:List<DictionaryInfoBean.DictionaryBean>)
    fun onGetSexListFinish()
    fun onModifyMemberInfoSucc(userInfoBean: UserInfoBean)
    fun onHeadImage(jsonObject: JsonObject)
}