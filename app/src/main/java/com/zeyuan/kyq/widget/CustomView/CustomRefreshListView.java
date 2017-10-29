package com.zeyuan.kyq.widget.CustomView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DataUtils;

/**
 * Created by Administrator on 2016-7-17.
 *
 *
 *
 * @author wwei
 */
public class CustomRefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "CustomRefreshListView";
    private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
    private int downY; // 按下时y轴的偏移量
    private int headerViewHeight; // 头布局的高度
    private View headerView; // 头布局的对象

    private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
    private final int RELEASE_REFRESH = 1; // 松开刷新
    private final int REFRESHING = 2; // 正在刷新中
    private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

    private Animation upAnimation; // 向上旋转的动画
    private Animation downAnimation; // 向下旋转的动画

//    private ImageView ivArrow; // 头布局的剪头
    private ProgressBar mProgressBar; // 头布局的进度条
    private TextView tvState; // 头布局的状态
    private TextView tvLastUpdateTime; // 头布局的最后更新时间

    private OnCustomRefreshListener mOnRefershListener;
    private boolean isScrollToBottom; // 是否滑动到底部
    private View footerView; // 脚布局的对象
    private int footerViewHeight; // 脚布局的高度
    private boolean isLoadingMore = false; // 是否正在加载更多中
    private String timeMills = System.currentTimeMillis()/1000+"";//记录上次的毫秒值
    private onCustomSrcoll mSrcoll;

    public void setmSrcoll(onCustomSrcoll mSrcoll) {
        this.mSrcoll = mSrcoll;
    }

    // 刷新成功
    public static final int SUCCEED = 0;
    // 刷新失败
    public static final int FAIL = 1;
    // 刷新MAX
    public static final int LOADING_MAX = 2;

    public void setTimeMills(String timeMills) {
        this.timeMills = timeMills;
    }

    public CustomRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
        this.setOnScrollListener(this);
    }

    private TextView loadTV;
    private ProgressBar mLoadProgreeBar;
    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.listview_footer, null);
        loadTV = (TextView)footerView.findViewById(R.id.loadstate_tv);
        mLoadProgreeBar = (ProgressBar)footerView.findViewById(R.id.loadstate_pb);
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        this.addFooterView(footerView);
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.listview_header, null);
//        ivArrow = (ImageView) headerView.findViewById(R.id.iv_listview_header_arrow);
        mProgressBar = (ProgressBar) headerView
                .findViewById(R.id.pb_listview_header);
        tvState = (TextView) headerView
                .findViewById(R.id.tv_listview_header_state);
        tvLastUpdateTime = (TextView) headerView
                .findViewById(R.id.tv_listview_header_last_update_time);

        // 设置最后刷新时间
        tvLastUpdateTime.setText("最后刷新时间: " );
//        + getLastUpdateTime()
        headerView.measure(0, 0); // 系统会帮我们测量出headerView的高度
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        this.addHeaderView(headerView); // 向ListView的顶部添加一个view对象
        initAnimation();
    }

    /**
     * 获得系统的最新时间
     *
     * @return
     */
    private String getLastUpdateTime() {
        String temp = DataUtils.showFormatTimeSecond(timeMills);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return sdf.format(System.currentTimeMillis());
        return temp;
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        upAnimation = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

        downAnimation = new RotateAnimation(-180f, -360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downY = (int) ev.getY();
                tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());
                break;
            case MotionEvent.ACTION_MOVE :
                int moveY = (int) ev.getY();
                // 移动中的y - 按下的y = 间距.
                int diff = (moveY - downY) / 2;
                // -头布局的高度 + 间距 = paddingTop
                int paddingTop = -headerViewHeight + diff;
                // 如果: -头布局的高度 > paddingTop的值 执行super.onTouchEvent(ev);
                if (firstVisibleItemPosition == 0
                        && -headerViewHeight < paddingTop) {
                    if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) { // 完全显示了.
                        Log.i(TAG, "松开刷新");
                        currentState = RELEASE_REFRESH;
                        refreshHeaderView();
                    } else if (paddingTop < 0
                            && currentState == RELEASE_REFRESH) { // 没有显示完全
                        Log.i(TAG, "下拉刷新");
                        currentState = DOWN_PULL_REFRESH;
                        refreshHeaderView();
                    }
                    // 下拉头布局
                    headerView.setPadding(0, paddingTop, 0, 0);
//                    return true;
                }
                break;
            case MotionEvent.ACTION_UP :
                // 判断当前的状态是松开刷新还是下拉刷新
                if (currentState == RELEASE_REFRESH) {
                    Log.i(TAG, "刷新数据.");
                    // 把头布局设置为完全显示状态
                    headerView.setPadding(0, 0, 0, 0);
                    // 进入到正在刷新中状态
                    currentState = REFRESHING;
                    refreshHeaderView();

                    if (mOnRefershListener != null) {
                        mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
                    }
                } else if (currentState == DOWN_PULL_REFRESH) {
                    // 隐藏头布局
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                }
                break;
            default :
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据currentState刷新头布局的状态
     */
    private void refreshHeaderView() {
        switch (currentState) {
            case DOWN_PULL_REFRESH : // 下拉刷新状态
                tvState.setText("下拉刷新");

//                ivArrow.startAnimation(downAnimation); // 执行向下旋转
                break;
            case RELEASE_REFRESH : // 松开刷新状态
                tvState.setText("松开刷新");
//                ivArrow.startAnimation(upAnimation); // 执行向上旋转
                break;
            case REFRESHING : // 正在刷新中状态
//                ivArrow.clearAnimation();
//                ivArrow.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                tvState.setText("正在刷新...");
                break;
            default :
                break;
        }
    }

    /**
     * 当滚动状态改变时回调
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {
            // 判断当前是否已经到了底部
            if (isScrollToBottom && !isLoadingMore) {
                isLoadingMore = true;
                // 当前到底部
                Log.i(TAG, "加载更多数据");
                footerView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());

                if (mOnRefershListener != null) {
                    mOnRefershListener.onLoadingMore();
                    mLoadProgreeBar.setVisibility(VISIBLE);
                }
            }
        }

        if(mSrcoll!=null){
            if(scrollState == SCROLL_STATE_IDLE){
                mSrcoll.forSrcollListener(false);
            }else {
                mSrcoll.forSrcollListener(true);
            }
        }

    }

    public void callLoadingViewByInfo(){
        if (!isLoadingMore) {
            isLoadingMore = true;
            // 当前到底部
            Log.i(TAG, "加载更多数据");
            footerView.setPadding(0, 0, 0, 0);
            this.setSelection(this.getCount());

            if (mOnRefershListener != null) {
                mOnRefershListener.onLoadingMore();
                mLoadProgreeBar.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 当滚动时调用
     *
     * @param firstVisibleItem
     *            当前屏幕显示在顶部的item的position
     * @param visibleItemCount
     *            当前屏幕显示了多少个条目的总数
     * @param totalItemCount
     *            ListView的总条目的总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;

        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }
    }

    /**
     * 设置刷新监听事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnCustomRefreshListener listener) {
        mOnRefershListener = listener;
    }

    /**
     * 隐藏头布局
     */
    public void hideHeaderView(int tag,boolean flag) {
        mProgressBar.setVisibility(View.GONE);
        if(tag == SUCCEED){
            tvState.setText("刷新成功");
        }else {
            tvState.setText("刷新失败");
        }
        if(flag){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                    tvState.setText("下拉刷新");
                    setTimeMills(System.currentTimeMillis() / 1000 + "");
                    currentState = DOWN_PULL_REFRESH;
                }
            },1000);
        }else{
            headerView.setPadding(0, -headerViewHeight, 0, 0);
            tvState.setText("下拉刷新");
            setTimeMills(System.currentTimeMillis() / 1000 + "");
            tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());
            currentState = DOWN_PULL_REFRESH;
        }
    }

    /**
     * 显示脚布局
     */
    public void setLoading(){
        isLoadingMore = true;
        // 当前到底部
        Log.i(TAG, "加载更多数据");
        footerView.setPadding(0, 0, 0, 0);
        this.setSelection(this.getCount());

        if (mOnRefershListener != null) {
            mOnRefershListener.onLoadingMore();
            mLoadProgreeBar.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏脚布局
     */
    public void hideFooterView(int tag,boolean flag) {

        mLoadProgreeBar.setVisibility(GONE);
        if(tag == SUCCEED){
            loadTV.setText("加载成功");
        }else if(tag == LOADING_MAX){
            loadTV.setText("没有更多了");
        }else {
            loadTV.setText("刷新失败");
        }
        if(flag){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadTV.setText("加载更多...");
                    footerView.setPadding(0, -footerViewHeight, 0, 0);
                    isLoadingMore = false;
                }
            }, 1000);
        }else {
            loadTV.setText("加载更多...");
            footerView.setPadding(0, -footerViewHeight, 0, 0);
            isLoadingMore = false;
        }
    }

    public interface onCustomSrcoll{
        void forSrcollListener(boolean fit);
    }

}