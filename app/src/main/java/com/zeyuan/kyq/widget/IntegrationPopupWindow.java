package com.zeyuan.kyq.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;

/**
 * Created by Administrator on 2017/5/16.
 * 积分弹窗
 */

public class IntegrationPopupWindow extends PopupWindow {
    private AnimationDrawable animationDrawable;
    private Handler handler;

    public IntegrationPopupWindow(Context context, String integration) {
        handler = new Handler();
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_integration, null);
        TextView tv_integration_info = (TextView) rootView.findViewById(R.id.tv_integration_info);
        tv_integration_info.setText(integration);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.iv_integration);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(rootView);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
    }

    //延迟显示弹窗
    public void delayShowPopupWindow(long delay, final Activity activity) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPopupWindow(activity);
            }
        }, delay);
    }

    public void showPopupWindow(Activity activity) {
        try {
            if (!this.isShowing()) {
                this.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                animationDrawable.start();
                delayDismiss();
            } else {
                this.dismiss();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showPopupWindow");
        }
    }

    //弹窗显示5秒自动关闭
    private void delayDismiss() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    animationDrawable.stop();
                    dismiss();
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "delayDismiss");
                }

            }
        }, 5000);
    }

}
