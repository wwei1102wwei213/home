package com.zeyuan.kyq.utils;

import android.text.TextUtils;

import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.Entity.QuotaItemChildEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/24.
 *
 * 搜索帮助类
 *
 * @author wwei
 */
public class SearchUtils {

    /***
     *
     * 根据标识获取搜索记录
     *
     * @param record
     * @param flag
     * @return
     */
    public static Object getSearchRecord(String record,String flag){
        Object object = null;
        if(Const.RECORD_DRUG.equals(flag)){
            List<ConfStepEntity> list = new ArrayList<>();
            ConfStepEntity entity;
            if(record.contains(Const.BREAK_BIG)){
                String[] args = record.split(Const.BREAK_BIG);
                for(int i=0;i<args.length;i++){
                    String[] argsChild = args[i].split(Const.BREAK_SMALL);
                    entity = new ConfStepEntity();
                    entity.setStepID(argsChild[0]);
                    entity.setStepName(argsChild[1]);
                    list.add(entity);
                }
            }else{
                String[] argsChild = record.split(Const.BREAK_SMALL);
                entity = new ConfStepEntity();
                entity.setStepID(argsChild[0]);
                entity.setStepName(argsChild[1]);
                list.add(entity);
            }
            object = list;
        }else if(Const.RECORD_QUOTA.equals(flag)){
            List<QuotaItemChildEntity> list = new ArrayList<>();
            QuotaItemChildEntity entity;
            if(record.contains(Const.BREAK_BIG)){
                String[] args = record.split(Const.BREAK_BIG);
                for(int i=0;i<args.length;i++){
                    String[] argsChild = args[i].split(Const.BREAK_SMALL);
                    entity = new QuotaItemChildEntity();
                    entity.setSpID(argsChild[0]);
                    entity.setSpName(argsChild[1]);
                    list.add(entity);
                }
            }else{
                String[] argsChild = record.split(Const.BREAK_SMALL);
                entity = new QuotaItemChildEntity();
                entity.setSpID(argsChild[0]);
                entity.setSpName(argsChild[1]);
                list.add(entity);
            }
            object = list;
        }
        return object;
    }

    /***
     *
     * 获取保存字符串
     *
     * @param record 当前字符串
     * @param recordStr 记录字符串
     * @return
     */
    public static String getSaveString(String record,String recordStr){
        String temp = "";
        if(TextUtils.isEmpty(record)){
            temp = recordStr;
        }else {
            if(!record.contains(Const.BREAK_BIG)){
                temp = recordStr + Const.BREAK_BIG + record;
            }else{
                String[] args = record.split(Const.BREAK_BIG);
                if(args.length>4){
                    for(int i = 0 ;i<4;i++){
                        temp += (Const.BREAK_BIG + args[i]);
                    }
                    temp = recordStr + temp;
                }else{
                    temp = recordStr + Const.BREAK_BIG + record;
                }
            }
        }
        return temp;
    }

}
