package com.zeyuan.kyq.utils;

import android.content.Context;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/15.
 *
 *
 */
public class InitZYUtils {

    public static void initFeedback(Context context){
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserName", UserinfoData.getInfoname(context));
                jsonObject.put("UserID", UserinfoData.getInfoID(context));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FeedbackAPI.setAppExtInfo(jsonObject);
        }catch (Exception e){

        }
    }

}
