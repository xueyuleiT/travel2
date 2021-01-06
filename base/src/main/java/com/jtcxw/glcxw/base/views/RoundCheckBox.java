package com.jtcxw.glcxw.base.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.jtcxw.glcxw.base.R;


public class RoundCheckBox extends AppCompatCheckBox {

    public RoundCheckBox(Context context) {
        this(context, null);
    }

    public RoundCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public RoundCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}