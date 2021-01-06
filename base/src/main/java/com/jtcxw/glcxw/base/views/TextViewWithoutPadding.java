package com.jtcxw.glcxw.base.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.jtcxw.glcxw.base.R;

public class TextViewWithoutPadding extends AppCompatTextView {

    private final Paint mPaint = new Paint();

    private final Rect mBounds = new Rect();

    public TextViewWithoutPadding(Context context) {
        this(context, null);
    }

    public TextViewWithoutPadding(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public TextViewWithoutPadding(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextViewWithoutPadding, defStyleAttr, 0);
        boolean bold = a.getBoolean(R.styleable.TextViewWithoutPadding_boldText,false);
        if (bold){
            mPaint.setFakeBoldText(true);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        final String text = calculateTextParams();

        final int left = mBounds.left;
        final int bottom = mBounds.bottom;
        mBounds.offset(-mBounds.left, -mBounds.top);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getCurrentTextColor());
        canvas.drawText(text, -left, mBounds.bottom - bottom, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateTextParams();
        setMeasuredDimension(mBounds.right - mBounds.left, -mBounds.top + mBounds.bottom);
    }

    private String calculateTextParams() {
        final String text = getText().toString();
        final int textLength = text.length();

        mPaint.setTextSize(getTextSize());
        mPaint.getTextBounds(text, 0, textLength, mBounds);
        if (textLength == 0) {
            mBounds.right = mBounds.left;
        }
        return text;
    }
}
