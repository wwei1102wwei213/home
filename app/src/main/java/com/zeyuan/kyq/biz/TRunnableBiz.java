package com.zeyuan.kyq.biz;

import android.util.Log;
import android.util.SparseArray;

import com.ta.util.db.TASQLiteDatabase;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.Entity.BodyPosEntity;
import com.zeyuan.kyq.Entity.CancerEntity;
import com.zeyuan.kyq.Entity.CancerGeneEntity;
import com.zeyuan.kyq.Entity.CancerStepEntity;
import com.zeyuan.kyq.Entity.CircleEntity;
import com.zeyuan.kyq.Entity.CureEntity;
import com.zeyuan.kyq.Entity.DigitDataEntity;
import com.zeyuan.kyq.Entity.GeneEntity;
import com.zeyuan.kyq.Entity.OtherStepEntity;
import com.zeyuan.kyq.Entity.PerformEntity;
import com.zeyuan.kyq.Entity.ProvinceEntity;
import com.zeyuan.kyq.Entity.TransferPosEntity;
import com.zeyuan.kyq.Entity.UnionEntity;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Administrator on 2016/4/20.
 *
 *
 * @author wwei
 */
public class TRunnableBiz implements Runnable{

    //癌种
    public static final int CANCERS = 1;
    //阶段
    //该标签中需要处理union(联合用药)，otherstep(其他step)
    public static final int STEPS = 2;
    //症状
    public static final int PERFORMS = 3;
    //身体部位
    public static final int BODYPOS = 4;
    //基因
    public static final int GENES = 5;
    //转移部位
    public static final int TRANSFERPOS = 6;
    //城市
    public static final int CITYS = 7;
    //圈子
    public static final int CIRCLES = 8;

    //分期数据
    //总分期
    public static final int DIGIT = 11;
    //数字分期
    public static final int DIGITCANCER = 12;
    //TNM分期
    public static final int DIGINTNM = 13;
    //TNM对应癌种分期查询
    public static final int TNM = 14;


    private static final String DATA = "data";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CANCER_ID = "CancerID";
    private static final String STEP = "step";
    private static final String stepunionid = "stepunionid";
    private static final String cureconfid = "cureconfid";
    private static final String stepallname = "stepallname";
    private static final String CITY = "City";

    private  int Tflag;
    private  String response;
    private ZYApplication application;

    public TRunnableBiz(ZYApplication application,int Tflag,String response){
        this.Tflag = Tflag;
        this.response = response;
        this.application = application;
    }

    @Override
    public void run() {
        JSONObject tempobj;
        JSONObject tempobj2;
        JSONObject tempobj3;
        JSONArray temparray;
        String temp = "";
        String temp2 = "";
        int tempint;
        int tempint2;



        int[] key = {0x61616161, 0x62626262, 0x63636363, 0x64646464};
        HttpSecretUtils.TEA.fn = 0;
        HttpSecretUtils.TEA.setKey(key);
        Log.i("ZYS", "RES:" + response);
        response = HttpSecretUtils.TEA.decryptByTea(response);
        Log.i("ZYS","RES:"+response);
        response = response.substring(response.indexOf(",") + 1, response.length());
        TASQLiteDatabase sqlitedb = application.getSQLiteDatabasePool().getSQLiteDatabase();
        try {
            JSONObject object = new JSONObject(response);
            switch (Tflag){

                /**癌种封装*/
                case SconfBiz.CANCERS:
                    Log.i("ZYS","CANCERS开始时间："+System.currentTimeMillis());
                    JSONObject cancer = object.getJSONObject("Cancer");
                    JSONArray cancerData = cancer.getJSONArray(DATA);
                    CancerEntity cancerEntity = new CancerEntity();
                    for(int i=0;i<cancerData.length();i++){
                        tempobj = cancerData.getJSONObject(i);
                        cancerEntity.setId(tempobj.getInt(ID));
                        cancerEntity.setParentid(tempobj.getInt("ParentID"));
                        cancerEntity.setLevel(tempobj.getString("Level"));
                        cancerEntity.setName(tempobj.getString(NAME));
                        try {
                            sqlitedb.insert(cancerEntity);
                        }catch (Exception e){
                            Log.i("ZYS", "cancer 封装出错：" + cancerEntity.toString());
                        }
                    }
                    Log.i("ZYS", "CANCERS封装ok");
                    Log.i("ZYS","CANCERS结束时间："+System.currentTimeMillis());
                    break;

                /**阶段，联合用药，其他阶段封装*/
                case SconfBiz.STEPS:
                    Log.i("ZYS","STEPS开始时间："+System.currentTimeMillis());
                    JSONObject cure = object.getJSONObject("Cancer2Step");
                    JSONArray cureData = cure.getJSONArray(DATA);
                    SparseArray<String> cureSA = new SparseArray<>();
                    CureEntity cureEntity = new CureEntity();
                    CancerStepEntity cancerStepEntity = new CancerStepEntity();
                    for(int i=0;i<cureData.length();i++){
                        tempobj = cureData.getJSONObject(i);
                        temparray = tempobj.getJSONArray("cure");
                        cancerStepEntity.setCancerid(tempobj.getInt(CANCER_ID));
                        cancerStepEntity.setCancerstepdata(temparray.toString());
                        try {
                            sqlitedb.insert(cancerStepEntity);
                        }catch (Exception e){
                            Log.i("ZYS","cancerstep 封装出错："+cancerStepEntity.toString());
                        }
                        for(int j=0;j<temparray.length();j++){
                            tempobj2 = temparray.getJSONObject(j);
                            tempint = tempobj2.getInt(ID);
                            temp = tempobj2.getString(NAME);
                            cureEntity.setCureid(tempint);
                            cureEntity.setCurename(temp);
                            cureEntity.setCureparentid(tempint);
                            try {
                                if(cureSA.get(tempint)==null){
                                    sqlitedb.insert(cureEntity);
                                    cureSA.put(tempint,temp);
                                }
                            }catch (Exception e){
                                Log.i("ZYS","cure 封装出错："+cureEntity.toString());
                            }
                            tempobj3 = tempobj2.getJSONObject(STEP);
                            Iterator<String> stepIterator = tempobj3.keys();
                            String stepkey;
                            while(stepIterator.hasNext()){
                                stepkey = stepIterator.next();
                                tempint2 = Integer.valueOf(stepkey);
                                temp2 = tempobj3.getString(stepkey);
                                cureEntity.setCureid(tempint2);
                                cureEntity.setCurename(temp2);
                                cureEntity.setCureparentid(tempint);
                                try {
                                    if(cureSA.get(tempint2)==null){
                                        sqlitedb.insert(cureEntity);
                                        cureSA.put(tempint2,temp2);
                                    }
                                }catch (Exception e){
                                    Log.i("ZYS","cure 封装出错："+cureEntity.toString());
                                }
                            }
                        }
                    }


                    JSONObject stepunion = object.getJSONObject("StepUnion");
                    JSONArray arrayData = stepunion.getJSONArray(DATA);
                    UnionEntity unionEntity = new UnionEntity();
                    for(int i=0;i<arrayData.length();i++){
                        tempobj = arrayData.getJSONObject(i);
                        temparray = tempobj.getJSONArray(STEP);
                        unionEntity.setCancerid(tempobj.getInt(CANCER_ID));
                        unionEntity.setUniondata(temparray.toString());
                        try {
                            sqlitedb.insert(unionEntity);
                        }catch (Exception e){
                            Log.i("ZYS","union 封装出错："+unionEntity.toString());
                        }
                        for(int j=0;j<temparray.length();j++){
                            tempobj2 = temparray.getJSONObject(j);
                            tempint = tempobj2.getInt(stepunionid);
                            cureEntity.setCureid(tempint);
                            temp = tempobj2.getString(stepallname);
                            cureEntity.setCurename(temp);
                            cureEntity.setCureparentid(tempobj2.getInt(cureconfid));
                            try {
                                if(cureSA.get(tempint)==null){
                                    sqlitedb.insert(cureEntity);
                                    cureSA.put(tempint,temp);
                                }
                            }catch (Exception e){
                                Log.i("ZYS","cure 封装出错："+cureEntity.toString());
                            }
                        }
                    }

                    JSONObject otherStep = object.getJSONObject("OtherStep");
                    JSONObject oSData = otherStep.getJSONObject(DATA);
                    OtherStepEntity otherStepEntity = new OtherStepEntity();
                    Iterator<String> otherIterator = oSData.keys();
                    String otherkey;
                    while(otherIterator.hasNext()){
                        otherkey = otherIterator.next();
                        otherStepEntity.setOtherstepid(Integer.valueOf(otherkey));
                        otherStepEntity.setOtherstepname(oSData.getString(otherkey));
                        try {
                            sqlitedb.insert(otherStepEntity);
                        }catch (Exception e){
                            Log.i("ZYS","otherstep 封装出错："+otherStepEntity.toString());
                        }
                    }

                    Log.i("ZYS", "STEPS封装ok");
                    Log.i("ZYS","STEPS结束时间："+System.currentTimeMillis());
                    break;

                case SconfBiz.PERFORMS:
                    Log.i("ZYS","PS开始时间："+System.currentTimeMillis());
                    JSONObject perfrom = object.getJSONObject("perform");
                    JSONObject perfromData = perfrom.getJSONObject(DATA);
                    PerformEntity performEntity = new PerformEntity();
                    Iterator<String> performIterator = perfromData.keys();
                    String performkey;
                    while(performIterator.hasNext()){
                        performkey = performIterator.next();
                        performEntity.setPerformid(Integer.valueOf(performkey));
                        performEntity.setPerformname(perfromData.getString(performkey));
                        try {
                            sqlitedb.insert(performEntity);
                        }catch (Exception e){
                            Log.i("ZYS","perform 封装出错："+performEntity.toString());
                        }
                    }
                    Log.i("ZYS", "PERFORMS封装ok");
                    Log.i("ZYS","PS结束时间："+System.currentTimeMillis());
                    break;

                case SconfBiz.CITYS:
                    JSONObject cityIDData = object.getJSONObject("CityIDData");
                    JSONArray cityData = cityIDData.getJSONArray(DATA);//city字段下的data
                    ProvinceEntity provinceEntity;
                    for(int i = 0; i < cityData.length(); i++){
                        JSONObject dataItem = cityData.getJSONObject(i);//data下的每个item
                        JSONObject city = dataItem.getJSONObject(CITY);//这个object里面装着一个省下的城市
                        provinceEntity = new ProvinceEntity();
                        provinceEntity.setId(dataItem.getString(ID));
                        provinceEntity.setName(dataItem.getString(NAME));
                        provinceEntity.setCityarray(city.toString());
                        try {
                            sqlitedb.insert(provinceEntity);
                        }catch (Exception e){
                            ExceptionUtils.ExceptionSend(e, "城市封装出错！" + "出错ID：" + dataItem.getString(ID));
                        }
                    }
                    Log.i("ZYS", "CITYS封装ok");
                    break;

                case SconfBiz.GENES:
                    Log.i("ZYS","GENES开始时间："+System.currentTimeMillis());
                    JSONObject genes = object.getJSONObject("GeneticMutation");
                    JSONArray geneData = genes.getJSONArray(DATA);
                    GeneEntity geneEntity = new GeneEntity();
                    CancerGeneEntity cancerGeneEntity = new CancerGeneEntity();
                    SparseArray<String> geneSA = new SparseArray<>();
                    for(int i=0;i<geneData.length();i++){
                        tempobj = geneData.getJSONObject(i);
                        temp = tempobj.getString(CANCER_ID);
                        tempobj2 = tempobj.getJSONObject("gene");
                        cancerGeneEntity.setCancerid(temp);
                        cancerGeneEntity.setGenelist(tempobj2.toString());
                        try {
                            sqlitedb.insert(cancerGeneEntity);
                        }catch (Exception e){
                            Log.i("ZYS","cancergene封装出错："+cancerGeneEntity.toString());
                        }
                        Iterator<String> geneIterator = tempobj2.keys();
                        String genekeys;
                        while (geneIterator.hasNext()){
                            genekeys = geneIterator.next();
                            temp2 = tempobj2.getString(genekeys);
                            tempint = Integer.valueOf(genekeys);
                            if(geneSA.get(tempint)==null){
                                geneEntity.setGeneid(tempint);
                                geneEntity.setGenname(temp2);
                                try {
                                    sqlitedb.insert(geneEntity);
                                }catch (Exception e){
                                    Log.i("ZYS","gene封装出错："+geneEntity.toString());
                                }
                                geneSA.put(tempint,temp2);
                            }
                        }
                    }
                    Log.i("ZYS", "GENES封装ok");
                    Log.i("ZYS","GENES结束时间："+System.currentTimeMillis());
                    break;

                case SconfBiz.BODYPOS:
                    Log.i("ZYS","BODYPOS开始时间："+System.currentTimeMillis());
                    JSONObject bodypos = object.getJSONObject("BodyPos");
                    JSONArray bodyposData = bodypos.getJSONArray(DATA);
                    BodyPosEntity bodyPosEntity = new BodyPosEntity();
                    for(int i=0;i<bodyposData.length();i++){
                        tempobj = bodyposData.getJSONObject(i);
                        bodyPosEntity.setId(tempobj.getInt(ID));
                        bodyPosEntity.setName(tempobj.getString(NAME));
                        bodyPosEntity.setPerforms(tempobj.getJSONArray("Perform").toString());
                        try {
                            sqlitedb.insert(bodyPosEntity);
                        }catch (Exception e){
                            Log.i("ZYS","bodyPos 封装出错："+bodyPosEntity.toString());
                        }
                    }
                    Log.i("ZYS", "BODYPOSS封装ok");
                    Log.i("ZYS","BODYPOS结束时间："+System.currentTimeMillis());
                    break;

                case SconfBiz.TRANSFERPOS:
                    Log.i("ZYS","TPOS开始时间："+System.currentTimeMillis());
                    JSONObject cancerTransferPos = object.getJSONObject("CancerTransferPos");
                    JSONObject transferposdData = cancerTransferPos.getJSONObject(DATA);
                    TransferPosEntity transferPosEntity = new TransferPosEntity();
                    Iterator<String> transferIterator = transferposdData.keys();
                    String transferkey;
                    while(transferIterator.hasNext()){
                        transferkey = transferIterator.next();
                        transferPosEntity.setTranrferid(Integer.valueOf(transferkey));
                        transferPosEntity.setTranrfername(transferposdData.getString(transferkey));
                        try {
                            sqlitedb.insert(transferPosEntity);
                        }catch (Exception e){
                            Log.i("ZYS","transferPos封装出错："+transferPosEntity.toString());
                        }
                    }
                    Log.i("ZYS", "TRANSFERPOS封装ok");
                    Log.i("ZYS","TPOS结束时间："+System.currentTimeMillis());
                    break;

                case SconfBiz.CIRCLES:
                    /**同城圈和抗癌圈*/
                    Log.i("ZYS","CIRCLES开始时间："+System.currentTimeMillis());
                    JSONObject circleIddData = object.getJSONObject("CircleIDData");
                    JSONArray cityCircleArray = circleIddData.getJSONArray("Citydata");
                    JSONArray cancerCircleArray = circleIddData.getJSONArray("Cancerdata");
                    CircleEntity circleEntity = new CircleEntity();
                    for(int i=0;i<cityCircleArray.length();i++){
                        tempobj = cityCircleArray.getJSONObject(i);
                        circleEntity.setCircleid(tempobj.getInt("CircleID"));
                        circleEntity.setCirclename(tempobj.getString("CircleName"));
                        try {
                            sqlitedb.insert(circleEntity);
                        }catch (Exception e){
                            Log.i("ZYS","citycircle封装出错："+circleEntity.toString());
                        }
                    }
                    for(int i=0;i<cancerCircleArray.length();i++){
                        tempobj = cancerCircleArray.getJSONObject(i);
                        circleEntity.setCircleid(tempobj.getInt("CircleID"));
                        circleEntity.setCirclename(tempobj.getString("CircleName"));
                        try {
                            sqlitedb.insert(circleEntity);
                        }catch (Exception e){
                            Log.i("ZYS","cancercircle封装出错："+circleEntity.toString());
                        }
                    }
                    Log.i("ZYS", "CIRCLES封装ok");
                    Log.i("ZYS","CIRCLES结束时间："+System.currentTimeMillis());
                    break;

                case SconfBiz.DIGIT:

                    JSONObject digit = object.getJSONObject("digit");//节点digit
                    JSONObject digitData = digit.getJSONObject("digitdata");//节点digit的digitData
                    Iterator<String> keysIterator = digitData.keys();
//                    String ;
                    DigitDataEntity digitDataEntity = new DigitDataEntity();
                    while (keysIterator.hasNext()) {
                        temp = keysIterator.next();
                        digitDataEntity.setDigitid(Integer.valueOf(temp));
                        digitDataEntity.setDigitvalue(DecryptUtils.strToAsc(digitData.getString(temp)));
                        Log.i("ZYS", "digitdata封装：" + digitDataEntity.toString());

                    }
                    break;

            }
        } catch (Exception e) {//
            e.printStackTrace();
            ExceptionUtils.ExceptionSend(e, "prasync");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(sqlitedb);
    }
}
