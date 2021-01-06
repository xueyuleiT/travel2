package com.jtcxw.glcxw.ui.my

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.baidu.ocr.ui.camera.CameraActivity
import com.glcxw.lib.util.FileProvider7
import com.google.gson.JsonObject
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.respmodels.ExtraOrderBean
import com.jtcxw.glcxw.base.respmodels.IDTypeBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.utils.*
import com.jtcxw.glcxw.databinding.FragmentAuthBinding
import com.jtcxw.glcxw.dialog.KVTypeDialog
import com.jtcxw.glcxw.listeners.CompressCallback
import com.jtcxw.glcxw.listeners.KVDialogCallback
import com.jtcxw.glcxw.localbean.KVBean
import com.jtcxw.glcxw.presenters.impl.AuthPresenter
import com.jtcxw.glcxw.utils.FileUtils
import com.jtcxw.glcxw.utils.ImageUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.AuthView
import me.yokeyword.fragmentation.SupportFragment
import java.io.File
import java.io.IOException

class AuthFragment:BaseFragment<FragmentAuthBinding,CommonModel>() ,AuthView{
    override fun onOrderStatisticsSucc(extraOrderBean: ExtraOrderBean) {
    }

    override fun onOrderStatisticsFinish() {
    }

    override fun onMemberInfoSucc(userInfoBean: UserInfoBean) {
        UserUtil.getUser().save(userInfoBean)
        init()
    }

    override fun onMemberInfoFailed(msg: String) {
    }

    override fun onMemberInfoFinish() {
        mDialog!!.dismiss()
    }

    override fun onGetIdTypeListSucc(idTypeBean: IDTypeBean) {
        mList.clear()
        idTypeBean.dictionaryInfo.forEach {
            val kvBean = KVBean(it.itemName,it.itemValue)
            mList.add(kvBean)
        }
    }

    private var hasSendFont = false
    private var hasSendBack = false
    private var mDialog:LoadingDialog?= null
    override fun onAuthenticationImageSucc(jsonObject: JsonObject,type:Int) {
        if (type == 0) {
            hasSendFont = true
        } else {
            hasSendBack = true
        }
        ToastUtil.toastSuccess("上传成功")
    }

    override fun onAuthenticationSucc(jsonObject: JsonObject) {
        if(jsonObject.get("Status").asString == "true") {
            UserUtil.getUserInfoBean().realNameVerifyFlag = "1"
            UserUtil.getUserInfoBean().realName = mBinding.etName.text.toString()
            UserUtil.getUserInfoBean().idCardNo = mBinding.etIdNo.text.toString()
            UserUtil.getUserInfoBean().idCardType = "身份证"
            ToastUtil.toastSuccess("实名认证成功")
            if (arguments!!.getString("type") == "openQr") {
                startWithPop(ChargeFragment())
            } else {
                mDialog = DialogUtil.getLoadingDialog(fragmentManager)
                val json = JsonObject()
                json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
                mPresenter!!.getMemberInfo(json)
            }
        } else {
            ToastUtil.toastWaring("实名认证失败")
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment,bundle: Bundle?) {
            val authFragment = AuthFragment()
            authFragment.arguments = bundle
            fragment.start(authFragment)
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("实名制认证")

        mPresenter = AuthPresenter(this)

        init()
//        mPresenter!!.getIdTypeList()

    }

    private fun init() {

        if (UserUtil.getUserInfoBean().realNameVerifyFlag == "1") {
            mBinding.vAuth.isEnabled = false
            mBinding.etName.isEnabled = false
            mBinding.tvIdType.isEnabled = false
            mBinding.tvIdType.setCompoundDrawables(null,null,null,null)
            mBinding.etIdNo.isEnabled = false
            mBinding.ivFont.isEnabled = false
            mBinding.ivBack.isEnabled = false
            mBinding.tvConfirm.isEnabled = false
            mBinding.tvConfirm.setBackgroundResource(R.drawable.shape_r5_disable)

            mBinding.llPic.visibility = View.GONE
            mBinding.rlBottom.visibility = View.GONE

            mBinding.vAuth.setImageResource(R.mipmap.icon_authed)
            mBinding.etName.setTextColor(resources.getColor(R.color.gray_9))
            mBinding.tvIdType.setTextColor(resources.getColor(R.color.gray_9))
            mBinding.etIdNo.setTextColor(resources.getColor(R.color.gray_9))


            mBinding.etName.setText(UserUtil.getUserInfoBean().realName)
            if (TextUtils.isEmpty(UserUtil.getUserInfoBean().idCardType)) {
                mBinding.tvIdType.text ="身份证"
            } else {
                mBinding.tvIdType.text = UserUtil.getUserInfoBean().idCardType
            }
            mBinding.etIdNo.setText(UserUtil.getUserInfoBean().idCardNo)
        } else {
            mBinding.tvConfirm.setOnClickListener(this)
            mBinding.ivBack.setOnClickListener(this)
            mBinding.ivFont.setOnClickListener(this)
            mBinding.tvIdType.setOnClickListener(this)

            mBinding.llPic.visibility = View.GONE
            mBinding.rlBottom.visibility = View.VISIBLE

            mBinding.vAuth.setImageResource(R.mipmap.icon_no_authed)
        }

        mBinding.tvName.text = UserUtil.getUserInfoBean().realName

    }

    private val REQUEST_CODE_CAMERA = 102

    val RC_CHOOSE_PHOTO_ID = 2
    val CROP_ALBUM_PHOTO_ID = 3

    val RC_CHOOSE_PHOTO_ID_BACK = 4
    val CROP_ALBUM_PHOTO_ID_BACK = 5

    var mPresenter: AuthPresenter?= null
    val ID_FONT_PATH_NAME = "id"
    val ID_BACK_PATH_NAME = "id_back"
    var mList = ArrayList<KVBean>()

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.tv_id_type -> {
                hideSoftInput()
                if (mList.isEmpty()){
                    mList.add(KVBean("1","身份证"))
                }
                KVTypeDialog()
                    .setData(mList)
                    .setCallback(object :KVDialogCallback{
                        override fun onKVDialogCallback(i: Int) {
                            mBinding.tvIdType.text = mList[i].value
                        }

                    }).show(fragmentManager!!,"KVTypeDialog")
            }
            R.id.iv_back -> {
                MaterialDialog(context!!)
                    .title(null,"提示")
                    .message(null, "请选择图片")
                    .negativeButton(null,SpannelUtil.getSpannelStr("相册", context!!.resources.getColor(R.color.blue_3A75F3)),
                        object : DialogCallback {
                            override fun invoke(p1: MaterialDialog) {
                                choosePhoto(RC_CHOOSE_PHOTO_ID_BACK)
                            }
                        })
                    .positiveButton(null, SpannelUtil.getSpannelStr("拍照", context!!.resources.getColor(R.color.blue_3A75F3)),
                        object : DialogCallback {
                            override fun invoke(p1: MaterialDialog) {
                                takePhoto(CameraActivity.CONTENT_TYPE_ID_CARD_BACK)
                            }
                        }
                    )
                    .lifecycleOwner(activity!!)
                    .cornerRadius(DimensionUtil.dpToPx(2), null)
                    .cancelable(true)
                    .show()
            }

            R.id.iv_font -> {
                MaterialDialog(context!!)
                    .title(null,"提示")
                    .message(null, "请选择图片")
                    .negativeButton(null, SpannelUtil.getSpannelStr("相册", context!!.resources.getColor(R.color.blue_3A75F3)),
                        object : DialogCallback {
                            override fun invoke(p1: MaterialDialog) {
                                choosePhoto(RC_CHOOSE_PHOTO_ID)
                            }
                        })
                    .positiveButton(null, SpannelUtil.getSpannelStr("拍照", context!!.resources.getColor(R.color.blue_3A75F3)),
                        object : DialogCallback {
                            override fun invoke(p1: MaterialDialog) {
                                takePhoto(CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
                            }
                        }
                    )
                    .lifecycleOwner(activity!!)
                    .cornerRadius(DimensionUtil.dpToPx(2), null)
                    .cancelable(true)
                    .show()
            }

            R.id.tv_confirm -> {
                if (TextUtils.isEmpty(mBinding.etName.text.toString())) {
                    ToastUtil.toastWaring("请输入姓名")
                    return
                }

                if (TextUtils.isEmpty(mBinding.tvIdType.text.toString())) {
                    ToastUtil.toastWaring("请选择证件类型")
                    return
                }

                if (TextUtils.isEmpty(mBinding.etIdNo.text.toString())) {
                    ToastUtil.toastWaring("请输入证件号")
                    return
                }

                if (mBinding.etIdNo.text.toString().length != 15 && mBinding.etIdNo.text.toString().length != 18) {
                    ToastUtil.toastWaring("身份证号码错误，请检查您输入的身份证号码")
                    return
                }

//                if (!hasSendFont) {
//                    ToastUtil.toastWaring("请上传证件正面照")
//                    return
//                }
//
//                if (!hasSendBack) {
//                    ToastUtil.toastWaring("请上传证件反面照")
//                    return
//                }

                val json = JsonObject()
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                json.addProperty("RealName",mBinding.etName.text.toString())
                var idCardType = ""
                mList.forEach {
                    if (it.checked) {
                        idCardType = it.key
                    }
                }
                json.addProperty("IdCardType",idCardType)
                json.addProperty("IdCardNo",mBinding.etIdNo.text.toString())

                mPresenter!!.authentication(json)

            }
        }
    }

    private fun takePhoto(type :String){

                //拍摄身份证反面
                XXPermissions.with(activity)
                    .permission(Permission.CAMERA) //不指定权限则自动获取清单中的危险权限
                    .request(object : OnPermission {

                        override fun hasPermission(granted: List<String>, isAll: Boolean) {
                            if (isAll) {
                                val intent = Intent(activity, CameraActivity::class.java)
                                intent.putExtra(
                                    CameraActivity.KEY_OUTPUT_FILE_PATH,
                                    FileUtils.getSaveFile(context,type).absolutePath
                                )
                                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, type)
                                startActivityForResult(intent, REQUEST_CODE_CAMERA)
                            } else {
                                ToastUtil.toastWaring("获取权限成功，部分权限未正常授予")
                            }
                        }

                        override fun noPermission(denied: List<String>, quick: Boolean) {
                            if (quick) {
                                XXPermissions.gotoPermissionSettings(context)
                            } else {
                                ToastUtil.toastWaring("获取权限失败")
                            }
                        }
                    })


    }


    private fun choosePhoto(requestCode: Int) {

                XXPermissions.with(activity)
                    .permission(Permission.READ_EXTERNAL_STORAGE) //不指定权限则自动获取清单中的危险权限
                    .request(object : OnPermission {

                        override fun hasPermission(granted: List<String>, isAll: Boolean) {
                            if (isAll) {
                                val intentToPickPic = Intent(Intent.ACTION_PICK, null)
                                intentToPickPic.setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*"
                                )
                                startActivityForResult(intentToPickPic, requestCode)
                            } else {
                                ToastUtil.toastWaring("获取权限成功，部分权限未正常授予")

                            }
                        }

                        override fun noPermission(denied: List<String>, quick: Boolean) {
                            if (quick) {
                                XXPermissions.gotoPermissionSettings(context)
                            } else {
                                ToastUtil.toastWaring("获取权限失败")
                            }
                        }
                    })

    }


    override fun doAfterAnim() {
    }

    private fun startPhotoZoom(uri: Uri?, requestCode: Int, pathName:String) {
        val intent = Intent("com.android.camera.action.CROP")
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        intent.putExtra("crop", "true")
        intent.setDataAndType(uri, "image/*")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", mBinding.ivBack.width * 1.0f/mBinding.ivBack.height)
        intent.putExtra("aspectY", 1)

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", mBinding.ivBack.width)
        intent.putExtra("outputY", mBinding.ivBack.height)
        intent.putExtra("scale", true)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("return-data", false)
        val file = getPhotoFile(pathName)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        val targetUri = FileProvider7.getUriForFile(context,getPhotoFile(pathName))
        intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri)
        //重要的一步，使用grantUriPermission来给对应的包提升读写指定uri的临时权限。否则即使调用成功，也会保存裁剪照片失败。
        val resInfoList = activity!!.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        resInfoList.forEach {
            val packageName = it.activityInfo.packageName
            activity!!.grantUriPermission(packageName, targetUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.putExtra("noFaceDetection", true)
        startActivityForResult(intent, requestCode)
    }

    private fun getPhotoFile(fileName :String): File {
        val fileDir = File(activity!!.filesDir.path + "/photoCover" + File.separator)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }

        val photoFile = File(fileDir, "$fileName.jpg")
        if (!photoFile.exists()) {
            try {
                photoFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return photoFile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != -1) {
            return
        }
        when (requestCode) {

            RC_CHOOSE_PHOTO_ID -> {
                if (data != null && data.data != null) {
                    startPhotoZoom(data!!.data, CROP_ALBUM_PHOTO_ID, ID_FONT_PATH_NAME)
                }
            }
            CROP_ALBUM_PHOTO_ID -> {
                mBinding.ivFont.setImageBitmap(BitmapFactory.decodeFile(getPhotoFile(ID_FONT_PATH_NAME).absolutePath))
                authenticationImage(getPhotoFile(ID_FONT_PATH_NAME).absolutePath,0)

            }

            RC_CHOOSE_PHOTO_ID_BACK -> {
                if (data != null && data.data != null) {
                    startPhotoZoom(data!!.data, CROP_ALBUM_PHOTO_ID_BACK, ID_BACK_PATH_NAME)
                }
            }
            CROP_ALBUM_PHOTO_ID_BACK -> {
                mBinding.ivBack.setImageBitmap(BitmapFactory.decodeFile(getPhotoFile(ID_BACK_PATH_NAME).absolutePath))
                authenticationImage(getPhotoFile(ID_BACK_PATH_NAME).absolutePath,1)
            }

            REQUEST_CODE_CAMERA -> {
                if (data != null) {
                    val contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE)
                    val filePath = FileUtils.getSaveFile(context,contentType).absolutePath

                    if (!TextUtils.isEmpty(contentType)) {
                        if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT == contentType) {
                            mBinding.ivFont.setImageBitmap(BitmapFactory.decodeFile(filePath))
                            authenticationImage(filePath,0)
                        } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK == contentType) {
                            mBinding.ivBack.setImageBitmap(BitmapFactory.decodeFile(filePath))
                            authenticationImage(filePath,1)
                        }

                    }

                }

            }
        }
    }

    private fun authenticationImage(file:String, side: Int) {
        var originBitmap = FileUtils.getimage(file)
        ImageUtil.compressImage(originBitmap, 500, object : CompressCallback {
            override fun onCompressCallback(bmp: Bitmap) {
                if (activity!!.isFinishing) {
                    return
                }
                activity!!.runOnUiThread {
                    val json = JsonObject()
                    json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
                    json.addProperty("ProsAndCons", side)
                    json.addProperty("Image64", ImageUtil.bitmapToBase64(bmp))
                    mPresenter!!.authenticationImage(json)
                }
            }
        })
    }
}