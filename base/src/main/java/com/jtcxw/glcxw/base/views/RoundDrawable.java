package com.jtcxw.glcxw.base.views;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.SystemClock;
import android.widget.ImageView.ScaleType;

public class RoundDrawable extends Drawable {
    // public static final String TAG = "RoundDrawable";

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width > 0 && height > 0) {
            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } else {
            bitmap = null;
        }

        return bitmap;
    }

    public static Drawable fromDrawable(Drawable drawable, float radius,
                                        int border, int borderColor, boolean isCircle) {
        if (drawable != null) {
            if (drawable instanceof TransitionDrawable) {
                TransitionDrawable td = (TransitionDrawable) drawable;
                int num = td.getNumberOfLayers();

                Drawable[] drawableList = new Drawable[num];
                for (int i = 0; i < num; i++) {
                    Drawable d = td.getDrawable(i);
                    if (d instanceof ColorDrawable) {
                        // TODO skip colordrawables for now
                        drawableList[i] = d;
                    } else {
                        drawableList[i] = new RoundDrawable(
                                drawableToBitmap(td.getDrawable(i)), radius,
                                border, borderColor, isCircle,drawable instanceof FadeDrawable);
                    }
                }
                return new TransitionDrawable(drawableList);
            }

            Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new RoundDrawable(bm, radius, border, borderColor,
                        isCircle,drawable instanceof FadeDrawable);
            }
        }
        return drawable;
    }

    private final RectF mBounds = new RectF();
    private final RectF mDrawableRect = new RectF();
    private float mCornerRadius;
    private final RectF mBitmapRect = new RectF();
    private final BitmapShader mBitmapShader;
    private final Paint mBitmapPaint;

    private Paint colorPaint;
    private/* final */ int mBitmapWidth;
    private/* final */ int mBitmapHeight;
    private final RectF mBorderRect = new RectF();

    private Paint mBorderPaint;

    private int mBorderWidth;

    // private boolean mIsCircle;


    private int mBorderColor;

    private ScaleType mScaleType = ScaleType.FIT_XY;

    private final Matrix mShaderMatrix = new Matrix();

    private boolean isDown = false;
    private boolean mFade;

    public RoundDrawable(Bitmap bitmap, float cornerRadius, int border,
                         int borderColor, boolean isCircle, boolean fade) {

        mBorderWidth = border;
        mBorderColor = borderColor;
        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mFade = fade;
        if (isCircle) {
            float widthScale = (mBitmapWidth / cornerRadius);
            float heightScale = (mBitmapHeight / cornerRadius);
            if (widthScale > 1 || heightScale > 1) {
                if (widthScale > heightScale) {
                    mBitmapWidth = (int) (mBitmapWidth / widthScale);
                    mBitmapHeight = (int) (mBitmapHeight / widthScale);
                } else {
                    mBitmapWidth = (int) (mBitmapWidth / heightScale);
                    mBitmapHeight = (int) (mBitmapHeight / heightScale);
                }
            }
        }

        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

        mCornerRadius = cornerRadius;
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mShaderMatrix);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setShader(mBitmapShader);

        colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    private static final float FADE_DURATION = 200f;
    private long startTimeMillis = SystemClock.uptimeMillis();
    private boolean animating = true;
    private int alpha = 0xFF;

    private PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

    @Override
    public void draw(Canvas canvas) {
        if (!mFade || !animating) {
            draw2(canvas);
        } else {
            float normalized = (SystemClock.uptimeMillis() - startTimeMillis) / FADE_DURATION;
            if (normalized >= 1f) {
                animating = false;
                draw2(canvas);
            } else {
                // setAlpha will call invalidateSelf and drive the animation.
                int partialAlpha = (int) (this.alpha * normalized);
                setAlpha(partialAlpha);
                draw2(canvas);
                setAlpha(this.alpha);
            }
        }
    }

    private void draw2(Canvas canvas) {
        if (mBorderWidth > 0) {
            if(!mFade) {
                colorPaint.setColor(Color.WHITE);
                colorPaint.setAlpha(255);
                canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius, colorPaint);
            }
            initBorderPaint( mBorderColor, mBorderWidth);
            canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius,
                    mBorderPaint);
            canvas.drawRoundRect(mDrawableRect,
                    Math.max(mCornerRadius - mBorderWidth, 0),
                    Math.max(mCornerRadius - mBorderWidth, 0), mBitmapPaint);

        } else {
            canvas.drawRoundRect(mDrawableRect, mCornerRadius, mCornerRadius, mBitmapPaint);
        }
        if (isDown) {
            colorPaint.setColor(Color.BLACK);
            colorPaint.setAlpha(100);
            canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius, colorPaint);
        }
    }

    private void initBorderPaint(int borderColor, int borderWidth) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(borderWidth);
            if (borderColor != 0) {
                mBorderPaint.setColor(borderColor);
            }
        } else {
            mBorderPaint.setStrokeWidth(borderWidth);
            if (borderColor != 0) {
                mBorderPaint.setColor(borderColor);
            }
        }
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public ScaleType getScaleType() {
        return mScaleType;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

//		mBounds.set(bounds);
        mBounds.set(mBorderWidth, mBorderWidth, bounds.right - mBorderWidth, bounds.bottom - mBorderWidth);

        // if (USE_VIGNETTE) {
        // RadialGradient vignette = new RadialGradient(
        // mDrawableRect.centerX(), mDrawableRect.centerY() * 1.0f / 0.7f,
        // mDrawableRect.centerX() * 1.3f,
        // new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
        // Shader.TileMode.CLAMP);
        //
        // Matrix oval = new Matrix();
        // oval.setScale(1.0f, 0.7f);
        // vignette.setLocalMatrix(oval);
        //
        // mBitmapPaint.setShader(
        // new ComposeShader(mBitmapShader, vignette,
        // PorterDuff.Mode.SRC_OVER));
        // }

        setMatrix();
    }

    @Override
    public void setAlpha(int alpha) {
        final int oldAlpha = mBitmapPaint.getAlpha();
        if (alpha != oldAlpha) {
            mBitmapPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    public void setBorderColor(int color) {
        this.mBorderColor = color;
    }

    public void setBorderWidth(int width) {
        this.mBorderWidth = width;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBitmapPaint.setColorFilter(cf);
    }

    public void setCornerRadius(float radius) {
        this.mCornerRadius = radius;
    }


    private void setMatrix() {
        mBorderRect.set(mBounds);
        mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth,
                mBorderRect.width() - mBorderWidth, mBorderRect.height()
                        - mBorderWidth);

        float scale;
        float dx = 0;
        float dy = 0;
        switch (mScaleType) {
            case CENTER:
                mBorderRect.set(mBounds);
                mDrawableRect.set(mBounds.left + mBorderWidth , mBounds.top + mBorderWidth ,
                        mBounds.right - mBorderWidth , mBounds.bottom - mBorderWidth );
                mShaderMatrix.set(null);
                dx = (mDrawableRect.width() - mBitmapWidth) * 0.5f  + mBorderWidth*2;
                dy = (mDrawableRect.height() - mBitmapHeight) * 0.5f + mBorderWidth*2;
                mShaderMatrix.setTranslate(dx,dy);
                break;
            case CENTER_CROP:
                mBorderRect.set(mBounds);
                mDrawableRect.set(mBounds.left + mBorderWidth , mBounds.top + mBorderWidth ,
                        mBounds.right - mBorderWidth , mBounds.bottom - mBorderWidth );

                mShaderMatrix.set(null);

                if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
                        * mBitmapHeight) {
                    scale = (float) mDrawableRect.height() / (float) mBitmapHeight;
                    dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f ;
                } else {
                    scale = (float) mDrawableRect.width() / (float) mBitmapWidth;
                    dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f ;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate(dx + mBorderWidth*2 ,
                        dy + mBorderWidth*2 );
                break;
            case CENTER_INSIDE:
                mShaderMatrix.set(null);

                if (mBitmapWidth <= mBounds.width()
                        && mBitmapHeight <= mBounds.height()) {
                    scale = 1.0f;
                } else {
                    scale = Math.min(
                            (float) mBounds.width() / (float) mBitmapWidth,
                            (float) mBounds.height() / (float) mBitmapHeight);
                }

                dx = (mBounds.width() - mBitmapWidth * scale) * 0.5f ;
                dy = (mBounds.height() - mBitmapHeight * scale) * 0.5f;

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate(dx, dy);

//			mBorderRect.set(mBitmapRect);
                mBorderRect.set(mBorderWidth, mBorderWidth,
                        mBitmapRect.right - mBorderWidth, mBitmapRect.bottom - mBorderWidth);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth , mBorderRect.top
                                + mBorderWidth , mBorderRect.right - mBorderWidth ,
                        mBorderRect.bottom - mBorderWidth );
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect,
                        Matrix.ScaleToFit.FILL);
                break;
            case FIT_CENTER:
//			mBorderRect.set(mBitmapRect);
                mBorderRect.set(mBorderWidth, mBorderWidth, mBitmapRect.right - mBorderWidth, mBitmapRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds,
                        Matrix.ScaleToFit.CENTER);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth , mBorderRect.top
                                + mBorderWidth , mBorderRect.right - mBorderWidth ,
                        mBorderRect.bottom - mBorderWidth );
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect,
                        Matrix.ScaleToFit.FILL);
                break;
            case FIT_END:
//			mBorderRect.set(mBitmapRect);
                mBorderRect.set(mBorderWidth, mBorderWidth, mBitmapRect.right - mBorderWidth, mBitmapRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds,
                        Matrix.ScaleToFit.END);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth , mBorderRect.top
                                + mBorderWidth , mBorderRect.right - mBorderWidth ,
                        mBorderRect.bottom - mBorderWidth );
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect,
                        Matrix.ScaleToFit.FILL);
                break;
            case FIT_START:
//			mBorderRect.set(mBitmapRect);
                mBorderRect.set(mBorderWidth, mBorderWidth, mBitmapRect.right - mBorderWidth, mBitmapRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds,
                        Matrix.ScaleToFit.START);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth , mBorderRect.top
                                + mBorderWidth , mBorderRect.right - mBorderWidth ,
                        mBorderRect.bottom - mBorderWidth );
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect,
                        Matrix.ScaleToFit.FILL);
                break;
            case FIT_XY:
            default:
//			mBorderRect.set(mBounds);
                mBorderRect.set(mBorderWidth, mBorderWidth, mBitmapRect.right - mBorderWidth, mBitmapRect.bottom - mBorderWidth);
                mDrawableRect.set(mBorderRect.left + mBorderWidth , mBorderRect.top + mBorderWidth ,
                        mBorderRect.right - mBorderWidth , mBorderRect.bottom
                                - mBorderWidth );
                mShaderMatrix.set(null);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect,
                        Matrix.ScaleToFit.FILL);
                break;
        }
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    public void setPressDown(boolean isDown) {
        this.isDown = isDown;
        invalidateSelf();
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ScaleType.FIT_XY;
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            setMatrix();
        }
    }

}