package com.zeyuan.kyq.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * Created by Administrator on 2016/4/15.
 *
 *
 * @author wwei
 */
public class CustomScrollView extends ScrollView{

    Context mContext;
    private View mView;
    private Rect mRect = new Rect();
    private float touchY;
    private OnRefreshScrollViewListener listener = null;

    private int lastY;
    private Scroller scroller = null;
//    private OnRefreshScrollViewListener listener = null;
    private LinearLayout scrollContainer = null;
    private ScrollViewHeader headerView = null;


    private LinearLayout header;
    private int headerHeight;   //头高度
    private int lastHeaderPadding; //最后一次调用Move Header的Padding
    private boolean isBack; //从Release 转到 pull

    private int headerState = DONE;
    static final private int RELEASE_To_REFRESH = 0;
    static final private int PULL_To_REFRESH = 1;
    static final private int REFRESHING = 2;
    static final private int DONE = 3;

    private int MAX_SCROLL_HEIGHT = 400;// 最大滑动距离
    private static final float SCROLL_RATIO = 0.8f;// 阻尼系数

    private final static int SCROLL_DURATION = 400;
    private final static float OFFSET_RADIO = 1.8f;
//    private int headerHeight = 0;
    private boolean enableRefresh = true;
    private boolean refreshing = false;
    private boolean move = true;

    public void setMove(boolean move){
        this.move = move;
    }

    private List<View> views = new ArrayList<View>();

    public CustomScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setMaxScrollHeight(int msh) {
        MAX_SCROLL_HEIGHT = msh;
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;

    }




    private ProgressBar headProgress;
    private TextView tipsTxt;
    private TextView lastUpdateTxt;
    private ImageView arrowImg;
    private RotateAnimation tipsAnimation;
    private RotateAnimation reverseAnimation;




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
            startMove();
            touchY = arg0.getY();
        }else if (arg0.getAction() == MotionEvent.ACTION_CANCEL){
            stopMove();
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
            /*if (ev.getAction() == MotionEvent.ACTION_CANCEL||ev.getAction() == MotionEvent.ACTION_UP){
                stopMove();
            }*/
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
                }else {
                    stopMove();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = touchY;
                float nowY = ev.getY();
                int deltaY = (int) (preY - nowY);
                touchY = nowY;
//                int interval = (int) (ev.getY() - beginY);
//                Log.i(Const.TAG.ZY_TEST,"interval:"+interval);
                if (isNeedMove()) {
                    if (mRect.isEmpty()) {
                        mRect.set(mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom());
                    }
                    int offset = mView.getTop() - deltaY;

//                    Log.i(Const.TAG.ZY_TEST,"offset:"+offset);
                    if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                        mView.layout(mView.getLeft(), mView.getTop() - (int) (deltaY * SCROLL_RATIO), mView.getRight(), mView.getBottom()
                                - (int) (deltaY * SCROLL_RATIO));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void startMove(){
        if (mScroll!=null) {
            mScroll.forScrollCustomChange(false);
        }
    }

    public void updata(){
        headerState = DONE;
        lastUpdateTxt.setText("最近更新:" + new Date().toLocaleString());
        changeHeaderViewByState();
    }

    private boolean isNeedMove() {
        int viewHight = mView.getMeasuredHeight();
        int srollHight = getHeight();
        int offset = viewHight - srollHight;
        int scrollY = getScrollY();

        if ((scrollY == 0 || scrollY == offset)&&move) {
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
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                stopMove();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void changeHeaderViewByState() {
        switch (headerState) {
            case PULL_To_REFRESH:
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    arrowImg.startAnimation(reverseAnimation);
                    tipsTxt.setText("下拉刷新");
                }
                tipsTxt.setText("下拉刷新");
                break;
            case RELEASE_To_REFRESH:
                arrowImg.setVisibility(View.VISIBLE);
                headProgress.setVisibility(View.GONE);
                tipsTxt.setVisibility(View.VISIBLE);
                lastUpdateTxt.setVisibility(View.VISIBLE);
                arrowImg.clearAnimation();
                arrowImg.startAnimation(tipsAnimation);
                tipsTxt.setText("松开刷新");
                break;
            case REFRESHING:
                lastHeaderPadding = 0;
                header.setPadding(0, lastHeaderPadding, 0, 0);
                header.invalidate();
                headProgress.setVisibility(View.VISIBLE);
                arrowImg.clearAnimation();
                arrowImg.setVisibility(View.INVISIBLE);
                tipsTxt.setText("正在刷新...");
                lastUpdateTxt.setVisibility(View.VISIBLE);
                break;
            case DONE:
                lastHeaderPadding = -1 * headerHeight;
                header.setPadding(0, lastHeaderPadding, 0, 0);
                header.invalidate();
                headProgress.setVisibility(View.GONE);
                arrowImg.clearAnimation();
                arrowImg.setVisibility(View.VISIBLE);
                tipsTxt.setText("下拉刷新");
                lastUpdateTxt.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
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

    private  onScrollCustomChange mScroll;

    public void setScrollCustomChange(onScrollCustomChange mScroll) {
        this.mScroll = mScroll;
    }

    /*@Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        if(mScroll!=null){
            if(screenState == SCROLL_STATE_IDLE){
               *//* if(isLoadingMore){
                    mSrcoll.forSrcollListener(true);
                }else {
                    mSrcoll.forSrcollListener(false);
                }*//*
                mScroll.forScrollCustomChange(false);
            }else {
                mScroll.forScrollCustomChange(true);
            }
        }
    }*/

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        if (clampedX||clampedY){
//            stopMove();
        }

    }



    private void stopMove(){
        if (mScroll!=null){
            mScroll.forScrollCustomChange(true);
        }
    }

    public interface onScrollCustomChange{
        void forScrollCustomChange(boolean fit);
    }

    private OnScrollListener onScrollListener;

    public interface OnScrollListener{
        public void onScroll(int x, int y, int oldx, int oldy);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onScrollListener!=null){
            onScrollListener.onScroll(l,t,oldl,oldt);
        }
    }

    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener=onScrollListener;
    }



}