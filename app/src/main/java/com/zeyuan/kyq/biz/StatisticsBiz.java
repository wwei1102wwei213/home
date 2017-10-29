package com.zeyuan.kyq.biz;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/17.
 *
 *
 *
 * @author wwei
 */
public class StatisticsBiz {

    public static void sendToUMeng(Context context,String eventId,String eventFlag ,String extras){
        try {
            Map<String,String> map = new HashMap<>();
            if(!TextUtils.isEmpty(eventFlag)){
                String userId = UserinfoData.getInfoID(context);
                String userName = UserinfoData.getInfoname(context);
                if(TextUtils.isEmpty(userId)){
                    userId = "未知用户";
                }
                if(TextUtils.isEmpty(userName)){
                    userName = "未知姓名";
                }
                if(TextUtils.isEmpty(extras)){
                    map.put(eventFlag, "UserID:" + userId + "; UserName:" + userName
                            + "; CreateTime:" + DataUtils.getDateForTimeMills());
                }else {
                    map.put(eventFlag, "UserID:" + userId + "; UserName:" + userName +"; Extras:" + extras
                            + "; CreateTime:" + DataUtils.getDateForTimeMills());
                }
            }
            LogCustom.i(Const.TAG.ZY_UMENG, "eventId:" + eventId);
            MobclickAgent.onEvent(context, eventId, map);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"友盟统计信息出错");
        }
    }

}
