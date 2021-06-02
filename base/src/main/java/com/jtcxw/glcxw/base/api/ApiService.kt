package com.jtcxw.glcxw.base.api

import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.localmodels.PubKeyBean
import com.jtcxw.glcxw.base.respmodels.*
import models.BaseBean
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable


interface
ApiService {

    companion object{

        /**
         * 生产和测试环境
         */
        private val DEV = Environment.DEV_OUTER
        private val PRO = Environment.PRODUCT
        var API_SERVER_URL = DEV

    }

    //登录
    @POST("Login/UserLogin")
    fun login(@Body params: JsonObject): Observable<Response<BaseBean<UserInfoBean>>>

    //获取图片验证码
    @POST("Login/VerifyPicture")
    fun loginVerifyCode(@Body params: JsonObject): Observable<Response<BaseBean<PicVerifyCodeBean>>>

    //获取PublicKey
    @POST("Api/PublicKey")
    fun publicKey(): Observable<Response<PubKeyBean>>

    //微信登录
    @POST("Login/WechatLogin")
    fun wechatLogin(@Body params: JsonObject):Observable<Response<BaseBean<WechatBean>>>

    //发送短信
    @POST("SmsVerify/SendSmsCode")
    fun sendSmsCode(@Body params: JsonObject): Observable<Response<BaseBean<SmsBean>>>

    //短信注册
    @POST("SmsVerify/SmsRegister")
    fun smsRegister(@Body params: JsonObject): Observable<Response<BaseBean<RegisterBean>>>

    //微信注册
    @POST("SmsVerify/WechatRegister")
    fun smsWechatRegister(@Body params: JsonObject): Observable<Response<BaseBean<RegisterBean>>>

    /**
     * SmsVerify/VerifySmsCode
     * 校验验证码
     */
    @POST("SmsVerify/VerifySmsCode")
    fun verifySmsCode(@Body params: JsonObject): Observable<Response<BaseBean<VerifySmsBean>>>

    //更换密码
    @POST("Member/ChangePwd")
    fun changePwd(@Body params: JsonObject): Observable<Response<BaseBean<ResetPwdBean>>>

    //短信登录
    @POST("SmsVerify/SmsCodeLogin")
    fun smsCodeLogin(@Body params: JsonObject): Observable<Response<BaseBean<SmsLoginBean>>>

    //查询站点和路线
    @POST("BusInquiry/querySiteOrLineNew")
    fun querySiteOrLine(@Body params: JsonObject): Observable<Response<BaseBean<SiteOrLineBean>>>

    //获取个人信息
    @POST("Member/GetMemberInfo")
    fun getMemberInfo(@Body params: JsonObject): Observable<Response<BaseBean<UserInfoBean>>>

    //获取协议信息
    @POST("Member/GetMemberTreaty")
    fun getMemberTreaty(@Body params: JsonObject):  Observable<Response<BaseBean<AgreementBean>>>

    //酒店信息
    @POST("Hotel/GetHotelInfoList")
    fun getHotelInfoList(@Body params: JsonObject):  Observable<Response<BaseBean<HotelBean>>>

    //景点信息
    @POST("Scenic/GetScenicInfoList")
    fun getScenicInfoList(@Body params: JsonObject):  Observable<Response<BaseBean<ScenicBean>>>


    //出行巴士
    @POST("BusInquiry/AnnexBusNew")
    fun annexBus(@Body params: JsonObject):Observable<Response<BaseBean<AnnexBusBean>>>

    //线路详情
    @POST("BusInquiry/QueryLineDetail")
    fun queryLineDetail(@Body params: JsonObject):Observable<Response<BaseBean<LineDetailBean>>>

    //自定义巴士的城市列表
    @POST("CustomizedBus/GetCitys")
    fun getCitys(@Body params: JsonObject):Observable<Response<BaseBean<CityBean>>>

    //自定义巴士的城市线路
    @POST("CustomizedBus/GetLines")
    fun getLines(@Body params: JsonObject):Observable<Response<BaseBean<LineBean>>>

    //自定义巴士的线路详情
    @POST("CustomizedBus/GetFrequencys")
    fun getFrequencys(@Body params: JsonObject):Observable<Response<BaseBean<FrequencyBean>>>

    //余票信息
    @POST("CustomizedBus/GetRoundTikmodelList")
    fun getRoundTikmodelList(@Body params: JsonObject):Observable<Response<BaseBean<TicketBean>>>

    //下订单
    @POST("CustomizedBus/AddOrder")
    fun addOrder(@Body params: JsonObject):Observable<Response<BaseBean<AddOrderBean>>>

    //订单详情
    @POST("CustomizedBus/GetOrderDetail")
    fun getOrderDetail(@Body params: JsonObject):Observable<Response<BaseBean<OrderConfirmBean>>>

    //订单列表
    @POST("CustomizedBus/GetListByCustomer")
    fun getListByCustomer(@Body params: JsonObject):Observable<Response<BaseBean<OrderListBean>>>

    //取消订单
    @POST("CustomizedBus/OrderCancel")
    fun orderCancel(@Body params: JsonObject):Observable<Response<BaseBean<OrderCancelBean>>>

    //删除订单
    @POST("CustomizedBus/DeleteBusOrder")
    fun deleteBusOrder(@Body params: JsonObject):Observable<Response<BaseBean<OrderDelBean>>>

    //获取支付类型
    @POST("BasicData/GetPayType")
    fun getPayType(@Body params: JsonObject):Observable<Response<BaseBean<PayTypeBean>>>

    //支付
    @POST("CustomizedBus/Pay")
    fun pay(@Body params: JsonObject):Observable<Response<BaseBean<PayBean>>>

    //招募列表
    @POST("BusRecruitLine/GetListByCustomer")
    fun getRecruitLine(@Body params: JsonObject):Observable<Response<BaseBean<RecruitBean>>>

    //加入招募
    @POST("BusRecruitLine/JoinRecruit")
    fun joinRecruit(@Body params: JsonObject):Observable<Response<BaseBean<RecruitResultBean>>>

    //新增招募
    @POST("BusRecruitLine/Add")
    fun busRecruitLineAdd(@Body params: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //风景详情
    @POST("Scenic/GetScenicDetail")
    fun getScenicDetail(@Body params: JsonObject):Observable<Response<BaseBean<ScenicDetailBean>>>

    //检票
    @POST("CustomizedBus/TicketChecking")
    fun ticketChecking(@Body params: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //上传实名照片
    @POST("Member/AuthenticationImage")
    fun authenticationImage(@Body params: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //实名认真
    @POST("Member/Authentication")
    fun authentication(@Body params: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //获取车辆的到站信息
    @POST("BusInquiry/ForcastArriveQuery")
    fun forcastArriveQuery(@Body jsonObject: JsonObject):Observable<Response<BaseBean<BusArriveListBean>>>

    //赠票
    @POST("CustomizedBus/ComplimentaryTicket")
    fun complimentaryTicket(@Body jsonObject: JsonObject):Observable<Response<BaseBean<ComplimentaryTicketBean>>>

    //退票
    @POST("CustomizedBus/TicketRefund")
    fun ticketRefund(@Body jsonObject: JsonObject):Observable<Response<BaseBean<TurnBackBean>>>

    //酒店详情
    @POST("Hotel/GetHotelDetail")
    fun getHotelDetail(@Body jsonObject: JsonObject):Observable<Response<BaseBean<HotelDetailBean>>>

    //身份实名的id列表
    @POST("BasicData/GetIdTypeList")
    fun getIdTypeList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<IDTypeBean>>>

    //查询站点
    @POST("BusInquiry/QuerySite")
    fun querySite(@Body jsonObject: JsonObject):Observable<Response<BaseBean<SiteDataBean>>>

    //获取banner
    @POST("Banner/GetBanner")
    fun getBanner(@Body jsonObject: JsonObject):Observable<Response<BaseBean<BannerBean>>>

    //获取充值列表
    @POST("Product/GetGoodsInfo")
    fun getGoodsInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<GoodListBean>>>

    //开通二维码
    @POST("BusCard/OpenQRCode")
    fun openQRCode(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //获取二维码
    @POST("BusCard/GetQRCode")
    fun getQRCode(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //订单列表
    @POST("Member/GetMemberOrder")
    fun getMemberOrder(@Body jsonObject: JsonObject):Observable<Response<BaseBean<OrderMixBean>>>

    //获取订单类型
    @POST("BasicData/GetOrderTypeList")
    fun getOrderTypeList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    //充值
    @POST("Pay/Recharge")
    fun payRecharge(@Body jsonObject: JsonObject):Observable<Response<BaseBean<PayRechargeBean>>>

    //获取首页的pager配置信息
    @POST("BasicData/GetContentTypeList")
    fun getContentTypeList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    //首页pager内容
    @POST("Content/GetContentList")
    fun getContentList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<ContentListBean>>>

    //保存搜索历史
    @POST("BusInquiry/SaveQueryHistory")
    fun saveQueryHistory(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //搜索历史
    @POST("BusInquiry/ListQueryHistory")
    fun listQueryHistory(@Body jsonObject: JsonObject):Observable<Response<BaseBean<SiteOrLineBean>>>

    //删除搜索历史
    @POST("BusInquiry/ClearQueryHistory")
    fun clearQueryHistory(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //获取账户信息
    @POST("Member/GetAccountInfo")
    fun getAccountInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<GoodsInfoBean>>>

    //扫码帮助
    @POST("FQA/GetFqaHtml")
    fun getFQA(@Body jsonObject: JsonObject):Observable<Response<BaseBean<CommonFQABean>>>

    //获取电话信息
    @POST("BasicData/GetMisceAneous")
    fun getMisceAneous(@Body jsonObject: JsonObject):Observable<Response<BaseBean<MisceAneousBean>>>

    //消费记录
    @POST("Member/GetMemberAccountHistory")
    fun getMemberAccountHistory(@Body jsonObject: JsonObject):Observable<Response<BaseBean<MemberAccountHistoryBean>>>

    //获取个人使用的出行和订单信息
    @POST("Member/GetOrderStatistics")
    fun getOrderStatistics(@Body jsonObject: JsonObject):Observable<Response<BaseBean<ExtraOrderBean>>>

    //获取性别列表
    @POST("BasicData/GetSexList")
    fun getSexList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    //获取国家列表
    @POST("BasicData/GetCountryList")
    fun getCountryList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    //获取职业列表
    @POST("BasicData/GetOccupationList")
    fun getOccupationList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    //更改个人信息
    @POST("Member/ModifyMemberInfo")
    fun modifyMemberInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<UserInfoBean>>>

    //头像
    @POST("Member/HeadImage")
    fun headImage(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //支付宝绑定
    @POST("Pay/UseragreementPageSign")
    fun useragreementPageSign(@Body jsonObject: JsonObject):Observable<Response<BaseBean<AliSignBean>>>

    //支付宝解绑
    @POST("Pay/UseragreementUnSign")
    fun useragreementUnSign(@Body jsonObject: JsonObject):Observable<Response<BaseBean<AliUnSignBean>>>

    //支付宝授权结果查询
    @POST("Pay/UseragreementQuery")
    fun useragreementQuery(@Body jsonObject: JsonObject):Observable<Response<BaseBean<AliSignStatusBean>>>

    //获取默认支付方式
    @POST("Member/GetDefaultPayList")
    fun getDefaultPayList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<PayListBean>>>

    //配置默认支付方式
    @POST("Member/SetDefaultPayList")
    fun setDefaultPayList(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //客服回复
    @POST("Server/GetCusServerInfo")
    fun getCusServerInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<List<CusServerBean>>>>

    //版本检测
    @POST("System/AppVersion")
    fun appVersion(@Body jsonObject: JsonObject):Observable<Response<BaseBean<VersionBean>>>

    @POST("Server/InsertCusServer")
    fun insertCusServer(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //投诉类型
    @POST("BasicData/GetCusServerType")
    fun getCusServerType(@Body jsonObject: JsonObject):Observable<Response<BaseBean<DictionaryInfoBean>>>

    //添加关注
    @POST("Member/AddCollection")
    fun addCollection(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //取消关注
    @POST("Member/CancelCollection")
    fun cancelCollection(@Body jsonObject: JsonObject):Observable<Response<BaseBean<JsonObject>>>

    //关注列表信息
    @POST("Member/CollectionInfo")
    fun collectionInfo(@Body jsonObject: JsonObject):Observable<Response<BaseBean<CollectionInfoBean>>>

    //取消加入招募
    @POST("BusRecruitLine/CancelRejoinRecruit")
    fun cancelRejoinRecruit(@Body jsonObject: JsonObject):Observable<Response<BaseBean<RecruitResultBean>>>

    //h5配置信息
    @POST("System/H5ModuleConfig")
    fun h5ModuleConfig(@Body jsonObject: JsonObject):Observable<Response<BaseBean<ModuleConfigBean>>>

}