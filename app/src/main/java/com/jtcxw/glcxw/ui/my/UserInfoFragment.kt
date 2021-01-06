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
import androidx.core.net.toFile
import com.baidu.ocr.ui.camera.CameraActivity
import com.bigkoo.pickerview.TimePickerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.glcxw.lib.util.FileProvider7
import com.google.gson.JsonObject
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.respmodels.DictionaryInfoBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentUserInfoBinding
import com.jtcxw.glcxw.dialog.KVTypeDialog
import com.jtcxw.glcxw.listeners.CompressCallback
import com.jtcxw.glcxw.listeners.DialogCallback
import com.jtcxw.glcxw.listeners.KVDialogCallback
import com.jtcxw.glcxw.localbean.KVBean
import com.jtcxw.glcxw.presenters.impl.UserInfoPresenter
import com.jtcxw.glcxw.utils.BottomDialogUtil
import com.jtcxw.glcxw.utils.FileUtils
import com.jtcxw.glcxw.utils.ImageUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.UserInfoView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserInfoFragment:BaseFragment<FragmentUserInfoBinding,CommonModel>() ,UserInfoView{
    override fun onGetOccupationListFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mDialog!!.dismiss()
        }
    }

    override fun onGetCountryListFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mDialog!!.dismiss()
        }
    }

    override fun onGetSexListFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mDialog!!.dismiss()
        }
    }

    var mFinishCount = 3

    override fun onHeadImage(jsonObject: JsonObject) {
        if (jsonObject.get("Status").asBoolean) {
            ToastUtil.toastSuccess("头像上传成功")
            mBinding.tvHead.text = "上传成功"
            UserUtil.getUserInfoBean().headImageUrl = jsonObject.get("HeadImgUrl").asString
        }
    }

    override fun onModifyMemberInfoSucc(userInfoBean: UserInfoBean) {
        UserUtil.getUser().save(userInfoBean)
        ToastUtil.toastSuccess("修改成功")
        pop()
    }

    override fun onGetOccupationListSucc(list: List<DictionaryInfoBean.DictionaryBean>) {
        list.forEach {
            val kvBean = KVBean(it.itemValue,it.itemName)
            mOccupationList.add(kvBean)
        }
    }

    override fun onGetCountryListSucc(list: List<DictionaryInfoBean.DictionaryBean>) {
        list.forEach {
            val kvBean = KVBean(it.itemValue,it.itemName)
            mCountryList.add(kvBean)
        }
    }

    override fun onGetSexListSucc(list: List<DictionaryInfoBean.DictionaryBean>) {
        list.forEach {
            val kvBean = KVBean(it.itemValue,it.itemName)
            mSexList.add(kvBean)
        }
    }

    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?) {
            val userInfoFragment = UserInfoFragment()
            userInfoFragment.arguments = bundle
            fragment.start(userInfoFragment)
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_info
    }

    var mOccupationList = ArrayList<KVBean>()
    var mCountryList = ArrayList<KVBean>()
    var mSexList = ArrayList<KVBean>()
    var mPresenter:UserInfoPresenter?= null
    var mDialog:LoadingDialog?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("用户信息")

        mPresenter = UserInfoPresenter(this)

        mBinding.llHead.setOnClickListener(this)
        mBinding.llName.setOnClickListener(this)
        mBinding.llSex.setOnClickListener(this)
        mBinding.llCountry.setOnClickListener(this)
        mBinding.llAddress.setOnClickListener(this)
        mBinding.llProfession.setOnClickListener(this)
        mBinding.llBirthday.setOnClickListener(this)


        if (!TextUtils.isEmpty(UserUtil.getUserInfoBean().headImageUrl)) {
            Glide.with(this)
                .load(UserUtil.getUserInfoBean().headImageUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(DimensionUtil.dpToPx(20).toInt())))
                .into(mBinding.vHead)
        }

        if (!TextUtils.isEmpty(UserUtil.getUserInfoBean().nikeName)) {
            mBinding.tvName.text = UserUtil.getUserInfoBean().nikeName
            mBinding.tvNick.text = UserUtil.getUserInfoBean().nikeName
        } else {
            mBinding.tvName.text = UserUtil.getUserInfoBean().telphoneNo
        }

        mBinding.tvSex.text = UserUtil.getUserInfoBean().sex

        mBinding.tvCountry.text = UserUtil.getUserInfoBean().nation
        mBinding.tvAddress.text = UserUtil.getUserInfoBean().address
        mBinding.tvProfession.text = UserUtil.getUserInfoBean().occupation
        mBinding.tvBirthday.text = UserUtil.getUserInfoBean().birthday
        mBinding.btnNext.setOnClickListener(this)

        mDialog = DialogUtil.getLoadingDialog(fragmentManager)
        mPresenter!!.getSexList()
        mPresenter!!.getCountryList()
        mPresenter!!.getOccupationList()

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.btn_next -> {
//                if (TextUtils.isEmpty(mBinding.tvNick.text.toString())){
//                    ToastUtil.toastWaring("请输入昵称")
//                    return
//                }
//
//                if (TextUtils.isEmpty(mBinding.tvSex.text.toString())){
//                    ToastUtil.toastWaring("请选择性别")
//                    return
//                }
//
//                if (TextUtils.isEmpty(mBinding.tvCountry.text.toString())){
//                    ToastUtil.toastWaring("请选择国籍")
//                    return
//                }
//
//
//                if (TextUtils.isEmpty(mBinding.tvAddress.text.toString())){
//                    ToastUtil.toastWaring("请填写地址")
//                    return
//                }
//
//                if (TextUtils.isEmpty(mBinding.tvProfession.text.toString())){
//                    ToastUtil.toastWaring("请选择职业")
//                    return
//                }
//
//                if (TextUtils.isEmpty(mBinding.tvBirthday.text.toString())){
//                    ToastUtil.toastWaring("请选择出生日期")
//                    return
//                }

                val json = JsonObject()
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                json.addProperty("NikeName", mBinding.tvNick.text.toString())
//                if (!TextUtils.isEmpty(mBinding.tvSex.text.toString())){
//                    if (mBinding.tvSex.text.toString() == "女"){
//                        json.addProperty("Sex","1")
//                    } else {
//                        json.addProperty("Sex","0")
//
//                    }
//                } else {
//                    json.addProperty("Sex","")
//                }

                mSexList.forEach {
                    if (mBinding.tvSex.text.toString() == it.value){
                        json.addProperty("Sex",it.key)
                    }
                }

                if (json.get("Sex") == null || json.get("Sex").asString == "") {
                    json.addProperty("Sex",mBinding.tvSex.text.toString())
                }

                mCountryList.forEach {
                    if (mBinding.tvCountry.text.toString() == it.value) {
                        json.addProperty("Nation",it.key)
                    }
                }

                if (json.get("Nation") == null || json.get("Nation").asString == "") {
                    json.addProperty("Nation",mBinding.tvCountry.text.toString())
                }

                mOccupationList.forEach {
                    if (mBinding.tvProfession.text.toString() == it.value) {
                        json.addProperty("Occupation",it.key)
                    }
                }

                if (json.get("Occupation") == null || json.get("Occupation").asString == "") {
                    json.addProperty("Occupation",mBinding.tvProfession.text.toString())
                }

                json.addProperty("Address", mBinding.tvAddress.text.toString())
                json.addProperty("Birthday", mBinding.tvBirthday.text.toString())
                mPresenter!!.modifyMemberInfo(json)


            }
            R.id.ll_head -> {
                BottomDialogUtil.showHeadDialog(context,object :DialogCallback{
                    override fun onDialogCallback(type: Int) {
                        if (type == 1) {
                            openCamera()
//                            takePhoto(CameraActivity.CONTENT_TYPE_GENERAL)
                        } else if (type == 2) {
                            choosePhoto(RC_CHOOSE_PHOTO_ID)
                        }
                    }

                })
            }

            R.id.ll_name -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_NICK,mBinding.tvNick.text.toString())
                NickNameFragment.newInstance(this,bundle)
            }

            R.id.ll_sex -> {
                if (mSexList.isEmpty()) {
                    mPresenter!!.getSexList()
                    return
                }
                KVTypeDialog().setCallback(object :KVDialogCallback {
                    override fun onKVDialogCallback(i: Int) {
                        mBinding.tvSex.text = mSexList[i].value
                    }

                }).setData(mSexList)
                    .show(fragmentManager!!,"KVTypeDialog")
            }

            R.id.ll_country -> {
                if (mCountryList.isEmpty()) {
                    mPresenter!!.getCountryList()
                    return
                }
                KVTypeDialog().setCallback(object :KVDialogCallback {
                    override fun onKVDialogCallback(i: Int) {
                        mBinding.tvCountry.text = mCountryList[i].value
                    }

                }).setData(mCountryList)
                    .show(fragmentManager!!,"KVTypeDialog")
            }

            R.id.ll_address -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_ADDRESS,mBinding.tvAddress.text.toString())
                AddressFragment.newInstance(this,bundle)
            }

            R.id.ll_profession -> {
                if (mOccupationList.isEmpty()) {
                    mPresenter!!.getOccupationList()
                    return
                }
                KVTypeDialog().setCallback(object :KVDialogCallback {
                    override fun onKVDialogCallback(i: Int) {
                        mBinding.tvProfession.text = mOccupationList[i].value
                    }

                }).setData(mOccupationList)
                    .show(fragmentManager!!,"KVTypeDialog")
            }

            R.id.ll_birthday -> {
                hideSoftInput()
                val timePickerView =
                    TimePickerView.Builder(context!!,
                        TimePickerView.OnTimeSelectListener { date, v ->
                            val sdf = SimpleDateFormat("yyyy-MM-dd")
                            mBinding.tvBirthday.text = sdf.format(date)
                        }).setType(booleanArrayOf(true, true, true, false, false, false))
                timePickerView.setDate(Calendar.getInstance())
                timePickerView.build().show()
            }
        }
    }

    private val REQUEST_CODE_CAMERA = 102
    val RC_CHOOSE_PHOTO_ID = 2
    val CROP_ALBUM_PHOTO_ID = 3

    val RC_CHOOSE_PHOTO_ID_BACK = 4
    val CROP_ALBUM_PHOTO_ID_BACK = 5

    val TAKE_PHOTO = 10

    val CROP_PHOTO = 11

    val ID_FONT_PATH_HEAD = "head"

    private var imageUri: Uri? = null
    private var cropImgUri: Uri? = null



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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != -1) {
            return
        }
        when (requestCode) {

            CROP_PHOTO -> {
                try {
                    Glide.with(mBinding.vHead)
                        .load(getPhotoFile(ID_FONT_PATH_HEAD))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(mBinding.vHead.height/2)))
                        .into(mBinding.vHead)

                    var originBitmap = FileUtils.getimage(getPhotoFile(ID_FONT_PATH_HEAD).absolutePath)
                    ImageUtil.compressImage(originBitmap, 500, object : CompressCallback {
                        override fun onCompressCallback(bmp: Bitmap) {
                            if (activity!!.isFinishing) {
                                return
                            }
                            activity!!.runOnUiThread {

                                val json = JsonObject()
                                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                                json.addProperty("Image64",ImageUtil.bitmapToBase64(bmp))
                                mPresenter!!.headImage(json)
                            }
                        }
                    })

                } catch (e : FileNotFoundException) {
                }
            }

            TAKE_PHOTO -> {
//                startPhotoCrop()
                startPhotoZoom(imageUri, CROP_PHOTO, ID_FONT_PATH_HEAD)
            }
            RC_CHOOSE_PHOTO_ID -> {
                if (data != null && data.data != null) {
                    startPhotoZoom(data!!.data, CROP_ALBUM_PHOTO_ID, ID_FONT_PATH_HEAD)
                }
            }
            CROP_ALBUM_PHOTO_ID -> {
                Glide.with(mBinding.vHead)
                    .load(getPhotoFile(ID_FONT_PATH_HEAD))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(mBinding.vHead.height/2)))
                    .into(mBinding.vHead)

                var originBitmap = FileUtils.getimage(getPhotoFile(ID_FONT_PATH_HEAD).absolutePath)
                ImageUtil.compressImage(originBitmap, 500, object : CompressCallback {
                    override fun onCompressCallback(bmp: Bitmap) {
                        if (activity!!.isFinishing) {
                            return
                        }
                        activity!!.runOnUiThread {

                            val json = JsonObject()
                            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                            json.addProperty("Image64",ImageUtil.bitmapToBase64(bmp))
                            mPresenter!!.headImage(json)
                        }
                    }
                })
            }

            RC_CHOOSE_PHOTO_ID_BACK -> {
                if (data != null && data.data != null) {
                    startPhotoZoom(data!!.data, CROP_ALBUM_PHOTO_ID_BACK, ID_FONT_PATH_HEAD)
                }
            }
            CROP_ALBUM_PHOTO_ID_BACK -> {
                Glide.with(mBinding.vHead)
                    .load(getPhotoFile(ID_FONT_PATH_HEAD))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(mBinding.vHead.height/2)))
                    .into(mBinding.vHead)

            }

            REQUEST_CODE_CAMERA -> {
                if (data != null) {
                    val contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE)

                    if (!TextUtils.isEmpty(contentType)) {
                        if (CameraActivity.CONTENT_TYPE_GENERAL == contentType) {
                            Glide.with(mBinding.vHead)
                                .load(FileUtils.getSaveFile(context,contentType))
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .apply(RequestOptions.bitmapTransform(RoundedCorners(mBinding.vHead.height/2)))
                                .into(mBinding.vHead)

                            var originBitmap = FileUtils.getimage(FileUtils.getSaveFile(context,contentType).absolutePath)
                            ImageUtil.compressImage(originBitmap, 500, object : CompressCallback {
                                override fun onCompressCallback(bmp: Bitmap) {
                                    if (activity!!.isFinishing) {
                                        return
                                    }
                                    activity!!.runOnUiThread {

                                        val json = JsonObject()
                                        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                                        json.addProperty("Image64",ImageUtil.bitmapToBase64(bmp))
                                        mPresenter!!.headImage(json)
                                    }
                                }
                            })
                        }
                    }

                }

            }
        }
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
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", mBinding.vHead.width)
        intent.putExtra("outputY", mBinding.vHead.height)
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

    override fun doAfterAnim() {

    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (resultCode == ISupportFragment.RESULT_OK && requestCode == 10) {
            mBinding.tvNick.text = data!!.getString(BundleKeys.KEY_NICK)
            mBinding.tvName.text = data!!.getString(BundleKeys.KEY_NICK)
        } else if (resultCode == ISupportFragment.RESULT_OK && requestCode == 11) {
            mBinding.tvAddress.text = data!!.getString(BundleKeys.KEY_ADDRESS)
        }
    }


    private fun openCamera() {


        //拍摄身份证反面
        XXPermissions.with(activity)
            .permission(Permission.CAMERA) //不指定权限则自动获取清单中的危险权限
            .request(object : OnPermission {

                override fun hasPermission(granted: List<String>, isAll: Boolean) {
                    if (isAll) {
                        val outputImage = File(context!!.externalCacheDir, "output_image.jpg")
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete()
                                outputImage.createNewFile()
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        imageUri = FileProvider7.getUriForFile(context,outputImage)
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        startActivityForResult(intent, TAKE_PHOTO)
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

    /**
     * 开启裁剪相片
     */
    private fun startPhotoCrop() {
        //创建file文件，用于存储剪裁后的照片
        //        File cropImage = new File(Environment.getExternalStorageDirectory(), "crop_image.jpg");
        val cropImage = File(context!!.externalCacheDir, "crop_image.jpg")
        try {
            if (cropImage.exists()) {
                cropImage.delete()
            }
            cropImage.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        cropImgUri = FileProvider7.getUriForFile(context,cropImage)

//        cropImgUri = Uri.fromFile(cropImage)
        val intent = Intent("com.android.camera.action.CROP")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        //设置源地址uri
        intent.setDataAndType(imageUri, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", 200)
        intent.putExtra("outputY", 200)
        intent.putExtra("scale", true)
        //设置目的地址uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImgUri)
        //重要的一步，使用grantUriPermission来给对应的包提升读写指定uri的临时权限。否则即使调用成功，也会保存裁剪照片失败。
        val resInfoList = activity!!.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        resInfoList.forEach {
            val packageName = it.activityInfo.packageName
            activity!!.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        //设置图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("return-data", false)//data不需要返回,避免图片太大异常
        intent.putExtra("noFaceDetection", true) // no face detection
        startActivityForResult(intent, CROP_PHOTO)
    }
}