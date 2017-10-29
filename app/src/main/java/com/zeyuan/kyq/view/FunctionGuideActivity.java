package com.zeyuan.kyq.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.DensityUtil;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.WidgetController;
import com.zeyuan.kyq.widget.CircularCoverView;

import static com.zeyuan.kyq.R.id.tv_ok;

/**
 * Created by Administrator on 2017/8/7.
 * 功能引导界面
 */

public class FunctionGuideActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showGuideView();
    }

    //展示引导图
    private void showGuideView() {
        int witch = getIntent().getIntExtra("witch", 0);
        switch (witch) {
            case 0:
                finish();
                break;
            case 1://发帖提示
                setContentView(R.layout.layout_guide_translucent);
                initView_guide_content();
                setViewLocation(42, 168, true, true, false);
                ccv_guide.setDPRadians(6);
                ccv_guide.drawTransparentRect(getIntent().getIntArrayExtra("xy"), getIntent().getIntExtra("width", 0), getIntent().getIntExtra("height", 0));
                setDefaultClickListener();
                break;
            case 2://病历功能提示
                setContentView(R.layout.layout_guide_translucent);
                initView_guide_content();
                setContent_record(1);
                break;
            case 201://病历分享提示
                setContentView(R.layout.layout_guide_translucent);
                initView_guide_content();
                setContent_RecordShare();
                break;
            case 3://文章点赞提示
                setContentView(R.layout.layout_guide_translucent);
                initView_guide_content();
                setContent_article(getIntent().getIntArrayExtra("xy"), getIntent().getIntExtra("width", 0), getIntent().getIntExtra("height", 0));
                break;
            case 4://精准内容提示
                setContentView(R.layout.layout_guide_translucent);
                initView_guide_content();
                setContent_accurate(getIntent().getIntArrayExtra("xy"), getIntent().getIntExtra("width", 0), getIntent().getIntExtra("height", 0));
                break;
            case 5://圈圈助手病情总体情况提示
                setContentView(R.layout.layout_guide_translucent);
                initView_guide_content();
                setOverallSituation(getIntent().getIntArrayExtra("xy"), getIntent().getIntExtra("width", 0), getIntent().getIntExtra("height", 0));
                break;
            case 6://文章作者信息提示
                setContentView(R.layout.layout_guide_translucent);
                initView_guide_content();
                setAuthorInfoTips(getIntent().getIntArrayExtra("xy"), getIntent().getIntExtra("width", 0), getIntent().getIntExtra("height", 0));
                break;
            case 7://成功关注好友提示
                setContentView(R.layout.layout_guide_image);
                initImageView(R.mipmap.ic_guide_focus_friend);
                break;
            case 8://成功关注大v
                setContentView(R.layout.layout_guide_image);
                initImageView(R.mipmap.ic_guide_focus_v);
                break;
            case 9://打开圈子的好友页
                setContentView(R.layout.layout_guide_image);
                initImageView(R.mipmap.ic_guide_friend_forum);
                break;
            case 10://打开圈子的项目页
                setContentView(R.layout.layout_guide_image);
                initImageView(R.mipmap.ic_guide_project);
                break;
            case 11://打开圈子的同城圈页
                setContentView(R.layout.layout_guide_image);
                initImageView(R.mipmap.ic_guide_local);
                break;
            case 12://显示选择当前阶段的提示
                setContentView(R.layout.layout_guide_translucent);
                initView_guide_content();
                setSelectCurrentStepTips();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.setContentView(layoutResID);
    }

    //提示内容框
    View view_guide_content;
    ImageView iv_guide;
    TextView tv_guide_title;
    TextView tv_guide_info;
    View ok;
    //提示内容框上的箭头
    ImageView iv_arrow;
    //有透明圆角矩形框的半透明View
    CircularCoverView ccv_guide;

    //初始化引导内容提示框
    private void initView_guide_content() {
        ok = findViewById(tv_ok);
        view_guide_content = findViewById(R.id.view_guide_content);
        iv_guide = (ImageView) findViewById(R.id.iv_guide);
        tv_guide_title = (TextView) findViewById(R.id.tv_guide_title);
        tv_guide_info = (TextView) findViewById(R.id.tv_guide_info);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        ccv_guide = (CircularCoverView) findViewById(R.id.ccv_guide);

    }


    //单图提示
    private void initImageView(int imageId) {
        ImageView iv_content = (ImageView) findViewById(R.id.iv_content);
        iv_content.setImageResource(imageId);
        iv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //默认点击事件
    private void setDefaultClickListener() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //病历顶部的分享按钮提示
    private void setContent_RecordShare() {
        tv_guide_title.setText("分享病历给医生/抗癌高手");
        tv_guide_info.setText("分享给医生/抗癌高手,病情一目了然");
        iv_guide.setImageResource(R.mipmap.ic_guide_record_share);
        ccv_guide.setDPRadians(4);
        //屏幕宽度
        int ww = DensityUtil.getWindowDPWidth(getApplicationContext());
        //透明矩形框位置和大小
        ccv_guide.drawTransparentRect(new int[]{DensityUtil.dip2px(getApplication(), ww - 35), DensityUtil.dip2px(getApplication(), 38)}, DensityUtil.dip2px(getApplication(), 22), DensityUtil.dip2px(getApplication(), 22));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setViewLocation(17, 41, true, false, true);
    }

    //病历页面下面的个提示框
    private void setContent_record(int step) {
        switch (step) {
            case 1:
                tv_guide_title.setText("记录主要治疗手段");
                tv_guide_info.setText("例如手术、靶向、放化疗等");
                iv_guide.setImageResource(R.mipmap.ic_guide_record_step);
                ccv_guide.setDPRadians(4);
                //屏幕高度
                int wh = DensityUtil.getWindowDPHeight(getApplicationContext());
                //屏幕宽度
                int ww1 = DensityUtil.getWindowDPWidth(getApplicationContext());
                //透明矩形框位置
                ccv_guide.drawTransparentRect(new int[]{DensityUtil.dip2px(getApplication(), 10), DensityUtil.dip2px(getApplication(), wh - 44)}, DensityUtil.dip2px(getApplication(), ww1 / 2 - 20), DensityUtil.dip2px(getApplication(), 40));
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContent_record(2);
                    }
                });
                setViewLocation(80, 46, false, true, false);
                break;
            case 2:
                tv_guide_title.setText("可记录每日的病情变化");
                tv_guide_info.setText("例如心态、身体、指标等变化");
                iv_guide.setImageResource(R.mipmap.ic_guide_record_take);
                //屏幕高度
                int wh1 = DensityUtil.getWindowDPHeight(getApplicationContext());
                //屏幕宽度
                int ww2 = DensityUtil.getWindowDPWidth(getApplicationContext());
                //透明矩形框位置
                ccv_guide.drawTransparentRect(new int[]{DensityUtil.dip2px(getApplication(), ww2 / 2 + 10), DensityUtil.dip2px(getApplication(), wh1 - 44)}, DensityUtil.dip2px(getApplication(), ww2 / 2 - 20), DensityUtil.dip2px(getApplication(), 40));
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                setViewLocation(80, 46, true, true, false);
                break;

        }
    }

    //设置文章点赞提示的内容
    private void setContent_article(int[] xy, int width, int height) {
        tv_guide_title.setText("可以给精彩的文章点赞哦!");
        tv_guide_info.setText("你的点赞是给作者最好的肯定和支持");
        iv_guide.setImageResource(R.mipmap.ic_guide_article_like);
        setDefaultClickListener();
        ccv_guide.setDPRadians(6);
        ccv_guide.drawTransparentRect(xy, width, height);
        setViewLocation(22, 46, true, true, false);
    }

    //首页精准内容提示
    private void setContent_accurate(int[] xy, int width, int height) {
        tv_guide_title.setText("精准的查看你关注的内容");
        tv_guide_info.setText("点击这里可以查询您想要关注的信息");
        iv_guide.setImageResource(R.mipmap.ic_guide_accurate);
        setDefaultClickListener();
        ccv_guide.setDPRadians(6);
        ccv_guide.drawTransparentRect(xy, width, height);
        setViewLocation(DensityUtil.getWindowDPWidth(getApplicationContext()) / 2 - 7, 44, true, false, true);
    }

    //病情总体情况提示
    private void setOverallSituation(int[] xy, int width, int height) {
        tv_guide_title.setText("个人信息展示区");
        tv_guide_info.setText("展示您的个人信息，点击可查看详情");
        iv_guide.setImageResource(R.mipmap.ic_guide_person_info);
        setDefaultClickListener();
        ccv_guide.setDPRadians(8);
        ccv_guide.drawTransparentRect(xy, width, height);
        setViewLocation(70, DensityUtil.px2dip(getApplication(), height + xy[1] - DensityUtil.getStatusBarHeight(getApplicationContext())) + 2, false, false, true);
    }

    //文章作者信息提示
    private void setAuthorInfoTips(int[] xy, int width, int height) {
        tv_guide_title.setText("可以关注喜欢的作者哦！");
        tv_guide_info.setText("关注作者，在首页可以看到他的新文章");
        iv_guide.setImageResource(R.mipmap.ic_guide_focus_author);
        setDefaultClickListener();
        ccv_guide.setDPRadians(8);
        ccv_guide.drawTransparentRect(xy, width, height);
        //屏幕宽度
        int ww1 = DensityUtil.getWindowDPWidth(getApplicationContext());
        setViewLocation(ww1 / 2, 48, true, false, true);
    }

    //编辑阶段选择当前阶段的提示
    private void setSelectCurrentStepTips() {
        tv_guide_title.setText("标记您正在使用该治疗方案");
        tv_guide_info.setText("此阶段是个性化推荐和诊断治疗的重要指标之一");
        iv_guide.setImageResource(R.mipmap.ic_guide_select_current_step);
        setDefaultClickListener();
        ccv_guide.setDPRadians(6);
        //屏幕宽度
        int ww1 = DensityUtil.getWindowDPWidth(getApplicationContext());
        //透明矩形框位置
        ccv_guide.drawTransparentRect(new int[]{DensityUtil.dip2px(getApplication(), ww1 - 78), DensityUtil.dip2px(getApplication(), 79)}, DensityUtil.dip2px(getApplication(), 58), DensityUtil.dip2px(getApplication(), 40));
        setViewLocation(40, 100, true, false, true);
    }

    /**
     * 调整提示框和箭头的位置
     *
     * @param arrowMarginX 箭头距离左边或右边的距离 单位：dp
     * @param arrowMarginY 箭头距离上边或下边的距离 单位：dp
     * @param isArrowRight 箭头是否居右
     * @param isBottom     整体是否居下
     * @param isArrowUp    箭头是否指向上
     */
    private void setViewLocation(int arrowMarginX, int arrowMarginY, boolean isArrowRight, boolean isBottom, boolean isArrowUp) {
        try {
            //提示框只需要调整y轴位置
            WidgetController.setLayoutY(view_guide_content, getContentLocation(arrowMarginY + 9, isBottom));
            //设置箭头指示方向
            if (isArrowUp) {
                iv_arrow.setImageResource(R.mipmap.ic_guide_sanjiao_up);
            } else {
                iv_arrow.setImageResource(R.mipmap.ic_guide_sanjiao_down);
            }
            //箭头需要调整x和y轴两个位置
            int[] xy2 = getArrowLocal(arrowMarginX, arrowMarginY, isArrowRight, isBottom);
            WidgetController.setLayout(iv_arrow, xy2[0], xy2[1]);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "setViewLocation");
        }
    }

    /**
     * 计算提示框的y值
     *
     * @param margin 间距 dp
     * @return px
     */
    private int getContentLocation(int margin, boolean isBottom) {
        WindowManager winManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (isBottom) {
            //提示框居下，需要减去距下宽度、提示框自身高度
            return winManager.getDefaultDisplay().getHeight() - DensityUtil.dip2px(getApplication(), margin + 62);
        } else {
            //提示框居上，需要加上状态栏高度
            return DensityUtil.dip2px(getApplication(), margin) + DensityUtil.getStatusBarHeight(getApplicationContext());
        }
    }

    /**
     * 计算箭头的x,y值
     *
     * @param marginX 上或下间距 dp
     * @param marginY 左或右间距 dp
     * @return px值数组
     */
    private int[] getArrowLocal(int marginX, int marginY, boolean isRight, boolean isBottom) {
        int[] xy = new int[2];
        WindowManager winManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (isRight) {
            //箭头居右，箭头的x坐标为屏宽减去距右宽度和箭头自身宽度
            xy[0] = winManager.getDefaultDisplay().getWidth() - DensityUtil.dip2px(getApplication(), marginX + 15);
        } else {
            xy[0] = DensityUtil.dip2px(getApplication(), marginX);
        }
        if (isBottom) {
            //箭头居下，箭头的y坐标为屏高减去距底部高度和箭头自身高度
            xy[1] = winManager.getDefaultDisplay().getHeight() - DensityUtil.dip2px(getApplication(), marginY + 10);
        } else {
            //箭头居上，距顶部高度加上状态栏高度
            xy[1] = DensityUtil.dip2px(getApplication(), marginY) + DensityUtil.getStatusBarHeight(getApplicationContext());
        }
        return xy;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
    }
}
