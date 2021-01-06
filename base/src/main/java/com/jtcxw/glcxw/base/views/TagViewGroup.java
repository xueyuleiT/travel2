package com.jtcxw.glcxw.base.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class TagViewGroup extends RelativeLayout {

    private final static int VIEW_MARGIN=2;

    public TagViewGroup(Context context) {
        super(context);
    }

    public TagViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            // measure
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }

    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        final int count = getChildCount();
        int row=0;// which row lay you view relative to parent
        int lengthX=arg1;    // right position of child relative to parent
        int lengthY=arg2;    // bottom position of child relative to parent
        for(int i=0;i<count;i++){

            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            lengthX+=width+VIEW_MARGIN;
            lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+arg2;
            //if it can't drawing on a same line , skip to next line
            if(lengthX>arg3){
                lengthX=width+VIEW_MARGIN+arg1;
                row++;
                lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+arg2;

            }

            child.layout(lengthX-width, lengthY-height, lengthX, lengthY);
        }

    }

}