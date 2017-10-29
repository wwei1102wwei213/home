package com.zeyuan.kyq.utils;

import android.content.Context;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2016/5/21.
 *
 * 配置数据处理类
 *
 * @author wwei
 */
public class ConfUtils {

    public static int getResIDForColorRecordBg(int drugTypeId){
        if(drugTypeId==4205){
            return R.drawable.drug_type_color_red;
        }else if(drugTypeId==4206||drugTypeId==4207||drugTypeId==4208){
            return R.drawable.drug_type_color_yellow;
        }else if(drugTypeId==4214){
            return R.drawable.drug_type_color_purple;
        }else {
            return R.drawable.drug_type_color_navy;
        }
    }

    public static int getResIDForColorRecordText(Context context,int drugTypeId){
        if(drugTypeId==4205){
            return context.getResources().getColor(R.color.color_record_4205);
        }else if(drugTypeId==4206||drugTypeId==4207||drugTypeId==4208){
            return context.getResources().getColor(R.color.color_record_4207);
        }else if(drugTypeId==4214){
            return context.getResources().getColor(R.color.color_record_4214);
        }else {
            return context.getResources().getColor(R.color.color_record_other);
        }
    }

}
