package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2016/8/10.
 *
 *
 *
 * @author wwei
 */
public class ExitDialog extends DialogFragment implements View.OnClickListener{

    private ExitCallback callback;

    public void setCallback(ExitCallback callback){
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_exit, null);
        initView(rootView);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.mystyle);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(rootView);
        return dialog;
    }

    private void initView(View v){
        v.findViewById(R.id.tv_exit).setOnClickListener(this);
        v.findViewById(R.id.tv_exit_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_exit:
                callback.onExitClick();
                dismiss();
                break;
            case R.id.tv_exit_cancel:
                dismiss();
                break;
        }
    }

    public interface ExitCallback{
        void onExitClick();
    }


}
