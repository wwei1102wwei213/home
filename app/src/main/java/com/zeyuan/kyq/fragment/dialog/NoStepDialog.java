package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.DialogCallBack;

/**
 * Created by Administrator on 2016/7/5.
 *
 *
 *
 * @author wwei
 */
public class NoStepDialog extends DialogFragment implements View.OnClickListener{

    public static final String type = "NoStepDialog";
    public static final int GO_ON = 1;
    public static final int NO_STEP = 2;
    private DialogCallBack callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_no_step_choose, null);
        initView(rootView);
        dialog.setContentView(rootView);
        return dialog;
    }

    private void initView(View v){
        (v.findViewById(R.id.btn_info_step_hint_finish)).setOnClickListener(this);
        (v.findViewById(R.id.btn_info_hint_step_no)).setOnClickListener(this);
    }

    public void setDialogCallBack(DialogCallBack callback){
        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_info_step_hint_finish:
                callback.getDataCallBack(null,GO_ON);
                dismiss();
                break;
            case R.id.btn_info_hint_step_no:
                callback.getDataCallBack(null, NO_STEP);
                dismiss();
                break;
        }
    }

    /*@Override
    public void onPause() {
        super.onPause();
        d
    }*/
}
