package com.jtcxw.glcxw.base.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.BaseDialog;


import com.jtcxw.glcxw.base.R;

import me.yokeyword.fragmentation.SupportActivity;

public class LoadingDialog extends BaseDialog {

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_loading;
    }

    @Override
    protected boolean cancelEnable() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
         return view;
    }

    @Override
    public void dismiss() {
        if (getFragmentManager() == null || getContext() == null || ((SupportActivity)getContext()).isFinishing()){
            return;
        }
        super.dismissAllowingStateLoss();
    }
}
