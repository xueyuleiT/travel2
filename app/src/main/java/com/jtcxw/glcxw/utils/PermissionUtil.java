package com.jtcxw.glcxw.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.jtcxw.glcxw.base.utils.BaseUtil;
import com.jtcxw.glcxw.base.utils.ToastUtil;

import java.util.List;

public class PermissionUtil {
    public static void goSetting(){
        String name= Build.MANUFACTURER;
        /**
         * HUAWEI，vivo，OPPO......手机机型标注不可以改变
         */
        if("HUAWEI".equals(name)){
            goHuaWeiMainager();
        }else if ("vivo".equals(name)){
            goVivoMainager();
        }else if ("OPPO".equals(name)){
            goOppoMainager();
        }else if ("Coolpad".equals(name)){
            goCoolpadMainager();
        }else if ("Meizu".equals(name)){
            goMeizuMainager();
            // getAppDetailSettingIntent();
        }else if ("Xiaomi".equals(name)){
            goXiaoMiMainager();
        }else if ("samsung".equals(name)){
            goSangXinMainager();
        }else{
            goIntentSetting();
        }
    }

    private static void goHuaWeiMainager() {
        try {
            Intent intent = new Intent("demo.vincent.com.tiaozhuan");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            BaseUtil.Companion.getSTopAct().startActivity(intent);
        } catch (Exception e) {
            ToastUtil.Companion.toastError("请手动前往权限设置页面");
            e.printStackTrace();
            goIntentSetting();
        }
    }
    private static void goXiaoMiMainager(){
        try {
            Intent localIntent = new Intent(
                    "miui.intent.action.APP_PERM_EDITOR");
            localIntent
                    .setClassName("com.miui.securitycenter",
                            "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname",BaseUtil.Companion.getSTopAct().getPackageName());
            BaseUtil.Companion.getSTopAct().startActivity(localIntent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            goIntentSetting();
        }
    }
    private static void goMeizuMainager(){
        try {
            Intent intent=new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", "xiang.settingpression");
            BaseUtil.Companion.getSTopAct().startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            goIntentSetting();
        }
    }
    private static void goSangXinMainager(){
        //三星4.3可以直接跳转
        goIntentSetting();
    }
    private static void goIntentSetting(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BaseUtil.Companion.getSTopAct().getPackageName(), null);
        intent.setData(uri);
        try {
            BaseUtil.Companion.getSTopAct().startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void goOppoMainager(){
        doStartApplicationWithPackageName("com.coloros.safecenter");
    }

    private static void goCoolpadMainager(){
        doStartApplicationWithPackageName("com.yulong.android.security:remote");
    }

    private static void goVivoMainager(){
        doStartApplicationWithPackageName("com.bairenkeji.icaller");
    }

    private static void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = BaseUtil.Companion.getSTopAct().getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            goIntentSetting();
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = BaseUtil.Companion.getSTopAct().getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveinfoList.size() > 0) {
            ResolveInfo resolveinfo = resolveinfoList.iterator().next();
            if (resolveinfo != null) {
                // packagename = 参数packname
                String packageName = resolveinfo.activityInfo.packageName;
                // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
                String className = resolveinfo.activityInfo.name;
                // LAUNCHER Intent
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                // 设置ComponentName参数1:packagename参数2:MainActivity路径
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                try {
                    BaseUtil.Companion.getSTopAct().startActivity(intent);
                } catch (Exception e) {
                    goIntentSetting();
                    e.printStackTrace();
                }
            }
        }else {
            goIntentSetting();
        }
    }

}
