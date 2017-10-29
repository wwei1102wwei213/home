package com.zeyuan.kyq.fragment.dialog;

import android.app.DialogFragment;

/**
 * User: zeyuan
 * Date: 2015-11-12
 * Time: 10:44
 * FIXME
 * 传递dialog的数据
 */


public interface DialogFragmentListener {
    /**
     * @param dialog 表明是哪个dialog
     * @param data 数据
     * @param position 数据的标记
     */
    void getDataFromDialog(DialogFragment dialog ,String data,int position);

}
