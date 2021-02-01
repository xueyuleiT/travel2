package com.jtcxw.glcxw.ui

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.glcxw.lib.util.FileProvider7
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.BuildConfig
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.databinding.FragmentWebBinding
import com.jtcxw.glcxw.utils.AlipayUtil
import com.jtcxw.glcxw.utils.DownloadTask
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.lakala.wtb.listener.HandleCallBack
import com.tencent.smtt.sdk.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import wendu.dsbridge.DWebView
import java.io.File


class WebFragment : BaseFragment<FragmentWebBinding, CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun doAfterAnim() {
//        mBinding.root.findViewById<Toolbar>(R.id.tool_bar).setNavigationOnClickListener {
//            if (mBinding.webview.canGoBack()){
//                mBinding.webview.goBack()
//            }else{
//                pop()
//            }
//        }

        if (mUrl!!.contains("?")) {
            val url = mUrl
            mBinding.webview.loadUrl(url)
        } else {
            val url = (mUrl)
            mBinding.webview.loadUrl(url)
        }

    }

    companion object {
        fun newInstance(from: SupportFragment, bundle: Bundle) {
            val fragment = WebFragment()
            fragment.arguments = bundle
            from.start(fragment,ISupportFragment.SINGLETOP)
        }
    }

    private var mUrl: String? = null
    private var mTitle: String? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_web
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebViewSettings()
        val ivClose = mBinding.root.findViewById<ImageView>(R.id.iv_close)
        ivClose.visibility = View.VISIBLE
        ivClose.setOnClickListener {
            pop()
        }

        mFileUri = File(Environment.getExternalStorageDirectory(), "web_upload.png")
        if (!mFileUri!!.exists()){
            mFileUri!!.parentFile.mkdir()
        }

        mFileVideoUri = File(Environment.getExternalStorageDirectory(), "web_upload_video.mp4")
        if (!mFileVideoUri!!.exists()){
            mFileVideoUri!!.parentFile.mkdir()
        }

        mTitle = arguments?.getString(BundleKeys.KEY_WEB_TITLE, "")
        mUrl = arguments?.getString(BundleKeys.KEY_WEB_URL, "")
        mBinding.webview!!.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(p0: WebView?, p1: String?) {
                super.onReceivedTitle(p0, p1)
                if (!p1!!.startsWith("http")) {
                    mBinding.root.findViewById<TextView>(R.id.tv_center_title).text = p1
                }
            }

            override fun onProgressChanged(p0: WebView?, newProgress: Int) {
                super.onProgressChanged(p0, newProgress)
                if (newProgress >= 100) {
                    mBinding.progressBar!!.visibility = View.GONE
                } else {
                    mBinding.progressBar!!.progress = newProgress
                }
            }

            //For Android  >= 4.1
            override fun openFileChooser(
                valueCallback: ValueCallback<Uri>,
                acceptType: String,
                capture: String
            ) {
                onOpenFileChooser(valueCallback,acceptType)
            }

            // For Android >= 5.0
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                showFileChooser(filePathCallback,fileChooserParams)
                return true
            }

        }

        mBinding.webview!!.setDownloadListener { p0, p1, p2, p3, p4 ->
            val fileName = URLUtil.guessFileName(p0, p2, p3)

            DownloadTask(object : HandleCallBack {
                override fun onHandle(s: String) {
                    if (!TextUtils.isEmpty(s))
                        if (s == "true") {
                            mBinding.webview.loadUrl("file:" + context!!.filesDir.absolutePath + File.separator + fileName)
                        } else {
                            ToastUtil.toastError(s)
                        }
                }

            }).execute(p0, context!!.filesDir.absolutePath + File.separator + fileName)
        }

        mBinding.webview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                p0: WebView?,
                p1: String?
            ): Boolean {
                if (AlipayUtil.isAlipay( p1 )){
                    AlipayUtil.goAlipays( activity, p1 );
                    return true
                }
                if (p1!!.startsWith("tel:")) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    val data = Uri.parse(p1)
                    intent.data = data
                    startActivity(intent)
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
        DWebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        initToolBar(mTitle!!)

    }



    private fun initWebViewSettings() {

        val webSetting = mBinding.webview.settings

        webSetting.allowContentAccess = true
        webSetting.javaScriptEnabled = true
        webSetting.defaultTextEncodingName = "UTF-8"
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        webSetting.domStorageEnabled = true
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE

        // settings 的设计
        webSetting.setAppCachePath(context!!.getDir("appcache", 0).path)
        webSetting.databasePath = context!!.getDir("databases", 0).path
        webSetting.setGeolocationDatabasePath(context!!.getDir("geolocation", 0).path)
        webSetting.loadWithOverviewMode = true
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding.webview.webChromeClient = null
        mBinding.webview.webViewClient = null
        mBinding.webview.settings.javaScriptEnabled = false
        mBinding.webview.clearCache(true)
        mBinding.webview.clearHistory()
        mBinding.webview.removeAllViews()
        mBinding.webview.clearFormData()

    }

    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mUploadMessageAboveL: ValueCallback<Array<Uri>>? = null
    private var mFileUri: File? = null
    private var mFileVideoUri: File? = null
    private var mImageUri: Uri? = null
    private var mVideoUri: Uri? = null
    private val PHOTO_REQUEST = 100
    private val VIDEO_REQUEST = 101
    private val FILE_CHOOSER_RESULT_CODE = 10000

    private fun showSelector(type: String) {
        val cusView = LayoutInflater.from(activity).inflate(R.layout.dialog_photo_layout, null)
        val tvMessage = cusView.findViewById<TextView>(R.id.tv_message)
        val tvPhoto = cusView.findViewById<TextView>(R.id.tv_photo)
        val tvVideo = cusView.findViewById<TextView>(R.id.tv_video)
        var tvFile = cusView.findViewById<TextView>(R.id.tv_file)
        tvMessage.text = "请选择方式"

        val dialog = MaterialDialog(context!!)
            .customView(null, cusView!!)
            .cornerRadius(DimensionUtil.dpToPx(2), null)
            .cancelable(false)
        dialog.show()
        cusView.setPadding(0, 0, 0, 0)
        tvPhoto.setOnClickListener {
            takePhoto()
            dialog.dismiss()
        }

        tvVideo.setOnClickListener {
            takeVideo()
            dialog.dismiss()
        }
        tvFile.setOnClickListener {
            openImageChooserActivity(type)
            dialog.dismiss()
        }

    }

    /**
     * 拍照
     */
    private fun takePhoto() {
        mImageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                activity!!.applicationContext,
                activity!!.packageName + ".basefileProvider",
                mFileUri!!
            )
        } else {
            Uri.fromFile(mFileUri)
        }
//        mImageUri = FileProvider7.getUriForFile(activity, mFileUri!!)
        takePicture(mImageUri!!, PHOTO_REQUEST)
    }

    private fun openImageChooserActivity(type: String) {
        mVideoUri = FileProvider7.getUriForFile(context, mFileUri)
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = type
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE)
    }

    private fun takeVideo() {
        mVideoUri = FileProvider7.getUriForFile(context, mFileVideoUri)
        //调用系统相机
        val intentCamera = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intentCamera.action = MediaStore.ACTION_VIDEO_CAPTURE
        intentCamera.addCategory(Intent.CATEGORY_DEFAULT)
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mVideoUri)
        startActivityForResult(intentCamera, VIDEO_REQUEST)
    }

    private fun takePicture(imageUri: Uri, requestCode: Int) {
        //调用系统相机
        val intentCamera = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        //        mImageUri= new File(Environment.getExternalStorageDirectory(), FileUtilsphoto.getPhotoFileName_new());
        intentCamera.action = MediaStore.ACTION_IMAGE_CAPTURE
        intentCamera.putExtra("android.intent.extras.CAMERA_FACING", 0) // 调用后置摄像头
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        startActivityForResult(intentCamera, requestCode)
    }


    private fun showFileChooser(
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: WebChromeClient.FileChooserParams
    ) {
        if (mUploadMessageAboveL != null) {
            mUploadMessageAboveL!!.onReceiveValue(null)
            mUploadMessageAboveL = null
        }
        mUploadMessageAboveL = filePathCallback
                XXPermissions.with(activity)
                    .permission(
                        Permission.CAMERA,
                        Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE
                    ) //不指定权限则自动获取清单中的危险权限
                    .request(object : OnPermission {
                        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                        override fun hasPermission(
                            granted: List<String>,
                            isAll: Boolean
                        ) {
                            if (context == null || isDetached) {
                                return
                            }
                            if (isAll) {
                                var type: String =
                                    if (fileChooserParams?.acceptTypes != null
                                        && fileChooserParams.acceptTypes.isNotEmpty()
                                        && !TextUtils.isEmpty(fileChooserParams.acceptTypes[0])
                                    ) {
                                        fileChooserParams.acceptTypes[0]
                                    } else {
                                        "*/*"
                                    }
                                when (type) {
                                    "*/*" -> showSelector(type)
                                    "video/*" -> takeVideo()
                                    else -> {

                                        val dialog = MaterialDialog(context!!)
                                            .title(null, "提示")
                                            .message(null, "请选择方式")
                                            .negativeButton(null,
                                                SpannelUtil.getSpannelStr(
                                                    "拍照",
                                                    context!!.resources.getColor(R.color.blue_3A75F3)
                                                ),
                                                object : DialogCallback {
                                                    override fun invoke(p1: MaterialDialog) {
                                                        takePhoto()
                                                    }

                                                })
                                            .positiveButton(null,
                                                SpannelUtil.getSpannelStr(
                                                    "图库",
                                                    context!!.resources.getColor(R.color.blue_3A75F3)
                                                ),
                                                object : DialogCallback {
                                                    override fun invoke(p1: MaterialDialog) {
                                                        openImageChooserActivity(type)
                                                    }
                                                }
                                            )
                                            .cornerRadius(DimensionUtil.dpToPx(2), null)
                                            .cancelable(false)
                                        dialog.show()
                                        dialog.setOnKeyListener { _, keyCode, keyEvent ->
                                            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                                                dialog.dismiss()
                                                if (mUploadMessage != null) {
                                                    mUploadMessage!!.onReceiveValue(null)
                                                    mUploadMessage = null
                                                }

                                                if (mUploadMessageAboveL != null) {
                                                    mUploadMessageAboveL!!.onReceiveValue(
                                                        null
                                                    )
                                                    mUploadMessageAboveL = null
                                                }
                                                true
                                            }
                                            false
                                        }
                                    }
                                }
                            }
                        }

                        override fun noPermission(
                            denied: List<String>,
                            quick: Boolean
                        ) {
                            if (mUploadMessageAboveL != null) {
                                mUploadMessageAboveL!!.onReceiveValue(null)
                                mUploadMessageAboveL = null
                            }
                            if (quick) {
                                ToastUtil.toastWaring("被永久拒绝授权，请手动授予权限")
                                //如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.gotoPermissionSettings(context)
                            } else {
                                ToastUtil.toastWaring("获取权限失败,可能会影响您的使用")
                            }
                        }
                    })
    }


    private fun onOpenFileChooser(
        valueCallback: ValueCallback<Uri>,
        acceptType: String
    ) {
        if (mUploadMessage != null) {
            mUploadMessage!!.onReceiveValue(null)
        }
        mUploadMessage = valueCallback
        XXPermissions.with(activity)
            .permission(
                Permission.CAMERA,
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE
            ) //不指定权限则自动获取清单中的危险权限
            .request(object : OnPermission {
                override fun hasPermission(
                    granted: List<String>,
                    isAll: Boolean
                ) {
                    if (context == null || isDetached) {
                        return
                    }
                    if (isAll) {
                        var type: String = if (!TextUtils.isEmpty(acceptType)) {
                            acceptType
                        } else {
                            "*/*"
                        }
                        when (type) {
                            "*/*" -> showSelector(type)
                            "video/*" -> takeVideo()
                            else -> {
                                val dialog = MaterialDialog(context!!)
                                    .title(null, "提示")
                                    .message(null, "请选择方式")
                                    .negativeButton(null,
                                        SpannelUtil.getSpannelStr(
                                            "拍照",
                                            context!!.resources.getColor(R.color.blue_3A75F3)
                                        ),
                                        object : DialogCallback {
                                            override fun invoke(p1: MaterialDialog) {
                                                takePhoto()
                                            }

                                        })
                                    .positiveButton(null,
                                        SpannelUtil.getSpannelStr(
                                            "图库",
                                            context!!.resources.getColor(R.color.blue_3A75F3)
                                        ),
                                        object : DialogCallback {
                                            override fun invoke(p1: MaterialDialog) {
                                                openImageChooserActivity(type)

                                            }
                                        }
                                    )
                                    .cornerRadius(DimensionUtil.dpToPx(2), null)
                                    .cancelable(false)
                                dialog.show()
                                dialog.setOnKeyListener { _, keyCode, keyEvent ->
                                    if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                                        dialog.dismiss()
                                        if (mUploadMessage != null) {
                                            mUploadMessage!!.onReceiveValue(null)
                                            mUploadMessage = null
                                        }

                                        if (mUploadMessageAboveL != null) {
                                            mUploadMessageAboveL!!.onReceiveValue(
                                                null
                                            )
                                            mUploadMessageAboveL = null
                                        }
                                        true
                                    }
                                    false
                                }
                            }
                        }

                    }
                }

                override fun noPermission(
                    denied: List<String>,
                    quick: Boolean
                ) {
                    if (mUploadMessage != null) {
                        mUploadMessage!!.onReceiveValue(null)
                        mUploadMessage = null
                    }
                    if (quick) {
                        ToastUtil.toastWaring("被永久拒绝授权，请手动授予权限")
                        //如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.gotoPermissionSettings(context)
                    } else {
                        ToastUtil.toastWaring("获取权限失败,可能会影响您的使用")

                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FILE_CHOOSER_RESULT_CODE -> {
                if (null == mUploadMessage && null == mUploadMessageAboveL) return
                if (mUploadMessageAboveL != null) {
                    onActivityResultAboveLFile(requestCode, resultCode, data)
                } else if (mUploadMessage != null) {
                    val result =
                        if (data == null || resultCode != Activity.RESULT_OK) null else data.data
                    mUploadMessage!!.onReceiveValue(result)
                    mUploadMessage = null
                }
            }

            VIDEO_REQUEST -> {
                if (null == mUploadMessage && null == mUploadMessageAboveL) return
                if (mUploadMessageAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, data)
                } else if (mUploadMessage != null) {
                    val result =
                        if (data == null || resultCode != Activity.RESULT_OK) null else data.data
                    mUploadMessage!!.onReceiveValue(result)
                    mUploadMessage = null
                }
            }

            PHOTO_REQUEST -> {
                if (null == mUploadMessage && null == mUploadMessageAboveL) return
                if (mUploadMessageAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, data)
                } else if (mUploadMessage != null) {
                    val result =
                        if (data == null || resultCode != Activity.RESULT_OK) null else data.data
                    mUploadMessage!!.onReceiveValue(result)
                    mUploadMessage = null
                }

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveLFile(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || mUploadMessageAboveL == null)
            return
        var results: Array<Uri>? = null
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                val dataString = intent.dataString
                if (dataString != null)
                    results = arrayOf(Uri.parse(dataString))
            }
        }
        mUploadMessageAboveL!!.onReceiveValue(results)
        mUploadMessageAboveL = null
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode != PHOTO_REQUEST && requestCode != VIDEO_REQUEST) || mUploadMessageAboveL == null) {
            return
        }
        var results: Array<Uri>? = null
        if (resultCode == Activity.RESULT_OK) {

            if (data == null) {
                results = if (requestCode == PHOTO_REQUEST) {
                    arrayOf(mImageUri!!)
                } else {
                    arrayOf(mVideoUri!!)
                }
            } else {
                val dataString = data!!.dataString
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }
        mUploadMessageAboveL!!.onReceiveValue(results)
        mUploadMessageAboveL = null

    }

}