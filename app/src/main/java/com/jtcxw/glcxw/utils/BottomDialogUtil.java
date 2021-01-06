package com.jtcxw.glcxw.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jtcxw.glcxw.R;
import com.jtcxw.glcxw.base.utils.ScreenUtil;
import com.jtcxw.glcxw.listeners.DialogCallback;

public class BottomDialogUtil {

    public static void showHeadDialog(Context context, DialogCallback dialogCallback) {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);
        bottomDialog.setContentView(R.layout.dialog_bottom_map);


        WindowManager.LayoutParams params = bottomDialog.getWindow().getAttributes();
        params.width = ScreenUtil.getScreenWidth(context);
        bottomDialog.getWindow().setAttributes(params);

        TextView tvPhoto = bottomDialog.findViewById(R.id.tv_gd);
        tvPhoto.setText("拍照");
        tvPhoto.setTextColor(context.getResources().getColor(R.color.green_light));
        tvPhoto.setOnClickListener(view -> {
            dialogCallback.onDialogCallback(1);
                bottomDialog.dismiss();
            });


        TextView tvGallery = bottomDialog.findViewById(R.id.tv_bd);
        tvGallery.setText("我的相册");
        tvGallery.setTextColor(context.getResources().getColor(R.color.green_light));
        tvGallery.setOnClickListener(view -> {
            dialogCallback.onDialogCallback(2);
                bottomDialog.dismiss();
            });


        TextView tvTx = bottomDialog.findViewById(R.id.tv_tx);
        tvTx.setVisibility(View.GONE);

        TextView tvCancel = bottomDialog.findViewById(R.id.tv_cancel);
        tvCancel.setText("取消");
        tvCancel.setTextColor(context.getResources().getColor(R.color.green_light));
        tvCancel.setOnClickListener(view -> {
            dialogCallback.onDialogCallback(0);
            bottomDialog.dismiss();
        });

        bottomDialog.show();
    }


}
