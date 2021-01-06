package com.jtcxw.glcxw.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


import com.jtcxw.glcxw.listeners.CompressCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by qin on 2018/5/3.
 */

public class ImageUtil {

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public static String bitmapToBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //参数100表示不压缩
        if (bit != null) {
            bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        }
        byte[] bytes = bos.toByteArray();
        //Base64算法加密，当字符串过长（一般超过76）时会自动在中间加一个换行符，字符串最后也会加一个换行符。
        // 导致和其他模块对接时结果不一致。所以不能用默认Base64.DEFAULT，而是Base64.NO_WRAP不换行
        //return new String(Base64.encode(bytes,Base64.NO_WRAP));
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static void compressImage(Bitmap image, int size, CompressCallback cmpCallback) throws Throwable {
        if (size <= 0){
            throw new Throwable("size 必须大于0");
        }
        new Thread(() -> {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 95;
            while (baos.toByteArray().length / 1024 > size && options > 0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset(); // 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options *= 0.95;// 每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
            try {
                isBm.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cmpCallback.onCompressCallback(bitmap);

        }).start();

    }


    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image,int size) throws Throwable {
        if (size <= 0){
            throw new Throwable("size 必须大于0");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 95;
        while (baos.toByteArray().length / 1024 > size && options > 0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options *= 0.95;// 每次都减少5
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        isBm.close();
        baos.close();
        return bitmap;

    }

}
