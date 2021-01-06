package com.jtcxw.glcxw.base.basic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtcxw.glcxw.base.R;
import com.jtcxw.glcxw.base.listeners.BindViewListener;
import com.lakala.wtb.base.BaseViewHolder;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T extends BaseAdapterModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected boolean needBackground = true;

    protected Boolean needEmpty = true;

    protected List<T> mDatas;

    protected BindViewListener mBindViewListener;
    //布局id
    protected int mLayoutId;

    public boolean mError = false;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == BaseAdapterModel.ItemType.EMPTY) {
            return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(getEmptyLayoutId(), viewGroup, false));
        } else  if (viewType == BaseAdapterModel.ItemType.ERROR) {
            return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(getErrorLayoutId(), viewGroup, false));
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(mLayoutId,viewGroup,false);
            return new BaseViewHolder(view);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (mBindViewListener != null && mDatas.get(position).getItemType() != BaseAdapterModel.ItemType.EMPTY
                    && mDatas.get(position).getItemType() != BaseAdapterModel.ItemType.ERROR){
                if (isNeedBackground()) {
                    if (getItemViewType(position) == BaseAdapterModel.ItemType.DEFAULT || mDatas.get(position).needBackground) {
                        if (position % 2 == 0) {
                            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.white));
                        } else {
                            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.c_fafcff));
                        }
                    }
                }
                mBindViewListener.onBindView(mDatas.get(position),holder.itemView,position);
            }
    }

    public boolean isNeedBackground() {
        return needBackground;
    }

    public void setNeedBackground(boolean needBackground) {
        this.needBackground = needBackground;
    }

    @Override
    public int getItemCount() {
        if (needEmpty) {
            if (mDatas.size() == 0) {
                Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                try {
                    T entity = entityClass.newInstance();
                    if (mError) {
                        entity.setItemType(BaseAdapterModel.ItemType.ERROR);
                    }else {
                        entity.setItemType(BaseAdapterModel.ItemType.EMPTY);
                    }
                    mDatas.add(entity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            } else if (mDatas.size() > 1) {
                mError = false;
                if (mDatas.get(0).getItemType() == BaseAdapterModel.ItemType.EMPTY) {
                    mDatas.remove(0);
                }
                if (mDatas.get(0).getItemType() == BaseAdapterModel.ItemType.ERROR) {
                    mDatas.remove(0);
                }
            }
        }else {
            mError = false;
        }
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getItemType();
    }

    protected  int getEmptyLayoutId(){
        return R.layout.layout_empty;
    }

    protected  int getErrorLayoutId(){
        return R.layout.layout_error;
    }

    public void update(ArrayList<T> lists){
        mDatas = lists;
        notifyDataSetChanged();
    }

    public void setError(boolean error) {
        if (error) {
            if (mDatas.size() == 1 && (mDatas.get(0).getItemType() == BaseAdapterModel.ItemType.EMPTY)) {
                mDatas.clear();
            }
        }
        this.mError = error;
    }


    public void clearData(){
        mDatas.clear();
    }
/**
     * 建造者，用来完成adapter的数据组合
     */
}
