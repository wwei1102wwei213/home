package com.zeyuan.kyq.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2017/5/24.
 * 分享弹窗
 */

public class ForumSharePopupWindow extends PopupWindow implements View.OnClickListener {

    private OnItemClickListener onItemClickListener;
//    private View ll_bottom_View;

    public ForumSharePopupWindow(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.popupwindow_share, null);
//        ll_bottom_View = rootView.findViewById(R.id.ll_bottom_View);
        TextView tv_pop_share = (TextView) rootView.findViewById(R.id.tv_pop_share);
        TextView tv_pop_edit = (TextView) rootView.findViewById(R.id.tv_pop_edit);
        TextView tv_pop_delete = (TextView) rootView.findViewById(R.id.tv_pop_delete);
        TextView tv_pop_cancel = (TextView) rootView.findViewById(R.id.tv_pop_cancel);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(rootView);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setClippingEnabled(false);
        this.setAnimationStyle(R.style.popwindow_anim_style);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        final ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        tv_pop_share.setOnClickListener(this);
        tv_pop_edit.setOnClickListener(this);
        tv_pop_delete.setOnClickListener(this);
        tv_pop_cancel.setOnClickListener(this);
        rootView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pop_share:
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(0);
                    dismiss();
                }
                break;
            case R.id.tv_pop_edit:
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(1);
                    dismiss();
                }
                break;
            case R.id.tv_pop_delete:
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(2);
                    dismiss();
                }
                break;
            case R.id.tv_pop_cancel:
                dismiss();
                break;
            case R.id.ll_bg:
                dismiss();
                break;


        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void showPopupWindow(Activity activity) {
        if (!this.isShowing()) {
            this.showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


}
