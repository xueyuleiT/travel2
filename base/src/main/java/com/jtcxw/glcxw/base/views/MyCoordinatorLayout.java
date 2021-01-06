package com.jtcxw.glcxw.base.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;


public class MyCoordinatorLayout extends CoordinatorLayout {
    public MyCoordinatorLayout(@NonNull Context context) {
        super(context);
    }

    public MyCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }



    private float mX,mY;
    private boolean mBlock = false;

    private int mLastXIntercept;
    private int mLastYIntercept;
    private boolean hasIntercepted = false;
    private boolean mBlockAble = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e){
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            boolean intercepted = false;
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            final int action = ev.getAction() & MotionEvent.ACTION_MASK;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mBlockAble = true;
                    mX = ev.getX();
                    mY = ev.getY();
                    for (int i = 0; i < getChildCount(); i ++) {
                        View v = getChildAt(i);
                        if (!(v instanceof AppBarLayout)) {
                            if (inRangeOfView(v,ev)) {
                                mBlockAble = false;
                            }
                        }
                    }

                    if (hasIntercepted && mBlockAble) {
                        hasIntercepted = false;
                        return true;
                    }
                    intercepted = super.onInterceptTouchEvent(ev);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mBlockAble) {
                        break;
                    }
                    //横坐标位移增量
                    int deltaX = x - mLastXIntercept;
                    //纵坐标位移增量
                    int deltaY = y - mLastYIntercept;
                    if (Math.abs(deltaX) < Math.abs(deltaY) && Math.abs(y - mY) > 10){
                        hasIntercepted = true;
                        intercepted = true;
                    }else{
                        intercepted = false;
//                    hasIntercepted = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_CANCEL:
                    intercepted = false;
                    hasIntercepted = false;
                    break;

                default:

                    break;
            }

            mLastXIntercept = x;
            mLastYIntercept = y;
            intercepted = intercepted || hasIntercepted;
            return intercepted;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (hasIntercepted) {
            event.setAction(MotionEvent.ACTION_DOWN);
//            dispatchTouchEvent(event);
//            return true;
        }
        return super.onTouchEvent(event);
    }


    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        return !(ev.getX() < x) && !(ev.getX() > (x + view.getWidth())) && !(ev.getY() < y) && !(ev.getY() > (y + view.getHeight()));
    }
}
