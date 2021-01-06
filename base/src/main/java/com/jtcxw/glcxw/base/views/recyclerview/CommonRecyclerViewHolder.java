package com.jtcxw.glcxw.base.views.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 通用的ViewHolder
 * <p/>
 * ViewHolder的主要的作用:
 * 1.通过成员变量存储对应的convertView中需要操作的子View，避免每次findViewById，从而提升运行的效率。
 * <p/>
 * Created by Chang.Xiao on 2016/4/11 21:46.
 *
 * @version 1.0
 */
public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder{

    // 对于不同的ItemType没有办法确定创建哪些成员变量View(convertView中的子view)，只能用集合来缓存了。
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;
    private int mLayoutId;

    /**
     * 构建ViewHolder
     *
     * @param context  上下文
     * @param itemView convertView
     */
    public CommonRecyclerViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.mConvertView = itemView;
        this.mViews = new SparseArray<>();
        mConvertView.setTag(this);
    }

    /**
     * 传入layoutId来获取ViewHolder对象
     *
     * @param context  上下文
     * @param convertView
     * @param parent   父布局
     * @param layoutId convertView(itemView)布局
     * @return ViewHolder
     */
    public static CommonRecyclerViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(context, itemView);
            holder.mLayoutId = layoutId;
            return holder;
        } else {
            CommonRecyclerViewHolder holder;
            if (convertView.getTag() == null) {
                holder = new CommonRecyclerViewHolder(context, convertView);
            } else {
                holder = (CommonRecyclerViewHolder) convertView.getTag();
            }
            return holder;
        }
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public <T extends View> T getView(int viewId, View parentView){
        return (T) parentView.findViewById(viewId);
    }
    public View getConvertView() {
        return mConvertView;
    }


    /* 我们的Item实际上使用的控件较多时候可能都是TextView,ImageView等，
    我们一般在convert方法都是去设置文本，图片什么的，那么我们可以在ViewHolder里面，写上如下的一些辅助方法： */
    /**
     * Sets the string value of the TextView.
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonRecyclerViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
 /* 我们的Item实际上使用的控件较多时候可能都是TextView,ImageView等，
    我们一般在convert方法都是去设置文本，图片什么的，那么我们可以在ViewHolder里面，写上如下的一些辅助方法： */
    /**
     * Sets the string value of the TextView.
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonRecyclerViewHolder setText(int viewId, @StringRes int text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
    /**
     * Sets a drawable as the content of this ImageView.
     *
     * @param viewId
     * @param resId  the resource identifier of the drawable
     * @return
     */
    public CommonRecyclerViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * Sets a Bitmap as the content of this ImageView.
     * @param viewId
     * @param bitmap
     * @return
     */
    public CommonRecyclerViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Sets a drawable as the content of this ImageView.
     * @param viewId
     * @param drawable
     * @return
     */
    public CommonRecyclerViewHolder setImageDrawer(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Sets the background color for this view.
     * @param viewId
     * @param color the color of the background
     * @return
     */
    public CommonRecyclerViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Set the background to a given resource. The resource should refer to
     * a Drawable object or 0 to remove the background.
     * @param viewId
     * @param backgroundRes The identifier of the resource.
     * @return
     */
    public CommonRecyclerViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Sets the text color for all the states (normal, selected,
     * focused) to be this color.
     * @param viewId
     * @param textColor
     * @return
     */
    public CommonRecyclerViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * Sets the text color for all the states (normal, selected,
     * focused) to be this color.
     * @param viewId
     * @param textColorRes
     * @return
     */
    public CommonRecyclerViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * Sets the opacity of the view to a value from 0 to 1, where 0 means the view is
     * completely transparent and 1 means the view is completely opaque.
     * @param viewId
     * @param value
     * @return
     */
    public CommonRecyclerViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set the enabled state of this view.
     * @param viewId
     * @param visible
     * @return
     */
    public CommonRecyclerViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     *  Scans the text of the provided TextView and turns all occurrences of
     *  the link types indicated in the mask into clickable links.  If matches
     *  are found the movement method for the TextView is set to
     *  LinkMovementMethod.
     * @param viewId
     * @return
     */
    public CommonRecyclerViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public CommonRecyclerViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * Set the current progress to the specified value. Does not do anything
     * if the progress bar is in indeterminate mode.
     * @param viewId
     * @param progress
     * @return
     */
    public CommonRecyclerViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public CommonRecyclerViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * Set the range of the progress bar to 0...<tt>max</tt>.
     * @param viewId
     * @param max the upper range of this progress bar
     * @return
     */
    public CommonRecyclerViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled).
     * @param viewId
     * @param rating The rating to set.
     * @return
     */
    public CommonRecyclerViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public CommonRecyclerViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the tag associated with this view. A tag can be used to mark
     * a view in its hierarchy and does not have to be unique within the
     * hierarchy. Tags can also be used to store data within a view without
     * resorting to another data structure.
     * @param viewId
     * @param tag an Object to tag the view with
     * @return
     */
    public CommonRecyclerViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public CommonRecyclerViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * Change the checked state of the view
     * @param viewId
     * @param checked The new checked state
     * @return
     */
    public CommonRecyclerViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public CommonRecyclerViewHolder setSelected(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }

    /**
     * Register a callback to be invoked when this view is clicked. If this view is not
     * clickable, it becomes clickable.
     *
     * @param viewId
     * @param listener The callback that will run
     * @return
     */
    public CommonRecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public CommonRecyclerViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }


    public int getLayoutId() {
        return mLayoutId;
    }
    /* 上面只给出了几个方法，你可以把常用控件的方法都写进去，并且在使用过程中不断完善即可 */
}
