package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SignInDialog extends Dialog{

    public SignInDialog(Context context) {
        super(context);
    }

    public SignInDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        //        private String negativeButtonText;
        private boolean cancelAble = true;
        private boolean miss = false;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private boolean html = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setHtmlMessage(String message){
            this.message = message;
            this.html = true;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder disMissLine(boolean miss){
            this.miss = miss;
            return this;
        }

        public Builder setCancelAble(boolean cancelAble){
            this.cancelAble = cancelAble;
            return this;
        }


        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
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

        public SignInDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final SignInDialog dialog = new SignInDialog(context, R.style.zydialog);
            View layout = inflater.inflate(R.layout.dialog_sign_in, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if (!TextUtils.isEmpty(title))
                    ((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
            }
            if (positiveButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                positiveButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }

            // set the content message
            if (message != null) {
                if (html){
                    ((TextView) layout.findViewById(R.id.message)).setText(Html.fromHtml(message));
                    html = false;
                }else {
                    ((TextView) layout.findViewById(R.id.message)).setText(message);
                }
            }

            if(cancelAble){
                dialog.setCancelable(true);
            }else{
                dialog.setCancelable(false);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }

}
