package com.zeyuan.kyq.utils;

import android.hardware.Camera;
import android.text.TextUtils;

/**
 * Created by Administrator on 2016/11/4.
 *
 *
 * @author wwei
 */
public class OtherUtils {

    public static boolean isEmpty(String str){
        if (TextUtils.isEmpty(str)) return true;
        if (Const.RESULT.equals(str)) return true;
        return false;
    }

    /**
     *  返回true 表示可以使用  返回false表示不可以使用
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

}
