package com.zeyuan.kyq.widget.CustomView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;


/**
 * Created by Administrator on 2017/2/21.
 *
 *
 * @author wwei
 */
public class CustomExpandableTextView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "ExpandableTextView";

    // The default number of lines;
    private static final int MAX_COLLAPSED_LINES = 3;

    // The default animation duration
    private static final int DEFAULT_ANIM_DURATION = 300;

    // The default alpha value when the animation starts
    private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;

    protected TextView mTv;

//    protected ImageButton mButton; // Button to expand/collapse
    protected TextView mTvBtn;

    private View mExpandFootView;

    private boolean mRelayout;

    private boolean mCollapsed = true; // Show short version as default.

    private int mCollapsedHeight;

    private int mMaxTextHeight;

    private int mMaxCollapsedLines;

    private int mMarginBetweenTxtAndBottom;

//    private Drawable mExpandDrawable;

//    private Drawable mCollapseDrawable;
    private String All_Spread = "展开全文";
    private String All_Retract = "收起全文";

    private int mAnimationDuration;

    @SuppressWarnings("unused")
    private float mAnimAlphaStart;

    public static final int ClickAll = 0;
    public static final int ClickFooter = 1;

    // when in listview , use this map to save collapsed status
    private SparseBooleanArray mConvertTextCollapsedStatus;
    private int mPosition;

    private int mClickType;

    public CustomExpandableTextView(Context context) {
        super(context);
    }

    public CustomExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @Override
    public void onClick(View view) {
        if (mExpandFootView.getVisibility() != View.VISIBLE) {
            return;
        }

        mCollapsed = !mCollapsed;
        if (mConvertTextCollapsedStatus != null) {
            mConvertTextCollapsedStatus.put(mPosition, mCollapsed);
        }
        Log.i(TAG, " put postion " + mPosition + " " + mCollapsed + " this " + this);

        mTvBtn.setText(mCollapsed ? All_Spread : All_Retract);

        Animation animation;

        Log.i(TAG, "click on position " + mPosition + " collapsed " + mCollapsed);

        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
        } else {
            animation = new ExpandCollapseAnimation(this, getHeight(), getHeight() +
                    mMaxTextHeight - mTv.getHeight());
        }

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        clearAnimation();
        startAnimation(animation);
    }

    @SuppressLint("DrawAllocation") @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.d(TAG, " onMeasure ");
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        mExpandFootView.setVisibility(View.GONE);
        mTv.setMaxLines(Integer.MAX_VALUE);

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If the text fits in collapsed mode, we are done.
        if (mTv.getLineCount() <= mMaxCollapsedLines) {
            return;
        }

        // Saves the text height w/ max lines
        mMaxTextHeight = getTextViewRealHeight(mTv);
        Log.i(TAG, " mMaxTextHeight" + mMaxTextHeight);

        // Doesn't fit in collapsed mode. Collapse text view as needed. Show
        // button.
        if (mCollapsed) {
            mTv.setMaxLines(mMaxCollapsedLines);
            mTv.setEllipsize(TextUtils.TruncateAt.END);
        }
//        mButton.setVisibility(View.VISIBLE);
        mExpandFootView.setVisibility(View.VISIBLE);

        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mCollapsed) {
            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
            mTv.post(new Runnable() {
                @Override
                public void run() {
                    mMarginBetweenTxtAndBottom = getHeight() - mTv.getHeight();
                }
            });
            // Saves the collapsed height of this ViewGroup
            mCollapsedHeight = getMeasuredHeight();
        }

    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
        mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableTextView_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
//        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable);
//        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable);
        mClickType = typedArray.getInt(R.styleable.ExpandableTextView_clickListenerType, ClickAll);

        /*if (mExpandDrawable == null) {
            mExpandDrawable = getResources().getDrawable(R.drawable.ic_expand_small_holo_light);
        }
        if (mCollapseDrawable == null) {
            mCollapseDrawable = getResources().getDrawable(R.drawable.ic_collapse_small_holo_light);
        }*/

        typedArray.recycle();
    }

    private static boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private void findViews() {
        mTv = (TextView) findViewById(R.id.expandable_text);
        /*mButton = (ImageButton) findViewById(R.id.expand_collapse);
        mButton.setImageDrawable(mCollapsed ? mExpandDrawable : mCollapseDrawable);*/
        mTvBtn = (TextView) findViewById(R.id.expand_collapse);
        mTvBtn.setText(mCollapsed ? All_Spread : All_Retract);
        mExpandFootView = findViewById(R.id.expand_footer);

        if (mClickType == ClickAll) {
            mTvBtn.setOnClickListener(this);
            setOnClickListener(this);
            mTv.setOnClickListener(this);
            mExpandFootView.setOnClickListener(this);
        } else if (mClickType == ClickFooter) {
            mTvBtn.setOnClickListener(this);
            mTv.setClickable(false);
            setClickable(false);
            mExpandFootView.setOnClickListener(this);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void applyAlphaAnimation(View view, float alpha) {
        if (isPostHoneycomb()) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            // make it instant
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
    }

    public void setText(String text) {
        mRelayout = true;
        if (mTv == null) {
            findViews();
        }
        mTv.setText(text);
        setVisibility(text.length() == 0 ? View.GONE : View.VISIBLE);
    }


    public void setConvertText(SparseBooleanArray convertStatus,int position,String text) {
        mConvertTextCollapsedStatus = convertStatus;
        boolean isCollapsed = mConvertTextCollapsedStatus.get(position, true);
        Log.i(TAG, "setConvertText is collapsed " + isCollapsed + " position" + position + " this " + this);
        mPosition = position;
        clearAnimation();
        mCollapsed = isCollapsed;
        if (mTvBtn != null) {
            mTvBtn.setText(mCollapsed ? All_Spread : All_Retract);
        }
        clearAnimation();
        if (mCollapsed) {
            if (mTv!=null){
                mTv.setMaxLines(mMaxCollapsedLines);
                mTv.setEllipsize(TextUtils.TruncateAt.END);
            }
        } else {
            if (mTv!=null) {
                mTv.setMaxLines(Integer.MAX_VALUE);
            }
        }
        this.getLayoutParams().height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        setText(text);
        requestLayout();
    }


    public CharSequence getText() {
        if (mTv == null) {
            return "";
        }
        return mTv.getText();
    }

    protected class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;

            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int)((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTv.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
//            applyAlphaAnimation(mTv, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize( int width, int height, int parentWidth, int parentHeight ) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds( ) {
            return true;
        }
    };

    private int getTextViewRealHeight(TextView pTextView) {
        Layout layout = pTextView.getLayout();
        int desired = layout.getLineTop(pTextView.getLineCount());
        int padding = pTextView.getCompoundPaddingTop() + pTextView.getCompoundPaddingBottom();
        return desired + padding;
    }
}
