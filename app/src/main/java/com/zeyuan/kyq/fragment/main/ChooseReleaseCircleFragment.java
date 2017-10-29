package com.zeyuan.kyq.fragment.main;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.forcallback.FragmentMoreCallBack;
import com.zeyuan.kyq.utils.BlurUtil.BlurColor;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.view.ReleaseForumActivity;
import com.zeyuan.kyq.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2017/5/18.
 * 发布帖子时选择圈子的界面
 */

public class ChooseReleaseCircleFragment extends DialogFragment {
    private Context context;
    private FragmentMoreCallBack callback;

    public void setCallback(FragmentMoreCallBack callback) {
        this.callback = callback;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.cancer_dialog);
        try {
            context = getActivity();
            dialog.setCancelable(false);
            View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_choose_release_circle, null);
            initView(rootView);
            initData();
            dialog.setContentView(rootView);
            Window window = dialog.getWindow();
            if (window != null) {
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
                window.setWindowAnimations(R.style.choose_step_type);
            }
            dialog.setCanceledOnTouchOutside(true);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "ChooseReleaseCircleFragment");
        }
        return dialog;
    }


    private TextView tv_cancer_circle_name;
    private FlowLayout fl_circle_tag;//选择癌症圈的子标签
    private FlowLayout fl_other_circle;//选择要发布的其他圈子
    private Button btn_release;

    private void initView(View rootView) {
        f_body = rootView.findViewById(R.id.f_body);
        //initBlur();
        tv_cancer_circle_name = (TextView) rootView.findViewById(R.id.tv_cancer_circle_name);
        fl_circle_tag = (FlowLayout) rootView.findViewById(R.id.fl_circle_tag);
        fl_other_circle = (FlowLayout) rootView.findViewById(R.id.fl_other_circle);
        btn_release = (Button) rootView.findViewById(R.id.btn_release);
        btn_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    checkData();
                }
            }
        });
    }

    private void checkData() {
        if (TextUtils.isEmpty(tagId) && TextUtils.isEmpty(otherCircleId)) {
            Toast.makeText(getActivity().getApplicationContext(), "请必须选择一个圈子", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(tagId)) {
            String circleString = tagId + "," + ZYApplication.DefaultCircles.get(0).get(0);
            callback.dataCallBack(circleString, 0, null, null, null, null);
            dismiss();
            return;
        }
        if (!TextUtils.isEmpty(otherCircleId)) {
            String circleString = otherCircleId + "," + ZYApplication.DefaultCircles.get(1).get(0) + "," + ZYApplication.DefaultCircles.get(0).get(0);
            callback.dataCallBack(circleString, 0, null, null, null, null);
            dismiss();
        }
    }

    private View f_body;
    private View bg;
    private BlurColor bc;

    //虚化背景
    private void initBlur() {

        bg = ((ReleaseForumActivity) getActivity()).getV_body();
        try {
            bc = new BlurColor(getActivity(), bg, f_body, 8, 20, BlurColor.BLUR_CUSTOM);
            bc.applyBlurView();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "applyBlur");
        }
    }

    private Map<String, String> circleTagMap;//根据id获取标签名称

    private void initData() {
        tv_cancer_circle_name.setText(MapDataUtils.getCircleValues(ZYApplication.DefaultCircles.get(1).get(0)));
        circleTagMap = (Map<String, String>) Factory.getData(Const.N_DataCircleValues);
        setFl_circle_tag();
        setFl_other_circle();
    }

    private String tagId;
    private int selectedItemId;

    //设置癌症圈的标签
    private void setFl_circle_tag() {
        if (ZYApplication.DefaultCircles == null)
            return;
        tagId = "";
        selectedItemId = -1;
        Map<String, List<String>> map = (Map<String, List<String>>) Factory.getData(Const.N_DataCircleCancer);
        final List<String> temp = map.get(ZYApplication.DefaultCircles.get(1).get(0));
        if (temp != null && temp.size() != 0) {
            fl_circle_tag.removeAllViews();
            for (int i = 0; i < temp.size(); i++) {
                final TextView box = (TextView) LayoutInflater.from(context).inflate(R.layout.item_tv_check, fl_circle_tag, false);
                final String child = temp.get(i);
                final String childName = getChildName(child);
                box.setText(childName);
                final int finalI = i;
                box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!box.isSelected()) {
                            box.setSelected(true);
                            clearTagStatus();
                            clearCircleStatus();
                            selectedItemId = finalI;
                            tagId = child;
                        }
                    }
                });

                fl_circle_tag.addView(box);
            }
        } else {
            fl_circle_tag.removeAllViews();
        }
    }

    private String getChildName(String id) {
        if (TextUtils.isEmpty(id)) return "";
        String temp = circleTagMap.get(id);
        if (TextUtils.isEmpty(temp)) return "";
        return temp;
    }

    private String otherCircleId;
    private int selectedCircleItemId = -1;

    //其他圈子选项
    private void setFl_other_circle() {
        if (ZYApplication.threadCircle == null)
            return;
        otherCircleId = "";
        selectedCircleItemId = -1;
        Set<String> keySet = ZYApplication.threadCircle.keySet();
        List<String> keyList = new ArrayList<>();
        for (String key : keySet
                ) {
            keyList.add(key);
        }
        if (keyList.size() > 0) {
            fl_other_circle.removeAllViews();
            for (int i = 0; i < keyList.size(); i++) {
                final TextView box = (TextView) LayoutInflater.from(context).inflate(R.layout.item_tv_check, fl_other_circle, false);
                final String child = keyList.get(i);
                final String childName = ZYApplication.threadCircle.get(child);
                box.setText(childName);
                final int finalI = i;
                box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!box.isSelected()) {
                            box.setSelected(true);
                            clearCircleStatus();//清除之前的圈子选择状态
                            clearTagStatus();//清除之前的标签选择状态
                            selectedCircleItemId = finalI;
                            otherCircleId = child;
                        }
                    }
                });

                fl_other_circle.addView(box);
            }
        } else {
            fl_other_circle.removeAllViews();
        }
    }

    //清除之前的标签选择状态
    private void clearTagStatus() {
        if (selectedItemId != -1)
            fl_circle_tag.getChildAt(selectedItemId).setSelected(false);
        tagId = "";
        selectedItemId = -1;
    }

    //清除之前的圈子选择状态
    private void clearCircleStatus() {
        if (selectedCircleItemId != -1)
            fl_other_circle.getChildAt(selectedCircleItemId).setSelected(false);
        otherCircleId = "";
        selectedCircleItemId = -1;
    }

    @Override
    public void onPause() {
        dismiss();
        super.onPause();
    }
}
