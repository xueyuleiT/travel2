package com.jtcxw.glcxw.base.utils;

import androidx.fragment.app.FragmentManager;

import com.jtcxw.glcxw.base.dialogs.LoadingDialog;

public class DialogUtil {

    public static LoadingDialog getLoadingDialog(FragmentManager fragmentManager){
        LoadingDialog loadingDialog = new LoadingDialog();
        if (fragmentManager != null && !fragmentManager.isStateSaved()) {
            loadingDialog.show(fragmentManager, "loadingDialog");
        }
        return loadingDialog;

    }

    public static LoadingDialog getLoadingDialog(FragmentManager fragmentManager, String loadingText){

        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(fragmentManager,"loadingDialog");
        return loadingDialog;

    }

}
