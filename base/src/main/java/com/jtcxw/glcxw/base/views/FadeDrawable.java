/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jtcxw.glcxw.base.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.ImageView;

import java.lang.ref.WeakReference;


public class FadeDrawable extends BitmapDrawable {

    private static final long FADE_DURATION = 200;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    static void setBitmap(ImageView target, Bitmap bitmap) {
        Drawable placeholder = target.getDrawable();
        if (placeholder instanceof Animatable) {
            ((Animatable) placeholder).stop();
        }
        target.setBackgroundDrawable(target.getDrawable());
        final WeakReference<ImageView> targetRef = new WeakReference<>(target);
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView target = targetRef.get();
                if (target != null) {
                    target.setBackgroundDrawable(null);
                }
            }
        }, FADE_DURATION);
        FadeDrawable fadeDrawable = new FadeDrawable(target.getContext().getApplicationContext(), bitmap, placeholder);
        target.setImageDrawable(fadeDrawable);
    }

    private Drawable placeholder;
    private long startTimeMillis;
    private boolean animating;
    private int alpha = 0xFF;

    FadeDrawable(Context context, Bitmap bitmap, Drawable placeholder) {
        super(context.getResources(), bitmap);
        this.placeholder = placeholder;
        animating = true;
        startTimeMillis = SystemClock.uptimeMillis();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!animating) {
            super.draw(canvas);
        } else {
            float normalized = (SystemClock.uptimeMillis() - startTimeMillis) * 1f / FADE_DURATION;
            if (normalized >= 1f) {
                animating = false;
                placeholder = null;
                super.draw(canvas);
            } else {
                if (placeholder != null) {
                    placeholder.draw(canvas);
                }
                // setAlpha will call invalidateSelf and drive the animation.
                int partialAlpha = (int) (this.alpha * normalized);
                super.setAlpha(partialAlpha);
                super.draw(canvas);
                super.setAlpha(this.alpha);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        if (placeholder != null) {
            placeholder.setAlpha(alpha);
        }
        super.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (placeholder != null) {
            placeholder.setColorFilter(cf);
        }
        super.setColorFilter(cf);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        if (placeholder != null) {
            placeholder.setBounds(bounds);
        }
        super.onBoundsChange(bounds);
    }

}