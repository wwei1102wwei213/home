package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2017/7/3.
 * 升级提醒框
 */

public class UpdateDialog extends Dialog {
    public UpdateDialog(@NonNull Context context) {
        super(context);
    }

    public UpdateDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String update_manager_avatar;
        private String update_manager_name;
        private String update_manager_position;
        private String content;
        private boolean cancelAble = true;
        private String positiveButtonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCancelAble(boolean cancelAble) {
            this.cancelAble = cancelAble;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }
        //推送更新的人的头像
        public Builder setAvatar(String avatar) {
            this.update_manager_avatar = avatar;
            return this;
        }

        //推送更新的人名字
        public Builder setUpdateManagerName(String name) {
            this.update_manager_name = name;
            return this;
        }
        //推送更新的人的职务
        public Builder setUpdateManagerPosition(String position) {
            this.update_manager_position = position;
            return this;
        }


        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public UpdateDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final UpdateDialog updateDialog = new UpdateDialog(context, R.style.zydialog);
            View layout = inflater.inflate(R.layout.dialog_update, null, false);
//            updateDialog.addContentView(layout, new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            updateDialog.setContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tv_close_dialog = (TextView) layout.findViewById(R.id.tv_close_dialog);
            if (cancelAble) {
                updateDialog.setCancelable(true);
                tv_close_dialog.setVisibility(View.VISIBLE);
                tv_close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cancelAble)
                            updateDialog.dismiss();
                    }
                });
            } else {
                updateDialog.setCancelable(false);
                tv_close_dialog.setVisibility(View.GONE);
            }

            ImageView avatar = (ImageView) layout.findViewById(R.id.civ_update_manager_avatar);
            if (!TextUtils.isEmpty(update_manager_avatar)) {
                Glide.with(context).load(update_manager_avatar).error(R.mipmap.default_avatar).into(avatar);
            } else {
                avatar.setImageResource(R.mipmap.ic_avatar_pm);
            }
            TextView tv_update_manager_name = (TextView) layout.findViewById(R.id.tv_update_manager_name);
            if (!TextUtils.isEmpty(update_manager_name)) {
                tv_update_manager_name.setText(update_manager_name);
            }
            TextView tv_update_manager_position = (TextView) layout.findViewById(R.id.tv_update_manager_position);
            if (!TextUtils.isEmpty(update_manager_position)) {
                tv_update_manager_position.setText(update_manager_position);
            }
            TextView tv_update_content = (TextView) layout.findViewById(R.id.tv_update_content);
            if (!TextUtils.isEmpty(content)) {
                tv_update_content.setText(content);
            } else {
                tv_update_content.setText("发现新版本！");
            }
            final TextView tv_update = (TextView) layout.findViewById(R.id.tv_update);
            if (!TextUtils.isEmpty(positiveButtonText)) {
                tv_update.setText(positiveButtonText);
            } else {
                tv_update.setText("立即更新");
            }
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (positiveButtonClickListener != null) {
                        positiveButtonClickListener.onClick(updateDialog, BUTTON_POSITIVE);
                    }
                }
            });
            //updateDialog.setContentView(layout);
            return updateDialog;
        }
    }
}
