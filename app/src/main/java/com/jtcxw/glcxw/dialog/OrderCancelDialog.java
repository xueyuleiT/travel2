package com.jtcxw.glcxw.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BaseBottomSheetDialogFragment;
import com.jtcxw.glcxw.R;
import com.jtcxw.glcxw.listeners.OrderCancelCallback;

public class OrderCancelDialog extends DialogFragment {

    private OrderCancelCallback mOrderCancelCallback = null;
    private String mTitle;

    public OrderCancelDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public OrderCancelDialog setOrderCancelCallback(OrderCancelCallback orderCancelCallback) {
        mOrderCancelCallback = orderCancelCallback;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_order_cancel, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BaseBottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.iv_close).setOnClickListener(view1 -> {
            dismiss();
        });
        if (!TextUtils.isEmpty(mTitle)) {
            ((TextView) view.findViewById(R.id.tv_title)).setText(mTitle);
        }
        view.findViewById(R.id.tv_confirm).setOnClickListener(view1 -> {
            if (mOrderCancelCallback != null) {
                mOrderCancelCallback.onOrderConfirm();
            }
            dismiss();
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> {
            if (mOrderCancelCallback != null) {
                mOrderCancelCallback.onOrderCancel();
            }
            dismiss();
        });


    }
}
