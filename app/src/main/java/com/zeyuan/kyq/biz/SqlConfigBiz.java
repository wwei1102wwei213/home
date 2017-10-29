package com.zeyuan.kyq.biz;


import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.ta.util.db.TASQLiteDatabase;
import com.zeyuan.kyq.Entity.CircleNewEntity;
import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.Entity.DigitConfEntity;
import com.zeyuan.kyq.Entity.ProvinceEntity;
import com.zeyuan.kyq.Entity.StepConfEntity;
import com.zeyuan.kyq.Entity.StepDataEntity;
import com.zeyuan.kyq.Entity.SyncConfEntity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.TNMObj;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.db.MemoryCache;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.DataSecretUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.StepDataUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/11.
 * <p>
 * Synconf封装类
 *
 * @author wwei
 */
public class SqlConfigBiz {


    private static final String MAXTIMESTAMP = "maxtimestamp";
    private static final String DATA = "data";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ZERO = "0";
    /***
     * SyncConf字段
     */
    private static final String PERFORM = "perform";
    private static final String BODYPOS = "BodyPos";
    private static final String Perform = "Perform";
    private static final String Cancer = "Cancer";
    private static final String ParentID = "ParentID";
    private static final String CityIDData = "CityIDData";
    private static final String CITY = "City";
    private static final String OtherStep = "OtherStep";

    private static final String STEP = "step";
    private static final String CANCER2_STEP = "Cancer2Step";
    private static final String CIRCLE_ID_DATA = "CircleIDData";
    private static final String Citydata = "Citydata";
    private static final String Cancerdata = "Cancerdata";
    private static final String CircleID = "CircleID";
    private static final String CircleName = "CircleName";

    private static final String GeneticMutation = "GeneticMutation";
    private static final String gene = "gene";
    private static final String StepUnion = "StepUnion";
    private static final String CURECONFID = "cureconfid";
    private static final String ALL = "0";
    private static final String stepunionid = "stepunionid";
    private static final String cureconfid = "cureconfid";
    private static final String stepallname = "stepallname";
    private static final String defaultID = "defaultChildID";
    /**
     * 分期字段
     */
    private static final String DIGIT = "digit";
    private static final String DIGIT_DATA = "digitdata";
    private static final String DIGIT_CANCER = "digitcancer";
    private static final String TNM = "TNM";
    private static final String TNM_DIGIT = "TNMDigit";
    private static final String DIGIT_ID_ALL = "DigitIDAll";
    private static final String CANCER_ID_ALL = "CancerIDAll";

    private static final String T = "T";
    private static final String N = "N";
    private static final String M = "M";

    private static final String CONTENT = "content";
    private static final String CANCER_ID = "CancerID";
    private static final String TNM_ID = "TNMID";
    private static final String DIGIT_ID = "DigitID";
    private static final String T_ID = "Tid";
    private static final String M_ID = "Mid";
    private static final String N_ID = "Nid";

    public interface ConfigCallback {

        void praseFinish(boolean sucecss, int flag);

        void praseFailed(int flag);

    }

    public static void saveSynconfSqlite(String response, String versionNum) {

        try {
            long begin = System.currentTimeMillis();

            DBHelper.getInstance().initDropSyncTable(ZYApplication.application);
            DBHelper.getInstance().initCreateSyncTable(ZYApplication.application);

            SyncConfEntity entity = new SyncConfEntity();
            entity.setId(0);
            entity.setVersionNum(versionNum);
            entity.setMaxtimestamp(begin / 1000 + "");
            entity.setData(response);

            TASQLiteDatabase sqlitedb = ZYApplication.application.getSQLiteDatabasePool().getSQLiteDatabase();
            try {
                sqlitedb.insert(entity);
            } catch (Exception e) {
                LogCustom.i(Const.TAG.ZY_DATA, "SyncConf数据写入数据库失败");
            }
            ZYApplication.application.getSQLiteDatabasePool().releaseSQLiteDatabase(sqlitedb);
            LogCustom.i(Const.TAG.ZY_DATA, "SyncConf数据写入数据库成功");

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "SYNC_CONF");
        }

    }

    public static void saveDigitConfSqlite(String response, String versionNum) {
        try {
            long begin = System.currentTimeMillis();
            DBHelper.getInstance().initDropDigitTable(ZYApplication.application);
            DBHelper.getInstance().initCreateDigitTable(ZYApplication.application);
            DigitConfEntity entity = new DigitConfEntity();
            entity.setId(0);
            entity.setVersionNum(versionNum);
            entity.setMaxtimestamp(begin / 1000 + "");
            //分期数据加密
            response = DataSecretUtils.TEA.encryptByTea(response);
            entity.setData(response);
            TASQLiteDatabase sqlitedb = ZYApplication.application.getSQLiteDatabasePool().getSQLiteDatabase();
            try {
                sqlitedb.insert(entity);
            } catch (Exception e) {
                LogCustom.i(Const.TAG.ZY_DATA, "DigitConf数据写入数据库失败");
            }
            ZYApplication.application.getSQLiteDatabasePool().releaseSQLiteDatabase(sqlitedb);
            LogCustom.i(Const.TAG.ZY_DATA, "DigitConf数据写入数据库成功");
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "DIGIT_CONF");
        }
    }

    public static void saveSteoConfSqlite(String response, String versionNum) {
        try {

            long begin = System.currentTimeMillis();

            DBHelper.getInstance().initDropStepTable(ZYApplication.application);
            DBHelper.getInstance().initCreateStepTable(ZYApplication.application);

            StepConfEntity entity = new StepConfEntity();
            entity.setId(0);
            entity.setVersionNum(versionNum);
            entity.setMaxtimestamp(begin / 1000 + "");
            entity.setData(response);

            TASQLiteDatabase sqlitedb = ZYApplication.application.getSQLiteDatabasePool().getSQLiteDatabase();
            try {
                sqlitedb.insert(entity);
            } catch (Exception e) {
                LogCustom.i(Const.TAG.ZY_DATA, "StepConf数据写入数据库失败");
            }
            ZYApplication.application.getSQLiteDatabasePool().releaseSQLiteDatabase(sqlitedb);
            LogCustom.i(Const.TAG.ZY_DATA, "StepConf数据写入数据库成功");

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "STEP_CONF");
        }
    }

    public static void setStepDataMemory(String response) {
        try {
            Gson mGson = new Gson();
            Object o = mGson.fromJson(response, StepDataEntity.class);
            try {
                StepDataEntity entity = (StepDataEntity) o;
                MemoryCache.putObjectInCache(Const.DataStepData, entity);
            } catch (Exception e) {
                LogCustom.i(Const.TAG.ZY_DATA, "StepDataEntity转型失败");
            }
            LogCustom.i(Const.TAG.ZY_DATA, "StepConf写入缓存成功");
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "StepConf写入缓存失败");
        }
    }

    public static void updataStep(StepDataEntity entity) {
        try {
//            MemoryCache.putObjectInCache(Const.DataStepData, entity);

            DBHelper.getInstance().initDropStepTable(ZYApplication.application);
            DBHelper.getInstance().initCreateStepTable(ZYApplication.application);

            StepConfEntity confEntity = new StepConfEntity();
            confEntity.setId(0);
            confEntity.setVersionNum(ZYApplication.versionNum);
            confEntity.setMaxtimestamp(entity.getStepData().getMaxtimestamp());
            // confEntity.setData(StepDataUtils.TEA.encryptByTea(entity.toString()));
            confEntity.setData(entity.toString());
            TASQLiteDatabase sqlitedb = ZYApplication.application.getSQLiteDatabasePool().getSQLiteDatabase();
            try {
                sqlitedb.insert(confEntity);
            } catch (Exception e) {
                LogCustom.i(Const.TAG.ZY_DATA, "StepConf配置数据更新失败");
            }
            ZYApplication.application.getSQLiteDatabasePool().releaseSQLiteDatabase(sqlitedb);
            LogCustom.i(Const.TAG.ZY_DATA, "StepConf配置数据更新成功");
            Object temp = getDataForSyncConfStep(Const.UPDATA_STEP, entity);
            if (Const.RESULT.equals(temp)) {
                LogCustom.i(Const.TAG.ZY_DATA, "StepConf客户端数据实时更新成功");
            } else {
                LogCustom.i(Const.TAG.ZY_DATA, "StepConf客户端数据实时更新失败");
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "updataStep失败");
        }
    }

    public static void saveSteoConfUpdata(String response, String versionNum) {
        try {
            Gson mGson = new Gson();
            Object o = mGson.fromJson(response, StepDataEntity.class);
            StepDataEntity entity = (StepDataEntity) o;
            List<ConfStepEntity> list = entity.getStepData().getData();
            LogCustom.i(Const.TAG.ZY_DATA, "Step数据SIZE:" + list.size());

            MemoryCache.putObjectInCache(Const.DataStepData, entity);

            DBHelper.getInstance().initDropStepTable(ZYApplication.application);
            DBHelper.getInstance().initCreateStepTable(ZYApplication.application);

            StepConfEntity confEntity = new StepConfEntity();
            confEntity.setId(0);
            confEntity.setVersionNum(versionNum);
            confEntity.setMaxtimestamp(entity.getStepData().getMaxtimestamp());
            LogCustom.i(Const.TAG.ZY_DATA, "Step数据时间戳:" + entity.getStepData().getMaxtimestamp());
            // confEntity.setData(StepDataUtils.TEA.encryptByTea(entity.toString()));
            confEntity.setData(entity.toString());
            TASQLiteDatabase sqlitedb = ZYApplication.application.getSQLiteDatabasePool().getSQLiteDatabase();
            try {
                sqlitedb.insert(confEntity);
            } catch (Exception e) {
                LogCustom.i(Const.TAG.ZY_DATA, "StepConf数据写入数据库失败");
            }
            ZYApplication.application.getSQLiteDatabasePool().releaseSQLiteDatabase(sqlitedb);
            LogCustom.i(Const.TAG.ZY_DATA, "StepConf数据写入数据库成功");


        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "saveSteoConfUpdata失败");
        }
    }

    /***
     *
     * 根据数据标识获取数据
     *
     * @param flag 数据标识
     * @return 数据
     */
    public static Object getDataForFlag(int flag) {
        if (flag < 10) {
            return getDataForDigitConf(flag);//分期数据
        } else if (10 < flag && flag < 39) {//基本数据
            return getDataForSynconf(flag);
        } else if (flag > 39) {
            return getDataForStepConf(flag);//阶段数据
        } else {
            LogCustom.i(Const.TAG.ZY_DATA, "数据标识（flag）超出范围" + flag);
            return null;
        }
    }

    public static Object getDataForFlag(int flag, String cancerID) {
        if (flag == Const.N_DataCancerStepNew) {
            Object object = null;
            //内存中有配置数据
            Object tempData = MemoryCache.getObjectFromCache(Const.DataStepData);
            if (tempData != null) {
                object = getDataForSyncConfStep(flag, (StepDataEntity) tempData, cancerID);
            } else {
                //内存缓存中没有则去数据库查找
                List<StepConfEntity> list = DBHelper.getInstance().queryStepConf(ZYApplication.application);
                if (list != null && list.size() != 0) {
                    LogCustom.i(Const.TAG.ZY_DATA, "缓存中没有药物数据，去数据库获取：" + flag);
                    String temp = StepDataUtils.TEA.decryptByTea(list.get(0).getData());
                    Gson mGson = new Gson();
                    tempData = mGson.fromJson(temp, StepDataEntity.class);
                    object = getDataForSyncConfStep(flag, (StepDataEntity) tempData, cancerID);
                } else {
                    //数据库中没有则去发请求
                    /*LogCustom.i(Const.TAG.ZY_DATA, "数据库中没有药物数据，去服务端获取:"+flag);
                    String temp = offSecretData(Const.N_DataConfStep, getDataAgain(Const.N_DataConfStep));
                    object = getDataForSyncConfStep(flag, temp);*/
                }
            }
            return object;
        }
        return null;
    }

    public static Object getDataForStepConf(int flag) {
        Object object = null;
        switch (flag) {
            case Const.N_DataDrugNames:
                object = MemoryCache.getObjectFromCache(Const.DataDrugNames);
                break;
            case Const.N_DataCancerStepNew:
                object = MemoryCache.getObjectFromCache(Const.DataCancerStepNew);
                break;
            case Const.N_DataSearchDrug:
                object = MemoryCache.getObjectFromCache(Const.DataSearchDrug);
                break;
            case Const.N_DataSearchDrugList:
                object = MemoryCache.getObjectFromCache(Const.DataSearchDrugList);
                break;
            case Const.N_DataStepData:
                object = MemoryCache.getObjectFromCache(Const.DataStepData);
                break;
        }
        //先去内存缓存中找
        if (object != null) {
            LogCustom.i(Const.TAG.ZY_DATA, "缓存中获取药物数据成功：" + flag);
            return object;
        } else {
            //内存中有配置数据
            Object tempData = MemoryCache.getObjectFromCache(Const.DataStepData);
            if (tempData != null) {
                object = getDataForSyncConfStep(flag, (StepDataEntity) tempData);
            } else {
                //内存缓存中没有则去数据库查找
                List<StepConfEntity> list = DBHelper.getInstance().queryStepConf(ZYApplication.application);
                if (list != null && list.size() != 0) {
                    LogCustom.i(Const.TAG.ZY_DATA, "缓存中没有药物数据，去数据库获取：" + flag);
                    // String temp = StepDataUtils.TEA.decryptByTea(list.get(0).getData());
                    String temp = list.get(0).getData();
                    Gson mGson = new Gson();
                    tempData = mGson.fromJson(temp, StepDataEntity.class);
                    object = getDataForSyncConfStep(flag, (StepDataEntity) tempData);
                } else {
                    //数据库中没有则去发请求
                    /*LogCustom.i(Const.TAG.ZY_DATA, "数据库中没有药物数据，去服务端获取:"+flag);
                    String temp = offSecretData(Const.N_DataConfStep, getDataAgain(Const.N_DataConfStep));
                    object = getDataForSyncConfStep(flag, temp);*/
                }
            }
        }
        return object;
    }

    /***
     *
     * 根据数据标识获取数据
     *
     * @param flag 数据标识
     * @return 数据
     */
    public static Object getDataForDigitConf(int flag) {
        Object object = null;
        switch (flag) {
            case Const.N_DataDigitValues:
                object = MemoryCache.getObjectFromCache(Const.DataDigitValues);
                break;
            case Const.N_DataDigitData:
                object = MemoryCache.getObjectFromCache(Const.DataDigitData);
                break;
            case Const.N_DataTnmObjs:
                object = MemoryCache.getObjectFromCache(Const.DataTnmObjs);
                break;
            case Const.N_DataDigitT:
                object = MemoryCache.getObjectFromCache(Const.DataDigitT);
                break;
            case Const.N_DataDigitN:
                object = MemoryCache.getObjectFromCache(Const.DataDigitN);
                break;
            case Const.N_DataDigitM:
                object = MemoryCache.getObjectFromCache(Const.DataDigitM);
                break;
        }
        //先去内存缓存中找
        if (object != null) {
            LogCustom.i(Const.TAG.ZY_DATA, "缓存中获取分期数据成功：" + flag);
            return object;
        } else {
            //内存缓存中没有则去数据库查找
            List<DigitConfEntity> list = DBHelper.getInstance().queryDigitConf(ZYApplication.application);
            if (list != null && list.size() != 0) {
                LogCustom.i(Const.TAG.ZY_DATA, "缓存中没有分期数据，去数据库获取：" + flag);
                  String temp = offSecretData(Const.N_DataDigitConf, list.get(0).getData());
                //String temp = list.get(0).getData();
                object = getDataForDigitConfData(flag, temp);
            } else {
                //数据库中没有则去发请求
                LogCustom.i(Const.TAG.ZY_DATA, "数据库中没有分期数据，去服务端获取:" + flag);
                 //String temp = offSecretData(Const.N_DataDigitConf, getDataAgain(Const.N_DataDigitConf));
                String temp = getDataAgain(Const.N_DataDigitConf);
                object = getDataForDigitConfData(flag, temp);
            }
        }
        return object;
    }

    /***
     *
     * 根据数据标识获取数据
     *
     * @param flag 数据标识
     * @return 数据
     */
    public static Object getDataForSynconf(int flag) {
        Object object = null;
        switch (flag) {
            case Const.N_DataCureConf:
                object = MemoryCache.getObjectFromCache(Const.DataCureConf);
                break;
            case Const.N_DataDefaultCancer:
                object = MemoryCache.getObjectFromCache(Const.DataDefaultCancer);
                break;
            case Const.N_DataCancerValues:
                object = MemoryCache.getObjectFromCache(Const.DataCancerValues);
                break;
            case Const.N_DataCancerData:
                object = MemoryCache.getObjectFromCache(Const.DataCancerData);
                break;
            case Const.N_DataPerformValues:
                object = MemoryCache.getObjectFromCache(Const.DataPerformValues);
                break;
            case Const.N_DataBodyPos:
                object = MemoryCache.getObjectFromCache(Const.DataBodyPos);
                break;
            case Const.N_DataTransferPos:
                object = MemoryCache.getObjectFromCache(Const.DataTransferPos);
                break;
            case Const.N_DataGeneValues:
                object = MemoryCache.getObjectFromCache(Const.DataGeneValues);
                break;
            case Const.N_DataGene:
                object = MemoryCache.getObjectFromCache(Const.DataGene);
                break;
            case Const.N_DataCircleValues:
                object = MemoryCache.getObjectFromCache(Const.DataCircleValues);
                break;
            case Const.N_DataCircleCancer:
                object = MemoryCache.getObjectFromCache(Const.DataCircleCancer);
                break;
            case Const.N_DataCircleClass:
                object = MemoryCache.getObjectFromCache(Const.DataCircleClass);
                break;
            case Const.N_DataCity:
                object = MemoryCache.getObjectFromCache(Const.DataCity);
                break;
            case Const.N_DataAllCity:
                object = MemoryCache.getObjectFromCache(Const.DataAllCity);
                break;
            case Const.N_DataCancerParent:
                object = MemoryCache.getObjectFromCache(Const.DataCancerParent);
                break;
            case Const.N_DataCircleForSearch:
                object = MemoryCache.getObjectFromCache(Const.DataCircleForSearch);
                break;
            case Const.N_DataBodyPosValues:
                object = MemoryCache.getObjectFromCache(Const.DataBodyPosValues);
                break;
            case Const.N_UrlAirtcleIndex:
//                object = Const.URL
                break;
            case Const.N_UrlForumIndex:
                break;
        }
        if (object != null) {
            LogCustom.i(Const.TAG.ZY_DATA, "缓存中获取配置数据成功：" + flag);
            return object;
        } else {
            //内存缓存中没有则去数据库查找
            List<SyncConfEntity> list = null;
            try {
                list = DBHelper.getInstance().querySyncConf(ZYApplication.application);
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "querySyncConf");
            }
            if (list != null && list.size() != 0) {
                LogCustom.i(Const.TAG.ZY_DATA, "缓存中没有配置数据，去数据库获取:" + flag);
                String temp = offSecretData(Const.N_DataSyncConf, list.get(0).getData());
                object = getDataForSyncConfData(flag, temp);
                LogCustom.i(Const.TAG.ZY_DATA, "调用数据成功:" + flag);
                return object;
            } else {
                //数据库中没有则去发请求
                LogCustom.i(Const.TAG.ZY_DATA, "数据库中没有配置数据，去服务端获取:" + flag);
                String temp = offSecretData(Const.N_DataSyncConf, getDataAgain(Const.N_DataSyncConf));
                object = getDataForSyncConfData(flag, temp);
            }
        }
        return object;
    }

    /***
     *
     * 解密配置数据
     *
     * @param flag 数据标识
     * @param response 数据
     * @return 数据明文
     */
    public static String offSecretData(int flag, String response) {
        String eqs = "";
        if (flag == Const.N_DataDigitConf) {
            eqs = "digit";
        } else if (flag == Const.N_DataSyncConf) {

            if (TextUtils.isEmpty(response)) {
                return getDataAgain(flag);
            } else {
                return response;
            }

        } else if (flag == Const.N_DataConfStep) {
            eqs = "StepData";
        }
        String secret = null;
        try {
            secret = DataSecretUtils.TEA.decryptByTea(response);
            LogCustom.i(Const.TAG.ZY_DATA, "data:" + flag + "\n" + secret);
            if (TextUtils.isEmpty(secret) || secret.length() < 15 || !secret.substring(0, 15).contains(eqs)) {
                LogCustom.i(Const.TAG.ZY_DATA, "解密数据不合需求:" + eqs + "secret:" + secret.substring(0, 15));
                String data = getDataAgain(flag);
                secret = DataSecretUtils.TEA.decryptByTea(data);
            }
            LogCustom.i(Const.TAG.ZY_DATA, "数据解密成功,数据类型:" + eqs);
        } catch (Exception e) {
            LogCustom.i(Const.TAG.ZY_DATA, "数据解密失败,数据类型:" + eqs);
            String data = getDataAgain(flag);
            secret = DataSecretUtils.TEA.decryptByTea(data);
        }
        return secret;
    }

    /**
     * 获取配置数据在主线程
     *
     * @param flag
     * @return
     */
    public static String getDataAgain(int flag) {

        int[] key = KeyBiz.getKeyResult();
        DataSecretUtils.TEA.setKey(key);
        HttpSecretUtils.TEA.setKey(key);
        String data = KeyBiz.getConfData(flag);
        if (TextUtils.isEmpty(data)) return null;
        if (flag == Const.N_DataDigitConf) {
            saveDigitConfSqlite(data, ZYApplication.versionNum);
        } else if (flag == Const.N_DataSyncConf) {
            saveSynconfSqlite(data, ZYApplication.versionNum);
        } else if (flag == Const.N_DataConfStep) {
            saveSteoConfSqlite(data, ZYApplication.versionNum);
        }
        return data;
    }

    /**
     * 获取配置数据在工作线程
     *
     * @param flag
     * @return
     */
    public static String getDataAgainOnThread(int flag) {
        int[] key = KeyBiz.getKeyResult();
        DataSecretUtils.TEA.setKey(key);
        HttpSecretUtils.TEA.setKey(key);
        String data = KeyBiz.getConfData(flag);
        if (TextUtils.isEmpty(data)) return null;
        if (flag == Const.N_DataDigitConf) {
            saveDigitConfSqlite(data, ZYApplication.versionNum);
        } else if (flag == Const.N_DataSyncConf) {
            saveSynconfSqlite(data, ZYApplication.versionNum);
        } else if (flag == Const.N_DataConfStep) {
            saveSteoConfSqlite(data, ZYApplication.versionNum);
        }
        return data;
    }

    /***
     *
     * 根据数据标识获得数据，数据源为DigitConf
     *
     * @param flag 数据标识
     * @param response 数据源
     * @return
     *      "0" 数据源解析成功
     *      null 数据为空，或者数据源解析失败
     *      NoNull 有数据返回
     */
    public static Object getDataForDigitConfData(int flag, String response) {
        try {

            JSONObject object = new JSONObject(response);
            JSONObject digit = object.getJSONObject(DIGIT);//节点digit
            JSONObject digitData = digit.getJSONObject(DIGIT_DATA);//节点digit的digitData
            JSONArray digitCancer = digit.getJSONArray(DIGIT_CANCER);//节点digit的digitCancer
            JSONArray tnm = digit.getJSONArray(TNM);
            JSONArray tnmDigit = digit.getJSONArray(TNM_DIGIT);

            LinkedHashMap<String, String> dataDigitValues = new LinkedHashMap<>();
            LinkedHashMap<String, List<String>> dataDigitData = new LinkedHashMap<>();
            LinkedHashMap<String, List<String>> dataDigitT = new LinkedHashMap<>();//癌症所对应的所有t分期 键是 癌症id 值是 对应的分期
            LinkedHashMap<String, List<String>> dataDigitN = new LinkedHashMap<>();//癌症所对应的所有n分期 键是 癌症id 值是 对应的分期
            LinkedHashMap<String, List<String>> dataDigitM = new LinkedHashMap<>();//癌症所对应的所有m分期 键是 癌症id 值是 对应的分期
            List<TNMObj> tnmObjs = new ArrayList<>();


            //-----------------------节点digit的digitdata----------------
            Iterator<String> keysIterator = digitData.keys();
            String key;
            while (keysIterator.hasNext()) {
                key = keysIterator.next();
                dataDigitValues.put(key, digitData.getString(key));
            }
            dataDigitValues.put("0", "未知");


            //-----------------------节点digit的digitCancer----------------

            for (int i = 0; i < digitCancer.length(); i++) {
                JSONObject digitCancerItem = digitCancer.getJSONObject(i);
                String cancerId = digitCancerItem.getString(CANCER_ID);
                JSONArray digitId = digitCancerItem.getJSONArray(DIGIT_ID);
                List<String> digitIdList = new ArrayList<>();
                for (int j = 0; j < digitId.length(); j++) {
                    String digitIdItem = (String) digitId.get(j);
                    digitIdList.add(digitIdItem);
                }
                dataDigitData.put(cancerId, digitIdList);
            }

            //-----------------------节点digit的TNM------------------------
//            JSONArray tnm = digit.getJSONArray(TNM);
            for (int i = 0; i < tnm.length(); i++) {
                JSONObject tnmItem = tnm.getJSONObject(i);
                String cancerId = tnmItem.getString(CANCER_ID);
                JSONArray t = tnmItem.getJSONArray(T);
                List<String> listT = new ArrayList<>();
                for (int j = 0; j < t.length(); j++) {
                    JSONObject titem = t.getJSONObject(j);
                    String id = titem.getString(ID);
                    String name = titem.getString(NAME);
                    String content = titem.getString(CONTENT);
                    dataDigitValues.put(id, name + " " + content);
                    listT.add(id);
                }
                dataDigitT.put(cancerId, listT);

                JSONArray n = tnmItem.getJSONArray(N);
                List<String> listN = new ArrayList<>();
                for (int j = 0; j < n.length(); j++) {
                    JSONObject titem = n.getJSONObject(j);
                    String id = titem.getString(ID);
                    String name = titem.getString(NAME);
                    String content = titem.getString(CONTENT);
                    listN.add(id);
                    dataDigitValues.put(id, name + " " + content);
                }
                dataDigitN.put(cancerId, listN);

                JSONArray m = tnmItem.getJSONArray(M);
                List<String> listM = new ArrayList<>();
                for (int j = 0; j < m.length(); j++) {
                    JSONObject titem = m.getJSONObject(j);
                    String id = titem.getString(ID);
                    String name = titem.getString(NAME);
                    String content = titem.getString(CONTENT);
                    listM.add(id);
                    dataDigitValues.put(id, name + " " + content);
                }
                dataDigitM.put(cancerId, listM);
            }
            //-----------------------

            //-----------------------节点digit的TNMDigit
//            JSONArray tnmDigit = digit.getJSONArray(TNM_DIGIT);

            for (int i = 0; i < tnmDigit.length(); i++) {
                JSONObject tnmDigitItem = tnmDigit.getJSONObject(i);
                String cancerId = tnmDigitItem.getString(CANCER_ID);
                JSONArray data = tnmDigitItem.getJSONArray(DATA);
                TNMObj obj = null;
                for (int j = 0; j < data.length(); j++) {
                    obj = new TNMObj();
                    obj.setCancerId(cancerId);
                    JSONObject dataItem = data.getJSONObject(j);
                    String tnmId = dataItem.getString(TNM_ID);
                    String tid = dataItem.getString(T_ID);
                    String nid = dataItem.getString(N_ID);
                    String mid = dataItem.getString(M_ID);
                    String digitId = dataItem.getString(DIGIT_ID);
                    obj.setTid(tid);
                    obj.setNid(nid);
                    obj.setMid(mid);
                    obj.setDigitId(digitId);
                    tnmObjs.add(obj);
                }
            }

            MemoryCache.putObjectInCache(Const.DataDigitValues, dataDigitValues);
            MemoryCache.putObjectInCache(Const.DataDigitData, dataDigitData);
            MemoryCache.putObjectInCache(Const.DataDigitT, dataDigitT);
            MemoryCache.putObjectInCache(Const.DataDigitN, dataDigitN);
            MemoryCache.putObjectInCache(Const.DataDigitM, dataDigitM);
            MemoryCache.putObjectInCache(Const.DataTnmObjs, tnmObjs);

            Object temp = null;
            switch (flag) {
                case Const.N_DataDigitValues:
                    //先去缓存中找
                    temp = dataDigitValues;
                    break;
                case Const.N_DataDigitData:
                    temp = dataDigitData;
                    break;
                case Const.N_DataTnmObjs:
                    temp = tnmObjs;
                    break;
                case Const.N_DataDigitT:
                    temp = dataDigitT;
                    break;
                case Const.N_DataDigitN:
                    temp = dataDigitN;
                    break;
                case Const.N_DataDigitM:
                    temp = dataDigitM;
                    break;
                case Const.N_DataDigitConf:
                    temp = "0";
                    break;
            }
            return temp;

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getDataForDigitConfData");
            return "-1";
        }

    }

    /***
     *
     * 根据数据标识获得数据，数据源为SyncConf
     *
     * @param flag 数据标识
     * @param response 数据源
     * @return
     *      "0" 数据源解析成功
     *      null 数据为空，或者数据源解析失败
     *      NoNull 有数据返回
     */
    public static Object getDataForSyncConfData(int flag, String response) {
        try {
            LogCustom.f(response, 100);
            JSONObject object = new JSONObject(response);
            Const.SHOW_DISCUZ_ARTICLE = object.getString("ArticleDURL");
            Const.SHOW_DISCUZ_FORUM = object.getString("ForumDURL");
            LogCustom.i(Const.TAG.ZY_DATA, Const.SHOW_DISCUZ_ARTICLE + "\n" + Const.SHOW_DISCUZ_FORUM);
            LinkedHashMap<String, List<String>> data_gene = new LinkedHashMap<>();
            LinkedHashMap<String, String> geneValues = new LinkedHashMap<>();
            LinkedHashMap<String, List<String>> data_cancerData = new LinkedHashMap<>();
            LinkedHashMap<String, String> cancerValues = new LinkedHashMap<>();
            LinkedHashMap<String, String> defaultCancerIDs = new LinkedHashMap<>();
            LinkedHashMap<String, String> circleValues = new LinkedHashMap<>();
            LinkedHashMap<String, String> performValues = new LinkedHashMap<>();
            LinkedHashMap<String, List<String>> bodyPos = new LinkedHashMap<>();
            LinkedHashMap<String, String> transferpos = new LinkedHashMap<>();
            LinkedHashMap<String, String> cureConf = new LinkedHashMap<>();
            LinkedHashMap<String, String> bodyPosValues = new LinkedHashMap<>();
            LinkedHashMap<String, List<String>> dataCancerCircle = new LinkedHashMap<>();
            LinkedHashMap<String, List<String>> dataCircleClass = new LinkedHashMap<>();
            try {
                JSONObject curef = object.getJSONObject("CureConf");
                JSONObject curefData = curef.getJSONObject(DATA);
                Iterator<String> fiterator = curefData.keys();
                String fkey = "";
                while (fiterator.hasNext()) {
                    fkey = fiterator.next();
                    cureConf.put(fkey, curefData.getString(fkey));
                }
            } catch (Exception e) {
                LogCustom.i(Const.TAG.ZY_DATA, "CureConf DATA:新加入字段无法解析");
                ExceptionUtils.ExceptionSend(e, "CureConf新加入字段解析错误");
            }

            /**
             * 解析cancer
             */
            /**封装数据*/
            Map<String, String> dataCancerParent = new HashMap<>();
            try {
                JSONObject cancer = object.getJSONObject(Cancer);
                JSONArray cancerData = cancer.getJSONArray(DATA);
                cancerValues.put("0", "全部");
                for (int i = 0; i < cancerData.length(); i++) {
                    JSONObject dataItem = cancerData.getJSONObject(i);
                    String id = dataItem.getString(ID);
                    String name = dataItem.getString(NAME);
                    if ("1".equals(id)) {
                        continue;
                    }
                    cancerValues.put(id, name);
                    try {
                        String defaultid = dataItem.getString(defaultID);
                        if (!TextUtils.isEmpty(defaultid) && !ZERO.equals(defaultid)) {
                            defaultCancerIDs.put(id, defaultid);
                        }
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionSend(e, "default");
                    }
                    String parentId = dataItem.getString(ParentID);
                    dataCancerParent.put(id, parentId);
                    if (data_cancerData.containsKey(parentId)) {
                        List<String> list = data_cancerData.get(parentId);
                        list.add(id);
                    } else {
                        List<String> datalist = new ArrayList<>();
                        datalist.add(id);
                        data_cancerData.put(parentId, datalist);
                    }
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "cancer Error");
            }


            /**
             * 解析perform
             */
            try {
                JSONObject perfrom = object.getJSONObject(PERFORM);
                JSONObject perfromData = perfrom.getJSONObject(DATA);
                Iterator<String> itemKey1 = perfromData.keys();
                String key4 = "";
                while (itemKey1.hasNext()) {
                    key4 = itemKey1.next();
                    performValues.put(key4, perfromData.getString(key4));//这儿put的时候根据需求来写
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "oerform Error");
            }


            /**
             * 解析bodypos
             */
            try {
                JSONObject bodypos = object.getJSONObject(BODYPOS);
                JSONArray bodyposData = bodypos.getJSONArray(DATA);
                for (int i = 0; i < bodyposData.length(); i++) {
                    JSONObject dataItem = bodyposData.getJSONObject(i);
                    String id = dataItem.getString(ID);
                    String name = dataItem.getString(NAME);
                    bodyPosValues.put(id, name);
                    JSONArray perform = dataItem.getJSONArray(Perform);
                    List<String> list = new ArrayList<>();
                    for (int j = 0; j < perform.length(); j++) {
                        String item = (String) perform.get(j);
                        list.add(item);
                    }
                    bodyPos.put(id, list);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "body Error");
            }


            /**
             * 解析CancerTransferPos
             */
            try {
                JSONObject cancerTransferPos = object.getJSONObject("CancerTransferPos");
                JSONObject transferposdData = cancerTransferPos.getJSONObject(DATA);
                Iterator<String> transferposdDataKey = transferposdData.keys();
                String transferposKey = "";
                while (transferposdDataKey.hasNext()) {
                    transferposKey = transferposdDataKey.next();
                    transferpos.put(transferposKey, transferposdData.getString(transferposKey));
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "cancerTrab Error");
            }


            /**
             * 解析突变情况 GeneticMutation
             */
            try {
                JSONObject genes = object.getJSONObject(GeneticMutation);
                JSONArray geneData = genes.getJSONArray(DATA);
                for (int i = 0; i < geneData.length(); i++) {
                    JSONObject geneItem = geneData.getJSONObject(i);//得到data中每个item项目
                    String cancerId = geneItem.getString(CANCER_ID);
                    JSONObject gene2 = geneItem.getJSONObject(gene);
                    List<String> list = new ArrayList<>();
                    Iterator<String> iterator2 = gene2.keys();
                    String key1 = "";
                    while (iterator2.hasNext()) {
                        key1 = iterator2.next();
                        list.add(key1);
                        geneValues.put(key1, gene2.getString(key1));
                    }
                    data_gene.put(cancerId, list);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "gene Error");
            }

            /**
             * 解析 circleiddata
             */
            List<String> tempList;
            List<CircleNewEntity> circleList = new ArrayList<>();
            CircleNewEntity circleEntity;
            try {
                JSONObject circleIddData = object.getJSONObject(CIRCLE_ID_DATA);
                JSONArray cityArray = circleIddData.getJSONArray(Citydata);
                JSONArray cancerArray = circleIddData.getJSONArray(Cancerdata);
                JSONArray cancerCircleData = circleIddData.getJSONArray("CancerNewdata");
                for (int i = 0; i < cityArray.length(); i++) {
                    JSONObject item = cityArray.getJSONObject(i);
                    String circleID = item.getString(CircleID);
                    String circleName = item.getString(CircleName);
                    circleEntity = new CircleNewEntity();
                    circleEntity.setCircleID(circleID);
                    circleEntity.setCircleName(circleName);
                    circleEntity.setCircleType(2);
                    circleList.add(circleEntity);
                    circleValues.put(circleID, circleName);
                }
                for (int i = 0; i < cancerArray.length(); i++) {
                    JSONObject item = cancerArray.getJSONObject(i);
                    String circleID = item.getString(CircleID);
                    String circleName = item.getString(CircleName);
                    String cancerID = item.getString(CANCER_ID);
                    tempList = dataCancerCircle.get(cancerID);
                    if (tempList == null) {
                        tempList = new ArrayList<>();
                        tempList.add(circleID);
                    } else {
                        tempList.add(circleID);
                    }
                    dataCancerCircle.put(cancerID, tempList);
                    circleValues.put(circleID, circleName);
                    if ("0".equals(cancerID)) {
                        circleEntity = new CircleNewEntity();
                        circleEntity.setCircleID(circleID);
                        circleEntity.setCircleName(circleName);
                        circleEntity.setCircleType(3);
                        circleList.add(circleEntity);
                    }
                }

                try {
                    List<String> tempList2;
                    LinkedHashMap<String, List<String>> tempMap = new LinkedHashMap<>();

                    for (int i = 0; i < cancerCircleData.length(); i++) {
                        JSONObject item = cancerCircleData.getJSONObject(i);
                        String circleID = item.getString("CircleID");
                        String circleName = item.getString("CircleName");
                        circleValues.put(circleID, circleName);
                        circleEntity = new CircleNewEntity();
                        circleEntity.setCircleID(circleID);
                        circleEntity.setCircleName(circleName);
                        circleEntity.setCircleType(1);
                        circleList.add(circleEntity);
                        String parentID = dataCancerParent.get(circleID);
                        if (!TextUtils.isEmpty(parentID)) {
                            if ("0".equals(parentID)) {
                                parentID = circleID;
                            }
                            if (tempMap.containsKey(parentID)) {
                                tempList2 = tempMap.get(parentID);
                            } else {
                                tempList2 = new ArrayList<>();
                            }
                            tempList2.add(circleID);
                            tempMap.put(parentID, tempList2);
                        }
                    }

                    for (String str : tempMap.keySet()) {
                        String parentID = dataCancerParent.get(str);
                        if (TextUtils.isEmpty(parentID) || "0".equals(parentID)) {
                            dataCircleClass.put(str, tempMap.get(str));
                        } else {
                            tempList = dataCircleClass.get(parentID);
                            if (tempList == null || tempList.size() == 0) {
                                dataCircleClass.put(parentID, tempMap.get(str));
                            } else {
                                tempList.addAll(tempMap.get(str));
                                dataCircleClass.put(parentID, tempList);
                            }
                        }
                    }

                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "CancerNewdata解析错误");
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "circle Error");
            }


            /**
             * 解析城市
             */
            List<ProvinceEntity> provinceEntityList = new ArrayList<>();
            JSONObject cityIDData = object.getJSONObject(CityIDData);
            JSONArray cityData = cityIDData.getJSONArray(DATA);//city字段下的data
            SparseArray<String> cityes = new SparseArray<>();
            ProvinceEntity provinceEntity;
            try {
                for (int i = 0; i < cityData.length(); i++) {
                    JSONObject dataItem = cityData.getJSONObject(i);//data下的每个item
                    JSONObject city = dataItem.getJSONObject(CITY);//这个object里面装着一个省下的城市
                    String id = dataItem.getString(ID);//省份的id
                    String name = dataItem.getString(NAME);//对应省份的name

                    provinceEntity = new ProvinceEntity();
                    provinceEntity.setId(id);
                    provinceEntity.setName(name);
                    provinceEntity.setCityarray(city.toString());
                    provinceEntityList.add(i, provinceEntity);

                    cityes.put(Integer.valueOf(id), name);
                    String key;
                    Iterator<String> iteratorKey = city.keys();//
                    while (iteratorKey.hasNext()) {//这儿不是顺序的.
                        key = iteratorKey.next();
                        cityes.put(Integer.valueOf(key), city.getString(key));
                    }
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "city Error");
            }

            MemoryCache.putObjectInCache(Const.DataGene, data_gene);
            MemoryCache.putObjectInCache(Const.DataGeneValues, geneValues);
            MemoryCache.putObjectInCache(Const.DataCancerData, data_cancerData);
            MemoryCache.putObjectInCache(Const.DataCancerValues, cancerValues);
            MemoryCache.putObjectInCache(Const.DataDefaultCancer, defaultCancerIDs);
            MemoryCache.putObjectInCache(Const.DataCircleValues, circleValues);
            MemoryCache.putObjectInCache(Const.DataPerformValues, performValues);
            MemoryCache.putObjectInCache(Const.DataBodyPos, bodyPos);
            MemoryCache.putObjectInCache(Const.DataTransferPos, transferpos);
            MemoryCache.putObjectInCache(Const.DataCity, provinceEntityList);
            MemoryCache.putObjectInCache(Const.DataAllCity, cityes);
            MemoryCache.putObjectInCache(Const.DataCureConf, cureConf);
            MemoryCache.putObjectInCache(Const.DataCircleCancer, dataCancerCircle);
            MemoryCache.putObjectInCache(Const.DataCircleClass, dataCircleClass);
            MemoryCache.putObjectInCache(Const.DataCancerParent, dataCancerParent);
            MemoryCache.putObjectInCache(Const.DataCircleForSearch, circleList);
            MemoryCache.putObjectInCache(Const.DataBodyPosValues, bodyPosValues);

            Object temp = null;

            switch (flag) {
                case Const.N_DataCureConf:
                    temp = cureConf;
                    break;
                case Const.N_DataCancerValues:
                    temp = cancerValues;
                    break;
                case Const.N_DataCancerData:
                    temp = data_cancerData;
                    break;
                case Const.N_DataCancerParent:
                    temp = dataCancerParent;
                    break;
                case Const.N_DataPerformValues:
                    temp = performValues;
                    break;
                case Const.N_DataBodyPos:
                    temp = bodyPos;
                    break;
                case Const.N_DataTransferPos:
                    temp = transferpos;
                    break;
                case Const.N_DataGeneValues:
                    temp = geneValues;
                    break;
                case Const.N_DataGene:
                    temp = data_gene;
                    break;
                case Const.N_DataCircleValues:
                    temp = circleValues;
                    break;
                case Const.N_DataCircleCancer:
                    temp = dataCancerCircle;
                    break;
                case Const.N_DataCircleClass:
                    temp = dataCircleClass;
                    break;
                case Const.N_DataCircleForSearch:
                    temp = circleList;
                    break;
                case Const.N_DataCity:
                    temp = provinceEntityList;
                    break;
                case Const.N_DataAllCity:
                    temp = cityes;
                    break;
                case Const.N_DataDefaultCancer:
                    temp = defaultCancerIDs;
                    break;
                case Const.N_DataBodyPosValues:
                    temp = bodyPosValues;
                    break;
                case Const.N_UrlAirtcleIndex:
                    temp = "0";
                    break;
                case Const.N_UrlForumIndex:
                    temp = "0";
                    break;
                case Const.N_DataDigitConf:
                    temp = "0";
                    break;
            }
            return temp;

        } catch (Exception e) {
            LogCustom.i(Const.TAG.ZY_DATA, "SyncConf配置解析失败");
            return null;
        }

    }

    public static Object getDataForSyncConfStep(int flag, StepDataEntity stepDataEntity) {

        try {
            List<ConfStepEntity> stepListEntities = stepDataEntity.getStepData().getData();
            SparseArray<ConfStepEntity> array = new SparseArray<>();
            List<ConfStepEntity> drugList = new ArrayList<>();
            SparseArray<List<ConfStepEntity>> drugArray = new SparseArray<>();
            for (ConfStepEntity item : stepListEntities) {
                if (!TextUtils.isEmpty(item.getIsDel()) && "1".equals(item.getIsDel())) {

                } else {
                    String stepId = item.getStepID();
                    String cureConfId = item.getCureConfID();
                    String flagSelector = item.getFlagSelector();
                    array.put(Integer.valueOf(stepId), item);
                    try {
                        if ("1".equals(flagSelector) || "2".equals(flagSelector)) {
                            drugList.add(item);
                            List<ConfStepEntity> confTempList;
                            int cureInt = Integer.valueOf(cureConfId);
                            if (drugArray.get(cureInt) == null) {
                                confTempList = new ArrayList<>();
                                confTempList.add(item);
                                drugArray.put(cureInt, confTempList);
                            } else {
                                confTempList = drugArray.get(cureInt);
                                confTempList.add(item);
                                drugArray.put(cureInt, confTempList);
                            }
                        }
                    } catch (Exception e) {
                        LogCustom.i(Const.TAG.ZY_DATA, "DataSearchDrug失败" + drugArray.size());
                    }
                }
            }
            MemoryCache.putObjectInCache(Const.DataSearchDrug, drugArray);
            MemoryCache.putObjectInCache(Const.DataDrugNames, array);
            MemoryCache.putObjectInCache(Const.DataStepData, stepDataEntity);
            MemoryCache.putObjectInCache(Const.DataSearchDrugList, drugList);
            LogCustom.i(Const.TAG.ZY_DATA, "ConfStep配置解析成功：" + array.size());
            Object temp = null;
            switch (flag) {
                case Const.N_DataDrugNames:
                    temp = array;
                    break;
                case Const.N_DataSearchDrug:
                    temp = drugArray;
                    break;
                case Const.N_DataSearchDrugList:
                    temp = drugList;
                    break;
                case Const.N_DataStepData:
                    temp = stepDataEntity;
                    break;
                case Const.UPDATA_STEP:
                    temp = "0";
                    break;
            }
            return temp;
        } catch (Exception e) {
            LogCustom.i(Const.TAG.ZY_DATA, "ConfStep配置解析失败");
            return null;
        }
    }


    //根据癌种获取对应的阶段数据
    public static Object getDataForSyncConfStep(int flag, StepDataEntity stepDataEntity, String cancerID) {
        try {
            List<ConfStepEntity> stepListEntities = stepDataEntity.getStepData().getData();
            LinkedHashMap<String, LinkedHashMap<String, List<String>>> cancerCure = new LinkedHashMap<>();
            cancerCure.put(cancerID, new LinkedHashMap<String, List<String>>());
            LinkedHashMap<String, List<String>> map;
            List<String> list;
            List<String> liststep;
            for (ConfStepEntity item : stepListEntities) {
                try {
                    if (!TextUtils.isEmpty(item.getIsDel()) && "1".equals(item.getIsDel())) {

                    } else {
                        String stepId = item.getStepID();
                        String cancerId = item.getCancerID();
                        String cureConfId = item.getCureConfID();
                        String flagSelector = item.getFlagSelector();
                        if ("1".equals(flagSelector) || "3".equals(flagSelector)) {
                            boolean cancerFlag = false;
                            if (!TextUtils.isEmpty(cancerId)) {
                                if (Const.RESULT.equals(cancerId)) {
                                    cancerFlag = true;
                                } else {
                                    String[] args = cancerId.split(",");
                                    for (String str : args) {
                                        if (cancerID.equals(str)) {
                                            cancerFlag = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (cancerFlag) {
                                map = cancerCure.get(cancerID);
                                if (map.get(cureConfId) != null) {
                                    list = map.get(cureConfId);
                                    list.add(stepId);
                                    map.put(cureConfId, list);
                                } else {
                                    liststep = new ArrayList<>();
                                    liststep.add(stepId);
                                    map.put(cureConfId, liststep);
                                }
                                cancerCure.put(cancerID, map);
                            }
                        }
                    }
                } catch (Exception e) {
                    LogCustom.i(Const.TAG.ZY_DATA, "DataCancerStepNew ERROR");
                }
            }
            return cancerCure.get(cancerID);
        } catch (Exception e) {
            LogCustom.i(Const.TAG.ZY_DATA, "ConfStep配置解析失败");
            return null;
        }
    }

    /***
     *
     * 二分法查找下标
     *
     * @param StepID
     * @param list
     * @return
     */
    public static int getPositionForStepID(int StepID, List<ConfStepEntity> list) {

        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (StepID == Integer.valueOf(list.get(middle).getStepID())) {
                return middle;
            }
            if (StepID > Integer.valueOf(list.get(middle).getStepID())) {
                low = middle + 1;
            }
            if (StepID < Integer.valueOf(list.get(middle).getStepID())) {
                high = middle - 1;
            }
        }
        return -1;
    }

}
