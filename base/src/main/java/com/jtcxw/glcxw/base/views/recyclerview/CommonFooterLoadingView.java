package com.jtcxw.glcxw.base.views.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.jtcxw.glcxw.base.R;
import com.scwang.smartrefresh.header.internal.MaterialProgressDrawable;

/**
 * Created by caiyu on 2017/7/28.
 */

public class CommonFooterLoadingView extends FrameLayout {

    private final ImageView mImageView;

    private final MaterialProgressDrawable mProgressDrawable;

    public CommonFooterLoadingView(@NonNull Context context) {
        this(context, null);
    }

    public CommonFooterLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mImageView = new ImageView(getContext());
        mImageView.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));
        mProgressDrawable = new MaterialProgressDrawable(getContext(), mImageView);
        initProgressDrawable();
        mImageView.setImageDrawable(mProgressDrawable);

        this.addView(mImageView);
    }

    private void initProgressDrawable() {
        mProgressDrawable.setAlpha(255);
        mProgressDrawable.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mProgressDrawable.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mProgressDrawable.stop();
    }

}
