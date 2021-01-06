package com.jtcxw.glcxw.base.views.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jtcxw.glcxw.base.R;
import com.jtcxw.glcxw.base.utils.DimensionUtil;
import com.jtcxw.glcxw.base.utils.ScreenUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiyu on 2017/12/26.
 */

public class RefreshLoadMoreRecyclerView extends RecyclerView {

    public static final int NO_VIEW = 0;
    public static final int FOOTER_TYPE_LOADING = 1;
    public static final int FOOTER_TYPE_LOAD_MORE = 2;
    public static final int FOOTER_TYPE_NO_MORE = 3;
    public static final int HEADER_TYPE_LOADING = 4;
    public static final int HEADER_TYPE_LOAD_MORE = 5;
    public static final int EMPTY_TYPE_NO_CONTENT = -1;
    public static final int EMPTY_TYPE_NO_NETWORK = -2;
    public static final int EMPTY_TYPE_SERVER_ERROR = -3;
    public static final int EMPTY_TYPE_NO_CONTENT_GUEST = -4;

    public static final int NO_LOAD_TYPE = 0;
    public static final int LOAD_TYPE_REFRESH = 1;
    public static final int LOAD_TYPE_MORE_NEXT = 2;
    public static final int LOAD_TYPE_MORE_PRE = 3;

    @IntDef({NO_VIEW, FOOTER_TYPE_LOADING, FOOTER_TYPE_LOAD_MORE, FOOTER_TYPE_NO_MORE,
            HEADER_TYPE_LOADING, HEADER_TYPE_LOAD_MORE,
            EMPTY_TYPE_NO_CONTENT, EMPTY_TYPE_NO_CONTENT_GUEST,EMPTY_TYPE_NO_NETWORK, EMPTY_TYPE_SERVER_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExtraViewType {
    }

    @IntDef({NO_LOAD_TYPE, LOAD_TYPE_REFRESH, LOAD_TYPE_MORE_PRE, LOAD_TYPE_MORE_NEXT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadType {
    }

    /**
     * 下拉刷新时联动处理的组件
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * 用户设置进来的不含footer,header的Adapter，作为setAdapter方法的参数
     *
     * @see #setAdapter(Adapter)
     */
    private BaseRecyclerAdapter mInnerAdapter;

    /**
     * 经过包装的包含footer,header的Adapter，通过getAdapter方法获取的是此Adapter
     *
     * @see #getAdapter()
     */
    private RecyclerAdapterWrapper mAdapterWrapper;

    private LinearLayoutManager mLayoutManager;

    private @ExtraViewType
    int mCurrentFooterType = NO_VIEW;
    private @ExtraViewType
    int mCurrentHeaderType = NO_VIEW;

    /**
     * RecyclerView的footer容器，用于包含footer,empty类型的子View
     */
    private FrameLayout mFooterContainer;

    /**
     * RecyclerView的header容器，用于包含header类型的子View
     */
    private FrameLayout mHeaderContainer;

    /**
     * 缓存对应各个footer,header,empty类型的子View
     */
    private SparseArrayCompat<View> mViewsCache = new SparseArrayCompat<>();

    private boolean mSupportLoadNextPage = false;
    private boolean mSupportLoadPrePage = false;
    private boolean mSupportScrollToTop = false;
    private boolean mShouldShowNoMore = true;
    private boolean mShowBottomNoMore = false;
    private boolean mSupportLoadMoreInAdvance = true;
    private OnRefreshListener mOnRefreshListener;
    private OnLoadNextPageListener mOnLoadNextPageListener;
    private OnLoadPrePageListener mOnLoadPrePageListener;
    private OnExtraViewCreatedListener mOnExtraViewCreatedListener;

    /**
     * 当前是否处于加载状态
     */
    private boolean mIsLoading = false;

    /**
     * 用于记录当前loading状态处理完成后，是否还有下一次刷新动作
     */
    private @LoadType
    int mNextLoadType = NO_LOAD_TYPE;

    /**
     * 是否有下一页
     */
    private boolean mHasNextPage = false;

    /**
     * 是否有上一页
     */
    private boolean mHasPrePage = false;

    /**
     * 当前的加载类型
     */
    private @LoadType
    int mCurLoadType = NO_LOAD_TYPE;

    private boolean mShouldRefreshPartWhenLoadMore = false;

    private boolean isLoadMoreEnabled = false;
    private boolean isLastTouchAtBottom = false;
    private boolean isLastTouchAtTop = false;

    private int mLoadingHeaderLayoutRes = R.layout.footer_loading;
    private int mLoadingFooterLayoutRes = R.layout.footer_loading;
    private int mLoadMoreHeaderLayoutRes = R.layout.view_pull_down_load_more;
    private int mLoadMoreFooterLayoutRes = R.layout.footer_load_more_hint;
    private int mNoMoreFooterLayoutRes = R.layout.footer_no_more_hint;
    private int mNoContentFooterLayoutRes = R.layout.view_no_content;
    private EmptyPageProperty mNoContentPageProperty = EmptyPageProperty.NO_CONTENT_DEFAULT;
    private OnClickListener mNoContentButtonClickListener;

    private Bitmap mScrollTopBitmap;
    private Paint mPaint;
    private boolean mIsScrollTopIconShowing = false;
    private int mFirstItemPositionToAllowScrollTop = 20;
    private int mMaxItemCountToAllowLoadMore = 6;   //提前加载下一页时，未显示的item最大数量

    private int mTouchSlop;

    public RefreshLoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshLoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final ViewConfiguration configuration = ViewConfiguration
                .get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mFooterContainer = new FrameLayout(getContext());
        mFooterContainer.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderContainer = new FrameLayout(getContext());
        mHeaderContainer.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        setHasFixedSize(true);
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            mLayoutManager = (LinearLayoutManager) layoutManager;
        } else {
            mLayoutManager = null;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter == null) return;
        if (adapter instanceof BaseRecyclerAdapter) {
            if (getLayoutManager() == null) {
                setLayoutManager(mLayoutManager = new LinearLayoutManager(getContext()));
            }
            mInnerAdapter = (BaseRecyclerAdapter) adapter;
            mAdapterWrapper = new RecyclerAdapterWrapper(mInnerAdapter);
            mAdapterWrapper.addFooterView(mFooterContainer);
            super.setAdapter(mAdapterWrapper);
            setItemAnimator(null);
            addOnScrollListener(mRecyclerScrollListener);
        } else {
            super.setAdapter(adapter);
        }
    }

    public void bindSwipeRefreshLayout(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            if (mOnRefreshListener != null) {
                mSwipeRefreshLayout.setOnRefreshListener(() -> performRefresh(false));
            }
        }
    }

    public void setOnRefreshListener(@Nullable OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(() -> {
                performRefresh(false);
            });
        }
    }

    public void setOnLoadNextPageListener(@Nullable OnLoadNextPageListener onLoadNextPageListener) {
        if (!mSupportLoadNextPage) {
            setSupportLoadNextPage(true);
        }
        mOnLoadNextPageListener = onLoadNextPageListener;
    }

    public void setOnLoadPrePageListener(@Nullable OnLoadPrePageListener onLoadPrePageListener) {
        if (!mSupportLoadPrePage) {
            setSupportLoadPrePage(true);
        }
        mOnLoadPrePageListener = onLoadPrePageListener;
    }

    /**
     * 设置是否支持加载下一页，如果需要临时禁用加载下一页功能，则调用此方法。
     * 初始化时调setOnLoadNextPageListener即可，无需此方法。
     *
     * @param supportLoadNextPage
     * @see #setOnLoadNextPageListener(OnLoadNextPageListener)
     */
    public void setSupportLoadNextPage(boolean supportLoadNextPage) {
        mSupportLoadNextPage = supportLoadNextPage;
    }

    public void setSupportLoadPrePage(boolean canLoadPrePage) {
        mSupportLoadPrePage = canLoadPrePage;
        if (!mSupportLoadPrePage) {
            if (mAdapterWrapper != null) {
                mAdapterWrapper.removeHeaderView(mHeaderContainer);
            }
        }
    }

    public void setSupportScrollToTop(boolean supportScrollToTop) {
        if (mSupportScrollToTop != supportScrollToTop) {
            mSupportScrollToTop = supportScrollToTop;
            if (mSupportScrollToTop) {
                if (shouldShowScrollTopIcon()) {
                    invalidate();
                }
            } else {
                if (mIsScrollTopIconShowing) {
                    invalidate();
                }
            }
        }
    }

    public void setShouldShowNoMore(boolean shouldShowNoMore) {
        mShouldShowNoMore = shouldShowNoMore;
    }

    public void setShowBottomNoMore(boolean showBottomNoMore) {
        mShowBottomNoMore = showBottomNoMore;
    }

    public void setSupportLoadMoreInAdvance(boolean supportLoadMoreInAdvance) {
        mSupportLoadMoreInAdvance = supportLoadMoreInAdvance;
    }

    public void setShouldRefreshPartWhenLoadMore(boolean shouldRefreshPartWhenLoadMore) {
        mShouldRefreshPartWhenLoadMore = shouldRefreshPartWhenLoadMore;
    }

    public void setOnExtraViewCreatedListener(OnExtraViewCreatedListener listener) {
        mOnExtraViewCreatedListener = listener;
    }

    public <E> void notifyLoadSuccess(List<E> backedData) {
        notifyLoadSuccess(backedData, hasMoreNext());
    }

    public <E> void notifyLoadSuccess(List<E> backedData, boolean hasNext) {
        notifyLoadSuccess(backedData, hasNext, hasMorePre());
    }

    /**
     * 数据请求成功后通知刷新UI：取消Loading状态，并且自动把数据设置到合适的位置
     *
     * @param backedData 返回的列表数据
     * @param hasNext    列表底部是否还有更多未加载
     * @param hasPre     列表头部是否还有更多未加载
     * @return 数据是否成功设置到RecyclerView
     */
    @SuppressWarnings("unchecked")
    public <E> boolean notifyLoadSuccess(List<E> backedData, boolean hasNext, boolean hasPre) {
        if (mAdapterWrapper == null || mInnerAdapter == null || backedData == null) {
            if(backedData == null){
                stopLoading();
            }
            return false;
        }
        mIsLoading = false;

        if (getScrollState() == SCROLL_STATE_DRAGGING && !isLoadMoreEnabled) {
            isLoadMoreEnabled = true;
        }

        if (mCurLoadType == LOAD_TYPE_REFRESH) {   //下拉刷新
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (backedData != mInnerAdapter.getData()) {
                mInnerAdapter.setData(backedData);
            } else {    //如果是在子类直接操作mData对象，则无需对数据进行处理，直接刷新
                mAdapterWrapper.notifyDataSetChanged();
            }
        } else if (mCurLoadType == LOAD_TYPE_MORE_NEXT) {    //上拉加载更多
            if (getScrollState() == SCROLL_STATE_SETTLING && isFooterVisible()) {
                stopScroll();
            }
            if (backedData != mInnerAdapter.getData() && !backedData.isEmpty()) {
                mInnerAdapter.addData(backedData, mShouldRefreshPartWhenLoadMore);
            } else if(!backedData.isEmpty()){
                mAdapterWrapper.notifyDataSetChanged();
            }
        } else if (mCurLoadType == LOAD_TYPE_MORE_PRE) {  //下拉加载更多
            if (getScrollState() == SCROLL_STATE_SETTLING && isHeaderVisible()) {
                stopScroll();
            }
            if (backedData != mInnerAdapter.getData()) {  //如果是在子类直接操作mData对象，则无需再次处理
                int curHeadOffset = -1;
                if (getChildCount() >= 2 && getChildAt(0) == mHeaderContainer) {
                    curHeadOffset = getChildAt(1).getTop();
                }
                mInnerAdapter.getData().addAll(0, backedData);
                mAdapterWrapper.notifyDataSetChanged();
                if (curHeadOffset >= 0) {
                    if (getLayoutManager() instanceof LinearLayoutManager) {
                        ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(
                                mAdapterWrapper.getHeadersCount() + backedData.size(), curHeadOffset
                        );
                    } else {
                        getLayoutManager().scrollToPosition(
                                mAdapterWrapper.getHeadersCount() + backedData.size());
                    }
                }
            } else {
                mAdapterWrapper.notifyDataSetChanged();
            }
        }

        if (mInnerAdapter.getData().isEmpty()) {  //列表设置后数据仍为空，显示emptyView
            showEmptyView(EMPTY_TYPE_NO_CONTENT);
        } else {    //列表有数据，更新header/footer状态
            if (mSupportLoadNextPage) {
                mHasNextPage = hasNext;
                if (hasNext) {
                    showFooterView(FOOTER_TYPE_LOAD_MORE);
                } else {
                    showFooterView(FOOTER_TYPE_NO_MORE);
                }
            } else {
                hideFooterView();
            }
            if (mSupportLoadPrePage) {
                mHasPrePage = hasPre;
                if (mHasPrePage) {
                    showHeaderView(HEADER_TYPE_LOAD_MORE);
                } else {
                    hideHeaderView();
                }
            }
        }
        performNextLoadActionIfNeed();
        return true;
    }

    /**
     * 通知加载错误，取消loading状态，并且显示错误提示
     *
     * @param errorType 错误类型
     */
    public void notifyLoadError(int errorType) {
        if (!mIsLoading)
            return;
        mIsLoading = false;

        if (getScrollState() == SCROLL_STATE_DRAGGING && !isLoadMoreEnabled) {
            isLoadMoreEnabled = true;
        }

        if (mCurLoadType == LOAD_TYPE_REFRESH) {   //下拉刷新
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

//        SnackbarUtil.showMultiLineToast(null, getContext().getString(
//                errorType == NetUtils.STATE_NO_NETWORK ?
//                R.string.toast_no_network : R.string.hint_internal_server_error));
        if (mInnerAdapter.getData().isEmpty()) {  //列表没有数据，显示emptyView
            switch (errorType) {
                    default: showEmptyView(EMPTY_TYPE_NO_NETWORK);
                    break;
            }
        } else {    //列表有数据，显示Toast
            if (mCurLoadType == LOAD_TYPE_MORE_NEXT) {
                showFooterView(FOOTER_TYPE_LOAD_MORE);
            } else if (mCurLoadType == LOAD_TYPE_MORE_PRE) {
                showHeaderView(HEADER_TYPE_LOAD_MORE);
            }
        }


        performNextLoadActionIfNeed();
    }

    public void stopLoading() {
        stopLoading(false);
    }

    /**
     * 直接结束请求，不进行数据设置，也不通知错误
     *
     * @param shouldCancelAllRequests 如果为true，则取消当前请求和后续等待中的请求；如果为false，只取消当前请求
     */
    public void stopLoading(boolean shouldCancelAllRequests) {
        if (shouldCancelAllRequests) {
            mNextLoadType = NO_LOAD_TYPE;
        }
        if (!mIsLoading)
            return;
        mIsLoading = false;

        if (getScrollState() == SCROLL_STATE_DRAGGING && !isLoadMoreEnabled) {
            isLoadMoreEnabled = true;
        }

        switch (mCurLoadType) {
            case LOAD_TYPE_REFRESH:
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                break;
            case LOAD_TYPE_MORE_NEXT:
                showFooterView(FOOTER_TYPE_LOAD_MORE);
                break;
            case LOAD_TYPE_MORE_PRE:
                showHeaderView(HEADER_TYPE_LOAD_MORE);
                break;
        }
        performNextLoadActionIfNeed();
    }

    public void performRefreshWithoutRequest(){
        if (mSwipeRefreshLayout != null ) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (!mIsLoading) {
            mIsLoading = true;
            mCurLoadType = LOAD_TYPE_REFRESH;
        } else {
            mNextLoadType = LOAD_TYPE_REFRESH;
        }
    }
    /**
     * 模拟下拉刷新操作
     */
    public void performRefresh() {
        performRefresh(true);
    }

    public void performRefresh(boolean isShowLoading) {
        if (mSwipeRefreshLayout != null && isShowLoading) {
            mSwipeRefreshLayout.setEnabled(true);
            mSwipeRefreshLayout.setRefreshing(true);
        }
        if (!mIsLoading) {
            mIsLoading = true;
            mCurLoadType = LOAD_TYPE_REFRESH;
            if (mOnRefreshListener != null) {
                mOnRefreshListener.onRefresh();
            }
        } else {
            mNextLoadType = LOAD_TYPE_REFRESH;
        }
    }

    public void performLoadNextPage(boolean isShowLoading) {
        if (!mSupportLoadNextPage)
            return;
        if (isShowLoading) {
            showFooterView(FOOTER_TYPE_LOADING);
        }
        if (!mIsLoading) {
            if (mHasNextPage) {
                mIsLoading = true;
                mCurLoadType = LOAD_TYPE_MORE_NEXT;
                if (mOnLoadNextPageListener != null) {
                    mOnLoadNextPageListener.onLoadNextPage();
                }
            }
        } else {
            mNextLoadType = LOAD_TYPE_MORE_NEXT;
        }
    }

    public void performLoadPrePage(boolean isShowLoading) {
        if (!mSupportLoadPrePage)
            return;
        if (isShowLoading) {
            showHeaderView(HEADER_TYPE_LOADING);
        }
        if (!mIsLoading) {
            if (mHasPrePage) {
                mIsLoading = true;
                mCurLoadType = LOAD_TYPE_MORE_PRE;
                if (mOnLoadPrePageListener != null) {
                    mOnLoadPrePageListener.onLoadPrePage();
                }
            }
        } else {
            mNextLoadType = LOAD_TYPE_MORE_PRE;
        }
    }

    private void performNextLoadActionIfNeed() {
        if (mNextLoadType == NO_LOAD_TYPE)
            return;
        int nextLoadType = mNextLoadType;
        mNextLoadType = NO_LOAD_TYPE;
        switch (nextLoadType) {
            case LOAD_TYPE_REFRESH:
                performRefresh(false);
                break;
            case LOAD_TYPE_MORE_NEXT:
                performLoadNextPage(false);
                break;
            case LOAD_TYPE_MORE_PRE:
                performLoadPrePage(false);
                break;
        }
    }

    public <E> void setNewData(List<E> newData) {
        setNewData(newData, hasMoreNext());
    }

    public <E> void setNewData(List<E> newData, boolean hasNextPage) {
        setNewData(newData, hasNextPage, hasMorePre());
    }

    /**
     * 不通过耗时的请求，直接设置新数据，比如从本地缓存取
     */
    @SuppressWarnings("unchecked")
    public <E> void setNewData(List<E> newData, boolean hasNextPage, boolean hasPrePage) {
        if (mInnerAdapter == null || mAdapterWrapper == null || newData == null)
            return;
        stopLoading(true);
        if (newData != mInnerAdapter.getData()) {
            mInnerAdapter.setData(newData);
        } else {
            mAdapterWrapper.notifyDataSetChanged();
        }

        if (mInnerAdapter.getData().isEmpty()) {  //列表设置后数据仍为空，显示emptyView
            showEmptyView(EMPTY_TYPE_NO_CONTENT);
        } else {    //列表有数据，更新header/footer状态
            if (mSupportLoadNextPage) {
                mHasNextPage = hasNextPage;
                if (mHasNextPage) {
                    showFooterView(FOOTER_TYPE_LOAD_MORE);
                } else {    //没有更多了
                    showFooterView(FOOTER_TYPE_NO_MORE);
                }
            } else {
                hideFooterView();
            }
            if (mSupportLoadPrePage) {
                mHasPrePage = hasPrePage;
                if (mHasPrePage) {
                    showHeaderView(HEADER_TYPE_LOAD_MORE);
                } else {    //没有更多了
                    hideHeaderView();
                }
            }
        }
    }

    private OnScrollListener mRecyclerScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                if (!mIsLoading) {
                    isLoadMoreEnabled = true;
                    isLastTouchAtBottom = isFooterVisible();
                    isLastTouchAtTop = isHeaderVisible();
                }
            } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (mIsLoading || !isLoadMoreEnabled || mInnerAdapter.getData().isEmpty())
                    return;
                if (mSupportLoadNextPage && isFooterVisible()
                        && mFooterContainer.getBottom() <= getHeight() - getPaddingBottom()
                        && isLastTouchAtBottom
                        && mHasNextPage) {
                    isLoadMoreEnabled = false;
                    showFooterView(FOOTER_TYPE_LOADING);
                    mIsLoading = true;
                    mCurLoadType = LOAD_TYPE_MORE_NEXT;
                    if (mOnLoadNextPageListener != null) {
                        mOnLoadNextPageListener.onLoadNextPage();
                    }
                } else if (mSupportLoadPrePage && isHeaderVisible()
                        && mHeaderContainer.getTop() >= getPaddingTop()
                        && isLastTouchAtTop
                        && mHasPrePage) {
                    isLoadMoreEnabled = false;
                    showHeaderView(HEADER_TYPE_LOADING);
                    mIsLoading = true;
                    mCurLoadType = LOAD_TYPE_MORE_PRE;
                    if (mOnLoadPrePageListener != null) {
                        mOnLoadPrePageListener.onLoadPrePage();
                    }
                } else {
                    isLoadMoreEnabled = false;
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mIsLoading || !isLoadMoreEnabled || mInnerAdapter.getData().isEmpty())
                return;
            if (mSupportLoadNextPage
                    && !isLastTouchAtBottom
                    && mHasNextPage
                    && (canLoadNextPageInAdvance() || isFooterVisible())
                    && dy >= 0) {
                isLoadMoreEnabled = false;
                showFooterView(FOOTER_TYPE_LOADING);
                mIsLoading = true;
                mCurLoadType = LOAD_TYPE_MORE_NEXT;
                if (mOnLoadNextPageListener != null) {
                    mOnLoadNextPageListener.onLoadNextPage();
                }
            } else if (mSupportLoadPrePage
                    && !isLastTouchAtTop
                    && mHasPrePage
                    && (canLoadPrePageInAdvance() || isHeaderVisible())
                    && dy <= 0) {
                isLoadMoreEnabled = false;
                showHeaderView(HEADER_TYPE_LOADING);
                mIsLoading = true;
                mCurLoadType = LOAD_TYPE_MORE_PRE;
                if (mOnLoadPrePageListener != null) {
                    mOnLoadPrePageListener.onLoadPrePage();
                }
            }

        }
    };

    private boolean isHeaderVisible() {
        return getChildAt(0) == mHeaderContainer;
    }

    private boolean isFooterVisible() {
        return getChildAt(getChildCount() - 1) == mFooterContainer;
    }

    private boolean canLoadPrePageInAdvance() {
//        if (!mSupportLoadMoreInAdvance)
//            return false;
//        if (mLayoutManager == null || mAdapterWrapper == null)
//            return false;
//
//        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
//        return firstVisibleItemPosition-mAdapterWrapper.getHeadersCount() <= mMaxItemCountToAllowLoadMore;
        return false;
    }

    private boolean canLoadNextPageInAdvance() {
        if (!mSupportLoadMoreInAdvance)
            return false;
        if (mLayoutManager == null || mAdapterWrapper == null || mInnerAdapter == null)
            return false;

        int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
        return mInnerAdapter.getData().size()-
                (lastVisibleItemPosition-mAdapterWrapper.getHeadersCount()+1) <= mMaxItemCountToAllowLoadMore;
    }

    /**
     * 设置对应类型的empty,footer,header
     *
     * @param type
     * @param layoutRes
     */
    public void setExtraView(@ExtraViewType int type, @LayoutRes int layoutRes) {
        ViewGroup parentView = null;
        switch (type) {
            case FOOTER_TYPE_LOAD_MORE:
                mLoadMoreFooterLayoutRes = layoutRes;
                parentView = mFooterContainer;
                break;
            case FOOTER_TYPE_LOADING:
                mLoadingFooterLayoutRes = layoutRes;
                parentView = mFooterContainer;
                break;
            case FOOTER_TYPE_NO_MORE:
                mNoMoreFooterLayoutRes = layoutRes;
                parentView = mFooterContainer;
                break;
            case HEADER_TYPE_LOAD_MORE:
                mLoadMoreHeaderLayoutRes = layoutRes;
                parentView = mHeaderContainer;
                break;
            case HEADER_TYPE_LOADING:
                mLoadingHeaderLayoutRes = layoutRes;
                parentView = mHeaderContainer;
                break;
            case EMPTY_TYPE_NO_CONTENT:
            case EMPTY_TYPE_NO_CONTENT_GUEST:
                mNoContentFooterLayoutRes = layoutRes;
                parentView = mFooterContainer;
                break;
        }
        //缓存中已存在该类型的View，需要及时从xml映射并替换
        if (parentView != null) {
            if (mViewsCache.get(type) != null) {
                View newView = LayoutInflater.from(getContext()).inflate(layoutRes, parentView, false);
                if (newView != null && mOnExtraViewCreatedListener != null) {
                    mOnExtraViewCreatedListener.onViewCreated(type, newView);
                }
                View oldView = mViewsCache.get(type);
                if (parentView.getChildAt(0) == oldView) {
                    parentView.removeView(oldView);
                }
                if (newView != null) {
                    parentView.addView(newView);
                }
                mViewsCache.put(type, newView);
            }

        }
    }

    public BaseRecyclerAdapter getInnerAdapter() {
        return mInnerAdapter;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getData() {
        if (mInnerAdapter != null)
            return mInnerAdapter.getData();
        return new ArrayList<>();
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    /**
     * @return 返回当前页面状态是否为加载中状态
     */
    public boolean isLoading() {
        return mIsLoading;
    }

    /**
     * 获取当前页面的加载类型
     *
     * @return
     */
    public @LoadType
    int getCurrentLoadType() {
        return mCurLoadType;
    }

    /**
     * 底部是否有更多数据未加载
     *
     * @return
     */
    public boolean hasMoreNext() {
        return mSupportLoadNextPage && mHasNextPage;
    }

    /**
     * 顶部是否有更多数据未加载
     *
     * @return
     */
    public boolean hasMorePre() {
        return mSupportLoadPrePage && mHasPrePage;
    }

    /**
     * 隐藏所有的footerView
     */
    private void hideFooterView() {
        if (mFooterContainer.getChildCount() > 0) {
            mFooterContainer.removeAllViews();
        }
        mCurrentFooterType = NO_VIEW;
    }

    /**
     * 根据当前需要的footerType设置footerView
     *
     * @param footerType
     */
    private void showFooterView(@ExtraViewType int footerType) {
        if ((mCurrentFooterType == footerType && (footerType != FOOTER_TYPE_NO_MORE || !mShowBottomNoMore)) || mFooterContainer == null)
            return;

        if (mFooterContainer.getChildCount() > 0) {
            mFooterContainer.removeAllViews();
        }
        if (footerType != FOOTER_TYPE_NO_MORE || mShouldShowNoMore) {
            View newFooter = inflateExtraView(footerType, mFooterContainer);
            if (newFooter != null) {
                if (footerType == FOOTER_TYPE_NO_MORE && mShowBottomNoMore) {
                    ((FrameLayout.LayoutParams) newFooter.getLayoutParams()).topMargin = 0;
                    newFooter.setVisibility(INVISIBLE); ////TO DO.. 直接measure得到的newFooter高度有误差？
                    mFooterContainer.addView(newFooter);
                    post(() -> {
                        if (!canScrollVertically(-1) && !canScrollVertically(1)) {
                            int extraSpace = getHeight() - getPaddingTop() - getPaddingBottom();
                            for (int i = 0; i < getChildCount(); i++) {
                                if (getChildAt(i) != mFooterContainer) {
                                    extraSpace -= getChildAt(i).getHeight();
                                }
                            }
                            if (newFooter.getMeasuredHeight() < extraSpace) {
                                ((FrameLayout.LayoutParams) newFooter.getLayoutParams())
                                        .topMargin = extraSpace - newFooter.getMeasuredHeight();
                                newFooter.requestLayout();
                            }
                        }
                        newFooter.setVisibility(VISIBLE);
                    });
                } else {
                    mFooterContainer.addView(newFooter);
                }
            }
        }
        mCurrentFooterType = footerType;
    }

    /**
     * 隐藏header
     */
    private void hideHeaderView() {
        if (mAdapterWrapper == null || mAdapterWrapper.getHeadersCount() == 0)
            return;

        if (mHeaderContainer.getChildCount() > 0) {
            mHeaderContainer.removeAllViews();
        }
//        mAdapterWrapper.removeHeaderView(mHeaderContainer);
        mCurrentHeaderType = NO_VIEW;
    }

    /**
     * 显示对应类型的headerView
     *
     * @param headerType
     */
    private void showHeaderView(@ExtraViewType int headerType) {
        if (mCurrentHeaderType == headerType || mAdapterWrapper == null)
            return;

        if (mAdapterWrapper.getHeadersCount() == 0) {
            mAdapterWrapper.addHeaderView(mHeaderContainer);
        }
        if (mHeaderContainer.getChildCount() > 0) {
            mHeaderContainer.removeAllViews();
        }
        View newHeader = inflateExtraView(headerType, mHeaderContainer);
        if (newHeader != null) {
            mHeaderContainer.addView(newHeader);
        }
        mCurrentHeaderType = headerType;
    }

    /**
     * 根据当前的类型获取相应的view
     *
     * @param type
     * @param parentView
     * @return
     */
    private View inflateExtraView(@ExtraViewType int type, FrameLayout parentView) {
        View view = null;
        if (mViewsCache.get(type) != null) {
            view = mViewsCache.get(type);
        }
        if (view != null) {
            return view;
        }

        int layoutId = 0;
        switch (type) {
            case FOOTER_TYPE_LOAD_MORE:
                layoutId = mLoadMoreFooterLayoutRes;
                break;
            case FOOTER_TYPE_LOADING:
                layoutId = mLoadingFooterLayoutRes;
                break;
            case FOOTER_TYPE_NO_MORE:
                layoutId = mNoMoreFooterLayoutRes;
                break;
            case HEADER_TYPE_LOADING:
                layoutId = mLoadingHeaderLayoutRes;
                break;
            case HEADER_TYPE_LOAD_MORE:
                layoutId = mLoadMoreHeaderLayoutRes;
                break;
            case EMPTY_TYPE_NO_CONTENT:
            case EMPTY_TYPE_NO_CONTENT_GUEST:
                layoutId = mNoContentFooterLayoutRes;
                break;
            case EMPTY_TYPE_NO_NETWORK:
            case EMPTY_TYPE_SERVER_ERROR:
                layoutId = R.layout.view_no_content;
                break;
        }
        if (layoutId != 0) {
            view = LayoutInflater.from(getContext()).inflate(layoutId, parentView, false);
            mViewsCache.put(type, view);
            if (layoutId == R.layout.view_no_content
                    && (type == EMPTY_TYPE_NO_CONTENT
                        || type == EMPTY_TYPE_NO_NETWORK
                        || type == EMPTY_TYPE_SERVER_ERROR
                        || type == EMPTY_TYPE_NO_CONTENT_GUEST)) {
                initEmptyView(view, type);
            } else {
                if (mOnExtraViewCreatedListener != null) {
                    mOnExtraViewCreatedListener.onViewCreated(type, view);
                }
            }
        } else {
            view = null;
        }

        return view;
    }

    /**
     * 根据类型设置当前空白view
     *
     * @param type
     */
    private void showEmptyView(@ExtraViewType int type) {
        if (mCurrentFooterType == type || mFooterContainer == null)
            return;

        hideHeaderView();
        if (mFooterContainer.getChildCount() > 0) {
            mFooterContainer.removeAllViews();
        }
        View newFooter = inflateExtraView(type, mFooterContainer);
        if (newFooter != null) {
            mFooterContainer.addView(newFooter);
        }
        mCurrentFooterType = type;
    }

    public void setNoContentPageProperty(EmptyPageProperty errorPagePropertiesEnum) {
        setExtraView(EMPTY_TYPE_NO_CONTENT, R.layout.view_no_content);
        mNoContentPageProperty = errorPagePropertiesEnum;
    }

    public void setNoContentButtonClickListener(OnClickListener onNoContentButtonClickListener) {
        mNoContentButtonClickListener = onNoContentButtonClickListener;
    }

    private void initEmptyView(View emptyView, int type) {
        if (emptyView == null)
            return;
        if (mNoContentPageProperty.getGravity() == Gravity.TOP) {
            emptyView.setPadding(emptyView.getPaddingLeft(), (int) DimensionUtil.Companion.dpToPx( 10), emptyView.getPaddingRight(), emptyView.getPaddingBottom());
        }
        EmptyPageProperty pageProperty;
        switch (type) {
            case EMPTY_TYPE_NO_CONTENT:
                pageProperty = mNoContentPageProperty;
                break;
            case EMPTY_TYPE_NO_NETWORK:
                pageProperty = EmptyPageProperty.NO_INTERNET;
                break;
            case EMPTY_TYPE_SERVER_ERROR:
                pageProperty = EmptyPageProperty.SERVER_ERROR;
                break;
            default:
                return;
        }
        ViewGroup.LayoutParams layoutParams = emptyView.getLayoutParams();
        layoutParams.width = ScreenUtil.getScreenWidth(this.getContext());
        emptyView.requestLayout();
        ImageView noContentImageView = emptyView.findViewById(R.id.iv_no_content);
        if (pageProperty.getDrawableResId() != -1) {
            noContentImageView.setImageResource(pageProperty.getDrawableResId());
            noContentImageView.setVisibility(VISIBLE);
        } else {
            noContentImageView.setVisibility(GONE);
        }

        TextView title = emptyView.findViewById(R.id.tv_title);
//        TextView subTitle = emptyView.findViewById(R.id.tv_subtitle);
//        TextView actionButton = emptyView.findViewById(R.id.btn_no_content);

        if (pageProperty.getTitleTextColor() != -1) {
            title.setTextColor(pageProperty.getTitleTextColor());
//            subTitle.setTextColor(pageProperty.getTitleTextColor());
        }

        if (pageProperty.getTitleResId() != -1) {
            title.setText(pageProperty.getTitleResId());
            title.setVisibility(VISIBLE);
        } else {
            title.setVisibility(GONE);
        }

//        if (pageProperty.getSubTitleResId() != -1) {
//            subTitle.setText(pageProperty.getSubTitleResId());
//            subTitle.setVisibility(View.VISIBLE);
//        } else {
//            subTitle.setVisibility(View.GONE);
//        }


        if (pageProperty.getBtnResId() != -1) {
//            actionButton.setVisibility(View.VISIBLE);
//            actionButton.setText(pageProperty.getBtnResId());
//            actionButton.setOnClickListener(v -> {
//                if (mNoContentButtonClickListener != null) {
//                    mNoContentButtonClickListener.onClick(actionButton);
//                }
//            });
        } else {
            if(mNoContentButtonClickListener != null){
                emptyView.setOnClickListener(v -> {
                    if (mNoContentButtonClickListener != null) {
                        mNoContentButtonClickListener.onClick(emptyView);
                    }
                });
            }
//            actionButton.setVisibility(View.GONE);
        }
    }

    public interface OnExtraViewCreatedListener {
        void onViewCreated(int viewType, View inflatedView);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (shouldShowScrollTopIcon()) {
            if (mScrollTopBitmap == null) {
                mScrollTopBitmap = BitmapFactory.decodeStream(getContext().getResources()
                        .openRawResource(+R.mipmap.ic_top_btn));
            }
            if (mPaint == null) {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            }
            canvas.drawBitmap(mScrollTopBitmap, getScrollTopIconLeft(), getScrollTopIconTop(), mPaint);
            mIsScrollTopIconShowing = true;
        } else {
            mIsScrollTopIconShowing = false;
        }
    }

    private boolean hasPressedScrollTopIcon = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hasPressedScrollTopIcon = isInScrollTopIconArea(event);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (hasPressedScrollTopIcon && isInScrollTopIconArea(event)) {
                smoothScrollToPosition(0);
            }
        }
        if (hasPressedScrollTopIcon) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    public void setFirstItemPositionToAllowScrollTop(int firstItemPosition) {
        mFirstItemPositionToAllowScrollTop = firstItemPosition;
    }

    public void setMaxItemCountToAllowLoadMore(int maxItemCountToAllowLoadMore) {
        if (maxItemCountToAllowLoadMore > 0) {
            mMaxItemCountToAllowLoadMore = maxItemCountToAllowLoadMore;
        }
    }

    private boolean shouldShowScrollTopIcon() {
        if (!mSupportScrollToTop)
            return false;
        ViewHolder firstChildHolder = getChildViewHolder(getChildAt(0));
        if (firstChildHolder == null)
            return false;
        return firstChildHolder.getLayoutPosition() >= mFirstItemPositionToAllowScrollTop;
    }

    private static final int DEFAULT_SCROLL_TOP_ICON_MARGIN = (int) DimensionUtil.Companion.dpToPx(15);
    private int mScrollTopIconBottomMargin = DEFAULT_SCROLL_TOP_ICON_MARGIN;
    private int mScrollTopIconRightMargin = DEFAULT_SCROLL_TOP_ICON_MARGIN;

    private boolean isInScrollTopIconArea(MotionEvent event) {
        return mIsScrollTopIconShowing && event.getX() > getScrollTopIconLeft()
                && event.getX() < getScrollTopIconRight()
                && event.getY() > getScrollTopIconTop()
                && event.getY() < getScrollTopIconBottom();
    }

    private int getScrollTopIconLeft() {
        return getWidth() - mScrollTopIconRightMargin -
                (mScrollTopBitmap == null ? 0 : mScrollTopBitmap.getWidth());
    }

    private int getScrollTopIconTop() {
        return getHeight() - getPaddingBottom() - mScrollTopIconBottomMargin -
                (mScrollTopBitmap == null ? 0 : mScrollTopBitmap.getHeight());
    }

    private int getScrollTopIconRight() {
        return getWidth() - mScrollTopIconRightMargin;
    }

    private int getScrollTopIconBottom() {
        return getHeight() - getPaddingBottom() - mScrollTopIconBottomMargin;
    }

    public void setScrollTopIconRightMargin(int rightMargin) {
        mScrollTopIconRightMargin = rightMargin;
        if (mIsScrollTopIconShowing) {
            invalidate();
        }
    }

    public void setScrollTopIconBottomMargin(int bottomMargin) {
        mScrollTopIconBottomMargin = bottomMargin;
        if (mIsScrollTopIconShowing) {
            invalidate();
        }
    }
    private float mStartY;
    private float mStartX;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartY = ev.getY();
                mStartX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - mStartX);
                float distanceY = Math.abs(endY - mStartY);
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
