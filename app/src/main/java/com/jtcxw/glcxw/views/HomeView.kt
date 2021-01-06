package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.DictionaryInfoBean
import com.jtcxw.glcxw.base.respmodels.ModuleConfigBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean

interface HomeView: HotelView,ScenicView,BannerView{
    fun onGetContentTypeListSucc(dictionaryInfoBean: DictionaryInfoBean)
    fun onGetContentTypeListFinish()

    fun onMemberInfoSucc(userInfoBean: UserInfoBean)
    fun onMemberInfoFailed(msg:String)
    fun onMemberInfoFinish()

    fun onModuleConfigSucc(moduleConfigBean: ModuleConfigBean)

}