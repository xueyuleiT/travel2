package androidx.fragment.app;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatDialog;

import com.jtcxw.glcxw.base.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

public abstract class BaseDialog extends DialogFragment {

    protected View mRoot;

    public BaseDialog() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_loading)));
        mRoot = inflater.inflate(getLayoutId(),null);
        return mRoot;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels);

        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (cancelEnable() && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                dismiss();
            }
            return true;
        });

        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //←重点在这里，来，都记下笔记

    }



    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null && bottomSheet.getLayoutParams() != null) {
                bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT; //可以写入自己想要的高度
            }
        }
        dialog.setCanceledOnTouchOutside(cancelEnable());

        if (!hideEnable()) {
            View root = getDialog().findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior behavior = BottomSheetBehavior.from(root);
            behavior.setHideable(false);
        }

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;

        window.setAttributes(windowParams);
    }
    protected boolean hideEnable() {
        return true;
    }


    protected boolean cancelEnable() {
        return true;
    }

   abstract protected int getLayoutId();

    @Override
    public void dismiss() {
        if (getFragmentManager() == null){
            return;
        }
        super.dismiss();
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        mDismissed = false;
        mShownByMe = true;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AppCompatDialog(getContext(), getTheme());
    }

    /** @hide */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        if (dialog instanceof AppCompatDialog) {
            // If the dialog is an AppCompatDialog, we'll handle it
            AppCompatDialog acd = (AppCompatDialog) dialog;
            switch (style) {
                case STYLE_NO_INPUT:
                    dialog.getWindow().addFlags(
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    // fall through...
                case STYLE_NO_FRAME:
                case STYLE_NO_TITLE:
                    acd.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            }
        } else {
            // Else, just let super handle it
            super.setupDialog(dialog, style);
        }
    }
}
