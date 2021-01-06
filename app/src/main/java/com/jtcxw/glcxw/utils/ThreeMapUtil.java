package com.jtcxw.glcxw.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jtcxw.glcxw.R;
import com.jtcxw.glcxw.base.utils.ScreenUtil;

import java.io.File;

public class ThreeMapUtil {
    private static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
    private static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
    private static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名

    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static void showDialog(Context context,double dlat,double dlon,String dname) {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);
        bottomDialog.setContentView(R.layout.dialog_bottom_map);


        WindowManager.LayoutParams params = bottomDialog.getWindow().getAttributes();
        params.width = ScreenUtil.getScreenWidth(context);
        bottomDialog.getWindow().setAttributes(params);

        TextView tvGd = bottomDialog.findViewById(R.id.tv_gd);
        if (isInstallPackage(PN_GAODE_MAP)) {
            tvGd.setText("高德地图(已安装)");
            tvGd.setOnClickListener(view -> {
                bottomDialog.dismiss();
                openGaoDeNavi(context,0,0,null,dlat,dlon,dname);
            });
        } else  {
            tvGd.setText("高德地图(未安装)");
            tvGd.setTextColor(context.getResources().getColor(R.color.gray_9));
        }


        TextView tvBd = bottomDialog.findViewById(R.id.tv_bd);
        if (isInstallPackage(PN_BAIDU_MAP)) {
            tvBd.setText("百度地图(已安装)");
            tvBd.setOnClickListener(view -> {
                bottomDialog.dismiss();
                openBaiDuNavi(context,0,0,null,dlat,dlon,dname);
            });
        } else  {
            tvBd.setText("百度地图(未安装)");
            tvBd.setTextColor(context.getResources().getColor(R.color.gray_9));
        }


        TextView tvTx = bottomDialog.findViewById(R.id.tv_tx);
        if (isInstallPackage(PN_TENCENT_MAP)) {
            tvTx.setText("腾讯地图(已安装)");
            tvTx.setOnClickListener(view -> {
                bottomDialog.dismiss();
                openTencentMap(context,0,0,null,dlat,dlon,dname);
            });
        } else  {
            tvTx.setText("腾讯地图(未安装)");
            tvTx.setTextColor(context.getResources().getColor(R.color.gray_9));
        }

        TextView tvCancel = bottomDialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(view -> {
            bottomDialog.dismiss();
        });

        bottomDialog.show();
    }


    /**
     * 打开高德地图导航功能
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openGaoDeNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        StringBuilder builder = new StringBuilder("amapuri://route/plan?sourceApplication=maxuslife");
        if (slat != 0) {
            builder.append("&sname=").append(sname)
                    .append("&slat=").append(slat)
                    .append("&slon=").append(slon);
        }
        builder.append("&dlat=").append(dlat)
                .append("&dlon=").append(dlon)
                .append("&dname=").append(dname)
                .append("&dev=0")
                .append("&t=0");
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_GAODE_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

    /**
     * 打开百度地图导航功能(默认坐标点是高德地图，需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openBaiDuNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        //终点坐标转换  需要实现的在此处进行坐标转换

        double destination[] = gaoDeToBaidu(dlat, dlon);
        dlat = destination[0];
        dlon = destination[1];

        StringBuilder builder = new StringBuilder("baidumap://map/direction?mode=driving&");
        if (slat != 0) {
            //起点坐标转换

            double[] origin = gaoDeToBaidu(slat, slon);
            slat = origin[0];
            slon = origin[1];

            builder.append("origin=latlng:")
                    .append(slat)
                    .append(",")
                    .append(slon)
                    .append("|name:")
                    .append(sname);
        }
        builder.append("&destination=latlng:")
                .append(dlat)
                .append(",")
                .append(dlon)
                .append("|name:")
                .append(dname);
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_BAIDU_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }


    /**
     * 打开腾讯地图
     * params
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openTencentMap(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        StringBuilder builder = new StringBuilder("qqmap://map/routeplan?type=drive&policy=0&referer=zhongshuo");
        if (slat != 0) {
            builder.append("&from=").append(sname)
                    .append("&fromcoord=").append(slat)
                    .append(",")
                    .append(slon);
        }
        builder.append("&to=").append(dname)
                .append("&tocoord=").append(dlat)
                .append(",")
                .append(dlon);
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_TENCENT_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

    private static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }
}
