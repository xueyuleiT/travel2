package com.glcxw.lib.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AmountUtil {
    public static String format(Number d){
        DecimalFormat df = new DecimalFormat("#.00");
        String result =  df.format(d);
        if (result != null && result.startsWith(".")){
            result = "0"+result;
        }
        return result;
    }

    public static double formatDouble(double d,int count) {
        BigDecimal bg = new BigDecimal(d).setScale(count, RoundingMode.UP);
        return bg.doubleValue();
    }

//    public static String format(String d,String pattern){
//        try {
//            Double dAmount =  Double.valueOf(d);
//            DecimalFormat df = new DecimalFormat(pattern);
//            return df.format(dAmount);
//        }catch (Exception e){
//            return d;
//        }
//    }
//
//    public static String format(String d){
//        try {
//            Double dAmount =  Double.valueOf(d);
//            if (d.contains(".")){
//                if (dAmount > 100000){
//                    dAmount = dAmount/10000.0;
//                    DecimalFormat df = new DecimalFormat("##.00");
//                    return df.format(dAmount) + "万";
//                }else {
//                    DecimalFormat df = new DecimalFormat("##.00");
//                    String result = df.format(dAmount);
//                    if (result.startsWith(".")){
//                        result = "0" + result;
//                    }
//                    return result;
//                }
//            }else {
//                if (dAmount > 100000) {
//                    dAmount = dAmount/10000.0;
//                    DecimalFormat df = new DecimalFormat("##.00");
//                    return df.format(dAmount) + "万";
//                }else {
//                    DecimalFormat df = new DecimalFormat("##");
//                    String result = df.format(dAmount);
//                    if (result.startsWith(".")){
//                        result = "0" + result;
//                    }
//                    return result;
//                }
//            }
//        }catch (Exception e){
//            return d;
//        }
//
//    }

    public static String formatTosepara(Number data) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        String result = df.format(data);
        if (result != null && result.startsWith(".")){
            result = "0"+result;
        }
        return result;
    }

    public static String formatTosepara(String data) {
        if (TextUtils.isEmpty(data)){
            return "";
        }
        try {
            Double dAmount =  Double.valueOf(data);
            String tag = dAmount >= 0 ? "" : "-";
            dAmount = Math.abs(dAmount);
            DecimalFormat df = new DecimalFormat("#,###.00");
            String result = df.format(dAmount);
            if (result != null && result.startsWith(".")){
                result = "0"+result;
            }
            result = tag + result;
            return result;
        } catch (Exception e){
            return data;
        }
    }

    public static String formatToseparaWithoutFloat(String data) {
        if (TextUtils.isEmpty(data)){
            return "";
        }
        try {
            Double dAmount =  Double.valueOf(data);
            String tag = dAmount >= 0 ? "" : "-";
            dAmount = Math.abs(dAmount);
            DecimalFormat df = new DecimalFormat("#,###");
            String result = df.format(dAmount);
            if (result != null && result.startsWith(".")){
                result = "0"+result;
            }
            result = tag + result;
            return result;
        } catch (Exception e){
            return data;
        }
    }


    public static String formatAmount(String amount){
        if (TextUtils.isEmpty(amount)){
            return "";
        }
        try {
           Double dAmount = Double.valueOf(amount);
           String tag = dAmount >= 0 ? "" : "-";
           dAmount = Math.abs(dAmount);
            if (dAmount > 100000){
                return tag + formatTosepara(dAmount/10000.0)+"万";
            }
            return tag + formatTosepara(dAmount);
        } catch (Exception e){
            return "";
        }
    }


}
