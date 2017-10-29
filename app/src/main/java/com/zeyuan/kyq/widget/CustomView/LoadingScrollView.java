package com.zeyuan.kyq.widget.CustomView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.zeyuan.kyq.widget.ScrollViewHeader;

import java.util.ArrayList;
import java.util.List;

/***
 * Created by Administrator on 2016/4/15.
 *
 *
 * @author wwei
 */
public class LoadingScrollView extends ScrollView {

    Context mContext;
    private View mView;
    private Rect mRect = new Rect();
    private float touchY;
    private OnRefreshScrollViewListener listener = null;
    private LoadingScrollViewListstener mListener = null;
    private int lastY;
    private Scroller scroller = null;
    private LinearLayout scrollContainer = null;
    private ScrollViewHeader headerView = null;

    public void setmListener(LoadingScrollViewListstener mListener) {
        this.mListener = mListener;
    }

    private LinearLayout header;
    private int headerHeight;   //头高度
    private int lastHeaderPadding; //最后一次调用Move Header的Padding
    private boolean isBack; //从Release 转到 pull

    private int headerState = DONE;
    static final private int RELEASE_To_REFRESH = 0;
    static final private int PULL_To_REFRESH = 1;
    static final private int REFRESHING = 2;
    static final private int DONE = 3;

    private static final int MAX_SCROLL_HEIGHT = 400;// 最大滑动距离
    private static final float SCROLL_RATIO = 0.4f;// 阻尼系数

    private final static int SCROLL_DURATION = 400;
    private final static float OFFSET_RADIO = 1.8f;
    //    private int headerHeight = 0;
    private boolean enableRefresh = true;
    private boolean refreshing = false;
    private boolean loading = false;

    private List<View> views = new ArrayList<View>();

    public void hideLoading(){
        loading = false;
    }

    public LoadingScrollView(Context context) {
        super(context);
        this.mContext = context;

    }

    public LoadingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

    }

    public LoadingScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            this.mView = getChildAt(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
            touchY = arg0.getY();
        }

        /*if(views!=null&&checkAllViews(views,arg0)){
            return true;
        }*/

        return super.onInterceptTouchEvent(arg0);
    }

    private int beginY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mView == null) {
            return super.onTouchEvent(ev);
        } else {
            commonOnTouchEvent(ev);
        }
        /*commonOnTouchEvent(ev);
        //如果Header是完全被隐藏的则让ScrollView正常滑动，让事件继续否则的话就阻断事件
        if(lastHeaderPadding > (-1*headerHeight) && headerState != REFRESHING) {
            return true;
        } else {
            return false;
        }*/
        return super.onTouchEvent(ev);
    }

    private void commonOnTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchY = ev.getY();
                beginY = (int) ((int) ev.getY() + getScrollY()*1.5);
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = touchY;
                float nowY = ev.getY();
                int deltaY = (int) (preY - nowY);
                touchY = nowY;

                if (isNeedMove()) {
                    if (mRect.isEmpty()) {
                        mRect.set(mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom());
                    }
                    int offset = mView.getTop() - deltaY;


                    if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                        mView.layout(mView.getLeft(), mView.getTop() - (int) (deltaY * SCROLL_RATIO), mView.getRight(), mView.getBottom()
                                - (int) (deltaY * SCROLL_RATIO));
                    }
                }

                if (isNeedLoading()&&!loading&&deltaY>0){
                    loading = true;
                    if(mListener!=null){
                        mListener.onLoadingData();
                    }
                }

                break;
            default:
                break;
        }

    }



    private boolean isNeedMove() {
        int viewHight = mView.getMeasuredHeight();
        int srollHight = getHeight();
        int offset = viewHight - srollHight;
        int scrollY = getScrollY();

        if (scrollY == 0 ) {
            return true;
        }
        return false;
    }

    private boolean isNeedLoading() {
        int viewHight = mView.getMeasuredHeight();
        int srollHight = getHeight();
        int offset = viewHight - srollHight;
        int scrollY = getScrollY();

        if ( scrollY == offset && scrollY != 0) {
            return true;
        }
        return false;
    }



    private boolean isNeedAnimation() {
        return !mRect.isEmpty();
    }

    private void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, mView.getTop(), mRect.top);
        ta.setDuration(200);
        mView.startAnimation(ta);
        mView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        mRect.setEmpty();
    }



    public interface OnRefreshScrollViewListener {
        void onRefresh();
    }

    public void addUnTouchableView(View view) {
        try {
            if (!views.contains(view)) {
                views.add(view);
            }
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
    }

    public void delUnTouchableView(View view) {
        try {
            if (views.contains(view)) {
                views.remove(view);
            }

        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
    }

    public void delAllUnTouchableView() {
        try {
            if (views.size() > 0) {
                views.clear();
            }

        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkAllViews(List<View> views, MotionEvent event) {
        for (View view : views) {
            if (checkInLvArea(view, event)) {
                return true;
            }
        }
        return false;
    }


    private boolean checkInLvArea(View v, MotionEvent event) {
        try {
            float x = event.getRawX();
            float y = event.getRawY();
            int[] locate = new int[2];
            v.getLocationOnScreen(locate);
            int l = locate[0];
            int r = l + v.getWidth();
            int t = locate[1];
            int b = t + v.getHeight();
            if (l < x && x < r && t < y && y < b) {
                return true;
            }
            return false;
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 更新headerview的高度,同时更改状态
     *
     * @param deltY
     */
    public void updateHeader(float deltY) {
        int currentMargin = (int) (headerView.getTopMargin() + deltY);
        headerView.updateMargin(currentMargin);
        if(enableRefresh && !refreshing) {
            if (currentMargin > 0) {
                headerView.setState(ScrollViewHeader.STATE_READY);
            } else {
                headerView.setState(ScrollViewHeader.STATE_NORMAL);
            }
        }
    }

    /**
     * 重置headerview的高度
     */
    public void resetHeaderView() {
        int margin = headerView.getTopMargin();
        if(margin == -headerHeight) {
            return ;
        }
        if(margin < 0 && refreshing) {
            //当前已经在刷新，又重新进行拖动,但未拖满,不进行操作
            return ;
        }
        int finalMargin = 0;
        if(margin <= 0 && !refreshing) {
            finalMargin = headerHeight;
        }
        //松开刷新，或者下拉刷新，又松手，没有触发刷新
        scroller.startScroll(0, -margin, 0, finalMargin + margin, SCROLL_DURATION);

        invalidate();
    }

    public interface  LoadingScrollViewListstener{
        void onLoadingData();
    }

}