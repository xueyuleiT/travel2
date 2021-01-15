package com.jtcxw.glcxw.api

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.Environment
import com.jtcxw.glcxw.base.localmodels.PubKeyBean
import com.jtcxw.glcxw.base.respmodels.*
import models.BaseBean
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable


interface ApiService {

    companion object{

        private val DEV = Environment.DEV_OUTER
        private val PRO = Environment.PRODUCT
        var API_SERVER_URL = DEV

    }

    @POST("Login/UserLogin")
    fun login(@Body params: JsonObject): Observable<Response<BaseBean<UserInfoBean>>>

    @POST("Login/VerifyPicture")
    fun loginVerifyCode(@Body params: JsonObject): Observable<Response<BaseBean<PicVerifyCodeBean>>>

    @POST("Api/PublicKey")
    fun publicKey(): Observable<Response<PubKeyBean>>

    @POST("SmsVerify/SendSmsCode")
    fun sendSmsCode(@Body params: JsonObject): Observable<Response<BaseBean<SmsBean>>>

    @POST("SmsVerify/SmsRegister")
    fun smsRegister(@Body params: JsonObject): Observable<Response<BaseBean<RegisterBean>>>

    /**
     * SmsVerify/VerifySmsCode
     */
    @POST("SmsVerify/VerifySmsCode")
    fun verifySmsCode(@Body params: JsonObject): Observable<Response<BaseBean<VerifySmsBean>>>

    @POST("Member/ChangePwd")
    fun changePwd(@Body params: JsonObject): Observable<Response<BaseBean<ResetPwdBean>>>

    @POST("SmsVerify/SmsCodeLogin")
    fun smsCodeLogin(@Body params: JsonObject): Observable<Response<BaseBean<SmsLoginBean>>>

    @POST("BusInquiry/querySiteOrLineNew")
    fun querySiteOrLine(@Body params: JsonObject): Observable<Response<BaseBean<SiteOrLineBean>>>

    @POST("Member/GetMemberInfo")
    fun getMemberInfo(@Body params: JsonObject): Observable<Response<BaseBean<UserInfoBean>>>

    @POST("Member/GetMemberTreaty")
    fun getMemberTreaty(@Body params: JsonObject):  Observable<Response<BaseBean<AgreementBean>>>

    @POST("Hotel/GetHotelInfoList")
    fun getHotelInfoList(@Body params: JsonObject):  Observable<Response<BaseBean<HotelBean>>>

    @POST("Scenic/GetScenicInfoList")
    fun getScenicInfoList(@Body params: JsonObject):  Observable<Response<BaseBean<ScenicBean>>>


    @POST("BusInquiry/AnnexBusNew")
    fun annexBus(@Body params: JsonObject):Observable<Response<BaseBean<AnnexBusBean>>>

    @POST("BusInquiry/QueryLineDetail")
    fun queryLineDetail(@Body params: JsonObject):Observable<Response<BaseBean<LineDetailBean>>>

    @POST("CustomizedBus/GetCitys")
    fun getCitys(@Body params: JsonObject):Observable<Response<BaseBean<CityBean>>>

    @POST("CustomizedBus/GetLines")
    fun getLines(@Body params: JsonObject):Observable<Response<BaseBean<LineBean>>>

    @POST("CustomizedBus/GetFrequencys")
    fun getFrequencys(@Body params: JsonObject):Observable<Response<BaseBean<FrequencyBean>>>

    @POST("CustomizedBus/GetRoundTikmodelList")
    fun getRoundTikmodelList(@Body params: JsonObject):Observable<Response<BaseBean<TicketBean>>>

    @POST("CustomizedBus/AddOrder")
    fun addOrder(@Body params: JsonObject):Observable<Response<BaseBean<AddOrderBean>>>

    @POST("CustomizedBus/GetOrderDetail")
    fun getOrderDetail(@Body params: JsonObject):Observable<Response<BaseBean<OrderConfirmBean>>>

    @POST("CustomizedBus/GetListByCustomer")
    fun getListByCustomer(@Body params: JsonObject):Observable<Response<BaseBean<OrderListBean>>>

    @POST("CustomizedBus/OrderCancel")
    fun orderCancel(@Body params: JsonObject):Observable<Response<BaseBean<OrderCancelBean>>>

    @POST("CustomizedBus/DeleteBusOrder")
    fun deleteBusOrder(@Body params: JsonObject):Observable<Response<BaseBean<OrderDelBean>>>

    @POST("BasicData/GetPayType")
    fun getPayType(@Body params: JsonObject):Observable<Response<BaseBean<PayTypeBean>>>

    @POST("CustomizedBus/Pay")
    fun pay(@Body params: JsonObject):Observable<Response<BaseBean<PayBean>>>

    @POST("BusRecruitLine/GetListByCustomer")
    fun getRecruitLine(@Body params: JsonObject):Observable<Response<BaseBean<RecruitBean>>>

    @POST("BusRecruitLine/JoinRecruit")
    fun joinRecruit(@Body params: JsonObject):Observable<Response<BaseBean<RecruitResultBean>>>

    @POST("BusRecruitLine/Add")
    fun busRecruitLineAdd(@Body params: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("Scenic/GetScenicDetail")
    fun getScenicDetail(@Body params: JsonObject):Observable<Response<BaseBean<ScenicDetailBean>>>

    @POST("CustomizedBus/TicketChecking")
    fun ticketChecking(@Body params: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("Member/AuthenticationImage")
    fun authenticationImage(@Body params: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("Member/Authentication")
    fun authentication(@Body params: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("BusInquiry/ForcastArriveQuery")
    fun forcastArriveQuery(@Body jsonObject: JsonObject):Observable<Response<BaseBean<BusArriveListBean>>>

    @POST("CustomizedBus/ComplimentaryTicket")
    fun complimentaryTicket(@Body jsonObject: JsonObject):Observable<Response<BaseBean<ComplimentaryTicketBean>>>

    @POST("CustomizedBus/TicketRefund")
    fun ticketRefund(@Body jsonObject: JsonObject):Observable<Response<BaseBean<TurnBackBean>>>

    @POST("Hotel/GetHotelDetail")
    fun getHotelDetail(@Body jsonObject: JsonObject):Observable<Response<BaseBean<HotelDetailBean>>>

    @POST("BasicData/GetIdTypeList")
    fun getIdTypeList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<IDTypeBean>>>

    @POST("Banner/GetBanner")
    fun getBanner(@Body jsonObject: JsonObject):Observable<Response<BaseBean<BannerBean>>>

    @POST("Product/GetGoodsInfo")
    fun getGoodsInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<GoodListBean>>>

    @POST("BusCard/OpenQRCode")
    fun openQRCode(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("BusCard/GetQRCode")
    fun getQRCode(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("Member/GetMemberOrder")
    fun getMemberOrder(@Body jsonObject: JsonObject):Observable<Response<BaseBean<OrderMixBean>>>

    @POST("BasicData/GetOrderTypeList")
    fun getOrderTypeList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    @POST("Pay/Recharge")
    fun payRecharge(@Body jsonObject: JsonObject):Observable<Response<BaseBean<PayRechargeBean>>>

    @POST("BasicData/GetContentTypeList")
    fun getContentTypeList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    @POST("Content/GetContentList")
    fun getContentList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<ContentListBean>>>

    @POST("BusInquiry/SaveQueryHistory")
    fun saveQueryHistory(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("BusInquiry/ListQueryHistory")
    fun listQueryHistory(@Body jsonObject: JsonObject):Observable<Response<BaseBean<SiteOrLineBean>>>

    @POST("BusInquiry/ClearQueryHistory")
    fun clearQueryHistory(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("Member/GetAccountInfo")
    fun getAccountInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<GoodsInfoBean>>>

    //扫码帮助
    @POST("FQA/GetFqaHtml")
    fun getFQA(@Body jsonObject: JsonObject):Observable<Response<BaseBean<CommonFQABean>>>

    @POST("BasicData/GetMisceAneous")
    fun getMisceAneous(@Body jsonObject: JsonObject):Observable<Response<BaseBean<MisceAneousBean>>>

    @POST("Member/GetMemberAccountHistory")
    fun getMemberAccountHistory(@Body jsonObject: JsonObject):Observable<Response<BaseBean<MemberAccountHistoryBean>>>

    @POST("Member/GetOrderStatistics")
    fun getOrderStatistics(@Body jsonObject: JsonObject):Observable<Response<BaseBean<ExtraOrderBean>>>

    @POST("BasicData/GetSexList")
    fun getSexList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    @POST("BasicData/GetCountryList")
    fun getCountryList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    @POST("BasicData/GetOccupationList")
    fun getOccupationList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    @POST("Member/ModifyMemberInfo")
    fun modifyMemberInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<UserInfoBean>>>

    @POST("Member/HeadImage")
    fun headImage(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("Pay/UseragreementPageSign")
    fun useragreementPageSign(@Body jsonObject: JsonObject):Observable<Response<BaseBean<AliSignBean>>>

    @POST("Pay/UseragreementUnSign")
    fun useragreementUnSign(@Body jsonObject: JsonObject):Observable<Response<BaseBean<AliUnSignBean>>>

    @POST("Pay/UseragreementQuery")
    fun useragreementQuery(@Body jsonObject: JsonObject):Observable<Response<BaseBean<AliSignStatusBean>>>


    @POST("Member/GetDefaultPayList")
    fun getDefaultPayList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<PayListBean>>>

    @POST("Member/SetDefaultPayList")
    fun setDefaultPayList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("Server/GetCusServerInfo")
    fun getCusServerInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<List<CusServerBean>>>>

    @POST("System/AppVersion")
    fun appVersion(@Body jsonObject: JsonObject):Observable<Response<BaseBean<VersionBean>>>

    @POST("Server/InsertCusServer")
    fun insertCusServer(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("BasicData/GetCusServerType")
    fun getCusServerType(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    @POST("Member/AddCollection")
    fun addCollection(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("Member/CancelCollection")
    fun cancelCollection(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    @POST("Member/CollectionInfo")
    fun collectionInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<CollectionInfoBean>>>

    @POST("BusRecruitLine/CancelRejoinRecruit")
    fun cancelRejoinRecruit(@Body jsonObject: JsonObject):Observable<Response<BaseBean<RecruitResultBean>>>

    @POST("System/H5ModuleConfig")
    fun h5ModuleConfig(@Body jsonObject: JsonObject):Observable<Response<BaseBean<ModuleConfigBean>>>

}