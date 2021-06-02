# 项目结构
*1.app          项目主体
*2.base         项目的核心，基类，网络请求，公共方法等
*3.banner       banner组建
*4.cardview     根据谷歌的组建重新修改后的cardview，可自定义颜色
*5.lib          一些公用的工具类
*6.ocr_ui       身份证拍照上传用到的包
*7.weekcalendar 日历组建
*8.router       跨模块通讯

# 项目架构
* 模式：采用MVP + databinding + viewmodel
* 页面搭建：采用单Activity + 多Fragment
* 消息通知：采用RXjava订阅
* 网络请求：采用RXjava + retrofit + okhttp
* 模块通讯：router全局注册 + 多模块引用实现模块间的消息通讯
* 图片加载：Glide
* 数据库：GreenDao

# 基类Fragment结构
> UIFragment 应用类
>> LocationFragmeng 定位基类
>>> AbstractFragment/BaseFragment 抽象基类

# 主要代码文件
> MyApplication 入口
>> MainActivity/MainFragment 主界面
>>> BusMapFragment 巴士地图
>>>> CustomizedBusFragment 定制巴士
>>>>> BusLineGoOrderFragment 定制巴士购票页面
>>>>>> OrderConfirmFragment 订单详情页面 包括下单 取消 和订单状态
>>>>>>> OrderPayFragment 订单支付页面
>>>>>>>> QRFragment 扫码乘车
>>>>>>>>> ChargeFragment 充值
>>>>>>>>>> MessageFragment 消息通知
>>>>>>>>>>> OrderListFragment 订单历史列表
>>>>>>>>>>>> VersionFragment 版本更新

# 配置代码
*编译相关配置
```
   applicationId = "cn.com.jttravel"
   compileSdkVersion = 29
   buildToolsVersion = "29.0.2"
   minSdkVersion = 17
   targetSdkVersion = 29
   versionCode = 60004
   versionName = "6.0.4"
```

*SDK和环境配置
```
    MyApplication {
    ...
    CrashReport.initCrashReport(applicationContext, "fe64931278", BuildConfig.DEBUG)//bugly异常
    JPushInterface.setDebugMode(BuildConfig.DEBUG)
    JPushInterface.init(this)//推送
    RouterUtil.add(AppRouter())//添加router，主要用于跨module通讯
    QbSdk.initX5Environment(this,cb) //X5内核
    CacheUtil.init(this) //缓存初始化
    ...
 }
```
*网络环境配置
```
    ApiService {
    ...
    companion object{

        private val DEV = Environment.DEV_OUTER
        private val PRO = Environment.PRODUCT
        var API_SERVER_URL = DEV

    }
    ...
 }
```



# 三方引用
[RxAndroid](https://github.com/ReactiveX/RxAndroid)
[retrofit网络请求](https://github.com/square/retrofit)
[okhttp](https://github.com/square/okhttp)
[FragmentationX框架](https://github.com/Liam6666/FragmentationX)
[glide图片加载](https://github.com/bumptech/glide)
[Toasty吐司](https://github.com/GrenderG/Toasty)
[umsdk推送相关](https://developer.umeng.com/docs?spm=a213m0.22488127.0.0.3cb275efDHnqGR)
[DSBridge网页桥接](https://github.com/wendux/DSBridge-Android)
[greendao数据库](https://github.com/greenrobot/greenDAO)
[flycoTabLayout](https://github.com/H07000223/FlycoTabLayout)
[Bugly异常上报](https://bugly.qq.com/docs/user-guide/instruction-manual-android/?v=20200622202242)
















