package com.jtcxw.glcxw.base.views.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.jtcxw.glcxw.base.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mark on 2017/11/13.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> implements View.OnLongClickListener {
    private @Nullable
    RecyclerAdapterWrapper mOuterAdapter;   //外部包装Adapter
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected final List<T> mDatas;         //仅在构造函数初始化，保证mDatas不可能为null
    protected OnItemClickListener mOnItemClickListener;

    public BaseRecyclerAdapter(Context context, List<T> data){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = (data==null) ? new ArrayList<>() : data;
    }

    protected abstract @LayoutRes
    int getConvertViewId(int viewType);

    protected abstract void convert(CommonRecyclerViewHolder holder, T data, int position);

    /**
     * 刷新数据
     * @param data
     */
    public void setData(List<T> data){
        if (data == null) return;
        int oldListSize = mDatas.size();
        if (data != mDatas) {
            mDatas.clear();
            mDatas.addAll(data);
        }
        if (mOuterAdapter == null) {
            notifyDataSetChanged();
        } else {
            mOuterAdapter.notifyItemRangeChanged(mOuterAdapter.getHeadersCount(),
                    oldListSize+mOuterAdapter.getFootersCount());
        }
    }

    /**
     * 添加数据
     * @param data
     */
    public void addData(List<T> data, boolean onlyRefreshPart) {
        if (data == null) return;
        if (data != mDatas) {
            mDatas.addAll(data);
        }
        if (mOuterAdapter == null) {
            notifyDataSetChanged();
        } else {
            if (onlyRefreshPart && data != mDatas) {
                mOuterAdapter.notifyItemRangeChanged(
                        mOuterAdapter.getHeadersCount()+mDatas.size()-data.size(),
                        data.size()+mOuterAdapter.getFootersCount());
            } else {
                mOuterAdapter.notifyDataSetChanged();
            }
        }
    }

    public @NonNull
    List<T> getData() {
        return mDatas;
    }

    public void setOuterAdapter(RecyclerAdapterWrapper outerAdapter){
        this.mOuterAdapter = outerAdapter;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getConvertViewId(viewType);
        View itemView = mInflater.inflate(layoutId, parent, false);
        return new CommonRecyclerViewHolder(mContext, itemView);
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
        setClickListener(holder.itemView,mDatas.get(position),holder,position);
        setLongClickListener(holder.itemView,mDatas.get(position),holder,position);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    protected void setClickListener(View view, T model, CommonRecyclerViewHolder holder, int position){
        if (view == null) {
            return;
        }
        view.setTag(R.id.view_holder_position, new Integer(position));
        view.setTag(R.id.view_holder_data, model);
        view.setTag(R.id.view_holder, holder);
        view.setOnClickListener(mOnClickListener);
    }

    protected void setLongClickListener(View view, T model, CommonRecyclerViewHolder holder, int position){
        if (view == null) {
            return;
        }
        view.setTag(R.id.view_holder_position, new Integer(position));
        view.setTag(R.id.view_holder_data, model);
        view.setTag(R.id.view_holder, holder);
        view.setOnLongClickListener(this);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag(R.id.view_holder_position);
            T data = (T) v.getTag(R.id.view_holder_data);
            CommonRecyclerViewHolder holder = (CommonRecyclerViewHolder) v.getTag(R.id.view_holder);

            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v,data,position);
            }
            BaseRecyclerAdapter.this.onClick(v, data, position, holder);
        }

    };

    @Override
    public boolean onLongClick(View v) {
        int position = (Integer) v.getTag(R.id.view_holder_position);
        T t = (T) v.getTag(R.id.view_holder_data);
        CommonRecyclerViewHolder holder = (CommonRecyclerViewHolder) v.getTag(R.id.view_holder);
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemLongClick(v,t,position);
        }
        return onLongClick(v,t,position,holder);
    }

    protected abstract void onClick(View view, T model, int position, CommonRecyclerViewHolder holder);
    protected boolean onLongClick(View view, T model, int position, CommonRecyclerViewHolder holder) {
        return false;
    }

    public void notifyAllItems(){
        if (mOuterAdapter != null) {
            mOuterAdapter.notifyDataSetChanged();
        } else {
            notifyDataSetChanged();
        }
    }

    public void updateItem(Object model){
        int position = mDatas.indexOf(model);
        updateItem(position);
    }

    public void updateItem(int position) {
        if (position >= 0 && position < mDatas.size()) {
            if (mOuterAdapter != null) {
                mOuterAdapter.notifyItemChanged(mOuterAdapter.getHeadersCount()+position);
            } else {
                notifyItemChanged(position);
            }
        }
    }

    public interface OnItemClickListener<T>{
        void onItemClick(View view, T model, int position);
        void onItemLongClick(View view, T model, int position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener){
        this.mOnItemClickListener = listener;
    }

    protected void stopItemAnimation(CommonRecyclerViewHolder holder){}
    protected void startItemAnimation(CommonRecyclerViewHolder holder){}

    @Override
    public void onViewDetachedFromWindow(CommonRecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        stopItemAnimation(holder);
    }

    @Override
    public void onViewAttachedToWindow(CommonRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        startItemAnimation(holder);
    }


    public RecyclerAdapterWrapper getOuterAdapter() {
        return mOuterAdapter;
    }


}
