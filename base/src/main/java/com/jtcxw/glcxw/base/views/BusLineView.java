package com.jtcxw.glcxw.base.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.jtcxw.glcxw.base.R;
import com.jtcxw.glcxw.base.localmodels.BusLineItem;
import com.jtcxw.glcxw.base.respmodels.BusArriveListBean;
import com.jtcxw.glcxw.base.utils.DimensionUtil;
import com.jtcxw.glcxw.base.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BusLineView extends View {

    private List<BusLineItem> mBusLines = new ArrayList<>();
    private List<BusArriveListBean.StationLineListBean.ForcastArriveVehsBean> mArriveVehs = new ArrayList<>();
    private int mBusStationWidth;
    private Drawable mStart,mEnd;
    private Drawable mBus, mBusSmall, mCurrentPosition, mCurrentPositionLocation, mRailway;
    private int mMargin1X, mMargin2P;
    private float mStrokeWidth;
    private int mColorPass, mColorUnreach, mSelectedColor, mColorBlock, mColorBusy, mColorNormal;

    private Rect mTxtRect;
    private RectF mBusRect, mLocationRectF;

    private Paint mStationCirclePaint, mTxtPaint;

    private float mNumTxtSize, mStationNameTxtSize, mSelectedTxtSize;
    private int mHeight;

    private OnBusStationClickListener mOnBusStationClickListener;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private boolean mIsScroll;
    private int mTouchSlop, mMinimumFlingVelocity;

    public BusLineView(Context context) {
        super(context);
        init(context);
    }

    public BusLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BusLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new OverScroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity() * 5;

        mStart = getResources().getDrawable(R.mipmap.icon_trans_start);
        mEnd = getResources().getDrawable(R.mipmap.icon_trans_end);
        mBus = getResources().getDrawable(R.mipmap.ic_bus);
        mBusSmall = getResources().getDrawable(R.drawable.ic_bus_small);
        mCurrentPosition = getResources().getDrawable(R.drawable.ic_current_bus_station);
        mCurrentPositionLocation = getResources().getDrawable(R.drawable.ic_current_bus_station_location);
        mRailway = getResources().getDrawable(R.drawable.ic_railway);

        mBusStationWidth = ((BitmapDrawable)mBus).getBitmap().getWidth() << 1;
        float density = getResources().getDisplayMetrics().density;
        mMargin1X = (int) (10 * density);
        mMargin2P = (int) (2 * density);

        mStationCirclePaint = new Paint();
        mStrokeWidth = 2 * density;
        mStationCirclePaint.setStrokeWidth(mStrokeWidth);
        mStationCirclePaint.setStyle(Paint.Style.STROKE);
        mStationCirclePaint.setAntiAlias(true);
        mTxtPaint = new Paint();
        mTxtPaint.setAntiAlias(true);

        mColorPass = getContext().getResources().getColor(R.color.green_light);
        mColorUnreach = Color.parseColor("#C0C0C0");
        mSelectedColor = Color.parseColor("#FE771D");

        mColorBlock = Color.parseColor("#F41C0D");
        mColorBusy = Color.parseColor("#FFAC00");
        mColorNormal = Color.parseColor("#00BD00");

        mTxtRect = new Rect();
        mBusRect = new RectF();
        mLocationRectF = new RectF();
        float scaleDensity = getResources().getDisplayMetrics().scaledDensity;
        mNumTxtSize = 14 * scaleDensity;
        mStationNameTxtSize = 14 * scaleDensity;
        mSelectedTxtSize = 16 * scaleDensity;
    }

    public List<BusLineItem> getBusLineData() {
        return this.mBusLines;
    }

    public List<BusArriveListBean.StationLineListBean.ForcastArriveVehsBean> getArriveVehs() {
        return this.mArriveVehs;
    }

    /**
     * 设置公交车数据
     * @param list 公交车列表数据
     */
    public void setBusLineData(List<BusLineItem> list) {
        mBusLines.clear();
        if (list == null) {
            return;
        }

        mBusLines.addAll(list);
        mHeight = calHeight();
        invalidate();
        setMeasuredDimension(mBusStationWidth * (mBusLines.size()) + getPaddingLeft() + getPaddingRight(),
                mHeight + getPaddingTop() + getPaddingBottom());
    }



    /**
     * 设置公交站显示的宽度，至少是公交车图标的两倍宽度。
     * @param width 宽度值
     */
    public void setBusStationWidth(int width) {
        if (width > mBusStationWidth) {
            mBusStationWidth = width;
        }
    }

    /**
     * 设置公交图标
     * @param d Drawable
     */
    public void setBusDrawable(Drawable d) {
        mBus = d;
        mBusStationWidth = d.getIntrinsicWidth() << 1;
    }

    /**
     * 设置小公交图标
     * @param d Drawable
     */
    public void setBusSmallDrawable(Drawable d) {
        mBusSmall = d;
    }

    /**
     * 设置当前位置显示的图标
     * @param d Drawable
     */
    public void setCurrentPositionDrawable(Drawable d) {
        mCurrentPosition = d;
    }

    /**
     * 设置定位的图标
     * @param d Drawable
     */
    public void setCurrentLocationDrawable(Drawable d) {
        mCurrentPositionLocation = d;
    }

    /**
     * 设置有地铁标识的公交站图标
     * @param d Drawable
     */
    public void setMetroStationDrawable(Drawable d) {
        mRailway = d;
    }

    /**
     * 设置不交通状况的颜色
     * @param normal 正常畅通的颜色
     * @param busy 交通繁忙的颜色
     * @param block 交通拥堵的颜色
     */
    public void setTrafficColor(int normal, int busy, int block) {
        mColorNormal = normal;
        mColorBusy = busy;
        mColorBlock = block;
    }

    /**
     * 设置路线的颜色
     * @param pass 到当前站点的颜色
     * @param unreach 当前站点以后和颜色
     * @param selected 选中公交站时的颜色
     */
    public void setBusLineColor(int pass, int unreach, int selected) {
        mColorPass = pass;
        mColorUnreach = unreach;
        mSelectedColor = selected;
    }

    /**
     * 设置公交站被点击的监听器
     * @param l listener
     */
    public void setOnBusStationClickListener(OnBusStationClickListener l) {
        mOnBusStationClickListener = l;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private float mDownX, mDownY;
    private long mDowntime = 0l;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    return super.dispatchTouchEvent(event);
                }

                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float dx = x - mDownX;
                float dy = y - mDownY;

                if (!mIsScroll && Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > mTouchSlop) {
                    mIsScroll = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        addVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mIsScroll && !mScroller.isFinished()) {
                    // fling后点击屏幕停止
                    mScroller.abortAnimation();
                }
                mDowntime = System.currentTimeMillis();
                mDownX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!mIsScroll) {
                    return super.onTouchEvent(event);
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                float x = event.getX();
                scrollBy((int) (mDownX - x), 0);
                mDownX = x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float velocityX = getScrollVelocity();

                Rect r = new Rect();
                getLocalVisibleRect(r);
                if (Math.abs(velocityX) <= mMinimumFlingVelocity) {
                    int sx = getScrollX();
                    int dx = sx + r.width() - getWidth();
                    if (sx < 0) {
                        mScroller.startScroll(sx, 0, -sx, 0, -sx * 2);
                        invalidate();
                    } else if (dx > 0) {
                        mScroller.startScroll(sx, 0, -dx, 0, dx * 2);
                        invalidate();
                    } else if (!mIsScroll && event.getAction() == MotionEvent.ACTION_UP && System.currentTimeMillis() - mDowntime < 500) {
                        onTapUp(event, r.width());
                    }

                    mIsScroll = false;
                    break;
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                mScroller.fling(getScrollX(), 0, (int) -velocityX, 0,
                        0, getWidth() - r.width(), 0, 0, r.width() / 2, 0);
                invalidate();

                mIsScroll = false;
                recycleVelocityTracker();

//                if (System.currentTimeMillis() - mDowntime < 500 && Math.abs(event.getX() - mDownX) < 10 && Math.abs(event.getY() - mDownY) < 10) {
//
//                }

                break;
        }

        return super.onTouchEvent(event);
    }

    public void scrollToCurrent() {
        int i = 0;
        for (BusLineItem item : mBusLines) {
            i ++;
            if (item.isCurrentPosition) {
                Rect r = new Rect();
                getLocalVisibleRect(r);
                int x = i * mBusStationWidth - r.width() / 2 - mBusStationWidth / 2;
                if (x < 0) {
                    scrollTo(0, mScroller.getCurrY());
                } else if (x + r.width() > getWidth()) {
                    scrollTo(getWidth() - r.width(), mScroller.getCurrY());
                } else {
                    scrollTo(x, mScroller.getCurrY());
                }
                invalidate();
                break;
            }
        }
    }

    private void onTapUp(MotionEvent event, int visibleWidth) {
        float x = event.getX();
        float y = event.getY();
        BusLineItem bli = null;

        for (BusLineItem item : mBusLines) {
            if (item.location != null && item.location.contains(x + getScrollX(), y + getScrollY()) && !item.isCurrentPosition) {
                bli = item;
                break;
            }
        }

        if (bli != null) {
            for (BusLineItem item : mBusLines) {
                item.isCurrentPosition = false;
            }

            bli.isCurrentPosition = true;
            BusLineItem firstItem = mBusLines.get(0);
            int pos = visibleWidth - mBusStationWidth;
            int dx = (int) (pos - bli.location.centerX() + getScrollX());

            if (getScrollX() - dx < 0) {
                // 第一个边界处理
                dx = getScrollX();
            } else if (getScrollX() + visibleWidth - dx > getWidth()) {
                // 最后一个边界处理
                dx = getScrollX() + visibleWidth - getWidth();
            }


            mScroller.startScroll(getScrollX(), 0, -dx, 0, Math.abs(dx) * 2);
            invalidate();

            if (mOnBusStationClickListener != null) {
                mOnBusStationClickListener.onBusStationClick(BusLineView.this, bli);
            }
        }
    }

    private void addVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private float getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        return mVelocityTracker.getXVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mBusStationWidth * (mBusLines.size()) + getPaddingLeft() + getPaddingRight(),
                mHeight + getPaddingTop() + getPaddingBottom());
    }

    int calHeight() {
        int busHeight = mBus.getIntrinsicHeight(); // 公交图片的高度
        int margin1X = mMargin1X;
        int offsetY = margin1X;
        offsetY += margin1X + busHeight;
        offsetY += margin1X * 4;

        int maxY = 0;
        mTxtPaint.setTextSize(mSelectedTxtSize);
        for (int i = 0, size = mBusLines.size(); i < size; ++i) {
            BusLineItem item = mBusLines.get(i);
            int oy = offsetY;
            item.name =  item.name.replaceAll("（","(");
            item.name =  item.name.replaceAll("）",")");
            for (int k = 0, length = item.name.length(); k < length; ++k) {
                String s = String.valueOf(item.name.charAt(k));
                if (s.equals("(") || s.equals(")")) {
                    measureText(s, mTxtRect, mTxtPaint);
                    if (s.equals(")")) {
                        oy -= mTxtRect.height();
                        oy += mTxtRect.width() + mMargin2P*2;
                    }
                    oy += mTxtRect.height();
                } else {
                    measureText(s, mTxtRect, mTxtPaint);
                    oy += mTxtRect.height() + mMargin2P;
                    if (s.equals("一")) {
                        oy += mTxtRect.height();
                    }
                }
            }



            if (item.lines != null) {
                oy += mMargin2P;
                Paint paint = new Paint();
                paint.setTextSize(mNumTxtSize);
                for (int k = 0, length = item.lines.size(); k < length; ++k) {
                    String s = item.lines.get(k);
                    oy += mMargin2P;
                    for (int l = 0; l < s.length(); l++) {
                        measureText(String.valueOf(s.charAt(l)), mTxtRect, paint);
                        oy += mTxtRect.height() + mMargin2P;
                    }
                    oy += mMargin2P * 2;
                }

                oy += mMargin2P * 2;
            }
            if (oy > maxY) {
                maxY = oy;
            }
        }
        return maxY;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBusLines.size() == 0) {
            return;
        }

        long start = 0;

        int margin1X = mMargin1X;
        float startX = getPaddingLeft();
        float startY = getPaddingTop();

        int busStationWidth = mBusStationWidth;

        int margin2X = margin1X << 1;
        int busHeight = mBus.getIntrinsicHeight(); // 公交图片的高度

        int currentPosWidth = mCurrentPosition.getIntrinsicWidth();
        int halfCurrentPosWidth = currentPosWidth >> 1;
        int locationHeight = mCurrentPosition.getIntrinsicHeight();
        int halfLocationHeight = locationHeight >> 1;

        float lineStartY = startY + busHeight + margin2X + (locationHeight >> 1);

        int halfBusStationWidth = busStationWidth >> 1;
        startX += halfBusStationWidth;

        // 整个线路
        mStationCirclePaint.setColor(mColorPass);
        mStationCirclePaint.setStrokeWidth(DimensionUtil.Companion.dpToPx(4));
        canvas.drawLine(startX, lineStartY, busStationWidth * mBusLines.size() - halfBusStationWidth, lineStartY, mStationCirclePaint);



        int halfBusWidth = ((BitmapDrawable)mBus).getBitmap().getWidth() >> 1;


        boolean hasDrawBus = false;

        for (int i = 0, size = mBusLines.size(); i < size; ++i) {
            BusLineItem item = mBusLines.get(i);
            if (item.location == null) {
                item.location = new RectF();
            }

            float pos = startX + i * busStationWidth; // 公交站点的起始位置
            // 路况
            if (item.trafficState != null) {
                for (int k = 0, length = item.trafficState.length; k < length; ++k) {
                    BusLineItem.RoadState rs = item.trafficState[k];
                    float sx = busStationWidth * rs.start;
                    switch (rs.state) {
                        case BusLineItem.RoadState.ROAD_BLOCK:
                            mStationCirclePaint.setColor(mColorBlock);
                            break;
                        case BusLineItem.RoadState.ROAD_BUSY:
                            mStationCirclePaint.setColor(mColorBusy);
                            break;
                        case BusLineItem.RoadState.ROAD_NORMAL:
                            mStationCirclePaint.setColor(mColorNormal);
                            break;
                        default:
                    }

                    canvas.drawLine(pos + sx, lineStartY, pos + sx + busStationWidth * rs.ratio, lineStartY, mStationCirclePaint);
                }
            }

            int offsetY = margin1X;
            if (!hasDrawBus){
                for (BusArriveListBean.StationLineListBean.ForcastArriveVehsBean mArriveVeh : mArriveVehs) {
                    if (Integer.parseInt(mArriveVeh.getNextLevel()) == 0){
                        canvas.drawBitmap(((BitmapDrawable) mBus).getBitmap(), startX - halfBusWidth , offsetY, null);
                    } else if (Integer.parseInt(mArriveVeh.getNextLevel()) < mBusLines.size()) {
                        canvas.drawBitmap(((BitmapDrawable) mBus).getBitmap(), startX + Integer.parseInt(mArriveVeh.getNextLevel()) * busStationWidth - busStationWidth / 2 - halfBusWidth, offsetY, null);
                    } else  {
                        canvas.drawBitmap(((BitmapDrawable) mBus).getBitmap(), startX + (Integer.parseInt(mArriveVeh.getNextLevel()) - 1) * busStationWidth - halfBusWidth , offsetY, null);
                    }
                }
                hasDrawBus = true;
            }
            offsetY += margin1X;

            offsetY += busHeight;
            item.location.top = offsetY;

            mStationCirclePaint.setStyle(Paint.Style.FILL);
            if (getBackground() instanceof ColorDrawable) {
                mStationCirclePaint.setColor(((ColorDrawable) getBackground()).getColor());
            } else {
                mStationCirclePaint.setColor(Color.WHITE);
            }

            if (i == 0){
                canvas.drawCircle(pos, offsetY + halfLocationHeight, (((BitmapDrawable) mStart).getBitmap().getWidth() >> 1) + (item.isCurrentPosition ? mStrokeWidth * 2 : mStrokeWidth) , mStationCirclePaint);
            } else if (i == mBusLines.size() - 1){
                canvas.drawCircle(pos, offsetY + halfLocationHeight, (((BitmapDrawable) mEnd).getBitmap().getWidth() >> 1) + (item.isCurrentPosition ? mStrokeWidth * 2 : mStrokeWidth) , mStationCirclePaint);
            } else {
                canvas.drawCircle(pos, offsetY + halfLocationHeight, halfCurrentPosWidth + (item.isCurrentPosition ? mStrokeWidth : 0), mStationCirclePaint);
            }
            mStationCirclePaint.setColor(mColorPass);
            if (item.isCurrentPosition) {
                canvas.drawCircle(pos, offsetY + halfLocationHeight, halfCurrentPosWidth, mStationCirclePaint);
            } else {
                canvas.drawCircle(pos, offsetY + halfLocationHeight, halfCurrentPosWidth - mStrokeWidth, mStationCirclePaint);
            }
            if (i == 0)
                canvas.drawBitmap(((BitmapDrawable) mStart).getBitmap(), pos - (((BitmapDrawable) mStart).getBitmap().getWidth() >> 1), offsetY + halfLocationHeight - (((BitmapDrawable) mStart).getBitmap().getHeight() >> 1), null);
            else if (i == mBusLines.size() - 1) {
                canvas.drawBitmap(((BitmapDrawable) mEnd).getBitmap(), pos - (((BitmapDrawable) mEnd).getBitmap().getWidth() >> 1) , offsetY + halfLocationHeight - (((BitmapDrawable) mEnd).getBitmap().getHeight() >> 1), null);
            }


            // 名称
            mTxtPaint.setColor(item.isCurrentPosition ? mSelectedColor : getContext().getResources().getColor(R.color.gray_9));
            mTxtPaint.setTextSize(item.isCurrentPosition ? mSelectedTxtSize : mStationNameTxtSize);
            offsetY +=  margin1X * 4;
                   int oy = offsetY;

            for (int k = 0, length = item.name.length(); k < length; ++k) {
                String s = String.valueOf(item.name.charAt(k));
                if (s.equals("(") || s.equals(")")) {
                    canvas.save();
                    measureText(s, mTxtRect, mTxtPaint);
                    if (s.equals(")")) {
                        oy -= mTxtRect.height();
                        oy += mTxtRect.width() + mMargin2P*2;
                    }
                    canvas.rotate(90,pos - (mTxtRect.width() + mMargin2P)/2 , oy );
                    canvas.drawText(s, pos - mTxtRect.height()/2, oy   , mTxtPaint);
                    oy += mTxtRect.height();
                    canvas.restore();
                } else {
                    measureText(s, mTxtRect, mTxtPaint);
                    canvas.drawText(s, pos -  (mTxtRect.width() >> 1), oy, mTxtPaint);
                    oy += mTxtRect.height() + mMargin2P;
                    if (s.equals("一")) {
                        oy += mTxtRect.height();
                    }
                }
            }

            if (item.lines != null) {
                oy += mMargin2P;
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setTextSize(mNumTxtSize);
                for (int k = 0, length = item.lines.size(); k < length; ++k) {
                    String s = item.lines.get(k);
                    int height = mMargin2P;
                    for (int l = 0; l < s.length(); l ++) {
                        measureText(String.valueOf(s.charAt(l)), mTxtRect, paint);
                        height += mTxtRect.height() + mMargin2P;

                    }
                    height += mMargin2P;
                    RectF rectF = new RectF();
                    rectF.left = mTxtRect.left + pos - mTxtRect.width() / 2 - mMargin2P;
                    rectF.right = mTxtRect.right + pos - mTxtRect.width() / 2 + mMargin2P;
                    rectF.top = oy;
                    rectF.bottom = height + oy ;
                    paint.setColor(getContext().getResources().getColor(R.color.green_light));
                    canvas.drawRoundRect(rectF, rectF.width() / 2, rectF.width() / 2, paint);
                    oy += mMargin2P;
                    paint.setColor(Color.WHITE);
                    for (int l = 0; l < s.length(); l ++) {
                        char value = s.charAt(l);
                            measureText(String.valueOf(value), mTxtRect, paint);
                            canvas.drawText(String.valueOf(value), pos - (mTxtRect.width() >> 1), oy + mTxtRect.height(), paint);
                            oy += mTxtRect.height() + mMargin2P;
                    }
                    oy += mMargin2P * 2;
                }

                oy += mMargin2P * 2;
            }

            item.location.left = pos + halfBusStationWidth - busStationWidth;
            item.location.right = item.location.left + busStationWidth;

            if (item.isRailwayStation) {
                // 地铁站
                canvas.drawBitmap(((BitmapDrawable) mRailway).getBitmap(), pos - (mRailway.getIntrinsicWidth() >> 1), oy - mTxtRect.height() + mMargin2P, null);
            }

            item.location.bottom = oy + mMargin2P;
        }

    }

    private void measureText(String s, Rect r, Paint p) {
        p.getTextBounds(s, 0, s.length(), r);
    }

    private boolean hit(RectF src, RectF dst) {
        return src.contains(dst)
                || src.contains(dst.left, dst.top)
                || src.contains(dst.left, dst.bottom)
                || src.contains(dst.right, dst.bottom)
                || src.contains(dst.right, dst.top)
                || dst.contains(src);
    }

    public interface OnBusStationClickListener {
        void onBusStationClick(View view, BusLineItem item);
    }
}

