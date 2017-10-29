package com.zeyuan.kyq.widget.PullToRefresh;

/**
 * Created by Administrator on 2016/5/13.
 */
import android.os.Handler;
import android.os.Message;



public class MyListener implements PullToRefreshLayout.OnRefreshListener
{

    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
    {
        // 下拉刷新操作
        new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // 千万别忘了告诉控件刷新完毕了哦！
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED,true);
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
    {
        // 加载操作
        new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // 千万别忘了告诉控件加载完毕了哦！
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED,true);
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }

}
