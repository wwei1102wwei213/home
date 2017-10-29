package com.zeyuan.kyq.widget.CustomView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zeyuan.kyq.R;




/**
 * Created by Administrator on 2016-7-17.
 */
public class CustomLoadingListView extends ListView  {

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

    public CustomLoadingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooterView();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

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
     * 当滚动时调用
     *
     * @param firstVisibleItem
     *            当前屏幕显示在顶部的item的position
     * @param visibleItemCount
     *            当前屏幕显示了多少个条目的总数
     * @param totalItemCount
     *            ListView的总条目的总数
     */
    /*@Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;

        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }
    }*/

    /**
     * 设置刷新监听事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnCustomRefreshListener listener) {
        mOnRefershListener = listener;
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