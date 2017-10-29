package com.zeyuan.kyq.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.widget.ZYDatePicker;

/**
 * Created by Administrator on 2016/4/29.
 *
 * 选择时间控件
 *
 * @author wwei
 */
public class ChooseTimeFragment extends DialogFragment {

    public static final String type = "ChooseTimeFragment";

    public static ChooseTimeFragment instance;
    private ChooseTimeInterface callback;

    public static ChooseTimeFragment getInstance(ChooseTimeInterface callback){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.CHOOSETIME,callback);
        instance = new ChooseTimeFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            callback = (ChooseTimeInterface)bundle.getSerializable(Const.CHOOSETIME);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_common_datetime, null);
        initView(rootView);
        dialog.setContentView(rootView);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.mystyle);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void initView(View v){
        final ZYDatePicker dp = (ZYDatePicker)v.findViewById(R.id.zydate_picker);
        v.findViewById(R.id.btn_time_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempmonth = dp.getMonth();
                tempmonth = Integer.valueOf(tempmonth) < 10 ? "0" + tempmonth : tempmonth;
                String tempday = dp.getDay();
                tempday = Integer.valueOf(tempday) < 10 ? "0" + tempday : tempday;
                String choosetime = dp.getYear() + "-" + tempmonth + "-" + tempday;
                try {
                    long temp = Long.parseLong(DataUtils.showTimeToLoadTime(choosetime)+"000");
                    if(temp>System.currentTimeMillis()){
                        Toast.makeText(getActivity(),"不能选择未来时间",Toast.LENGTH_SHORT).show();
                    }else {
                        if(!TextUtils.isEmpty(choosetime)){
                            callback.timeCallBack(choosetime);
                        }
                        dismiss();
                    }
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"Choose Time Error");
                }
            }
        });
    }

    @Override
    public void onPause() {
        dismiss();
        super.onPause();
    }
}
