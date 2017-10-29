package com.zeyuan.kyq.widget.CustomView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2016/10/14.
 *
 * 配合PullableCallUpScrollView实现上拉功能
 *      不需要下拉功能
 *
 * @author wwei
 */
public class CustomCallUpListView extends ListView{

    private static final String TAG = "CustomRefreshListView";

    private OnCustomRefreshListener mOnRefershListener;


    private View footerView; // 脚布局的对象
    private int footerViewHeight; // 脚布局的高度
    private boolean isLoadingMore = false; // 是否正在加载更多中

    //滑动状态改变监听
    /*private onCustomScrollListView mScroll;

    public void setCustomScrollChangeForListView(onCustomScrollListView mScroll) {
        this.mScroll = mScroll;
    }*/

    // 刷新成功
    public static final int SUCCEED = 0;
    // 刷新失败
    public static final int FAIL = 1;
    // 刷新MAX
    public static final int LOADING_MAX = 2;


    public CustomCallUpListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooterView();
//        this.setOnScrollListener(this);
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
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     *
     * 在父控件到达底部后调用此方法，实现上拉
     *
     */
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

}
