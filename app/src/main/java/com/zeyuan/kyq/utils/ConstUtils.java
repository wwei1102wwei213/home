package com.zeyuan.kyq.utils;

import android.text.TextUtils;
import android.util.SparseArray;

import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.Entity.ChartEntity;
import com.zeyuan.kyq.Entity.ChartFloatEntity;
import com.zeyuan.kyq.Entity.MedicalRecordBean;
import com.zeyuan.kyq.Entity.RecordItemEntity;
import com.zeyuan.kyq.Entity.StepUserEntity;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.http.bean.UserStepBean;
import com.zeyuan.kyq.http.bean.UserStepComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 *
 * 发请求参数处理帮助类
 *
 * @author wwei
 */
public class ConstUtils {

    /**
     * 复制记录实体类
     *
     * @param entity
     * @return
     */
    public static RecordItemEntity copyRecordItemEntity(RecordItemEntity entity){
        RecordItemEntity temp = new RecordItemEntity();
        temp.setID(entity.getID());
        temp.setType(entity.getType());
        temp.setIs_show(entity.getIs_show());
        temp.setCancerMark(entity.getCancerMark());
        temp.setDoctor(entity.getDoctor());
        temp.setHospital(entity.getHospital());
        temp.setGeneID(entity.getGeneID());
        temp.setPerform(entity.getPerform());
        temp.setPic(entity.getPic());
        temp.setRecordTime(entity.getRecordTime());
        temp.setRemark(entity.getRemark());
        temp.setTransferBody(entity.getTransferBody());
        temp.setTumourInfo(entity.getTumourInfo());
        return temp;
    }

    /**
     * 将用","隔开的字符串转成List集合
     *
     * @param str
     * @return
     */
    public static ArrayList<String> getArrayListForString(String str){
        ArrayList<String> temp = new ArrayList<>();
        if (TextUtils.isEmpty(str)) return temp;
        String[] args = str.split(",");
        if (args.length>0){
            for (String s:args){
                temp.add(s);
            }
        }
        return temp;
    }

    /**
     * 将用","隔开的字符串转成List集合
     *
     * @param str
     * @return
     */
    public static ArrayList<String> getArrayListForStringExpZero(String str){
        ArrayList<String> temp = new ArrayList<>();
        if (OtherUtils.isEmpty(str)) return temp;
        String[] args = str.split(",");
        if (args.length>0){
            for (String s:args){
                temp.add(s);
            }
        }
        return temp;
    }

    /**
     *
     * 病历数据处理成列表数据，并排序
     *
     * @param bean
     * @return
     */
    public static List<StepUserEntity> getListForMedicalRecord(MedicalRecordBean bean){
        List<StepUserEntity> list = new ArrayList<>();
        if (bean==null) return list;
        List<StepUserEntity> temp1 = bean.getStepUser();
        if (temp1!=null&&temp1.size()>0){
            for (StepUserEntity entity:temp1){
                list.add(entity);
            }
        }
        StepUserEntity mEntity;
        //肿瘤大小数据
        List<RecordItemEntity> temp2 = bean.getTumourInfoData();
        if (temp2!=null&&temp2.size()>0){
            for (RecordItemEntity entity:temp2){
                mEntity = new StepUserEntity();
                mEntity.setType(Const.RECORD_TYPE_14);
                mEntity.setRI(entity);
                list.add(mEntity);
            }
        }
        //肿瘤指标数据
        List<RecordItemEntity> temp3 = bean.getCancerMarkData();
        if (temp3!=null&&temp3.size()>0){
            for (RecordItemEntity entity:temp3){
                mEntity = new StepUserEntity();
                mEntity.setType(Const.RECORD_TYPE_15);
                mEntity.setRI(entity);
                list.add(mEntity);
            }
        }
        //症状数据
        List<RecordItemEntity> temp4 = bean.getStepDetailPerform();
        if (temp4!=null&&temp4.size()>0){
            for (RecordItemEntity entity:temp4){
                mEntity = new StepUserEntity();
                mEntity.setType(Const.RECORD_TYPE_13);
                mEntity.setRI(entity);
                list.add(mEntity);
            }
        }
        //基因数据
        List<RecordItemEntity> temp5 = bean.getTransferGene();
        if (temp5!=null&&temp5.size()>0){
            for (RecordItemEntity entity:temp5){
                mEntity = new StepUserEntity();
                mEntity.setType(Const.RECORD_TYPE_11);
                mEntity.setRI(entity);
                list.add(mEntity);
            }
        }
        //转移数据
        List<RecordItemEntity> temp6 = bean.getTransferRecord();
        if (temp6!=null&&temp6.size()>0){
            for (RecordItemEntity entity:temp6){
                mEntity = new StepUserEntity();
                mEntity.setType(Const.RECORD_TYPE_12);
                mEntity.setRI(entity);
                list.add(mEntity);
            }
        }
        //其他数据
        List<RecordItemEntity> temp7 = bean.getPresentationOther();
        if (temp7!=null&&temp7.size()>0){
            for (RecordItemEntity entity:temp7){
                mEntity = new StepUserEntity();
                mEntity.setType(entity.getType());
                mEntity.setRI(entity);
                list.add(mEntity);
            }
        }
        Collections.sort(list, new Comparator<StepUserEntity>() {
            @Override
            public int compare(StepUserEntity s1, StepUserEntity s2) {
                if (s1.getType() == 0 && s2.getType() == 0) {
                    if (s1.getEndTime() == 0 && s2.getEndTime() == 0) {
                        return (int) (s2.getStartTime() - s1.getStartTime());
                    } else if (s1.getEndTime() == 0) {
                        return -1;
                    } else if (s2.getEndTime() == 0) {
                        return 1;
                    } else {
                        if (s1.getEndTime() == s2.getEndTime()) {
                            return (int) (s2.getStartTime() - s1.getStartTime());
                        } else {
                            return (int) (s2.getEndTime() - s1.getEndTime());
                        }
                    }
                } else if (s1.getType() != 0 && s2.getType() == 0) {
                    return (int) (s2.getStartTime() - s1.getRI().getRecordTime());
                } else if (s1.getType() == 0 && s2.getType() != 0) {
                    return (int) (s2.getRI().getRecordTime() - s1.getStartTime());
                } else {
                    return (int) (s2.getRI().getRecordTime() - s1.getRI().getRecordTime());
                }
            }
        });
        return list;
    }


    public static List<StepUserEntity> sort(List<StepUserEntity> list){
        Collections.sort(list, new Comparator<StepUserEntity>() {
            @Override
            public int compare(StepUserEntity s1, StepUserEntity s2) {
                if (s1.getType()==0&&s2.getType()==0){
                    if (s1.getEndTime()==0&&s2.getEndTime()==0){
                        return (int)(s2.getStartTime()-s1.getStartTime());
                    }else if (s1.getEndTime()==0){
                        return -1;
                    }else if (s2.getEndTime()==0){
                        return 1;
                    }else {
                        if (s1.getEndTime()==s2.getEndTime()){
                            return (int)(s2.getStartTime()-s1.getStartTime());
                        }else {
                            return (int)(s2.getEndTime()-s1.getEndTime());
                        }
                    }
                }else if (s1.getType()!=0&&s2.getType()==0){
                    return (int)(s2.getStartTime() - s1.getRI().getRecordTime());
                }else if (s1.getType()==0&&s2.getType()!=0){
                    return (int)(s2.getRI().getRecordTime() - s1.getStartTime());
                }else {
                    return (int)(s2.getRI().getRecordTime() - s1.getRI().getRecordTime());
                }
            }
        });
        return list;
    }





    /**
     * 将癌种大小数据转成按TypeID为键的SparseArray
     *
     * @param list
     * @param InfoID
     * @return
     */
    public static SparseArray<List<CancerSizeItemEntity>> getArrayForCancerSizeList(
            List<CancerSizeItemEntity> list,int InfoID){

        SparseArray<List<CancerSizeItemEntity>> array = new SparseArray<>();
        if (list==null||list.size()==0) return array;
        List<CancerSizeItemEntity> temp;
        for (CancerSizeItemEntity entity:list){
            entity.setInfoID(InfoID);
            entity.setPatientID(InfoID);
            int TypeID = entity.getTypeID();
            temp = array.get(TypeID);
            if (temp==null) temp = new ArrayList<>();
            temp.add(entity);
            array.put(TypeID,temp);
        }
        return array;
    }

    /**
     * 将癌种大小数据转成按TypeID为键的SparseArray
     *
     * @param list
     * @return
     */
    public static SparseArray<List<CancerSizeItemEntity>> getArrayForChartList(
            List<CancerSizeItemEntity> list){
        SparseArray<List<CancerSizeItemEntity>> array = new SparseArray<>();
        if (list==null||list.size()==0) return array;
        List<CancerSizeItemEntity> temp;
        for (CancerSizeItemEntity entity:list){
            int TypeID = entity.getTypeID();
            temp = array.get(TypeID);
            if (temp==null) temp = new ArrayList<>();
            temp.add(entity);
            array.put(TypeID,temp);
        }
        return array;
    }

    public static List<List<CancerSizeItemEntity>> getListForChartList(List<CancerSizeItemEntity> data){
        SparseArray<List<CancerSizeItemEntity>> array = getArrayForChartList(data);
        List<List<CancerSizeItemEntity>> list = new ArrayList<>();
        if (array==null||array.size()==0){
            return list;
        }
        List<Integer> keys = new ArrayList<>();
        for (int i=0;i<array.size();i++){
            keys.add(array.get(array.keyAt(i)).get(0).getTypeID());
        }
        Collections.sort(keys, new Comparator<Integer>() {
            @Override
            public int compare(Integer h1, Integer h2) {
                return h1 - h2;
            }
        });
        for (int i:keys){
            list.add(array.get(i));
        }
        return list;
    }

    /**
     * 处理肿瘤大小图标数据
     *
     * @param array
     * @param InfoID
     * @return
     */
    public static SparseArray<CancerSizeItemEntity> getCancerSizeCharArray
                    (SparseArray<List<CancerSizeItemEntity>> array,int InfoID){
        SparseArray<CancerSizeItemEntity> mArray = new SparseArray<>();
        if (array!=null&&array.size()>0){
            for (int i=0;i<array.size();i++){
                int TypeID = array.keyAt(i);
                List<CancerSizeItemEntity> temp = array.get(TypeID);
                CancerSizeItemEntity entity = new CancerSizeItemEntity();
                for (int j=0;j<temp.size();j++){
                    if (j==0){
                        entity = temp.get(0);
                    }else {
                        if (temp.get(j).getRecordTime()>entity.getRecordTime()){
                            entity = temp.get(j);
                        }
                    }
                }
                mArray.put(TypeID,entity);
            }
            return mArray;
        }
        CancerSizeItemEntity mEntity = new CancerSizeItemEntity();
        mEntity.setTypeID(1);
        mEntity.setDelflag(0);
        mEntity.setInfoID(InfoID);
        mEntity.setPatientID(InfoID);
        mEntity.setName("主肿瘤大小");
        mArray.put(1, mEntity);
        return mArray;
    }

    public static SparseArray<CancerSizeItemEntity> getEditCancerChartArray (SparseArray<CancerSizeItemEntity> array,
                                                                             RecordItemEntity entity,int InfoID){
        SparseArray<CancerSizeItemEntity> mArray = new SparseArray<>();
        if (array==null||array.size()==0) return mArray;
        List<CancerSizeItemEntity> temp = entity.getTumourInfo();
        if (temp==null||temp.size()==0) return array;
        List<String> key = new ArrayList<>();
        List<String> arrayKey = new ArrayList<>();
        for (CancerSizeItemEntity item:temp){
            key.add(item.getTypeID()+"");
        }
        for (int i = 0;i<array.size();i++){
            String id = array.keyAt(i)+"";
            arrayKey.add(id);
            if (key.contains(id)){
                array.put(array.keyAt(i),getSameTypeIDEntity(temp, array.keyAt(i), InfoID));
            }else {
                array.put(array.keyAt(i), getSameNotTypeIDEntity(array.get(array.keyAt(i)),array.keyAt(i),InfoID));
            }
        }
        for (CancerSizeItemEntity item:temp){
            String id = item.getTypeID()+"";
            if (!arrayKey.contains(id)){
                array.put(item.getTypeID(),item);
            }
        }

        return array;
    }

    private static CancerSizeItemEntity getSameTypeIDEntity(List<CancerSizeItemEntity> temp,int id,int InfoID){
        for (CancerSizeItemEntity entity:temp){
            if (entity.getTypeID()==id){
                return entity;
            }
        }
        CancerSizeItemEntity mEntity = new CancerSizeItemEntity();
        mEntity.setTypeID(id);
        mEntity.setDelflag(0);
        mEntity.setInfoID(InfoID);
        mEntity.setPatientID(InfoID);
        mEntity.setName("未知肿瘤");
        return mEntity;
    }

    private static CancerSizeItemEntity getSameNotTypeIDEntity(CancerSizeItemEntity entity,int id,int InfoID){
        CancerSizeItemEntity mEntity = new CancerSizeItemEntity();
        mEntity.setTypeID(id);
        mEntity.setDelflag(0);
        mEntity.setInfoID(InfoID);
        mEntity.setPatientID(InfoID);
        mEntity.setName(entity.getName());
        mEntity.setSize("");
        return mEntity;
    }

    /**
     * 处理肿瘤指标图标数据
     *
     * @param array
     * @param check
     * @param InfoID
     * @return
     */
    public static SparseArray<CancerSizeItemEntity> getQuotaCharArrayEdit(SparseArray<List<CancerSizeItemEntity>> array
            ,ArrayList<String> check,int InfoID,RecordItemEntity old,boolean isEdit){
        SparseArray<CancerSizeItemEntity> mArray = new SparseArray<>();
        List<CancerSizeItemEntity> record = old.getCancerMark();
        if (record==null) record = new ArrayList<>();

        CancerSizeItemEntity mEntity;
        if (check!=null&&check.size()>0&&record.size()>0){
            SparseArray<String> keys = new SparseArray<>();
            for (int i=0;i<record.size();i++){
                keys.put(record.get(i).getTypeID(),record.get(i).getSize());
            }
            for (int i=0;i<check.size();i++){
                int TypeID = Integer.valueOf(check.get(i));
                mEntity  = new CancerSizeItemEntity();
                mEntity.setTypeID(TypeID);
                mEntity.setDelflag(0);
                mEntity.setInfoID(InfoID);
                mEntity.setPatientID(InfoID);
                mArray.put(TypeID,mEntity);
            }
            if (array!=null&&array.size()>0){
                for (int i=0;i<array.size();i++){
                    int TypeID = array.keyAt(i);
                    if (check.contains(TypeID+"")){
                        mEntity  = new CancerSizeItemEntity();
                        mEntity.setTypeID(TypeID);
                        mEntity.setDelflag(0);
                        mEntity.setInfoID(InfoID);
                        mEntity.setPatientID(InfoID);
                        if (!TextUtils.isEmpty(keys.get(TypeID))){
                            mEntity.setSize(keys.get(TypeID));
                        }
                        mArray.put(TypeID,mEntity);
                    }
                }
            }
        }
        return mArray;
    }

    /**
     * 处理肿瘤指标图标数据
     *
     * @param array
     * @param check
     * @param InfoID
     * @return
     */
    public static SparseArray<CancerSizeItemEntity> getQuotaCharArray(SparseArray<List<CancerSizeItemEntity>> array
            ,ArrayList<String> check,int InfoID,boolean isEdit){
        SparseArray<CancerSizeItemEntity> mArray = new SparseArray<>();
        CancerSizeItemEntity mEntity;
        if (check!=null&&check.size()>0){
            for (int i=0;i<check.size();i++){
                int TypeID = Integer.valueOf(check.get(i));
                mEntity  = new CancerSizeItemEntity();
                mEntity.setTypeID(TypeID);
                mEntity.setDelflag(0);
                mEntity.setInfoID(InfoID);
                mEntity.setPatientID(InfoID);
                mArray.put(TypeID,mEntity);
            }
            if (array!=null&&array.size()>0){
                for (int i=0;i<array.size();i++){
                    int TypeID = array.keyAt(i);
                    if (check.contains(TypeID+"")){
                        mEntity  = new CancerSizeItemEntity();
                        mEntity.setTypeID(TypeID);
                        mEntity.setDelflag(0);
                        mEntity.setInfoID(InfoID);
                        mEntity.setPatientID(InfoID);
                        if (isEdit){
                            List<CancerSizeItemEntity> temp = array.get(TypeID);
                            CancerSizeItemEntity entity = new CancerSizeItemEntity();
                            for (int j=0;j<temp.size();j++){
                                if (j==0){
                                    entity = temp.get(0);
                                }else {
                                    if (temp.get(j).getRecordTime()>entity.getRecordTime()){
                                        entity = temp.get(j);
                                    }
                                }
                            }
                            mEntity.setSize(entity.getSize());
                        }
                        mArray.put(TypeID,mEntity);
                    }
                }
            }
        }
        return mArray;
    }

    /**
     * 获得图表的键
     *
     * @param array
     * @return
     */
    public static int[] getTypeIDArray(SparseArray<CancerSizeItemEntity> array){
        int[] result = new int[array.size()];
        for (int i=0;i<array.size();i++){
            result[i] = array.keyAt(i);
        }
        Arrays.sort(result);
        return result;
    }

    /**
     * 获得图表的键
     *
     * @param array
     * @return
     */
    public static int[] getTypeIDArrayAll(SparseArray<List<CancerSizeItemEntity>> array){
        int[] result = new int[array.size()];
        for (int i=0;i<array.size();i++){
            result[i] = array.keyAt(i);
        }
        Arrays.sort(result);
        return result;
    }

    /**
     * 获取新增的TypeID
     *
     * @param array
     * @return
     */
    public static int getTypeIDNext(SparseArray<List<CancerSizeItemEntity>> array){
        if (array==null||array.size()==0) return 2;
        int Max = 0;
        int temp;
        for (int i=0;i<array.size();i++){
            temp = array.keyAt(i);
            if (temp>Max) Max = temp;
        }
        return Max+1;
    }

    /**
     * 获取新增肿瘤的实体类数据
     *
     * @param TypeID
     * @param InfoID
     * @return
     */
    public static CancerSizeItemEntity getNextCancerEntity(int TypeID,int InfoID){
        CancerSizeItemEntity entity = new CancerSizeItemEntity();
        entity.setTypeID(TypeID);
        entity.setDelflag(0);
        entity.setInfoID(InfoID);
        entity.setPatientID(InfoID);
        entity.setName("副肿瘤" + (TypeID - 1));
        return entity;
    }

    /**
     * 将肿瘤大小数据转换成表格数据
     *
     * @param list
     * @return
     */
    public static List<ChartEntity> getCancerChartData(List<CancerSizeItemEntity> list){
        Collections.sort(list, new Comparator<CancerSizeItemEntity>() {
            @Override
            public int compare(CancerSizeItemEntity lhs, CancerSizeItemEntity rhs) {
                return (int) (lhs.getRecordTime() - rhs.getRecordTime());
            }
        });
        List<ChartEntity> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (CancerSizeItemEntity entity:list){
            ChartEntity mEntity = new ChartEntity();
            String size = entity.getSize();
            if (!OtherUtils.isEmpty(size)){
                int index = size.indexOf("*");
                String width = size.substring(0,index);
                String height = size.substring(index+1,size.length());
                mEntity.setWidth(Integer.valueOf(width));
                mEntity.setHeight(Integer.valueOf(height));
            }
            long time = entity.getRecordTime();
            calendar.setTimeInMillis(time * 1000);
            int year = calendar.get(Calendar.YEAR);
            mEntity.setYear(year);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            String date = format.format(new Date(entity.getRecordTime()*1000));
            mEntity.setDate(date);
            result.add(mEntity);
        }
        return result;
    }

    /**
     * 将肿瘤指标数据转换成表格数据
     *
     * @param list
     * @return
     */
    public static List<ChartFloatEntity> getQuotaChartData(List<CancerSizeItemEntity> list){
        Collections.sort(list, new Comparator<CancerSizeItemEntity>() {
            @Override
            public int compare(CancerSizeItemEntity lhs, CancerSizeItemEntity rhs) {
                return (int) (lhs.getRecordTime() - rhs.getRecordTime());
            }
        });
        List<ChartFloatEntity> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (CancerSizeItemEntity entity:list){
            ChartFloatEntity mEntity = new ChartFloatEntity();
            String size = entity.getSize();
            if (!OtherUtils.isEmpty(size)){
                mEntity.setSize(Float.valueOf(size));
            }
            long time = entity.getRecordTime();
            calendar.setTimeInMillis(time * 1000);
            int year = calendar.get(Calendar.YEAR);
            mEntity.setYear(year);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            String date = format.format(new Date(entity.getRecordTime()*1000));
            mEntity.setDate(date);
            result.add(mEntity);
        }
        return result;
    }

    /**
     *
     * 将array数据转换成参数串
     *
     * @param array
     * @return
     */
    public static String getTumourDataString(SparseArray<CancerSizeItemEntity> array){
        if (array==null||array.size()==0) return "[]";
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        boolean one = true;
        for (int i=0;i<array.size();i++){
            if (!OtherUtils.isEmpty(array.get(array.keyAt(i)).getSize())){
                if (one){
                    one = false;
                }else {
                    sb.append(",");
                }
                sb.append(array.get(array.keyAt(i)).toString());
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 判断array数据是否为空
     *
     * @param array
     * @return
     */
    public static boolean isEmptyForDataString(SparseArray<CancerSizeItemEntity> array){
        if (array==null||array.size()==0) return true;
        for (int i=0;i<array.size();i++){
            if (!OtherUtils.isEmpty(array.get(array.keyAt(i)).getSize())){
                return false;
            }
        }
        return true;
    }

    /**
     * 将List转换成字符串，并将子项用","隔开
     *
     * @param list
     * @return
     */
    public static String getParamsForPic(List<String> list){
        if (list==null||list.size()==0) return "";
        String result = "";
        for (int i=0;i<list.size();i++){
            if (i==0) {
                result += list.get(0);
            }else {
                result += "," +list.get(i);
            }
        }
        return result;
    }

    /**
     * 将阶段数据转成参数串
     *
     * @param bean
     * @return
     */
    public static String[] getParamsForPatient(PatientDetailBean bean){
        String[] args = null;
        int setBit = 0;
        String temp = "";
        temp += Contants.InfoID +",,,"+Const.InfoID;
        String cancerid = bean.getCancerID();
        if (!TextUtils.isEmpty(cancerid)) {//修改癌种后，提交未知的分期和基因突变的信息（0）
            setBit += Const.CANCER_TYPE_CHANGE;
            temp += ",,," + Contants.CancerID + ",,," + cancerid;
            String gene = bean.getGene();
            if (TextUtils.isEmpty(gene)) {
                bean.setGene("0");
            }
            String PeriodID = bean.getPeriodID();
            if (TextUtils.isEmpty(PeriodID)) {
                bean.setPeriodID("0");
            }
            String PeriodType = bean.getPeriodType();
            if (TextUtils.isEmpty(PeriodType)) {
                bean.setPeriodType("1");
            }
        }
        String InfoName = bean.getInfoName();
        if (!TextUtils.isEmpty(InfoName)) {
            temp += ",,," + Contants.InfoName+ ",,," + InfoName;
            setBit += Const.NAME_CHANGE;
        }
        String Headimgurl = bean.getHeadimgurl();
        if (!TextUtils.isEmpty(Headimgurl)) {
            temp += ",,," + Contants.Headimgurl+ ",,," + Headimgurl;
            setBit += Const.AVATAR_CHANGE;
        }
        String TransferPos = bean.getTransferPos();
        if (TransferPos!=null) {
            temp += ",,," + Contants.TransferPos+ ",,," + TransferPos;
            setBit += Const.CANCER_POS;
        }
        String Gene = bean.getGene();
        if (Gene!=null) {
            temp += ",,," + Contants.Gene + ",,," + Gene;
            setBit += Const.GENE_CHANGE;
        }
        String DiscoverTime = bean.getDiscoverTime();
        if (!TextUtils.isEmpty(DiscoverTime)) {
            temp += ",,," + Contants.DiscoverTime + ",,," + DiscoverTime;
            setBit += Const.DISCOVER_TIME_CHANGE;
        }
        String Sex = bean.getSex();
        if (!TextUtils.isEmpty(Sex)) {
            temp += ",,," + Contants.Sex + ",,," + Sex;
            setBit += Const.SEX_CHANGE;
        }
        String Age = bean.getAge();
        if (!TextUtils.isEmpty(Age)) {
            temp += ",,," + Contants.Age + ",,," + Age;
            setBit += Const.AGE_CHANGE;
        }
        String City = bean.getCity();

        if (!TextUtils.isEmpty(City)&&!City.equals("0")) {
            String ProvinceID = City.substring(0,2)+"0000";
            temp += ",,," + Contants.City + ",,," + City;
            temp += ",,," + "Province" + ",,," + ProvinceID;
            setBit += Const.LOCATION_CHANGE;
        }
        String PeriodID = bean.getPeriodID();
        String PeriodType = bean.getPeriodType();
        if (!TextUtils.isEmpty(PeriodID) && !TextUtils.isEmpty(PeriodType)) {
            temp += ",,," + Contants.PeriodID + ",,," + PeriodID;
            temp += ",,," + Contants.PeriodType + ",,," + PeriodType;
            setBit += Const.TNM_CHANGE;
        }
        temp += ",,," + Contants.SetBit + ",,," + setBit;
        args = temp.split(",,,");
        setBit = 0;
        return args;
    }

    /**
     * 将List集合转成参数串
     *
     * @param list
     * @return
     */
    public static String[] getParamsForList(List<String> list){
        if(list==null&&list.size()==0){
            return null;
        }
        String[] args = new String[list.size()];
        for(int i = 0;i<list.size();i++){
            args[i] = list.get(i);
        }
        return args;
    }

    /**
     * 判断要添加的用户症状和指标是否有效
     * 无效时需要添加空窗期
     *
     * @param recordTime
     * @return 有效时返回null, 否则返回需要创建空窗期的时间
     */
    public static TimeSlot validRecordTime(long recordTime,List<UserStepBean> listData) {
        List<UserStepBean> lstItem = listData;

        if (lstItem == null || lstItem.isEmpty()) {
            return new TimeSlot(TimeSlot.TIME_FIRST, TimeSlot.TIME_LAST);
        }

        Collections.sort(lstItem, new UserStepComparator());

        int size = lstItem.size();
        UserStepBean temp;
        long begTime = -1;
        long endTime = -1;
        for (int i = 0; i < size; i++) {
            temp = lstItem.get(i);
            if ((temp.getCompareDateBeg() <= recordTime) && (recordTime <= temp.getCompareDateEnd())) {
                return new TimeSlot(temp);
            }

            if (recordTime < temp.getCompareDateBeg()) {
                if (endTime == -1 || temp.getCompareDateBeg() < endTime) {
                    endTime = temp.getCompareDateBeg() - 1;
                }
            }

            if (recordTime > temp.getCompareDateEnd()) {
                if (begTime == -1 || temp.getCompareDateEnd() > begTime) {
                    begTime = temp.getCompareDateEnd() + 1;
                }
            }
        }

        if (begTime == -1) {
            begTime = TimeSlot.TIME_FIRST;
        }
        if (endTime == -1) {
            begTime = TimeSlot.TIME_LAST;
        }

        return new TimeSlot(begTime, endTime);
    }

    public static class TimeSlot {

        public final static long TIME_FIRST = 1;
        public final static long TIME_LAST = 0;
        public boolean isValid;
        public UserStepBean userStep;
        public long begTime;
        public long endTime;

        public TimeSlot(long begTime, long endTime) {
            this.begTime = begTime;
            this.endTime = endTime;
        }

        public TimeSlot(UserStepBean userStep) {
            this.userStep = userStep;
            this.isValid = true;
        }
    }

    /**
     * 验证要添加的阶段数据
     *
     * @param lstSrc
     * @param beans
     * @return 为null时表示验证通过，其他为验证错误消息
     */
    public static ValidUserStepModify validUserStepAdd(List<UserStepBean> lstSrc, final List<UserStepBean> beans) {
        ValidUserStepModify result = new ValidUserStepModify();

        if (beans == null || beans.size() == 0) {
            return result.error("没有要添加的数据！");
        }


        if ((lstSrc == null || lstSrc.isEmpty())) {
            if (beans.size() == 1) {
                return result.pass();//不用验证就是OK的
            }
        }

        List<UserStepBean> lstDst = new ArrayList<>();
        if ((lstSrc != null && !lstSrc.isEmpty())) {
            UserStepBean temp = null;
            int sizeSrc = lstSrc.size();
            for (int i = 0; i < sizeSrc; i++) {
                temp = lstSrc.get(i);
                if (!TextUtils.isEmpty(temp.getStepID())) {
                    lstDst.add(temp);
                }
            }
        }

        UserStepBean temp = null;
        for (int i = 0; i < beans.size(); i++) {
            temp = beans.get(i);

            if (temp.getCompareDateBeg() >= temp.getCompareDateEnd()) {
                return result.error("开始日期必须早于结束日期");
            }
            //lstDst.add(temp);
        }

        Collections.sort(lstDst, new UserStepComparator());

        UserStepBean temp0 = null;
        UserStepBean temp1 = null;
//        List<UserStepBean> lstClashReali = new ArrayList<>();
        result.mNeedDelSpace = new ArrayList<>();
        int sizeDst = lstDst.size();
        for (int i = 1; i < sizeDst; i++) {

            temp0 = lstDst.get(i - 1);
            temp1 = lstDst.get(i);
            if (temp0.getCompareDateEnd() >= temp1.getCompareDateBeg()) {

                if (temp0.$isEditor() || temp0.$isChanged()) {
                    temp = temp1;
                } else {
                    temp = temp0;
                }

                if (temp.isSpace()) {
                    if (!result.mNeedDelSpace.contains(temp)) {
                        result.mNeedDelSpace.add(temp);
                    }
                } else {
                    return result.error("阶段所选时间与第" + temp.$number() + "阶段冲突，请重新修改");
                }
            }
        }
        if (result.mNeedDelSpace.isEmpty()) {
            return result.pass();
        }
        return result;
    }

    public static class ValidUserStepModify {
        public boolean valid;
        public String errmsg;
        public List<UserStepBean> mNeedDelSpace;

        public ValidUserStepModify error(String msg) {
            errmsg = msg;
            return this;
        }

        public ValidUserStepModify pass() {
            valid = true;
            return this;
        }
    }
}
