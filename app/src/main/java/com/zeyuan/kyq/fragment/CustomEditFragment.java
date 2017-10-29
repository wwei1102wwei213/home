package com.zeyuan.kyq.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.EditBackInterface;
import com.zeyuan.kyq.utils.ExceptionUtils;

/**
 * Created by Administrator on 2016/6/4.
 *
 * 输入框窗口
 *
 * @author wwei
 */
public class CustomEditFragment extends DialogFragment implements View.OnClickListener{

    public static final String type = "CustomEditFragment";
    public static final String EDIT_TEXT = "edit_text";
    public static final String EDIT_CALLBACK = "edit_callback";
    private String edText;
    private EditBackInterface callback;

    /***
     *
     * 获得自定义对象
     *
     * @param callback 回调页面
     * @param text 编辑文本
     * @param tag 标识
     * @param flag 标识
     * @return
     */
    public static CustomEditFragment getInstance(EditBackInterface callback,String text,String tag,int flag){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EDIT_CALLBACK,callback);
        bundle.putString(EDIT_TEXT,text);

        CustomEditFragment instance = new CustomEditFragment();
        instance.setArguments(bundle);
        return instance;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle==null) return;
        callback = (EditBackInterface)bundle.getSerializable(EDIT_CALLBACK);
        edText = bundle.getString(EDIT_TEXT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_custom_edittext, null);
        initview(view);
        dialog.setContentView(view);
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

    private EditText et;
    private void initview(View view){
        et = (EditText)view.findViewById(R.id.et_dialog_custom_edit);
        Button yes = (Button)view.findViewById(R.id.btn_yes_dialog);
        Button no = (Button)view.findViewById(R.id.btn_no_dialog);
        Button clear = (Button)view.findViewById(R.id.btn_clear_edit);
        et.setSelected(true);
        if(!TextUtils.isEmpty(edText)){
            et.setText(edText);
        }
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        clear.setOnClickListener(this);
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        /*InputMethodManager imm =  (InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm!=null){
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }*/
    }

    private String getFinishText(){
        try {
            String temp = et.getText().toString();
            if(TextUtils.isEmpty(temp)){
                return "";
            }else {
                return temp.trim();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"编辑框输入回调异常");
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_yes_dialog:
                callback.editCallBack(getFinishText());
                dismiss();
                break;
            case R.id.btn_no_dialog:
                dismiss();
                break;
            case R.id.btn_clear_edit:
                et.setText("");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager inManager = (InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        },0);
    }

    @Override
    public void onPause() {
        dismiss();
        super.onPause();
    }

    /*@Override
    public void dismiss() {
        InputMethodManager imm =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm!=null){
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
        super.dismiss();
    }*/
}
