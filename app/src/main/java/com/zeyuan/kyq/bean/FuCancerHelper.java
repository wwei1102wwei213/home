package com.zeyuan.kyq.bean;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FuCancerHelper implements Serializable{

    public TextView fu_length;
    public TextView fu_width;
    public EditText fu_name;

    public FuCancerHelper(TextView fu_length, TextView fu_width, EditText fu_name) {
        this.fu_length = fu_length;
        this.fu_width = fu_width;
        this.fu_name = fu_name;
    }

    public int getFu_length() {
        String str_fu_length = fu_length.getText().toString().trim();
        if (TextUtils.isEmpty(str_fu_length)) {
            return 0;
        }
        return Integer.valueOf(str_fu_length);
    }

    public int getFu_width() {
        String str_fu_length = fu_width.getText().toString().trim();
        if (TextUtils.isEmpty(str_fu_length)) {
            return 0;
        }
        return Integer.valueOf(str_fu_length);
    }

    public String getFu_name() {
        String temp = fu_name.getText().toString().trim();
        if(!TextUtils.isEmpty(temp)){
            temp = temp.replace("(点击可修改)","");
        }
        return temp;
    }

    public SlaverCancer getCancer() {
        SlaverCancer cancer = new SlaverCancer();
        int fu_length = getFu_length();
        int fu_width = getFu_width();
        String fu_name = getFu_name();
        if (0 == fu_length || 0 == fu_width ||TextUtils.isEmpty(fu_name)) {
            return null;
        }
        cancer.setSlaveLen(fu_length);
        cancer.setSlaveWidth(fu_width);
        cancer.setSlaveName(fu_name);
        return cancer;
    }


}
