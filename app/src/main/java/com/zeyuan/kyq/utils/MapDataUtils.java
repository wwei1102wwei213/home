package com.zeyuan.kyq.utils;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.umeng.message.entity.UMessage;
import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.db.DBHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/7.
 *
 *
 *
 * @author zeyuan
 */
public class MapDataUtils {
    public static final String BXY_ID = "4205";//靶向药id
    public static final String HLY_ID = "4206";//化疗药id

    /**
     * 后台给的是联合用药的id 和 药物的id组合在一起
     *
     * @param
     * @return
     */
    public static String getAllStepName(String id) {
        if (TextUtils.isEmpty(id)) {
            return "";
        }

        SparseArray<ConfStepEntity>  array = (SparseArray<ConfStepEntity>)Factory.getData(Const.N_DataDrugNames);
        if(array == null) return "";
        String temp = null;
        try {
            int t = Integer.valueOf(id);
            temp = array.get(t).getStepName();
        }catch (Exception e){

        }
        if (!TextUtils.isEmpty(temp)) {
            return temp;
        }
        return "";
    }


    /**
     * 是否为靶向药
     *
     * @param id 药物和这联合用药的id
     * @return true 为靶向药 false不是靶向药
     */
    public static boolean isBXY(String id) {
        if (BXY_ID.equals(getAllCureconfID(id))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据症状的id 得到对应的症状
     *
     * @param id
     * @return
     */
    public static String getPerform(String id) {
        if (TextUtils.isEmpty(id)) {
            return "";
        }
        Map<String, String> performValues = (Map<String, String>)Factory.getData(Const.N_DataPerformValues);
        String temp = performValues.get(id);
        if(!TextUtils.isEmpty(temp)){
            return temp;
        }
        return "";
    }

    /**
     * 根据症状的id 得到对应的症状
     *
     * @param
     * @return
     */
    public static String getPerformForString(String str){
        if (TextUtils.isEmpty(str)) return "";
        Map<String, String> performValues = (Map<String, String>)Factory.getData(Const.N_DataPerformValues);
        return getMapValuesStr(performValues,str);
    }

    /**
     * 根据基因的id 得到对应的症状
     *
     * @param
     * @return
     */
    public static String getGeneForString(String str){
        if (TextUtils.isEmpty(str)) return "";
        Map<String,String> map = (Map<String,String>) Factory.getData(Const.N_DataGeneValues);
        return getMapValuesStr(map,str);
    }

    /**
     * 根据转移部位的id 得到对应的症状
     *
     * @param
     * @return
     */
    public static String getTranslateForString(String str){
        if (TextUtils.isEmpty(str)) return "";
        Map<String,String> map = (Map<String,String>) Factory.getData(Const.N_DataTransferPos);
        return getMapValuesStr(map,str);
    }

    /*public static String getCityValue(Context context,int id) {
        try {
            SparseArray<String> citys = SyncConfigUtils.parseCitys(UserinfoData.getSyncConfData(context));
            return citys.get(id);
        }catch (JSONException e){
            ExceptionUtils.ExceptionSend(e,"MapDataUtils:城市索引类解析异常");
        }
        return null;
    }*/

    /**
     * 根据药物id 获取 cureconfid
     *
     * @param id
     * @return
     */
    public static String getAllCureconfID(String id) {
        if(TextUtils.isEmpty(id)) return "";
        SparseArray<ConfStepEntity>  array = (SparseArray<ConfStepEntity>)Factory.getData(Const.N_DataDrugNames);
        if(array == null) return "";
        String temp = null;
        try {
            int t = Integer.valueOf(id);
            temp = array.get(t).getCureConfID();
        }catch (Exception e){

        }
        if (!TextUtils.isEmpty(temp)) {
            return temp;
        }
        return "";
    }

    /**
     * 获取阶段类型的名称
     *
     * @param id
     * @return
     */
    public static String getCureValues(String id) {
        Map<String, String> cureValues = (Map<String, String>)Factory.getData(Const.N_DataCureConf);
        if(cureValues==null||cureValues.size()==0){
            return "";
        }else {
            String temp = cureValues.get(id);
            if(TextUtils.isEmpty(temp)){
                return "";
            }else {
                return temp;
            }
        }
    }

    /**
     * 得到圈子的值
     *
     * @param id
     * @return
     */
    public static String getCircleValues(String id) {
        if(TextUtils.isEmpty(id)) return "";
        Map<String, String> circleValues = (Map<String, String>)Factory.getData(Const.N_DataCircleValues);
        if(circleValues == null) return "其他圈子";
        String temp = circleValues.get(id);
        if(TextUtils.isEmpty(temp)){
            return "其他圈子";
        }else{
            return temp;
        }
    }


    public static LinkedHashMap getBodyPos() {
        return (LinkedHashMap<String, List<String>>)Factory.getData(Const.N_DataBodyPos);
    }

    public static String getDigitValues(String id) {
        if (TextUtils.isEmpty(id)) return "";
        Map<String, String> DigitValues = (Map<String, String>)Factory.getData(Const.N_DataDigitValues);
        String temp = DigitValues.get(id);
        if(TextUtils.isEmpty(temp)){
            temp = "";
        }
        return temp;
    }

    public static String getPerfromValues(String ids) {
        Map<String, String> performValues = (Map<String, String>)Factory.getData(Const.N_DataPerformValues);
        return getMapValuesStr(performValues, ids);
    }

    /**
     * 把由许多id组成的由逗号分开的项， 转换成显示的
     * 如 556,557 转换成 易瑞沙，特罗凯
     *
     * @param map    id 对用 的values
     * @param idsStr 由逗号分隔id 的字符串
     * @return 显示由逗号隔开的字符串
     */
    public static String getMapValuesStr(Map<String, String> map, String idsStr) {
        if (TextUtils.isEmpty(idsStr)||map==null||map.size()==0) {
            return "";
        }
        String[] strings = idsStr.split(",");
        StringBuilder sb = new StringBuilder();
        try {
            if (strings.length > 0) {
                for (String str : strings) {
                    if (sb.length() == 0) {
                        sb.append(map.get(str));
                    } else {
                        sb.append("," + map.get(str));
                    }
                }
            }
        }catch (Exception e){

        }
        return sb.toString();
    }

    /**
     * 把list转换成由逗号隔开的string
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (String str : list) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }


    public static String getTransPosValues(String ids) {
        Map<String, String> transferpos = (Map<String, String>)Factory.getData(Const.N_DataTransferPos);
        return getMapValuesStr(transferpos, ids);
    }

    public static String getCancerValues(String ids) {
        if (TextUtils.isEmpty(ids)) return "";
        String result = "";
        try {
            Map<String, String> cancerValues = ( Map<String, String>)Factory.getData(Const.N_DataCancerValues);
            result = cancerValues.get(ids);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getCancerValues");
        }
        return result;
    }

    public static List<ForumListBean.ForumnumEntity> getZhiDingList(List<ForumListBean.ForumnumEntity> list){
        if(list==null||list.size()==0) return new ArrayList<>();
        List<ForumListBean.ForumnumEntity> data = new ArrayList<>();
        for(ForumListBean.ForumnumEntity tempEntity: list){
            String type = tempEntity.getPosttype();
            if(!TextUtils.isEmpty(type)){
                int n = Integer.valueOf(type);
                if(n!=0&&(n&Const.FLAG_FORUM_TOP)==Const.FLAG_FORUM_TOP){
                    data.add(tempEntity);
                }
            }
        }
        if(data.size()>0){
            list.removeAll(data);
            data.addAll(list);
            return data;
        }else{
            return list;
        }
    }

    public static List<ForumListBean.ForumnumEntity> getZhiDingList(List<ForumListBean.ForumnumEntity> list
            ,List<ForumListBean.ForumnumEntity> old){
        if(list==null||list.size()==0) return old;
        List<ForumListBean.ForumnumEntity> data = new ArrayList<>();
        for(ForumListBean.ForumnumEntity tempEntity: list){
            String type = tempEntity.getPosttype();
            if(!TextUtils.isEmpty(type)){
                int n = Integer.valueOf(type);
                if(n!=0&&(n&Const.FLAG_FORUM_TOP)==Const.FLAG_FORUM_TOP){
                    data.add(tempEntity);
                }
            }
        }
        if(data.size()>0){
            list.removeAll(data);
            data.addAll(old);
            data.addAll(list);
            return data;
        }else{
            old.addAll(list);
            return old;
        }
    }

    //
    public static String getParamsFoot(String[] shareParams){
        String params = "";
        String temp = "";
        if(shareParams==null||shareParams.length==0) return null;
        if(shareParams.length%2!=0) return null;
        for(int i = 0 ; i<shareParams.length ; i++){

            switch (shareParams[i]){
                case Contants.InfoID:
                    temp = "&&" + "in=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.StepID:
                    temp = "&&" + "st=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.CancerID:
                    temp = "&&" + "ca=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.PeriodID:
                    temp = "&&" + "pe=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.PerformID:
                    temp = "&&" + "pf=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.SelfSelectNum:
                    temp = "&&" + "ssn=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.GeneID:
                    temp = "&&" + "ge=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.CureConfID:
                    temp = "&&" + "cc=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.QuestionID + "0":
                    temp = "&&" + "q0=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.AnswerID + "0":
                    temp = "&&" + "a0=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.BodyStatusID:
                    temp = "&&" + "bs=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.Type:
                    temp = "&&" + "ty=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.ChemistryID:
                    temp = "&&" + "ch=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.TargetID:
                    temp = "&&" + "ta=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.CommPolicyID:
                    temp = "&&" + "cp=" + shareParams[i+1];
                    params += temp;
                    break;
                case Const.ISNEWVERSION:
                    temp = "&&" + "inv=" + shareParams[i+1];
                    params += temp;
                    break;
                case "SpID":
                    temp = "&&" + "sp=" + shareParams[i+1];
                    params += temp;
                    break;
            }
        }
        if(params.length()>=2){
            params = params.substring(2,params.length());
            Log.i("ZYS", "分享参数：" + params);
        }else{
            return null;
        }
        return params;
    }

    /***
     *
     * 获取城市名字
     *
     * @param id
     * @return
     */
    public static String getCityName(String id){
        if(TextUtils.isEmpty(id)){
            return "";
        }
        SparseArray<String> citys = (SparseArray<String>)Factory.getData(Const.N_DataAllCity);
        if(citys==null||citys.size()==0){
            return "";
        }else {
            String temp = citys.get(Integer.valueOf(id));
            if(TextUtils.isEmpty(temp)){
                return "";
            }else {
                return temp;
            }
        }
    }

    public static Map<String,String> getOtherCircleNames(int type){
        Map<String,String> map = new HashMap<>();
        if(type==1){
            map.put("7003","全部");
            map.put("10377","肺癌");
            map.put("10378","肝癌");
            map.put("10379","肠癌");
            map.put("10380","乳腺癌");
            map.put("10381","胃癌");
            map.put("10382","食管癌");
            map.put("10383","其他癌种");
        }else if(type==2){
            map.put("7007","全部");
            map.put("10384","保健品");
            map.put("10385","常用药品");
            map.put("10386","医疗器械");
            map.put("10387","其他");
        }
        return map;
    }

    public static int getPushMsgNum() {
        List<UMessage> list = new ArrayList<>();
        List<PushNewEntity> data = DBHelper.getInstance().queryPush(ZYApplication.application);
        if(data!=null&&data.size()!=0){
            try {
                Map<String,String> map;
                for(PushNewEntity entity:data){
                    UMessage msg = new UMessage(new JSONObject(entity.getMsg()));
                    map = msg.extra;
                    if(map!=null&&map.get("jump")!=null){
                        int jump = Integer.valueOf(map.get("jump"));
                        if(jump==0) list.add(msg);
                    }
                }
                return list.size();
            }catch (Exception e){

            }
        }
        return 0;
    }

}
