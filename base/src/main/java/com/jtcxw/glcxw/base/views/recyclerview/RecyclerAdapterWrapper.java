package com.jtcxw.glcxw.base.views.recyclerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 封装支持添加header和footer的RecyclerView Adapter
 * Created by mark on 2017/11/15.
 */

public class RecyclerAdapterWrapper extends RecyclerView.Adapter<CommonRecyclerViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    private BaseRecyclerAdapter mInnerAdapter;  //封装在内部的Adapter

    public RecyclerAdapterWrapper(@NonNull BaseRecyclerAdapter adapter) {
        mInnerAdapter = adapter;
        mInnerAdapter.setOuterAdapter(this);
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            CommonRecyclerViewHolder holder = CommonRecyclerViewHolder.get(parent.getContext(), mHeaderViews.get(viewType), parent, 0);
            return holder;
        } else if (mFooterViews.get(viewType) != null) {
            CommonRecyclerViewHolder holder = CommonRecyclerViewHolder.get(parent.getContext(), mFooterViews.get(viewType), parent, 0);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            convertHeader(holder);
            return;
        } else if (isFooterViewPos(position)) {
            convertFooter(holder);
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFooterViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer或header的位置，那么该item占据整行单元格，正常情况下占据1个单元格
                    return (isHeaderViewPos(position) || isFooterViewPos(position)) ?
                            gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    protected void convertHeader(CommonRecyclerViewHolder holder) {}
    protected void convertFooter(CommonRecyclerViewHolder holder) {}

    /**
     * 当前未知是否为头部
     * @param position
     * @return
     */
    public boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    /**
     * 当前位置是否为尾部
     * @param position
     * @return
     */
    public boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    /**
     * 添加头部View
     * @param view
     */
    public void addHeaderView(View view) {
        if (view == null)
            return;
        int index = mHeaderViews.indexOfValue(view);
        if (index == -1) {
            mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
            notifyDataSetChanged();
        }
    }

    public void removeHeaderView(View view) {
        if (view == null)
            return;
        int index = mHeaderViews.indexOfValue(view);
        if (index != -1) {
            mHeaderViews.removeAt(index);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加尾部view
     * @param view
     */
    public void addFooterView(View view) {
        if (view == null)
            return;
        int index = mFooterViews.indexOfValue(view);
        if (index == -1) {
            mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
            notifyDataSetChanged();
        }
    }

    public void removeFooterView(View view){
        if (view == null)
            return;
        int index = mFooterViews.indexOfValue(view);
        if (index != -1) {
            mFooterViews.removeAt(index);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取头部view数量
     * @return
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    /**
     * 获取尾部view数量
     * @return
     */
    public int getFootersCount() {
        return mFooterViews.size();
    }

    /**
     * 获取除去header和footer的item数量
     * @return
     */
    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }
}
