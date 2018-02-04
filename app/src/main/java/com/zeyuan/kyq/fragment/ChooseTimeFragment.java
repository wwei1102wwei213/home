package com.zeyuan.kyq.fragment;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeNewInterface;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.widget.wheelview.Common;
import com.zeyuan.kyq.widget.wheelview.WheelView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/4/29.
 *
 * 选择时间控件
 *
 * @author wwei
 */
public class ChooseTimeFragment extends Dialog {


    public static final String type = "ChooseTimeFragment";
    private int chooseType = 1;
    private int ViewTag;
    private int selection;
    private boolean isEnd = false;
    private String oldTime;

    public static ChooseTimeFragment getInstance(ChooseTimeInterface callback, Context context){
        ChooseTimeFragment fragment = new ChooseTimeFragment(context, callback);
        return fragment;
    }

    public static ChooseTimeFragment getInstance(ChooseTimeNewInterface callback, Context context , boolean isEnd,int ViewTag,int selection,String oldTime){
        ChooseTimeFragment fragment = new ChooseTimeFragment(context, callback, isEnd, ViewTag, selection, oldTime);
        return fragment;
    }

    public ChooseTimeFragment(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        init();
    }

    public ChooseTimeFragment(Context context, ChooseTimeInterface callback) {
        super(context, R.style.ActionSheetDialogStyle);
        setCallback(callback);
        this.context = context;
        init();
    }

    public ChooseTimeFragment(Context context, ChooseTimeNewInterface callback,  boolean isEnd,int ViewTag,int selection,String oldTime) {
        super(context, R.style.ActionSheetDialogStyle);
        this.callback2 = callback;
        this.context = context;
        this.isEnd = isEnd;
        this.ViewTag = ViewTag;
        this.selection = selection;
        this.oldTime = oldTime;
        this.chooseType = 2;
        init();
    }

    public ChooseTimeFragment(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private ChooseTimeInterface callback;
    private Context context;
    private ChooseTimeNewInterface callback2;

    public void setCallback(ChooseTimeInterface callback) {
        this.callback = callback;
    }

    public void init () {
        //获取当前Activity所在的窗体
        Window dialogWindow = this.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = 0;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        initView();
    }

    private void initView(){
        View outerView1 = LayoutInflater.from(context).inflate(R.layout.dialog_common_datetime, null);
        //日期滚轮
        final WheelView wv1 = (WheelView) outerView1.findViewById(R.id.wv1);
        //小时滚轮
        final WheelView wv2 = (WheelView) outerView1.findViewById(R.id.wv2);
        //分钟滚轮
        final WheelView wv3 = (WheelView) outerView1.findViewById(R.id.wv3);

        // 格式化当前时间，并转换为年月日整型数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String[] split = sdf.format(new Date()).split("-");
        int currentYear = Integer.parseInt(split[0]);
        int currentMonth = Integer.parseInt(split[1]);
        int currentDay = Integer.parseInt(split[2]);
        wv1.setItems(Common.getYearData(currentYear),0);
        wv2.setItems(Common.getMonthData(),currentMonth-1);
        wv3.setItems(Common.setDays(Common.getLastDay(currentYear, currentMonth)),currentDay-1);

        //联动逻辑效果
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index,String item) {
                try {
                    int selectYear = Integer.valueOf(wv1.getSelectedItem());
                    int selectMonth = Integer.valueOf(wv2.getSelectedItem());
                    int selectDay = Integer.valueOf(wv3.getSelectedItem());
                    int lastDay = Common.getLastDay(selectYear, selectMonth);
                    if (selectDay > lastDay) {
                        wv3.setItems(Common.setDays(lastDay), lastDay - 1);
                    } else {
                        wv3.setItems(Common.setDays(lastDay), selectDay - 1);
                    }
                } catch (Exception e){
                    ExceptionUtils.ExceptionSend(e, "");
                }
            }
        });
        wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index,String item) {
                try {
                    int selectYear = Integer.valueOf(wv1.getSelectedItem());
                    int selectMonth = Integer.valueOf(wv2.getSelectedItem());
                    int selectDay = Integer.valueOf(wv3.getSelectedItem());
                    int lastDay = Common.getLastDay(selectYear, selectMonth);
                    if (selectDay > lastDay) {
                        wv3.setItems(Common.setDays(lastDay), lastDay - 1);
                    } else {
                        wv3.setItems(Common.setDays(lastDay), selectDay - 1);
                    }
                } catch (Exception e){
                    ExceptionUtils.ExceptionSend(e, "");
                }
            }
        });

        //点击确定
        outerView1.findViewById(R.id.btn_time_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String tempmonth = wv2.getSelectedItem();
                tempmonth = Integer.valueOf(tempmonth) < 10 ? "0" + tempmonth : tempmonth;
                String tempday = wv3.getSelectedItem();
                tempday = Integer.valueOf(tempday) < 10 ? "0" + tempday : tempday;
                String choosetime = wv1.getSelectedItem() + "-" + tempmonth + "-" + tempday;
                try {
                    long temp = Long.parseLong(DataUtils.showTimeToLoadTime(choosetime)+"000");
                    if(temp>System.currentTimeMillis()){
                        Toast.makeText(context,"不能选择未来时间",Toast.LENGTH_SHORT).show();
                    }else {
                        if(!TextUtils.isEmpty(choosetime)){
                            try {
                                if (chooseType==2){
                                    callback2.onTimeCallBack((temp/1000)+"",ViewTag,selection);
                                } else {
                                    callback.timeCallBack(choosetime);
                                }
                            } catch (Exception e){

                            }

                        }
                        dismiss();
                    }
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"Choose Time Error");
                }
            }
        });
        try {
            View now = outerView1.findViewById(R.id.btn_time_choose_now);
            if (isEnd){
                now.setVisibility(View.VISIBLE);
                now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback2.onTimeCallBack("0", ViewTag, selection);
                        dismiss();
                    }
                });
            }else {
                now.setVisibility(View.GONE);
            }
        } catch (Exception e){

        }


        setContentView(outerView1);
    }


}
