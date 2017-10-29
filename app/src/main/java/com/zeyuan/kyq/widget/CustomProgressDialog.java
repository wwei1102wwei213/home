package com.zeyuan.kyq.widget;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2015/11/17.
 */
public class CustomProgressDialog extends Dialog {
    private Context context = null;
    private static CustomProgressDialog customProgressDialog = null;

    private ProgressDialog mProgressDialog;
    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static CustomProgressDialog createCustomDialog(Context context) {
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.custom_progress_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return customProgressDialog;
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog == null) {
            return;
        }
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();


    }

    public CustomProgressDialog setMessage(String strMessage){
        TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
        if (tvMsg != null){
            tvMsg.setText(strMessage);
        }
        return customProgressDialog;
    }
}
