package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.EmptyPageFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 *
 * Time 2016-12-05
 * 预后方案
 *
 * @author wwei
 */
public class PrognosisProgramActivity extends BaseActivity implements View.OnClickListener,HttpResponseInterface{

    //用户信息
    private TextView tv_able;
    //图标高度的阶梯值
    private int GradeHeight = 0;
    //滑动器控件
    private View sv;
        //图表控件
        private View v_gj;
        private View v_gn;
        private View v_sy;
        private View v_other;
        private View v_draw_gj;
        private View v_draw_gn;
        private View v_draw_sy;
        private View v_draw_other;
        private TextView tv_gj;
        private TextView tv_gn;
        private TextView tv_sy;
        private TextView tv_other;
    //方案指标控件
    private View v_a;
    private View v_b;
    private View v_c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏
        setStatusBarTranslucent();
        setContentView(R.layout.activity_prognosis_program);
        initStatusBar();
        //用户数据
        initParams();
        //设置视图
        initView();
        //设置数据
        initData();
    }

    private String CancerID;
    private String StepID;
    private String PeriodID;
    private void initParams(){
        CancerID = UserinfoData.getCancerID(this);
        StepID = UserinfoData.getStepID(this);
        PeriodID = UserinfoData.getPeriodID(this);
    }

    private void initView(){
        //设置返回键
        findViewById(R.id.iv_back).setOnClickListener(this);
        //设置昵称
        ((TextView)findViewById(R.id.name)).setText("HI," + UserinfoData.getInfoname(this));
        //sv
        sv = findViewById(R.id.sv);
        //设置文本
        tv_able = (TextView)findViewById(R.id.tv_able);
        setTVAble();
        //图表控件
        v_gj = findViewById(R.id.item_gj);
        v_gn = findViewById(R.id.item_gn);
        v_sy = findViewById(R.id.item_sy);
        v_other = findViewById(R.id.item_other);
        v_draw_gj = findViewById(R.id.v_item_gj);
        v_draw_gn = findViewById(R.id.v_item_gn);
        v_draw_sy = findViewById(R.id.v_item_sy);
        v_draw_other = findViewById(R.id.v_item_other);
        tv_gj = (TextView)findViewById(R.id.tv_num_gj);
        tv_gn = (TextView)findViewById(R.id.tv_num_gn);
        tv_sy = (TextView)findViewById(R.id.tv_num_sy);
        tv_other = (TextView)findViewById(R.id.tv_num_other);
        //指标控件
        v_a = findViewById(R.id.view_a);
        v_b = findViewById(R.id.view_b);
        v_c = findViewById(R.id.view_c);
        setViewABC(v_a,1,"1");
        setViewABC(v_b,2,"1");
        setViewABC(v_c,3,"1");

    }

    private void initData(){

    }

    private void setTVAble(){
        String cancer = MapDataUtils.getCancerValues(CancerID);
        String step = MapDataUtils.getAllStepName(StepID);
        String digit = MapDataUtils.getDigitValues(PeriodID);
        tv_able.setText("");
        tv_able.append("病人正在进行  ");
        tv_able.append(Html.fromHtml("<font color=\"#38DDE3\" font-size:\"30px\">"+step+"治疗"+cancer+
                (TextUtils.isEmpty(digit)?"":digit+"期")+"  </font>"));
        tv_able.append(getClickableSpan());
        tv_able.append("，如果出现肿瘤进展" +"  ，您可能有以下N种方式的治疗建议，其中包括：");
        tv_able.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //flag 1:a 2:b 3:c
    private void setViewABC(View view, final int flag,Object data){
        TextView title = (TextView)view.findViewById(R.id.tv_title_abc);
        TextView unit = (TextView)view.findViewById(R.id.tv_unit);
        TextView num = (TextView)view.findViewById(R.id.tv_num);
        TextView hint = (TextView)view.findViewById(R.id.tv_hint_abc);
        TextView look = (TextView)view.findViewById(R.id.v_look);
        ImageView iv_abc = (ImageView)view.findViewById(R.id.iv_abc);
        if (data==null){
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
            switch (flag){
                case 1:
                    title.setText(getString(R.string.text_abc_a));
                    unit.setText(getString(R.string.text_abc_unit_a));
                    iv_abc.setImageResource(R.mipmap.text_a);
                    break;
                case 2:
                    title.setText(getString(R.string.text_abc_b));
                    unit.setText(getString(R.string.text_abc_unit_b));
                    iv_abc.setImageResource(R.mipmap.text_b);
                    break;
                case 3:
                    title.setText(getString(R.string.text_abc_c));
                    unit.setText(getString(R.string.text_abc_unit_b));
                    iv_abc.setImageResource(R.mipmap.text_c);
                    break;
            }
            look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast(flag+"");
                }
            });
        }
    }

    private void setViewHeight(View v,int size){
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (size>9){
            params.height = 10*getGradeHeight();
        }else {
            params.height = size*getGradeHeight();
        }
        v.setLayoutParams(params);
    }

    private int getGradeHeight(){
        if (GradeHeight == 0) {
            GradeHeight = DensityUtils.dip2px(this, 8);
        }
        return GradeHeight;
    }

    private void toEmptyResult(){
        setEmptyPageFragment(R.mipmap.no_result, "圈圈努力搜查后，未发现匹配的结果，可能是：\n" +
                "1、该症状不常见于肿瘤并发症；\n" +
                "2、该副作用及并发症不常见于患者所处的治疗阶段；\n" +
                "3、该症状未被圈圈收录，请耐心等待。", "去反馈", new EmptyPageFragment.EmptyClickListener() {
            @Override
            public void onEmptyClickListener() {
                FeedbackAPI.openFeedbackActivity();
                finish();
            }
        }, R.id.fl, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {

    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!CancerID.equals(UserinfoData.getCancerID(this))||!StepID.equals(UserinfoData.getStepID(this))
                ||!PeriodID.equals(UserinfoData.getPeriodID(this))){
            initParams();
            setTVAble();
            Factory.post(this, Const.EGetCancerProcess);
        }
    }

    private SpannableString getClickableSpan(){
        View.OnClickListener l = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(PrognosisProgramActivity.this, PerfectDataActivity.class)
                        .putExtra("Diagnosis", 1));
            }
        };
        SpannableString spanableInfo = new SpannableString("（不正确？去修改>）");
        int start = 0;
        int end = spanableInfo.length();
        spanableInfo.setSpan(new Clickable(l), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }

    class Clickable extends ClickableSpan implements View.OnClickListener{
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l){
            mListener = l;
        }

        @Override
        public void onClick(View v){
            mListener.onClick(v);
        }
    }
}
